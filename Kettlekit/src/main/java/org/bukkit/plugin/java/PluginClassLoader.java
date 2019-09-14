package org.bukkit.plugin.java;

import com.google.common.io.ByteStreams;
import mc.kettle.remapper.Transformer;
import net.md_5.specialsource.JarMapping;
import org.apache.commons.lang.Validate;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.PluginDescriptionFile;

import java.io.*;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * A ClassLoader for plugins, to allow shared classes across multiple plugins
 */
final class PluginClassLoader extends URLClassLoader {
    private static JarMapping jarMapping;

    // Spigot Start
    static {
        try {
            java.lang.reflect.Method method = ClassLoader.class.getDeclaredMethod("registerAsParallelCapable");
            if (method != null) {
                boolean oldAccessible = method.isAccessible();
                method.setAccessible(true);
                method.invoke(null);
                method.setAccessible(oldAccessible);
                org.bukkit.Bukkit.getLogger().log(java.util.logging.Level.INFO, "Set PluginClassLoader as parallel capable");
            }
        } catch (NoSuchMethodException ex) {
            // Ignore
        } catch (Exception ex) {
            org.bukkit.Bukkit.getLogger().log(java.util.logging.Level.WARNING, "Error setting PluginClassLoader as parallel capable", ex);
        }
    }

    final JavaPlugin plugin;
    private final JavaPluginLoader loader;
    private final Map<String, Class<?>> classes = new ConcurrentHashMap<>(); // Spigot
    private final PluginDescriptionFile description;
    private final File dataFolder;
    private final File file;
    private final JarFile jar;
    private final Manifest manifest;
    private final URL url;
    private final boolean debug = false;
    private JavaPlugin pluginInit;
    // Spigot End
    private IllegalStateException pluginState;

    PluginClassLoader(final JavaPluginLoader loader, final ClassLoader parent, final PluginDescriptionFile description, final File dataFolder, final File file) throws IOException, InvalidPluginException {
        super(new URL[]{file.toURI().toURL()}, parent);
        Validate.notNull(loader, "Loader cannot be null");

        this.loader = loader;
        this.description = description;
        this.dataFolder = dataFolder;
        this.file = file;
        this.jar = new JarFile(file);
        this.manifest = jar.getManifest();
        this.url = file.toURI().toURL();

        if (jarMapping == null) {
            try {
                jarMapping = new JarMapping();
                jarMapping.loadMappings(new BufferedReader(
                        new InputStreamReader(loader.getClass().getClassLoader().getResourceAsStream("vcb2kettle.srg"))), null, null, false);
                Transformer.loadMapping(jarMapping);
            } catch (IOException | IllegalArgumentException | SecurityException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        try {
            Class<?> jarClass;
            try {
                jarClass = Class.forName(description.getMain(), true, this);
            } catch (ClassNotFoundException ex) {
                throw new InvalidPluginException("Cannot find main class `" + description.getMain() + "'", ex);
            }

            Class<? extends JavaPlugin> pluginClass;
            try {
                pluginClass = jarClass.asSubclass(JavaPlugin.class);
            } catch (ClassCastException ex) {
                throw new InvalidPluginException("main class `" + description.getMain() + "' does not extend JavaPlugin", ex);
            }

            plugin = pluginClass.newInstance();
        } catch (IllegalAccessException ex) {
            throw new InvalidPluginException("No public constructor", ex);
        } catch (InstantiationException ex) {
            throw new InvalidPluginException("Abnormal plugin type", ex);
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return findClass(name, true);
    }

    Class<?> findClass(String name, boolean checkGlobal) throws ClassNotFoundException {
        if (name.startsWith("org.bukkit.") || name.startsWith("net.minecraft.")) {
            throw new ClassNotFoundException(name);
        }
        Class<?> result = classes.get(name);

        if (result == null) {
            if (checkGlobal) {
                result = loader.getClassByName(name);
            }

            if (result == null) {
                String path = name.replace('.', '/').concat(".class");
                JarEntry entry = jar.getJarEntry(path);

                if (entry != null) {
                    byte[] classBytes;

                    try (InputStream is = jar.getInputStream(entry)) {
                        classBytes = ByteStreams.toByteArray(is);
                    } catch (IOException ex) {
                        throw new ClassNotFoundException(name, ex);
                    }

                    int dot = name.lastIndexOf('.');
                    if (dot != -1) {
                        String pkgName = name.substring(0, dot);
                        if (getPackage(pkgName) == null) {
                            try {
                                if (manifest != null) {
                                    definePackage(pkgName, manifest, url);
                                } else {
                                    definePackage(pkgName, null, null, null, null, null, null, null);
                                }
                            } catch (IllegalArgumentException ex) {
                                if (getPackage(pkgName) == null) {
                                    throw new IllegalStateException("Cannot find package " + pkgName);
                                }
                            }
                        }
                    }

                    CodeSigner[] signers = entry.getCodeSigners();
                    CodeSource source = new CodeSource(url, signers);

                    result = defineClass(name, classBytes, 0, classBytes.length, source);
                }

                if (result == null) {
                    result = remappedFindClass(name);
                }

                if (result != null) {
                    loader.setClass(name, result);
                }
            }

            if (result != null) classes.put(name, result);
        }

        return result;
    }

    private Class<?> remappedFindClass(String name) throws ClassNotFoundException {
        Class<?> result = null;

        try {
            String path = name.replace('.', '/').concat(".class");
            URL url = findResource(path);
            if (url != null) {
                InputStream stream = url.openStream();
                if (stream != null) {
                    byte[] bytecode = ByteStreams.toByteArray(stream);
                    Transformer.setInheritenceClassLoader(this);
                    byte[] remappedBytecode = Transformer.transformSS(bytecode);

                    if (debug) {
                        File file = new File("remapped-plugin-classes/" + name + ".class");
                        file.getParentFile().mkdirs();
                        try {
                            FileOutputStream fos = new FileOutputStream(file);
                            fos.write(remappedBytecode);
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                    URL jarUrl = jarURLConnection.getJarFileURL();
                    CodeSource codeSource = new CodeSource(jarUrl, new CodeSigner[0]);

                    result = defineClass(name, remappedBytecode, 0, remappedBytecode.length, codeSource);
                    if (result != null) {
                        resolveClass(result);
                    }
                }
            }
        } catch (Throwable t) {
            if (debug) {
                System.out.println("remappedFindClass(" + name + ") exception: " + t);
                t.printStackTrace();
            }
            throw new ClassNotFoundException("Failed to remap class " + name, t);
        }

        return result;
    }

    @Override
    public void close() throws IOException {
        try {
            super.close();
        } finally {
            jar.close();
        }
    }

    Set<String> getClasses() {
        return classes.keySet();
    }

    synchronized void initialize(JavaPlugin javaPlugin) {
        Validate.notNull(javaPlugin, "Initializing plugin cannot be null");
        Validate.isTrue(javaPlugin.getClass().getClassLoader() == this, "Cannot initialize plugin outside of this class loader");
        if (this.plugin != null || this.pluginInit != null) {
            throw new IllegalArgumentException("Plugin already initialized!", pluginState);
        }

        pluginState = new IllegalStateException("Initial initialization");
        this.pluginInit = javaPlugin;

        javaPlugin.init(loader, loader.server, description, dataFolder, file, this);
    }
}

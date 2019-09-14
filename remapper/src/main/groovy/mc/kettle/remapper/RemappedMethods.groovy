package mc.kettle.remapper

import java.lang.reflect.Field
import java.lang.reflect.Method

import static mc.kettle.remapper.Utils.*

class RemappedMethods {

    static Class<?> forName(String className) throws ClassNotFoundException {
        forName(className, true, ReflectionUtils.getCallerClassLoader())
    }

    static Class<?> forName(String className, boolean initialize, ClassLoader loader) throws ClassNotFoundException {
        className = Transformer.remapper.map(className.replace('.', '/')).replace('/', '.')
        Class.forName(className, initialize, loader)
    }

    static String getSimpleName(Class<?> inst) {
        String[] name = getName(inst).split("\\.")
        name[name.length - 1]
    }

    static String getName(Class<?> inst) {
        reverseMapExternal(inst)
    }

    static Field getField(Class<?> inst, String name) throws NoSuchFieldException, SecurityException {
        inst.getField(Transformer.remapper.mapFieldName(reverseMap(inst), name, null))
    }

    static Field getDeclaredField(Class<?> inst, String name) throws NoSuchFieldException, SecurityException {
        inst.getDeclaredField(Transformer.remapper.mapFieldName(reverseMap(inst), name, null))
    }

    static Method getMethod(Class<?> inst, String name, Class<?>... paramTypes) throws NoSuchMethodException, SecurityException {
        if (inst.getName() == "net.minecraft.server.MinecraftServer" && name == "getServer") {
            Class.forName("org.spongepowered.common.SpongeImpl").getMethod("getServer")
        }

        inst.getDeclaredMethod(mapMethod(inst, name, paramTypes as Class<?>), paramTypes)
    }

    static String getName(Field field) {
        String name = field.name
        String match = reverseMap(field.declaringClass)

        for (Map.Entry<String, String> entry : Transformer.jarMapping.fields.entrySet()) {
            if (entry.key.startsWith(match) && entry.value == name) {
                String[] matched = entry.key.split("/")
                matched[matched.length - 1]
            }
        }

        name
    }

    static String getName(Method method) {
        String name = method.name
        String match = reverseMap(method.declaringClass)

        for (Map.Entry<String, String> entry : Transformer.jarMapping.methods.entrySet()) {
            if (entry.key.startsWith(match) && entry.value == name) {
                String[] matched = entry.key.split("\\s+")[0].split("/")
                matched[matched.length - 1]
            }
        }

        name
    }

    static String getName(Package aPackage) {
        if (aPackage == null) null
        String name = aPackage.name
        String check = name.replace('.', '/').concat('/')
        for (Map.Entry<String, String> entry : Transformer.jarMapping.packages.entrySet()) {
            if (entry.value == check) {
                String match = entry.key.replace('/', '.')
                match.substring(0, match.length() - 1)
            }
        }

        name
    }
}

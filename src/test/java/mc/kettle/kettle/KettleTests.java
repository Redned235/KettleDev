package mc.kettle.kettle;

import com.google.common.reflect.ClassPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Game;
import org.spongepowered.api.plugin.PluginContainer;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.mockito.Mockito.mock;

public final class KettleTests {

    private KettleTests() {
    }

    public static final String PACKAGE = "mc.kettle.kettle.";

    public static void mockPlugin() {
        if (Kettle.instance == null) {
            new Kettle(mock(Game.class), LoggerFactory.getLogger("Kettle"), mock(PluginContainer.class));
            Kettle.testLogger = Kettle.getLogger();
        }
    }

    public static Logger getLogger() {
        mockPlugin();
        return Kettle.getTestLogger();
    }

    private static Field modifiers;

    public static void setConstants(Class<?>... classes) throws Exception {
        if (modifiers == null) {
            Field modifiers = Field.class.getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            KettleTests.modifiers = modifiers;
        }

        for (Class<?> type : classes) {
            for (Field field : type.getFields()) {
                int access = field.getModifiers();
                if (Modifier.isPublic(access) && Modifier.isStatic(access)) {
                    if (field.get(null) != null) {
                        continue;
                    }

                    if (Modifier.isFinal(access)) {
                        modifiers.setInt(field, access & ~Modifier.FINAL);
                        field.setAccessible(true);
                    }

                    field.set(null, mock(field.getType()));
                }
            }
        }
    }

    private static ClassPath classPath;

    public static ClassPath getClassPath() throws IOException {
        if (classPath == null) {
            classPath = ClassPath.from(ClassLoader.getSystemClassLoader());
        }

        return classPath;
    }
}
package mc.kettle.remapper

import java.lang.reflect.Method

class ReflectionUtils {
    private static final Method GET_CALLER_CLASS

    static {
        final Class<?> reflection = Class.forName("sun.reflect.Reflection")
        GET_CALLER_CLASS = reflection.getMethod("getCallerClass", int.class)
    }

    static Class<?> getCallerClass(Integer skip) {
        if (GET_CALLER_CLASS != null) {
            GET_CALLER_CLASS.invoke(null, skip + 1) as Class<?>
        }

        throw new RuntimeException("getCallerClass somehow doesn't exist?")
    }

    static ClassLoader getCallerClassLoader() {
        getCallerClass(3).classLoader
    }
}

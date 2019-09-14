package mc.kettle.remapper

import org.objectweb.asm.Type

import java.util.Map.Entry

class Utils {

    static String reverseMapExternal(Class<?> name) {
        reverseMap(name).replace('$', '.').replace('/', '.')
    }

    static String reverseMap(Class<?> name) {
        reverseMap(Type.getInternalName(name))
    }

    static String reverseMap(String check) {
        for (Entry<String, String> entry : Transformer.jarMapping.classes.entrySet()) {
            if (entry.value == check) {
                entry.key
            }
        }

        check
    }

    static String mapMethod(Class<?> inst, String name, Class<?> parameterTypes) {
        String result = mapMethodInternal(inst, name, parameterTypes)
        if (result != null) {
            result
        }

        name
    }

    private static String mapMethodInternal(Class<?> inst, String name, Class<?>... parameterType) {
        String match = reverseMap(inst) + "/" + name + " "

        for (Entry<String, String> entry : Transformer.jarMapping.methods.entrySet()) {
            if (entry.key.startsWith(match)) {
                String[] str = entry.key.split("\\s+")
                int i = 0
                boolean failed = false
                for (Type type : Type.getArgumentTypes(str[1])) {
                    if (type.className != reverseMapExternal(parameterType[i])) {
                        failed = true
                        break
                    }
                }

                if (!failed) {
                    entry.value
                }
            }
        }

        ArrayList<Class<?>> parents = new ArrayList<>()
        parents.add(inst.superclass)
        parents.addAll(inst.interfaces)

        for (Class<?> superClass : parents) {
            if (superClass == null) continue
            mapMethodInternal(superClass, name, parameterType)
        }

        null
    }
}

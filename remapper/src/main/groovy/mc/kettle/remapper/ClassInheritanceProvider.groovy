package mc.kettle.remapper

import net.md_5.specialsource.provider.InheritanceProvider

class ClassInheritanceProvider implements InheritanceProvider {
    @Override
    Collection<String> getParents(String className) {
        className = Transformer.remapper.map(className)

        Collection<String> parents = new HashSet<>()
        Class<?> reference = Class.forName(className.replace('/', '.').replace('$', '.'), false, this.getClass().getClassLoader())
        Class<?> extend = reference.superclass
        if (extend != null) {
            parents.add(reverseMap(extend))
        }

        for (Class<?> inter : reference.interfaces) {
            if (inter != null) {
                parents.add(reverseMap(inter))
            }
        }

        if (parents != null) {
            parents
        } else {
            null
        }
    }
}

package mc.kettle.remapper

import jdk.internal.org.objectweb.asm.tree.MethodInsnNode
import net.md_5.specialsource.JarMapping
import net.md_5.specialsource.JarRemapper
import net.md_5.specialsource.provider.ClassLoaderProvider
import net.md_5.specialsource.provider.JointProvider
import net.md_5.specialsource.repo.RuntimeRepo
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.MethodNode

class Transformer {
    protected static JarMapping jarMapping
    protected static JarRemapper remapper

    static void setInheritenceClassLoader(ClassLoader loader) {
        JointProvider provider = new JointProvider()
        provider.add(new ClassInheritanceProvider())
        provider.add(new ClassLoaderProvider(loader))
        jarMapping.setFallbackInheritanceProvider(provider)
    }

    static void loadMapping(JarMapping mapping) throws IllegalArgumentException {
        if (jarMapping != null) {
            throw new IllegalArgumentException("Already loading a mapping")
        }

        jarMapping = mapping
        remapper = new JarRemapper(jarMapping)
    }

    static byte[] transformSS(byte[] code) {
        remapper.remapClassFile(transform(code), RuntimeRepo.getInstance())
    }

    static byte[] transform(byte[] code) {
        ClassReader reader = new ClassReader(code)
        ClassNode node = new ClassNode()
        reader.accept(node, 0)

        for (MethodNode method : node.methods) {
            ListIterator<AbstractInsnNode> insnIterator = method.instructions.iterator()
            while (insnIterator.hasNext()) {
                AbstractInsnNode insn = insnIterator.next()
                switch (insn.opcode) {
                    case Opcodes.INVOKEVIRTUAL:
                        remapVirtual(insn)
                        break
                    case Opcodes.INVOKESTATIC:
                        remapForName(insn)
                        break
                }
            }
        }

        ClassWriter writer = new ClassWriter(0)
        node.accept(writer)
        writer.toByteArray()
    }

    static void remapForName(AbstractInsnNode insn) {
        MethodInsnNode method = insn as MethodInsnNode
        if (method.owner != "java/lang/Class" || method.name != "forName") return
        method.owner = "mc/kettle/remapper/RemappedMethods"
    }

    static void remapVirtual(AbstractInsnNode insn) {
        MethodInsnNode method = insn as MethodInsnNode

        if (!(method.owner == "java/lang/Package" && method.name == "getName") &&
                !(method.owner == "java/lang/Class" && (method.name == "getField" || method.name == "getDeclaredField"
                        || method.name == "getMethod" || method.name == "getDeclaredMethod"
                        || method.name == "getName" || method.name == "getSimpleName")) &&
                !(method.owner == "java/lang/reflect/Field" && method.name == "getName") &&
                !(method.owner == "java/lang/reflect/Method" && method.name == "getName")) {
            return
        }

        Type returnType = Type.getReturnType(method.desc)
        ArrayList<Type> args = new ArrayList<>()
        args.add(Type.getObjectType(method.owner))
        args.addAll(Type.getArgumentTypes(method.desc))
        method.setOpcode(Opcodes.INVOKESTATIC)
        method.owner = "mc/kettle/remapper/RemappedMethods"
        method.desc = Type.getMethodDescriptor(returnType, args.toArray(new Type[args.size()]) as Type)
    }
}

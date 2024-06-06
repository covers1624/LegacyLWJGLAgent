package net.covers1624.lwjglagent.shim;

import net.covers1624.quack.asm.annotation.AnnotationLoader;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.HashMap;
import java.util.Map;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;

/**
 * Created by covers1624 on 5/6/24.
 */
public class ShimLoader {

    public static @Nullable ShimInfo loadShim(byte[] classData) {
        ClassReader cr = new ClassReader(classData);

        Visitor visitor = new Visitor(cr.getClassName(), cr.getSuperName());
        cr.accept(visitor, ClassReader.SKIP_CODE);
        if (visitor.target == null || visitor.target.isEmpty()) return null;

        if (visitor.methods.isEmpty()) {
            throw new RuntimeException("Shim class has no methods? " + cr.getClassName());
        }

        return new ShimInfo(cr.getClassName(), visitor.target, visitor.methods);
    }

    private static String getShimTarget(Shim shim, String superClass) {
        String target = shim.value().replace('.', '/');
        if (!target.isEmpty()) return target;

        if (superClass.equals("java/lang/Object")) {
            throw new RuntimeException("Shim class " + superClass + " extends Object. Extend target or add value to @Shim");
        }
        return superClass;
    }

    private static class Visitor extends ClassVisitor {

        public final String cName;
        private final String sName;
        public final AnnotationLoader annotations;
        public final Map<String, ShimInfo.ShimMethod> methods = new HashMap<>();

        public @Nullable String target;

        public Visitor(String cName, String sName) {
            this(cName, sName, new AnnotationLoader());
        }

        public Visitor(String cName, String sName, AnnotationLoader annotations) {
            super(Opcodes.ASM9, annotations.forClass());
            this.cName = cName;
            this.sName = sName;
            this.annotations = annotations;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);

            if (target == null) {
                Shim shim = annotations.getAnnotation(Shim.class);
                if (shim == null) {
                    target = "";
                } else {
                    target = getShimTarget(shim, sName);
                }
            }
            if (target.isEmpty()) return mv; // No target, do nothing, not a shim class.

            if ((access & ACC_PUBLIC) == 0) return mv;
            if ((access & ACC_STATIC) == 0) return mv;
            if (name.equals("<clinit>")) return mv;

            AnnotationLoader annotations = new AnnotationLoader(true);
            methods.put(
                    name + descriptor,
                    new ShimInfo.ShimMethod(
                            cName,
                            target,
                            access,
                            name,
                            descriptor,
                            signature,
                            exceptions,
                            annotations
                    )
            );
            return annotations.forMethod(mv);
        }
    }

}

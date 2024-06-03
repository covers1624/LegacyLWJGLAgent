package net.covers1624.lwjglagent;

import net.covers1624.lwjglagent.shim.Shim;
import net.covers1624.lwjglagent.shim.Named;
import net.covers1624.quack.asm.annotation.AnnotationLoader;
import net.covers1624.quack.collection.ColUtils;
import net.covers1624.quack.io.IOUtils;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.nio.file.Path;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.objectweb.asm.Opcodes.*;

/**
 * Created by covers1624 on 2/6/24.
 */
public class ClassShimTransformer implements ClassFileTransformer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassShimTransformer.class);

    private final Map<String, String> shims = new HashMap<>();

    public ClassShimTransformer(Path jar) throws IOException {
        LOGGER.info("Scanning for shim classes..");
        try (ZipFile zip = new ZipFile(jar.toFile())) {
            for (ZipEntry entry : ColUtils.iterable(zip.entries())) {
                if (entry.isDirectory()) continue;
                String eName = entry.getName();
                if (!eName.endsWith(".class")) continue;

                try (InputStream is = zip.getInputStream(entry)) {
                    ClassReader cr = new ClassReader(is);
                    AnnotationLoader anns = new AnnotationLoader(true);
                    cr.accept(anns.forClass(), ClassReader.SKIP_CODE);
                    if (anns.getAnnotation(Shim.class) == null) continue;
                    String cName = cr.getClassName();
                    String sName = cr.getSuperName();
                    if (sName.equals("java/lang/Object")) {
                        throw new RuntimeException("Shim class " + cName + " extends Object, must extend target.");
                    }
                    LOGGER.info("  Found @Shim: {} -> {}", cName, sName);
                    shims.put(sName, cName);
                }
            }
        }
        LOGGER.info("Identified {} shim classes.", shims.size());
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] bytes) throws IllegalClassFormatException {
        String shimImpl = shims.get(className);
        if (shimImpl == null) return bytes;

        try {
            return emitStubs(bytes, getClassBytes(shimImpl));
        } catch (Throwable ex) {
            LOGGER.error("Failed to transform {}", className, ex);
            return bytes;
        }
    }

    private static byte[] emitStubs(byte[] bytes, byte[] shimBytes) {
        ClassNode shimNode = readClass(shimBytes, ClassReader.SKIP_CODE);

        ClassReader cr = new ClassReader(bytes);
        ClassWriter cw = new ClassWriter(cr, 0);

        String cName = cr.getClassName();
        LOGGER.info("Found shim class {} for class {}. Emitting bridges..", shimNode.name, cName);

        ClassVisitor transformer = new ClassVisitor(ASM9, cw) {
            final Set<String> existingMethods = new HashSet<>();

            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                existingMethods.add(name + descriptor);
                return super.visitMethod(access, name, descriptor, signature, exceptions);
            }

            @Override
            public void visitEnd() {
                for (MethodNode method : shimNode.methods) {
                    if ((method.access & ACC_PUBLIC) == 0) continue;
                    if ((method.access & ACC_STATIC) == 0) continue;
                    if (method.name.equals("<clinit>")) continue;
                    if (existingMethods.contains(method.name + method.desc)) {
                        LOGGER.error("Method already exists at target: {}{}", method.name, method.desc);
                        continue;
                    }
                    AnnotationLoader anns = new AnnotationLoader();
                    method.accept(anns.forMethod());
                    Named named = anns.getAnnotation(Named.class);
                    LOGGER.info("  Making bridge shim for {}{}", method.name, method.desc);
                    if (named != null) {
                        LOGGER.info("   Bridge has explicit name {}", named.value());
                    }

                    int localIdx = 0;
                    MethodVisitor mv = super.visitMethod(method.access, named != null ? named.value() : method.name, method.desc, method.signature, null);
                    mv.visitCode();
                    for (Type arg : Type.getArgumentTypes(method.desc)) {
                        mv.visitVarInsn(arg.getOpcode(ILOAD), localIdx);
                        localIdx += arg.getSize();
                    }

                    mv.visitMethodInsn(INVOKESTATIC, shimNode.name, method.name, method.desc, false);
                    mv.visitInsn(Type.getReturnType(method.desc).getOpcode(IRETURN));
                    mv.visitMaxs(localIdx, localIdx); // Max locals and stack are identical for static bouncer methods.
                    mv.visitEnd();
                }
            }
        };

        cr.accept(transformer, 0);
        LOGGER.info("Bridges made!");
        return cw.toByteArray();
    }

    private static byte[] getClassBytes(String cName) throws IOException {
        try (InputStream is = ClassShimTransformer.class.getResourceAsStream("/" + cName + ".class")) {
            if (is == null) throw new FileNotFoundException("Failed to find class: " + cName);
            return IOUtils.toBytes(is);
        }
    }

    private static ClassNode readClass(byte[] bytes, int flags) {
        ClassReader cr = new ClassReader(bytes);
        ClassNode cNode = new ClassNode();
        cr.accept(cNode, flags);
        return cNode;
    }
}

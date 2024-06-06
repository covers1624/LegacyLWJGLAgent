package net.covers1624.lwjglagent;

import net.covers1624.lwjglagent.shim.Shim;
import net.covers1624.lwjglagent.shim.ShimInfo;
import net.covers1624.lwjglagent.shim.ShimLoader;
import net.covers1624.quack.collection.ColUtils;
import net.covers1624.quack.collection.FastStream;
import net.covers1624.quack.io.IOUtils;
import org.objectweb.asm.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.ClassFileTransformer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.ProtectionDomain;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.objectweb.asm.Opcodes.*;

/**
 * Created by covers1624 on 2/6/24.
 */
public class ClassShimTransformer implements ClassFileTransformer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassShimTransformer.class);

    // Just to avoid rewriting all classes ever loaded.
    // Only transform things which don't match this filter.
    // These should not need to be touched by this agent.
    // TODO sort based on frequency.
    private static final String[] TRANSFORM_EXCLUSIONS = {
            "com/sun/",
            "sun",
            "java/",
            "javax/",
            "com/google/",
            "com/ibm/",
            "akka/",
            "com/typesafe/",
            "org/apache/",
            "org/codehaus/",
            "io/netty/",
            "it/unimi/",
            "joptsimple/",
            "gnu/trove/",
            "org/objectweb/"
    };

    private final Map<String, ShimInfo> targetToShim = new HashMap<>();
    private final Map<String, ShimInfo.RewireShim> rewireShims;

    public ClassShimTransformer(Path jar) throws IOException {
        LOGGER.info("Scanning for shim classes..");
        List<ShimInfo> shims = new ArrayList<>();
        try (ZipFile zip = new ZipFile(jar.toFile())) {
            for (ZipEntry entry : ColUtils.iterable(zip.entries())) {
                if (entry.isDirectory()) continue;
                String eName = entry.getName();
                if (!eName.endsWith(".class")) continue;

                try (InputStream is = zip.getInputStream(entry)) {
                    ShimInfo info = ShimLoader.loadShim(IOUtils.toBytes(is));
                    if (info != null) {
                        shims.add(info);
                        targetToShim.put(info.target, info);
                        LOGGER.info("  Found @Shim: {} -> {}", info.name, info.target);
                    }
                }
            }
        }
        LOGGER.info("Identified {} shim classes.", targetToShim.size());
        rewireShims = FastStream.of(shims)
                .flatMap(e -> e.rewireMethods)
                .toMap(e -> e.target + e.targetName + e.desc, Function.identity());
        LOGGER.info("Identified {} rewire methods.", rewireShims.size());
        rewireShims.forEach((k, v) -> LOGGER.info("{} -> {}.{}{}", k, v.owner, v.name, v.desc));
    }

    public static String getShimTarget(Shim shim, String superClass) {
        String target = shim.value().replace('.', '/');
        if (!target.isEmpty()) return target;

        if (superClass.equals("java/lang/Object")) {
            throw new RuntimeException("Shim class " + superClass + " extends Object. Extend target or add value to @Shim");
        }
        return superClass;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] bytes) {
        if (!shouldTransform(className)) return bytes;
        try {
            ClassReader cr = new ClassReader(bytes);
            ClassWriter cw = new ClassWriter(0);

            AtomicBoolean didModify = new AtomicBoolean(false);
            ClassVisitor cv = rewireVisitor(cw, didModify);

            ShimInfo shim = targetToShim.get(className);
            if (shim != null && !shim.bridgeMethods.isEmpty()) {
                didModify.set(true);
                cv = bridgeVisitor(cv, shim);
            }
            cr.accept(cv, 0);

            if (!didModify.get()) return bytes;

            byte[] modified = cw.toByteArray();
            if (LWJGLAgent.DEBUG) {
                try {
                    Files.write(IOUtils.makeParents(Paths.get("./asm/lwjglagent/" + className + ".class")), modified);
                } catch (IOException ex) {
                    LOGGER.error("Failed to dump to file.", ex);
                }
            }

            return modified;
        } catch (Throwable ex) {
            LOGGER.error("Failed to transform {}", className, ex);
            return bytes;
        }
    }

    private ClassVisitor bridgeVisitor(ClassVisitor delegate, ShimInfo shim) {
        return new ClassVisitor(ASM9, delegate) {
            final Set<String> existingMethods = new HashSet<>();

            @Override
            public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                super.visit(version, access, name, signature, superName, interfaces);
                LOGGER.info("Found shim class {} for class {}. Emitting bridges..", shim.name, name);
            }

            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                existingMethods.add(name + descriptor);
                return super.visitMethod(access, name, descriptor, signature, exceptions);
            }

            @Override
            public void visitEnd() {
                for (ShimInfo.ShimMethod method : shim.bridgeMethods) {
                    if (existingMethods.contains(method.name + method.desc)) {
                        LOGGER.error("Method already exists at target: {}{}", method.name, method.desc);
                        continue;
                    }
                    LOGGER.info("  Making bridge shim for {}{}", method.name, method.desc);
                    if (!method.name.equals(method.targetName)) {
                        LOGGER.info("   Bridge has explicit name {}", method.targetName);
                    }

                    int localIdx = 0;
                    MethodVisitor mv = super.visitMethod(method.access, method.targetName, method.desc, method.signature, null);
                    mv.visitCode();
                    for (Type arg : Type.getArgumentTypes(method.desc)) {
                        mv.visitVarInsn(arg.getOpcode(ILOAD), localIdx);
                        localIdx += arg.getSize();
                    }

                    mv.visitMethodInsn(INVOKESTATIC, method.owner, method.name, method.desc, false);
                    Type ret = Type.getReturnType(method.desc);
                    mv.visitInsn(ret.getOpcode(IRETURN));
                    // Max locals and stack are identical for static bouncer methods.
                    mv.visitMaxs(
                            Math.max(localIdx, ret.getSize()),
                            localIdx
                    );
                    mv.visitEnd();
                }
                LOGGER.info("Bridges made!");
            }
        };
    }

    private ClassVisitor rewireVisitor(ClassVisitor delegate, AtomicBoolean modified) {
        return new ClassVisitor(ASM9, delegate) {

            String cName;

            @Override
            public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                cName = name;
                super.visit(version, access, name, signature, superName, interfaces);
            }

            @Override
            public MethodVisitor visitMethod(int access, String mName, String mDesc, String signature, String[] exceptions) {
                return new MethodVisitor(ASM9, super.visitMethod(access, mName, mDesc, signature, exceptions)) {
                    @Override
                    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean isInterface) {
                        ShimInfo.RewireShim shim = rewireShims.get(owner + name + desc);
                        if (shim != null && FastStream.of(shim.rewire.value()).noneMatch(e -> cName.startsWith(owner))) {
                            modified.set(true);
                            LOGGER.info(
                                    "Rewiring call in {}.{}{} from {}.{}{} -> {}.{}{}",
                                    cName, mName, mDesc,
                                    owner, name, desc,
                                    shim.owner, shim.name, shim.desc
                            );
                            super.visitMethodInsn(opcode, shim.owner, shim.name, shim.desc, isInterface);
                        } else {
                            super.visitMethodInsn(opcode, owner, name, desc, isInterface);
                        }
                    }
                };
            }
        };
    }

    public static boolean shouldTransform(String cName) {
        for (String ex : TRANSFORM_EXCLUSIONS) {
            if (cName.startsWith(ex)) {
                return false;
            }
        }
        return true;
    }
}

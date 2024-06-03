package net.covers1624.lwjglagent.tools;

import net.covers1624.quack.collection.ColUtils;
import net.covers1624.quack.collection.FastStream;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.objectweb.asm.Opcodes.*;

/**
 * Created by covers1624 on 2/6/24.
 */
public class MergeGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MergeGenerator.class);

    private static final List<Path> LWJGL2_PATHS;
    private static final List<Path> LWJGL3_PATHS;

    static {
        try {
            LWJGL2_PATHS = FastStream.of(Files.readAllLines(Paths.get("./lwjgl2.paths")))
                    .map(Paths::get)
                    .toList();
            LWJGL3_PATHS = FastStream.of(Files.readAllLines(Paths.get("./lwjgl3.paths")))
                    .map(Paths::get)
                    .toList();
        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            LOGGER.error("Expected one argument");
            return;
        }
        Map<String, ClassEntry> lwjgl2 = loadJars(LWJGL2_PATHS);
        Map<String, ClassEntry> lwjgl3 = loadJars(LWJGL3_PATHS);

        String targetClass = args[0].replace('.', '/');
        ClassEntry l2Ent = lwjgl2.get(targetClass);
        if (l2Ent == null) {
            LOGGER.error("Class does not exist in LWJGL2 {}", targetClass);
            return;
        }
        ClassEntry l3Ent = lwjgl3.get(targetClass);
        if (l3Ent == null) {
            LOGGER.error("Class does not exist in LWJGL3 {}", targetClass);
            return;
        }

        List<MethodEntry> methods = new ArrayList<>();
        for (MethodEntry method : l2Ent.methods.values()) {
            if ((method.access & ACC_PUBLIC) == 0) continue;
            if ((method.access & ACC_STATIC) == 0) continue;
            if (method.name.equals("<clinit>")) continue;

            if (!l3Ent.methods.containsKey(method.name + method.desc)) {
                methods.add(method);
            }
        }

        if (methods.isEmpty()) {
            LOGGER.info("No stubs!");
            return;
        }
        int lastSlash = targetClass.lastIndexOf("/");
        String shortName = targetClass.substring(lastSlash + 1);

        LOGGER.info("Stub class:");

        generateClass(shortName, targetClass, methods, l3Ent);
    }

    private static void generateClass(String shortName, String targetClass, List<MethodEntry> methods, ClassEntry l3Ent) {
        List<String> imports = new ArrayList<>();
        imports.add("net.covers1624.lwjglagent.StubbedMethod");
        imports.add("net.covers1624.lwjglagent.shim.Shim");

        List<String> l = new ArrayList<>();
        l.add("@Shim");
        l.add("public class " + shortName + " extends " + targetClass.replace('/', '.') + " {");
        for (MethodEntry method : methods) {
            l.add("");
            Type ret = Type.getReturnType(method.desc);
            Type[] args = Type.getArgumentTypes(method.desc);
            StringBuilder sb = new StringBuilder("    public static ");
            sb.append(javaName(imports, ret)).append(" ");
            sb.append(method.name).append("(");
            int localIdx = 0;
            Type lastArg = null;
            for (int i = 0; i < args.length; i++) {
                if (i != 0) {
                    sb.append(", ");
                }
                lastArg = args[i];
                sb.append(javaName(imports, lastArg)).append(" ");
                sb.append(method.getLocalName(localIdx));
                localIdx += lastArg.getSize();
            }
            if (method.exceptions != null) {
                LOGGER.warn("Exceptions not printed for {}", method.name + method.desc);
            }
            sb.append(") {");
            l.add(sb.toString());
            // LWLJGL2 omitted some of the method suffixes, such as f and fv on various GL calls.
            // If we match the correct method we can just emit a bouncer instead of requiring a human.
            String suff = findL3MethodWithSuffix(method, lastArg, l3Ent);
            if (suff != null) {
                StringBuilder builder = new StringBuilder("        ");
                if (ret.getSort() != Type.VOID) {
                    builder.append("return ");
                }
                builder.append(method.name).append(suff).append("(");
                localIdx = 0;
                for (int i = 0; i < args.length; i++) {
                    if (i != 0) {
                        builder.append(", ");
                    }
                    lastArg = args[i];
                    builder.append(method.getLocalName(localIdx));
                    localIdx += lastArg.getSize();
                }
                builder.append(");");
                l.add(builder.toString());
            } else {
                l.add("        throw new StubbedMethod();");
            }
            l.add("    }");
        }
        l.add("}");
        imports.sort(Comparator.naturalOrder());
        imports.forEach(e -> System.out.println("import " + e + ";"));
        System.out.println();
        l.forEach(System.out::println);
    }

    private static @Nullable String findL3MethodWithSuffix(MethodEntry method, @Nullable Type lastArg, ClassEntry l3Ent) {
        if (lastArg == null) return null;
        String letter;
        switch(lastArg.getClassName()) {
            case "java.nio.IntBuffer":
                letter = "i";
                break;
            case "java.nio.FloatBuffer":
                letter = "f";
                break;
            case "java.nio.DoubleBuffer":
                letter = "d";
                break;
            default:
                return null;
        }
        // glGetTexParameter(IILjava/nio/FloatBuffer;)V -> glGetTexParameterf(IILjava/nio/FloatBuffer;)V
        if (l3Ent.methods.containsKey(method.name + letter + method.desc)) {
            return letter;
        }
        // glGetTexParameter(IILjava/nio/FloatBuffer;)V -> glGetTexParameterfv(IILjava/nio/FloatBuffer;)V
        if (l3Ent.methods.containsKey(method.name + letter + "v" + method.desc)) {
            return letter + "v";
        }
        return null;
    }

    private static String javaName(List<String> imports, Type type) {
        String name = type.getClassName();
        if (type.getSort() == Type.OBJECT) {
            int lastDot = name.lastIndexOf(".");
            String shortName = name.substring(lastDot + 1);
            if (name.equals("java.lang." + shortName)) return shortName;
            if (imports.contains(name)) return shortName;
            imports.add(name);
            return shortName;
        }
        return name;
    }

    private static Map<String, ClassEntry> loadJars(List<Path> jars) throws IOException {
        Map<String, ClassEntry> entries = new HashMap<>();
        for (Path jar : jars) {
            loadJar(jar, entries);
        }
        return entries;
    }

    private static void loadJar(Path jar, Map<String, ClassEntry> entries) throws IOException {
        try (ZipFile file = new ZipFile(jar.toFile())) {
            for (ZipEntry entry : ColUtils.iterable(file.entries())) {
                if (entry.isDirectory()) continue;
                String eName = entry.getName();
                if (eName.startsWith("META-INF")) continue;
                if (eName.endsWith("module-info.class")) continue;
                if (!eName.endsWith(".class")) continue;

                try (InputStream is = file.getInputStream(entry)) {
                    ClassReader cr = new ClassReader(is);
                    String cName = cr.getClassName();
                    ClassEntry cEnt = new ClassEntry(cName);
                    cr.accept(cEnt.visitor(), 0);
                    ClassEntry existing = entries.put(cName, cEnt);
                    if (existing != null) {
                        throw new RuntimeException("Duplicate class: " + cName);
                    }
                }
            }
        }
    }

    private static final class ClassEntry {

        public final String name;
        public final Map<String, MethodEntry> methods = new LinkedHashMap<>();

        public ClassEntry(String name) {
            this.name = name;
        }

        public ClassVisitor visitor() {
            return visitor(null);
        }

        public ClassVisitor visitor(@Nullable ClassVisitor delegate) {
            return new ClassVisitor(Opcodes.ASM9, delegate) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                    MethodEntry entry = new MethodEntry(access, name, descriptor, signature, exceptions);
                    methods.put(name + descriptor, entry);
                    return new MethodVisitor(ASM9, super.visitMethod(access, name, descriptor, signature, exceptions)) {
                        @Override
                        public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
                            super.visitLocalVariable(name, descriptor, signature, start, end, index);
                            if (index >= entry.locals.length) return;

                            entry.locals[index] = name;
                        }
                    };
                }
            };
        }
    }

    private static final class MethodEntry {

        public final int access;
        public final String name;
        public final String desc;
        public final @Nullable String sig;
        public final String @Nullable [] exceptions;
        public final String[] locals;

        public MethodEntry(int access, String name, String desc, @Nullable String sig, String @Nullable [] exceptions) {
            this.access = access;
            this.name = name;
            this.desc = desc;
            this.sig = sig;
            this.exceptions = exceptions;

            locals = new String[FastStream.of(Type.getArgumentTypes(desc)).intSum(Type::getSize)];
        }

        public String getLocalName(int idx) {
            if (locals.length == 0) return "arg" + idx;

            String local = locals[idx];
            return local != null ? local : "arg" + idx;
        }
    }
}

package net.covers1624.lwjglagent.tools;

import net.covers1624.lwjglagent.LWJGLAgent;
import net.covers1624.quack.asm.annotation.AnnotationLoader;
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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static net.covers1624.quack.util.SneakyUtils.sneak;
import static org.objectweb.asm.Opcodes.ASM9;

/**
 * Created by covers1624 on 2/6/24.
 */
public class ShimTools {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShimTools.class);

    public final List<Path> lwjgl2Paths;
    public final List<Path> lwjgl3Paths;
    public final Path shimCompileDir;

    public final List<Path> runtimePaths = new ArrayList<>();

    public final Map<Path, Map<String, ClassEntry>> classes;

    private ShimTools() throws IOException {
        lwjgl2Paths = FastStream.of(Files.readAllLines(Paths.get("./lwjgl2.paths")))
                .map(Paths::get)
                .toList();
        lwjgl3Paths = FastStream.of(Files.readAllLines(Paths.get("./lwjgl3.paths")))
                .map(Paths::get)
                .toList();
        Path intellijOutput = Paths.get("./../out/shim/classes");
        Path gradleOutput = Paths.get("./../build/classes/java/shim");
        if (Files.exists(intellijOutput.resolve("org/lwjgl/LWJGLException.class"))) {
            shimCompileDir = intellijOutput;
        } else {
            shimCompileDir = gradleOutput;
        }

        runtimePaths.addAll(lwjgl3Paths);
        runtimePaths.addAll(FastStream.of(lwjgl2Paths)
                .filterNot(e -> LWJGLAgent.isStrippedJar(e.toString()))
                .toList()
        );
        runtimePaths.add(shimCompileDir);

        classes = FastStream.concat(lwjgl2Paths, lwjgl3Paths, FastStream.ofNullable(shimCompileDir))
                .toMap(e -> e, sneak(ShimTools::load));
    }

    public static void main(String[] args) throws IOException {
        ShimTools shimTools = new ShimTools();
        if (args.length == 1) {
            new ShimGenerator(shimTools).generate(args[0].replace('.', '/'));
            return;
        }
        if (args.length == 0) {
            new BinCompatChecker(shimTools).scan();
            return;
        }
        LOGGER.error("Unhandled number of arguments. {}", args.length);
    }

    public Map<String, ClassEntry> getEntries(List<Path> paths) {
        Map<String, ClassEntry> entries = new LinkedHashMap<>();
        for (Path path : paths) {
            entries.putAll(classes.get(path));
        }
        return entries;
    }

    private static Map<String, ClassEntry> load(Path path) throws IOException {
        if (path.toString().endsWith(".jar")) return loadJar(path);
        if (Files.isDirectory(path)) return loadFolder(path);

        throw new RuntimeException("Can't load: " + path);
    }

    private static Map<String, ClassEntry> loadJar(Path jar) throws IOException {
        Map<String, ClassEntry> entries = new LinkedHashMap<>();
        try (ZipFile file = new ZipFile(jar.toFile())) {
            for (ZipEntry entry : ColUtils.iterable(file.entries())) {
                if (entry.isDirectory()) continue;
                String eName = entry.getName();
                if (eName.startsWith("META-INF")) continue;
                if (eName.endsWith("module-info.class")) continue;
                if (!eName.endsWith(".class")) continue;

                try (InputStream is = file.getInputStream(entry)) {
                    loadClass(is, entries);
                }
            }
        }
        return entries;
    }

    private static Map<String, ClassEntry> loadFolder(Path folder) throws IOException {
        Map<String, ClassEntry> entries = new LinkedHashMap<>();
        try (Stream<Path> stream = Files.walk(folder)) {
            for (Path path : FastStream.of(stream)) {
                if (Files.isDirectory(path)) continue;
                String fName = path.getFileName().toString();
                if (fName.contains("META-INF")) continue;
                if (fName.equals("module-info.class")) continue;
                if (!fName.endsWith(".class")) continue;

                try (InputStream is = Files.newInputStream(path)) {
                    loadClass(is, entries);
                }
            }
        }
        return entries;
    }

    private static void loadClass(InputStream is, Map<String, ClassEntry> entries) throws IOException {
        class Visitor extends ClassVisitor {

            public final Map<String, MethodEntry> methods = new LinkedHashMap<>();

            public Visitor(ClassVisitor parent) {
                super(ASM9, parent);
            }

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
        }
        ClassReader cr = new ClassReader(is);
        AnnotationLoader al = new AnnotationLoader(true);
        Visitor visitor = new Visitor(al.forClass());
        cr.accept(visitor, 0);

        ClassEntry cEnt = new ClassEntry(cr.getClassName(), cr.getSuperName(), cr.getInterfaces(), cr.getAccess(), visitor.methods, al);
        entries.put(cEnt.name, cEnt);
    }

    public static final class ClassEntry {

        public final String name;
        public final String superClass;
        public final String[] interfaces;
        public final int access;
        public final Map<String, MethodEntry> methods;
        public final AnnotationLoader annotations;

        public ClassEntry(String name, String superClass, String[] interfaces, int access, Map<String, MethodEntry> methods, AnnotationLoader annotations) {
            this.name = name;
            this.superClass = superClass;
            this.interfaces = interfaces;
            this.access = access;
            this.methods = methods;
            this.annotations = annotations;
        }
    }

    public static final class MethodEntry {

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

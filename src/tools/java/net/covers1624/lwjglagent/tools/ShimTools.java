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
public class ShimTools {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShimTools.class);

    public final List<Path> lwjgl2Paths;
    public final List<Path> lwjgl3Paths;
    public final Map<String, ClassEntry> lwjgl2Classes;
    public final Map<String, ClassEntry> lwjgl3Classes;

    private ShimTools() throws IOException {
        lwjgl2Paths = FastStream.of(Files.readAllLines(Paths.get("./lwjgl2.paths")))
                .map(Paths::get)
                .toList();
        lwjgl3Paths = FastStream.of(Files.readAllLines(Paths.get("./lwjgl3.paths")))
                .map(Paths::get)
                .toList();
        lwjgl2Classes = loadJars(lwjgl2Paths);
        lwjgl3Classes = loadJars(lwjgl3Paths);
    }

    public static void main(String[] args) throws IOException {
        ShimTools shimTools = new ShimTools();
        if (args.length == 1) {
            new ShimGenerator(shimTools).generate(args[0].replace('.', '/'));
            return;
        }
        LOGGER.error("Unhandled number of arguments. " + args.length);
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

    public static final class ClassEntry {

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

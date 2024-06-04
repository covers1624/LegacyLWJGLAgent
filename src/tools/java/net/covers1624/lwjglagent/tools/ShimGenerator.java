package net.covers1624.lwjglagent.tools;

import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;

/**
 * Created by covers1624 on 4/6/24.
 */
public class ShimGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShimGenerator.class);

    public final ShimTools shimTools;

    public ShimGenerator(ShimTools shimTools) {
        this.shimTools = shimTools;
    }

    public void generate(String target) {
        ShimTools.ClassEntry l2Ent = shimTools.lwjgl2Classes.get(target);
        if (l2Ent == null) {
            LOGGER.error("Class does not exist in LWJGL2 {}", target);
            return;
        }
        ShimTools.ClassEntry l3Ent = shimTools.lwjgl3Classes.get(target);
        if (l3Ent == null) {
            LOGGER.error("Class does not exist in LWJGL3 {}", target);
            return;
        }

        List<ShimTools.MethodEntry> methods = new ArrayList<>();
        for (ShimTools.MethodEntry method : l2Ent.methods.values()) {
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
        int lastSlash = target.lastIndexOf("/");
        String shortName = target.substring(lastSlash + 1);

        LOGGER.info("Stub class:");

        generateClass(shortName, target, methods, l3Ent);
    }

    private void generateClass(String shortName, String targetClass, List<ShimTools.MethodEntry> methods, ShimTools.ClassEntry l3Ent) {
        List<String> imports = new ArrayList<>();
        imports.add("net.covers1624.lwjglagent.StubbedMethod");
        imports.add("net.covers1624.lwjglagent.shim.Shim");

        List<String> l = new ArrayList<>();
        l.add("@Shim");
        l.add("public class " + shortName + " extends " + targetClass.replace('/', '.') + " {");
        for (ShimTools.MethodEntry method : methods) {
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

    private @Nullable String findL3MethodWithSuffix(ShimTools.MethodEntry method, @Nullable Type lastArg, ShimTools.ClassEntry l3Ent) {
        if (lastArg == null) return null;
        String letter;
        switch (lastArg.getClassName()) {
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

    private String javaName(List<String> imports, Type type) {
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
}

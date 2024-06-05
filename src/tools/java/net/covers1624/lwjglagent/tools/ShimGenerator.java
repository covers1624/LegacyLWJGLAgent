package net.covers1624.lwjglagent.tools;

import net.covers1624.quack.collection.FastStream;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.objectweb.asm.Opcodes.*;

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
        ShimTools.ClassEntry l2Ent = shimTools.getEntries(shimTools.lwjgl2Paths).get(target);
        if (l2Ent == null) {
            LOGGER.error("Class does not exist in LWJGL2 {}", target);
            return;
        }
        ShimTools.ClassEntry l3Ent = shimTools.getEntries(shimTools.lwjgl3Paths).get(target);
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

        LOGGER.info("Stub class:");

        generateClass(target, methods, l3Ent);
    }

    public static List<String> generateClass(String targetClass, List<ShimTools.MethodEntry> methods, @Nullable ShimTools.ClassEntry l3Ent) {
        int lastSlash = targetClass.lastIndexOf("/");
        String shortName = targetClass.substring(lastSlash + 1);

        List<String> imports = new ArrayList<>();
        imports.add("net.covers1624.lwjglagent.StubbedMethod");

        List<String> l = new ArrayList<>();

        if (l3Ent != null) {
            imports.add("net.covers1624.lwjglagent.shim.Shim");
            l.add("@Shim");
        }

        l.add("public class " + shortName + " extends " + targetClass.replace('/', '.') + " {");
        for (ShimTools.MethodEntry method : methods) {
            l.add("");
            Type ret = Type.getReturnType(method.desc);
            Type[] args = Type.getArgumentTypes(method.desc);
            StringBuilder sb = new StringBuilder("    ");
            sb.append(getAccess(method.access)).append(" ");
            if (method.name.equals("<init>")) {
                sb.append(shortName);
            } else {
                sb.append(javaName(imports, ret)).append(" ");
                sb.append(method.name);
            }
            sb.append("(");
            int localIdx = (method.access & ACC_STATIC) != 0 ? 0 : 1;
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
            sb.append(") ");
            if (method.exceptions != null) {
                sb.append("throws ");
                for (String exception : method.exceptions) {
                    sb.append(javaName(imports, Type.getObjectType(exception)));
                    sb.append(" ");
                }
            }
            sb.append("{");
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

        return FastStream.of(imports)
                .sorted()
                .map(e -> "import " + e + ";")
                .concat(FastStream.of(""))
                .concat(l)
                .toList();
    }

    public static String getAccess(int access) {
        StringBuilder sb = new StringBuilder();
        if ((access & ACC_PUBLIC) != 0) sb.append("public ");
        if ((access & ACC_PRIVATE) != 0) sb.append("private ");
        if ((access & ACC_PROTECTED) != 0) sb.append("protected ");
        if ((access & ACC_STATIC) != 0) sb.append("static ");
        if ((access & ACC_FINAL) != 0) sb.append("final ");
        if ((access & ACC_SYNCHRONIZED) != 0) sb.append("synchronized ");
        if ((access & ACC_BRIDGE) != 0) sb.append("bridge ");
        if ((access & ACC_VARARGS) != 0) sb.append("varargs ");
        if ((access & ACC_NATIVE) != 0) sb.append("native ");
        if ((access & ACC_ABSTRACT) != 0) sb.append("abstract ");
        if ((access & ACC_STRICT) != 0) sb.append("strict ");
        if ((access & ACC_SYNTHETIC) != 0) sb.append("synthetic ");
        if ((access & ACC_MANDATED) != 0) sb.append("mandated ");
        return sb.toString().trim();
    }

    private static @Nullable String findL3MethodWithSuffix(ShimTools.MethodEntry method, @Nullable Type lastArg, @Nullable ShimTools.ClassEntry l3Ent) {
        if (l3Ent == null) return null;
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
}

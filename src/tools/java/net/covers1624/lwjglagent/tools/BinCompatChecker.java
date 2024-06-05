package net.covers1624.lwjglagent.tools;

import net.covers1624.lwjglagent.ClassShimTransformer;
import net.covers1624.lwjglagent.shim.Shim;
import net.covers1624.lwjglagent.tools.ShimTools.ClassEntry;
import net.covers1624.lwjglagent.tools.ShimTools.MethodEntry;
import net.covers1624.quack.collection.ColUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static net.covers1624.lwjglagent.tools.ShimGenerator.getAccess;
import static org.objectweb.asm.Opcodes.*;

/**
 * Created by covers1624 on 4/6/24.
 */
public class BinCompatChecker {

    private static final Logger LOGGER = LoggerFactory.getLogger(BinCompatChecker.class);

    private final ShimTools tools;
    private final Map<String, ClassEntry> lwjgl2;
    private final Map<String, ClassEntry> runtime;

    private static final List<String> IGNORED_PREFIXES = Arrays.asList(
            "org/lwjgl/util/", // All Util always exists, we keep lwjgl2 util.
            "net/java/games/"
    );

    public BinCompatChecker(ShimTools tools) {
        this.tools = tools;

        lwjgl2 = tools.getEntries(tools.lwjgl2Paths);
        runtime = tools.getEntries(tools.runtimePaths);
    }

    public void scan() {
        // TODO we should probably check class hierarchy as well..
        //      there is also newly inherited methods that aren't handled.
        for (ClassEntry clazz : lwjgl2.values()) {
            if (ColUtils.anyMatch(IGNORED_PREFIXES, clazz.name::startsWith)) continue;
            // TODO PROTECTED, should be checked, but iirc it requires checking the outer class, that's effort :harold:
            if ((clazz.access & ACC_PRIVATE) != 0) continue; // Private inner class
            if ((clazz.access & (ACC_PRIVATE | ACC_PROTECTED | ACC_PUBLIC)) == 0) continue; // package private

            ClassEntry runClazz = runtime.get(clazz.name);
            if (runClazz == null) {
                LOGGER.info(clazz.name);
                LOGGER.info("  Class missing.");
                continue;
            }
            ClassEntry shimClass = getShimClass(clazz);

            List<String> errors = new ArrayList<>();
            for (MethodEntry value : clazz.methods.values()) {
                if ((value.access & ACC_PUBLIC) == 0) continue;
                MethodEntry runEntry = runClazz.methods.get(value.name + value.desc);
                MethodEntry shimEntry = shimClass != null ? shimClass.methods.get(value.name + value.desc) : null;
                if (runEntry == null && shimEntry == null) {
                    errors.add("Missing:" + value.name + value.desc);
                } else if (runEntry != null) {
                    int a2 = value.access & ~(ACC_NATIVE);
                    int a3 = runEntry.access & ~(ACC_NATIVE);
                    if (a2 != a3) {
                        errors.add("Access flags don't match " + value.name + value.desc);
                        errors.add("  2'" + getAccess(a2) + "' -> 3'" + getAccess(a3) + "'");
                    }

                }
            }

            if (!errors.isEmpty()) {
                LOGGER.info(clazz.name);
                errors.forEach(msg -> LOGGER.info("  {}", msg));
            }
        }
    }

    private @Nullable ClassEntry getShimClass(ClassEntry clazz) {
        for (ClassEntry value : runtime.values()) {
            Shim shim = value.annotations.getAnnotation(Shim.class);
            if (shim == null) continue;
            String target = ClassShimTransformer.getShimTarget(shim, value.superClass);
            if (clazz.name.equals(target)) {
                return value;
            }
        }
        return null;
    }
}

package net.covers1624.lwjglagent.shim;

import net.covers1624.quack.asm.annotation.AnnotationLoader;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by covers1624 on 5/6/24.
 */
public class ShimInfo {

    public final String name;
    public final String target;

    public final Map<String, ShimMethod> methods;

    public final List<ShimMethod> bridgeMethods = new ArrayList<>();
    public final List<RewireShim> rewireMethods = new ArrayList<>();

    public ShimInfo(String name, String target, Map<String, ShimMethod> methods) {
        this.name = name;
        this.target = target;
        this.methods = methods;

        for (ShimMethod value : methods.values()) {
            Rewire rewire = value.annotations.getAnnotation(Rewire.class);
            if (rewire == null) {
                bridgeMethods.add(value);
            } else {
                rewireMethods.add(new RewireShim(value, rewire));
            }
        }
    }

    public static class ShimMethod {

        public final String owner;
        public final String target;
        public final int access;
        public final String name;
        public final String desc;

        public final String signature;
        public final String @Nullable [] exceptions;

        public final AnnotationLoader annotations;

        public final String targetName;

        public ShimMethod(String owner, String target, int access, String name, String desc, String signature, String @Nullable [] exceptions, AnnotationLoader annotations) {
            this.owner = owner;
            this.target = target;
            this.access = access;
            this.name = name;
            this.desc = desc;
            this.signature = signature;
            this.exceptions = exceptions;
            this.annotations = annotations;
            Named named = annotations.getAnnotation(Named.class);
            targetName = named != null ? named.value() : name;
        }
    }

    public static final class RewireShim extends ShimMethod {

        public final Rewire rewire;

        public RewireShim(ShimMethod mth, Rewire rewire) {
            super(mth.owner, mth.target, mth.access, mth.name, mth.desc, mth.signature, mth.exceptions, mth.annotations);
            this.rewire = rewire;
        }
    }
}

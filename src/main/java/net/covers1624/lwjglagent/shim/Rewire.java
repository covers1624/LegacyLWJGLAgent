package net.covers1624.lwjglagent.shim;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Any method in a {@link Shim} class annotated with this,
 * will have any call to the target rewritten to the annotated method.
 * <p>
 * This will not create a bridge shim in the target class.
 * <p>
 * May be combined with {@link Named} to override the target method.
 * <p>
 * Created by covers1624 on 5/6/24.
 */
@Target (ElementType.METHOD)
@Retention (RetentionPolicy.RUNTIME)
public @interface Rewire {

    /**
     * The package exclusions filter.
     * <p>
     * This defaults to {@code org/lwjgl/}, preventing any
     * LWJGL code from being rewritten.
     * <p>
     * This is useful to redirect calls _into_ LWJGL.
     */
    String[] value() default { "org/lwjgl/" };
}

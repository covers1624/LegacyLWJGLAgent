package net.covers1624.lwjglagent.shim;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates a Shim class.
 * <p>
 * Shims must have a target, either denoted by the annotated class extending
 * something other than {@link Object}, or by specifying the target manually
 * via {@link #value()}.
 * <p>
 * Each static member inside a Shim class has a bouncer created in the target
 * class, redirecting to the annotated class. The generated methods use the same
 * name, descriptor, signature and exceptions as the member.
 * <p>
 * Shim methods may additionally be annotated with {@link Named} and {@link Rewire}.
 * <p>
 * {@link Named} will set an explicit name for the generated method, instead of using the name
 * of the annotated method. This is useful in cases where there is a parameters + name conflict
 * when extending the target class.
 * <p>
 * {@link Rewire} modifies how the transformer operates, instead of generating a bouncer
 * method inside the target class, will rewrite all calls to the resolved target method
 * with calls directly to the annotated method. {@link Rewire} supports overriding
 * the target name with {@link Named}.
 * <p>
 * Created by covers1624 on 3/6/24.
 */
// TODO shim instance methods, still static method, first param as this.
@Target (ElementType.TYPE)
@Retention (RetentionPolicy.RUNTIME)
public @interface Shim {

    /**
     * The target class. When not specified, the annotated class's
     * parent class is used.
     *
     * @return The target class.
     */
    String value() default "";
}

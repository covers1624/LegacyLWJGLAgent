package net.covers1624.lwjglagent.shim;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Sets an explicit name for the bouncer.
 * <p>
 * Useful in situations where only the return types differ.
 * <p>
 * Created by covers1624 on 3/6/24.
 */
@Target (ElementType.METHOD)
@Retention (RetentionPolicy.RUNTIME)
public @interface Named {

    String value();
}

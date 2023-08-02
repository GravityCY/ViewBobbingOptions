package me.gravityio.viewboboptions.lib.other;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks something as not yet implemented.<br>
 * IDK cause why not
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
public @interface Unimplemented {
}

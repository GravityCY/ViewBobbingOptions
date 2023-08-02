package me.gravityio.viewboboptions.lib.yacl.annotations;

import me.gravityio.viewboboptions.lib.other.Unimplemented;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A Slider for Decimal Numbers
 * 0.0 -> 33.33 -> 66.66 -> 100.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Unimplemented
public @interface DecimalSlider {
    float min() default 0;
    float max() default 100;
}

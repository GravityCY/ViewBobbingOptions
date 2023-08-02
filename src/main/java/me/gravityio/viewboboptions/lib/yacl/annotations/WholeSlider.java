package me.gravityio.viewboboptions.lib.yacl.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A Slider for Whole Numbers
 * 0 -> 33 -> 66 -> 100
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface WholeSlider {
    int min() default 0;
    int max() default 100;
}

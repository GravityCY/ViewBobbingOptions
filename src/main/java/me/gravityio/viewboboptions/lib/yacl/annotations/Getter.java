package me.gravityio.viewboboptions.lib.yacl.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks this method as a getter that will
 * return the value of the field that has the same id of this method <br><br>
 *
 * If this annotation is not given an explicit id, it will be automatically generated based off of the method name
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Getter {
    String id() default "";
}

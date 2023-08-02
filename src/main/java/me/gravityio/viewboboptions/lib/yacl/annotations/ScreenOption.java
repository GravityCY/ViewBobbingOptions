package me.gravityio.viewboboptions.lib.yacl.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks this field as an option to be present in the config screen,
 * will automatically generate a Translatable Text using the fields names, can be overriden by
 * specifying keyLabel or keyDescription
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ScreenOption {
    /**
     * This is used for connecting getters and setters with this
     * field, and is also used for the automatically generated Translatable Text.
     */
    String id() default "";

    /**
     * Overrides the automatically generated Translatable Text Label to use this explicit key
     */
    String labelKey() default "";
    /**
     * Overrides the automatically generated Translatable Text Description to use this explicit key
     */
    String descriptionKey() default "";
}

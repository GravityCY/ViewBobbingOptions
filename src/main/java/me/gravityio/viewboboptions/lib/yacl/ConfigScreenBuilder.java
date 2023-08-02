package me.gravityio.viewboboptions.lib.yacl;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.config.ConfigInstance;
import dev.isxander.yacl3.gui.controllers.BooleanController;
import dev.isxander.yacl3.gui.controllers.slider.IntegerSliderController;
import me.gravityio.viewboboptions.lib.yacl.annotations.*;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Builds a Config Screen for YACL based off of some basic annotations
 */
public class ConfigScreenBuilder {

    public static String DEFAULT_NAMESPACED_FORMAT = "yacl.%s.%s";
    public static String DEFAULT_LABEL_FORMAT = DEFAULT_NAMESPACED_FORMAT + ".label";
    public static String DEFAULT_DESCRIPTION_FORMAT = DEFAULT_NAMESPACED_FORMAT + ".description";

    private static boolean isValidField(Field field) {
        return field.isAnnotationPresent(ScreenOption.class);
    }

    /**
     * Finds the getter method for a particular ID
     */
    private static Method getGetterMethod(Method[] methods, String id) {
        for (Method method : methods) {
            if (!method.isAnnotationPresent(Getter.class)) continue;
            Getter getter = method.getAnnotation(Getter.class);
            var gid = getter.id();
            if (gid.equals("")) gid = method.getName();
            if (gid.equals(id)) return method;
        }
        return null;
    }

    /**
     * Finds the setter method for a particular ID
     */
    private static Method getSetterMethod(Method[] methods, String id) {
        for (Method method : methods) {
            if (!method.isAnnotationPresent(Setter.class)) continue;
            Setter getter = method.getAnnotation(Setter.class);
            var sid = getter.id();
            if (sid.equals("")) sid = method.getName();
            if (sid.equals(id)) return method;
        }
        return null;
    }

    /**
     * Creates a supplier using the getter of a config option
     */
    private static Supplier<Object> getSupplier(Object instance, Method method) {
        return () -> {
            try {
                return method.invoke(instance);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        };
    }

    /**
     * Creates a consumer using the setter of a config option
     */
    private static Consumer<Object> getConsumer(Object instance, Method method) {
        return (v) -> {
            try {
                method.invoke(instance, v);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        };
    }

    private static boolean isValidOption(Field field) {
        return field.isAnnotationPresent(BooleanToggle.class)
                || field.isAnnotationPresent(WholeSlider.class)
                || field.isAnnotationPresent(DecimalSlider.class);
    }

    /**
     * Gets the actual YACL Option using the OptionData
     */
    private static Option<?> getOption(OptionData option) {
        if (!isValidOption(option.field)) return null;

        Option.Builder<?> builder = Option.createBuilder();
        var label = option.getLabel(DEFAULT_LABEL_FORMAT);
        var description = option.getDescription(DEFAULT_DESCRIPTION_FORMAT);

        builder.name(label).description(OptionDescription.of(description));

        if (option.field.isAnnotationPresent(BooleanToggle.class)) {
            var booleanAnnot = option.field.getAnnotation(BooleanToggle.class);
            var useCustomFormatter = booleanAnnot.useCustomFormatter();

            Function<Boolean, Text> formatter;
            if (useCustomFormatter) {
                var namespacedKey = option.getNamespacedKey(DEFAULT_NAMESPACED_FORMAT);
                var on = namespacedKey + ".on";
                var off = namespacedKey + ".off";
                formatter = v -> v ? Text.translatable(on) : Text.translatable(off);
            } else {
                formatter = BooleanController.ON_OFF_FORMATTER;
            }

            ((Option.Builder<Boolean>) builder).controller(opt -> BooleanControllerBuilder.create(opt)
                            .valueFormatter(formatter)
                    ).binding((Boolean) option.def(), () -> (Boolean) option.getter.get(), option.setter::accept);

        } else if (option.field.isAnnotationPresent(WholeSlider.class)) {
            var min = option.field.getAnnotation(WholeSlider.class).min();
            var max = option.field.getAnnotation(WholeSlider.class).max();

            ((Option.Builder<Integer>) builder)
                    .customController(opt -> new IntegerSliderController(opt, min, max, 1, i -> Text.literal("%d%%".formatted(i))))
                    .binding((Integer) option.def(), () -> (Integer) option.getter.get(), option.setter::accept);

        }
        return builder == null ? null : builder.build();
    }

    private static Object doGetField(Object instance, Field field) {
        try {
            return field.get(instance);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates the config screen
     */
    public static Screen getScreen(ConfigInstance<?> instance, Screen parent) {
        var config = (ConfigScreenFrame) instance.getConfig();
        Class<?> conclass = config.getClass();
        if (!conclass.isAnnotationPresent(Config.class)) return null;
        var configAnnot = conclass.getAnnotation(Config.class);
        var namespace = configAnnot.namespace();

        return YetAnotherConfigLib.create(instance, (d, c, builder) -> {
            var category = ConfigCategory.createBuilder()
                    .name(Text.translatable("yacl.%s.title".formatted(namespace)));

            var fields = conclass.getDeclaredFields();
            var options = new HashMap<String, Option<?>>();
            for (Field field : fields) {
                if (!isValidField(field)) continue;
                var data = OptionData.fromField(config, field);
                var option = getOption(data);
                options.put(data.id, option);
                category.option(option);
            }

            config.onFinishingTouches(options);

            builder.title(Text.translatable("yacl.%s.title".formatted(namespace)));
            builder.category(category.build());

            return builder;
        }).generateScreen(parent);
    }

    /**
     * Data Containing everything the option needs to be created
     */
    private record OptionData(String namespace, String id, @Nullable String keyLabel, @Nullable String keyDescription, Object def, Field field, Supplier<Object> getter, Consumer<Object> setter) {

        public Text getLabel(String format) {
            return Text.translatable(this.getLabelKey(format));
        }

        public Text getDescription(String format) {
            return Text.translatable(this.getDescriptionKey(format));
        }

        public String getLabelKey(String format) {
            if (keyLabel != null)
                return keyLabel;
            return this.getNamespacedKey(format);
        }

        public String getDescriptionKey(String format) {
            if (keyDescription != null)
                return keyDescription;
            return this.getNamespacedKey(format);
        }

        public String getNamespacedKey(String format) {
            return format.formatted(namespace, id);
        }

        public static OptionData fromField(Object instance, Field field) {
            var conclass = instance.getClass();

            var methods = conclass.getDeclaredMethods();

            var config = conclass.getAnnotation(Config.class);
            var namespace = config.namespace();

            ScreenOption optionAnnot = field.getAnnotation(ScreenOption.class);
            var id = optionAnnot.id();
            if (id.isEmpty()) id = null;
            var keyLabel = optionAnnot.labelKey();
            if (keyLabel.isEmpty()) keyLabel = null;
            var keyDescription = optionAnnot.descriptionKey();
            if (keyDescription.isEmpty()) keyDescription = null;

            if (id == null) id = field.getName();

            var def = doGetField(instance, field);
            var getter = getGetterMethod(methods, id);
            var setter = getSetterMethod(methods, id);
            var supplier = getSupplier(instance, getter);
            var consumer = getConsumer(instance, setter);
            return new OptionData(namespace, id, keyLabel, keyDescription, def, field, supplier, consumer);
        }
    }

}

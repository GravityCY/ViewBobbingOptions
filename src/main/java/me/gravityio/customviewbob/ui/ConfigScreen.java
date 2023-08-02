package me.gravityio.customviewbob.ui;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.gui.controllers.BooleanController;
import dev.isxander.yacl3.gui.controllers.slider.IntegerSliderController;
import me.gravityio.customviewbob.ModConfig;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ConfigScreen {

    private static final Text TITLE = Text.translatable("config.customviewbob.title");
    private static final Text MAIN_CATEGORY = Text.translatable("config.customviewbob.title");
    private static final Text SHOW_IN_OPTIONS_LABEL = Text.translatable("config.customviewbob.show_in_options.label");
    private static final Text SHOW_IN_OPTIONS_DESCRIPTION = Text.translatable("config.customviewbob.show_in_options.description");
    private static final Text SHOW_IN_OPTIONS_ON = Text.translatable("config.customviewbob.show_in_options.on");
    private static final Text SHOW_IN_OPTIONS_OFF = Text.translatable("config.customviewbob.show_in_options.off");
    private static final Text SEPARATE_BOB_LABEL = Text.translatable("config.customviewbob.seperate_bob.label");
    private static final Text SEPARATE_BOB_DESCRIPTION = Text.translatable("config.customviewbob.seperate_bob.description");
    private static final Text ALL_BOB_LABEL = Text.translatable("config.customviewbob.all_bob.label");
    private static final Text ALL_BOB_DESCRIPTION = Text.translatable("config.customviewbob.all_bob.description");
    private static final Text HAND_BOB_LABEL = Text.translatable("config.customviewbob.hand_bob.label");
    private static final Text HAND_BOB_DESCRIPTION = Text.translatable("config.customviewbob.hand_bob.description");
    private static final Text CAMERA_BOB_LABEL = Text.translatable("config.customviewbob.camera_bob.label");
    private static final Text CAMERA_BOB_DESCRIPTION = Text.translatable("config.customviewbob.camera_bob.description");


    public static Screen getModConfigScreenFactory(Screen parent) {
        return YetAnotherConfigLib.create(ModConfig.GSON, (defaults, config, builder) -> {

            var separateBobToggle = createSeparateBob(defaults, config);
            var showInOptionsToggle = createShowInOptions(defaults, config);
            var allBobSlider = createAllBobSlider(defaults, config);
            var handBobSlider = createHandBob(defaults, config);
            var cameraBobSlider = createCameraBob(defaults, config);
            separateBobToggle.addListener((booleanOption, v) -> {
                allBobSlider.setAvailable(!v);
                handBobSlider.setAvailable(v);
                cameraBobSlider.setAvailable(v);
            });
            allBobSlider.setAvailable(!config.get_separate_bobs());
            handBobSlider.setAvailable(config.get_separate_bobs());
            cameraBobSlider.setAvailable(config.get_separate_bobs());
            var category = TestBuilder.create()
                    .name(MAIN_CATEGORY)
                    .option(
                            showInOptionsToggle,
                            separateBobToggle,
                            allBobSlider,
                            handBobSlider,
                            cameraBobSlider
                    )
                    .build();
            builder.title(TITLE)
                    .category(category);
            builder.save(ModConfig.GSON::save);
            return builder;
        }).generateScreen(parent);
    }

    private static Option<Boolean> createShowInOptions(ModConfig defaults, ModConfig config) {
        return createSimpleBoolean(
                SHOW_IN_OPTIONS_LABEL, SHOW_IN_OPTIONS_DESCRIPTION,
                SHOW_IN_OPTIONS_ON, SHOW_IN_OPTIONS_OFF,
                defaults.show_in_options,
                config::get_show_in_options, config::set_show_in_options
        );
    }

    private static Option<Boolean> createSeparateBob(ModConfig defaults, ModConfig config) {
        return createSimpleBoolean(
                SEPARATE_BOB_LABEL, SEPARATE_BOB_DESCRIPTION,
                null, null,
                defaults.separate_bobs,
                config::get_separate_bobs, config::set_separate_bobs
        );
    }

    private static Option<Integer> createAllBobSlider(ModConfig defaults, ModConfig config) {
        return createSimpleSlider(
                ALL_BOB_LABEL, ALL_BOB_DESCRIPTION,
                defaults.all_bobbing_strength, 0, 100,
                config::get_all_bobbing_strength, config::set_all_bobbing_strength
        );
    }

    private static Option<Integer> createHandBob(ModConfig defaults, ModConfig config) {
        return createSimpleSlider(
                HAND_BOB_LABEL, HAND_BOB_DESCRIPTION,
                defaults.hand_bobbing_strength, 0, 100,
                config::get_hand_bobbing_strength, config::set_hand_bobbing_strength
        );
    }

    private static Option<Integer> createCameraBob(ModConfig defaults, ModConfig config) {
        return createSimpleSlider(
                CAMERA_BOB_LABEL, CAMERA_BOB_DESCRIPTION,
                defaults.camera_bobbing_strength, 0, 100,
                config::get_camera_bobbing_strength, config::set_camera_bobbing_strength
        );
    }

    private static Option<Boolean> createSimpleBoolean(Text label, Text description, @Nullable Text valueOn, @Nullable Text valueOff, boolean def, Supplier<Boolean> getter, Consumer<Boolean> setter) {
        final Function<Boolean, Text> formatter;
        if (valueOn != null) {
            formatter = v -> v ? valueOn : valueOff;
        } else {
            formatter = BooleanController.ON_OFF_FORMATTER;
        }

        return Option.<Boolean>createBuilder()
                .name(label)
                .description(OptionDescription.of(description))
                .controller(opt -> BooleanControllerBuilder.create(opt).valueFormatter(formatter))
                .binding(def, getter, setter)
                .build();
    }

    private static Option<Integer> createSimpleSlider(Text name, Text desc, int def, int min, int max, Supplier<Integer> getter,  Consumer<Integer> setter) {
        return Option.<Integer>createBuilder()
                .name(name)
                .description(OptionDescription.of(desc))
                .customController(opt -> new IntegerSliderController(opt, min, max, 1, i -> Text.literal("%d%%".formatted(i))))
                .binding(def, getter, setter)
                .build();
    }
}

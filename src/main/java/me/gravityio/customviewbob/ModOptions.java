package me.gravityio.customviewbob;

import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;

public class ModOptions {

    public static final SimpleOption<Integer> ALL_BOBBING_STRENGTH = new SimpleOption<>(
            "options.customviewbob.all_bobbing_strength.label",
            value -> Tooltip.of(Text.translatable("options.customviewbob.all_bobbing_strength.description")),
            (optionText, value) -> GameOptions.getGenericValueText(optionText, Text.literal("%s%%".formatted(value))),
            new SimpleOption.ValidatingIntSliderCallbacks(0, 100), 100, value -> {}
    );

    public static final SimpleOption<Integer> HAND_BOBBING_STRENGTH = new SimpleOption<>(
            "options.customviewbob.hand_bobbing_strength.label",
            value -> Tooltip.of(Text.translatable("options.customviewbob.hand_bobbing_strength.description")),
            (optionText, value) -> GameOptions.getGenericValueText(optionText, Text.literal("%s%%".formatted(value))),
            new SimpleOption.ValidatingIntSliderCallbacks(0, 100), 100, value -> {}
    );

    public static final SimpleOption<Integer> CAMERA_BOBBING_STRENGTH = new SimpleOption<>(
            "options.customviewbob.camera_bobbing_strength.label",
            value -> Tooltip.of(Text.translatable("options.customviewbob.camera_bobbing_strength.description")),
            (optionText, value) -> GameOptions.getGenericValueText(optionText, Text.literal("%s%%".formatted(value))),
            new SimpleOption.ValidatingIntSliderCallbacks(0, 100), 100, value -> {}
    );


}

package me.gravityio.viewboboptions;

import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;

public class VanillaOptions {

    public static final SimpleOption<Integer> ALL_BOBBING_STRENGTH = new SimpleOption<>(
            "options.viewboboptions.all_bobbing_strength.label",
            SimpleOption.constantTooltip(Text.translatable("options.viewboboptions.all_bobbing_strength.description")),
            (optionText, value) -> GameOptions.getGenericValueText(optionText, Text.literal("%s%%".formatted(value))),
            new SimpleOption.ValidatingIntSliderCallbacks(0, 100), 100, value -> {}
    );

    public static final SimpleOption<Integer> HAND_BOBBING_STRENGTH = new SimpleOption<>(
            "options.viewboboptions.hand_bobbing_strength.label",
            SimpleOption.constantTooltip(Text.translatable("options.viewboboptions.hand_bobbing_strength.description")),
            (optionText, value) -> GameOptions.getGenericValueText(optionText, Text.literal("%s%%".formatted(value))),
            new SimpleOption.ValidatingIntSliderCallbacks(0, 100), 100, value -> {}
    );

    public static final SimpleOption<Integer> CAMERA_BOBBING_STRENGTH = new SimpleOption<>(
            "options.viewboboptions.camera_bobbing_strength.label",
            SimpleOption.constantTooltip(Text.translatable("options.viewboboptions.camera_bobbing_strength.description")),
            (optionText, value) -> GameOptions.getGenericValueText(optionText, Text.literal("%s%%".formatted(value))),
            new SimpleOption.ValidatingIntSliderCallbacks(0, 100), 100, value -> {}
    );


}

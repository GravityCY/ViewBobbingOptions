package me.gravityio.viewboboptions;

import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;

public class VanillaOptions {

    public static final SimpleOption<Integer> ALL_BOBBING_STRENGTH = new SimpleOption<>(
            "options.vanilla.viewboboptions.all_bobbing_strength.label",
            value -> Tooltip.of(Text.translatable("options.vanilla.viewboboptions.all_bobbing_strength.description")),
            (optionText, value) -> GameOptions.getGenericValueText(optionText, Text.literal("%s%%".formatted(value))),
            new SimpleOption.ValidatingIntSliderCallbacks(0, 100), 100, value -> {}
    );

    public static final SimpleOption<Integer> HAND_BOBBING_STRENGTH = new SimpleOption<>(
            "options.vanilla.viewboboptions.hand_bobbing_strength.label",
            value -> Tooltip.of(Text.translatable("options.vanilla.viewboboptions.hand_bobbing_strength.description")),
            (optionText, value) -> GameOptions.getGenericValueText(optionText, Text.literal("%s%%".formatted(value))),
            new SimpleOption.ValidatingIntSliderCallbacks(0, 100), 100, value -> {}
    );

    public static final SimpleOption<Integer> CAMERA_BOBBING_STRENGTH = new SimpleOption<>(
            "options.vanilla.viewboboptions.camera_bobbing_strength.label",
            value -> Tooltip.of(Text.translatable("options.vanilla.viewboboptions.camera_bobbing_strength.description")),
            (optionText, value) -> GameOptions.getGenericValueText(optionText, Text.literal("%s%%".formatted(value))),
            new SimpleOption.ValidatingIntSliderCallbacks(0, 100), 100, value -> {}
    );


    public static void init() {
        ALL_BOBBING_STRENGTH.setValue(ModConfig.INSTANCE.all_bobbing_strength());
        HAND_BOBBING_STRENGTH.setValue(ModConfig.INSTANCE.hand_bobbing_strength());
        CAMERA_BOBBING_STRENGTH.setValue(ModConfig.INSTANCE.camera_bobbing_strength());
    }
}

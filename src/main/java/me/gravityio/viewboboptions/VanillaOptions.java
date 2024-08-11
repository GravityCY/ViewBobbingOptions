package me.gravityio.viewboboptions;

import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;

public class VanillaOptions {

    public static final OptionInstance<Integer> ALL_BOBBING_STRENGTH = new OptionInstance<>(
            "options.vanilla.viewboboptions.all_bobbing_strength.label",
            value -> Tooltip.create(Component.translatable("options.vanilla.viewboboptions.all_bobbing_strength.description")),
            (optionText, value) -> Options.genericValueLabel(optionText, Component.literal("%s%%".formatted(value))),
            new OptionInstance.IntRange(0, 100), 100, value -> {
                ModConfig.INSTANCE.all_bobbing_strength = value.shortValue();
                ModConfig.GSON.save();
            }
    );

    public static final OptionInstance<Integer> HAND_BOBBING_STRENGTH = new OptionInstance<>(
            "options.vanilla.viewboboptions.hand_bobbing_strength.label",
            value -> Tooltip.create(Component.translatable("options.vanilla.viewboboptions.hand_bobbing_strength.description")),
            (optionText, value) -> Options.genericValueLabel(optionText, Component.literal("%s%%".formatted(value))),
            new OptionInstance.IntRange(0, 100), 100, value -> {
                ModConfig.INSTANCE.hand_bobbing_strength = value.shortValue();
                ModConfig.GSON.save();
            }
    );

    public static final OptionInstance<Integer> CAMERA_BOBBING_STRENGTH = new OptionInstance<>(
            "options.vanilla.viewboboptions.camera_bobbing_strength.label",
            value -> Tooltip.create(Component.translatable("options.vanilla.viewboboptions.camera_bobbing_strength.description")),
            (optionText, value) -> Options.genericValueLabel(optionText, Component.literal("%s%%".formatted(value))),
            new OptionInstance.IntRange(0, 100), 100, value -> {
                ModConfig.INSTANCE.camera_bobbing_strength = value.shortValue();
                ModConfig.GSON.save();
            }
    );


    public static void init() {
        ALL_BOBBING_STRENGTH.set((int) ModConfig.INSTANCE.all_bobbing_strength);
        HAND_BOBBING_STRENGTH.set((int) ModConfig.INSTANCE.hand_bobbing_strength);
        CAMERA_BOBBING_STRENGTH.set((int) ModConfig.INSTANCE.camera_bobbing_strength);
    }
}

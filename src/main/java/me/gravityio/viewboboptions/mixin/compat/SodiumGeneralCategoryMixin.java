package me.gravityio.viewboboptions.mixin.compat;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.gravityio.viewboboptions.ModConfig;
//? if >=1.21 {
import net.caffeinemc.mods.sodium.client.gui.SodiumGameOptionPages;
import net.caffeinemc.mods.sodium.client.gui.options.OptionGroup;
import net.caffeinemc.mods.sodium.client.gui.options.OptionImpl;
import net.caffeinemc.mods.sodium.client.gui.options.control.ControlValueFormatter;
import net.caffeinemc.mods.sodium.client.gui.options.control.SliderControl;
import net.caffeinemc.mods.sodium.client.gui.options.storage.OptionStorage;
//?} else {
/*import me.jellysquid.mods.sodium.client.gui.SodiumGameOptionPages;
import me.jellysquid.mods.sodium.client.gui.options.OptionGroup;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpl;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
import me.jellysquid.mods.sodium.client.gui.options.control.SliderControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.OptionStorage;
*///?}
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Pseudo
@Mixin(value = SodiumGameOptionPages.class, remap = false)
public class SodiumGeneralCategoryMixin {

    @ModifyExpressionValue(method = "general",
            at = @At(
                    value = "INVOKE",
//? if >=1.21 {
                    target = "Lnet/caffeinemc/mods/sodium/client/gui/options/OptionGroup$Builder;add(Lnet/caffeinemc/mods/sodium/client/gui/options/Option;)Lnet/caffeinemc/mods/sodium/client/gui/options/OptionGroup$Builder;",
//?} else {
                    /*target = "Lme/jellysquid/mods/sodium/client/gui/options/OptionGroup$Builder;add(Lme/jellysquid/mods/sodium/client/gui/options/Option;)Lme/jellysquid/mods/sodium/client/gui/options/OptionGroup$Builder;",
*///?}
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
//? if >=1.21 {
                            target = "Lnet/caffeinemc/mods/sodium/client/gui/options/OptionImpl;createBuilder(Ljava/lang/Class;Lnet/caffeinemc/mods/sodium/client/gui/options/storage/OptionStorage;)Lnet/caffeinemc/mods/sodium/client/gui/options/OptionImpl$Builder;",
//?} else {
                            /*target = "Lme/jellysquid/mods/sodium/client/gui/options/OptionImpl;createBuilder(Ljava/lang/Class;Lme/jellysquid/mods/sodium/client/gui/options/storage/OptionStorage;)Lme/jellysquid/mods/sodium/client/gui/options/OptionImpl$Builder;",
*///?}
                            ordinal = 2
                    )
            )
    )
    private static OptionGroup.Builder addBobbingOptions(OptionGroup.Builder original) {
        if (!ModConfig.INSTANCE.show_in_options) return original;

        var storage = new OptionStorage<ModConfig>() {
            @Override
            public ModConfig getData() {
                return ModConfig.INSTANCE;
            }

            @Override
            public void save() {
                ModConfig.GSON.save();
            }
        };

        if (ModConfig.INSTANCE.separate_bobs) {
            var handOpt = OptionImpl.createBuilder(int.class, storage)
                    .setName(Component.translatable("options.sodium.viewboboptions.hand_bobbing_strength.label"))
                    .setTooltip(Component.translatable("options.sodium.viewboboptions.hand_bobbing_strength.description"))
                    .setControl(opt -> new SliderControl(opt, 0, 100, 1, ControlValueFormatter.percentage()))
                    .setBinding((config, v) -> ModConfig.INSTANCE.setHandBobbingStrength(v.shortValue()), config -> (int) ModConfig.INSTANCE.hand_bobbing_strength)
                    .build();
            var cameraOpt = OptionImpl.createBuilder(int.class, storage)
                    .setName(Component.translatable("options.sodium.viewboboptions.camera_bobbing_strength.label"))
                    .setTooltip(Component.translatable("options.sodium.viewboboptions.camera_bobbing_strength.description"))
                    .setControl(opt -> new SliderControl(opt, 0, 100, 1, ControlValueFormatter.percentage()))
                    .setBinding((config, v) -> ModConfig.INSTANCE.setCameraBobbingStrength(v.shortValue()), config -> (int) ModConfig.INSTANCE.camera_bobbing_strength)
                    .build();
            original.add(handOpt).add(cameraOpt);
        } else {
            var allOpt = OptionImpl.createBuilder(int.class, storage)
                    .setName(Component.translatable("options.sodium.viewboboptions.all_bobbing_strength.label"))
                    .setTooltip(Component.translatable("options.sodium.viewboboptions.all_bobbing_strength.description"))
                    .setControl(opt -> new SliderControl(opt, 0, 100, 1, ControlValueFormatter.percentage()))
                    .setBinding((config, v) -> ModConfig.INSTANCE.setAllBobbingStrength(v.shortValue()), config -> (int) ModConfig.INSTANCE.all_bobbing_strength)
                    .build();
            original.add(allOpt);
        }

        var handSwayOpt = OptionImpl.createBuilder(int.class, storage)
                .setName(Component.translatable("options.sodium.viewboboptions.hand_sway_strength.label"))
                .setTooltip(Component.translatable("options.sodium.viewboboptions.hand_sway_strength.description"))
                .setControl(opt -> new SliderControl(opt, 0, 100, 1, ControlValueFormatter.percentage()))
                .setBinding((config, v) -> ModConfig.INSTANCE.setHandSwayStrength(v.shortValue()), config -> (int) ModConfig.INSTANCE.hand_sway_strength)
                .build();
        original.add(handSwayOpt);

        return original;
    }
}

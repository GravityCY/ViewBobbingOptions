package me.gravityio.viewboboptions.mixin.compat;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.gravityio.viewboboptions.ViewBobbingOptions;
import me.gravityio.viewboboptions.ModConfig;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptionPages;
import me.jellysquid.mods.sodium.client.gui.options.OptionGroup;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpl;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
import me.jellysquid.mods.sodium.client.gui.options.control.SliderControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.MinecraftOptionsStorage;
import me.jellysquid.mods.sodium.client.gui.options.storage.OptionStorage;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Pseudo
@Mixin(value = SodiumGameOptionPages.class, remap = false)
public class SodiumGeneralCategoryMixin {
    @ModifyExpressionValue(method = "general",
            at = @At(
                    value = "INVOKE",
                    target = "Lme/jellysquid/mods/sodium/client/gui/options/OptionGroup$Builder;add(Lme/jellysquid/mods/sodium/client/gui/options/Option;)Lme/jellysquid/mods/sodium/client/gui/options/OptionGroup$Builder;",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lme/jellysquid/mods/sodium/client/gui/options/OptionGroup;createBuilder()Lme/jellysquid/mods/sodium/client/gui/options/OptionGroup$Builder;",
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
                ModConfig.INSTANCE.save();
            }
        };

        if (ModConfig.INSTANCE.separate_bobs) {
            var handOpt = OptionImpl.createBuilder(int.class, storage)
                    .setName(Text.translatable("options.sodium.viewboboptions.hand_bobbing_strength.label"))
                    .setTooltip(Text.translatable("options.sodium.viewboboptions.hand_bobbing_strength.description"))
                    .setControl(opt -> new SliderControl(opt, 0, 100, 1, ControlValueFormatter.percentage()))
                    .setBinding((config, v) -> ModConfig.INSTANCE.hand_bobbing_strength(v), config -> ModConfig.INSTANCE.hand_bobbing_strength())
                    .build();
            var cameraOpt = OptionImpl.createBuilder(int.class, storage)
                    .setName(Text.translatable("options.sodium.viewboboptions.camera_bobbing_strength.label"))
                    .setTooltip(Text.translatable("options.sodium.viewboboptions.camera_bobbing_strength.description"))
                    .setControl(opt -> new SliderControl(opt, 0, 100, 1, ControlValueFormatter.percentage()))
                    .setBinding((config, v) -> ModConfig.INSTANCE.camera_bobbing_strength(v), config -> ModConfig.INSTANCE.camera_bobbing_strength())
                    .build();
            original.add(handOpt).add(cameraOpt);
        } else {
            var allOpt = OptionImpl.createBuilder(int.class, storage)
                    .setName(Text.translatable("options.sodium.viewboboptions.all_bobbing_strength.label"))
                    .setTooltip(Text.translatable("options.sodium.viewboboptions.all_bobbing_strength.description"))
                    .setControl(opt -> new SliderControl(opt, 0, 100, 1, ControlValueFormatter.percentage()))
                    .setBinding((config, v) -> ModConfig.INSTANCE.all_bobbing_strength(v), config -> ModConfig.INSTANCE.all_bobbing_strength())
                    .build();
            original.add(allOpt);
        }

        return original;
    }
}

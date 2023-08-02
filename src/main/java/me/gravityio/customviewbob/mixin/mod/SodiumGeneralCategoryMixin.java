package me.gravityio.customviewbob.mixin.mod;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.gravityio.customviewbob.CustomViewBobbing;
import me.gravityio.customviewbob.ModConfig;
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
    @Shadow @Final private static MinecraftOptionsStorage vanillaOpts;

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
                CustomViewBobbing.LOGGER.info(String.valueOf(ModConfig.INSTANCE));
                CustomViewBobbing.LOGGER.info(String.valueOf(ModConfig.GSON.getConfig()));

                return ModConfig.INSTANCE;
            }

            @Override
            public void save() {
                ModConfig.GSON.save();
            }
        };

        if (ModConfig.INSTANCE.separate_bobs) {
            var handOpt = OptionImpl.createBuilder(int.class, storage)
                    .setName(Text.translatable("options.customviewbob.hand_bobbing_strength.label"))
                    .setTooltip(Text.translatable("options.customviewbob.hand_bobbing_strength.description"))
                    .setControl(opt -> new SliderControl(opt, 0, 100, 1, ControlValueFormatter.percentage()))
                    .setBinding((config, v) -> ModConfig.INSTANCE.set_hand_bobbing_strength(v), config -> ModConfig.INSTANCE.get_hand_bobbing_strength())
                    .build();
            var cameraOpt = OptionImpl.createBuilder(int.class, storage)
                    .setName(Text.translatable("options.customviewbob.camera_bobbing_strength.label"))
                    .setTooltip(Text.translatable("options.customviewbob.camera_bobbing_strength.description"))
                    .setControl(opt -> new SliderControl(opt, 0, 100, 1, ControlValueFormatter.percentage()))
                    .setBinding((config, v) -> ModConfig.INSTANCE.set_camera_bobbing_strength(v), config -> ModConfig.INSTANCE.get_camera_bobbing_strength())
                    .build();
            original.add(handOpt).add(cameraOpt);
        } else {
            var allOpt = OptionImpl.createBuilder(int.class, storage)
                    .setName(Text.translatable("options.customviewbob.all_bobbing_strength.label"))
                    .setTooltip(Text.translatable("options.customviewbob.all_bobbing_strength.description"))
                    .setControl(opt -> new SliderControl(opt, 0, 100, 1, ControlValueFormatter.percentage()))
                    .setBinding((config, v) -> ModConfig.INSTANCE.set_all_bobbing_strength(v), config -> ModConfig.INSTANCE.get_all_bobbing_strength())
                    .build();
            original.add(allOpt);
        }

        return original;
    }
}

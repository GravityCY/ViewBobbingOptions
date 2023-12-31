package me.gravityio.viewboboptions.mixin.mod;

import me.gravityio.viewboboptions.ModConfig;
import me.gravityio.viewboboptions.VanillaOptions;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.screen.option.VideoOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.util.Window;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Adds the Vanilla Bobbing Option Instances to the Video Options Screen
 */
@Mixin(VideoOptionsScreen.class)
public class VideoOptionsScreenMixin extends GameOptionsScreen {
    @Shadow private OptionListWidget list;

    public VideoOptionsScreenMixin(Screen parent, GameOptions gameOptions, Text title) {
        super(parent, gameOptions, title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {
        if (!ModConfig.INSTANCE.show_in_options) return;

        if (ModConfig.INSTANCE.separate_bobs) {
            this.list.addOptionEntry(VanillaOptions.HAND_BOBBING_STRENGTH, VanillaOptions.CAMERA_BOBBING_STRENGTH);
        } else {
            this.list.addOptionEntry(VanillaOptions.ALL_BOBBING_STRENGTH, null);
        }

    }

    @Inject(method = "method_19865", at = @At("HEAD"))
    private void onSave(Window window, ButtonWidget button, CallbackInfo ci) {
        if (!ModConfig.INSTANCE.show_in_options) return;

        if (ModConfig.INSTANCE.separate_bobs) {
            ModConfig.INSTANCE.hand_bobbing_strength(VanillaOptions.HAND_BOBBING_STRENGTH.getValue());
            ModConfig.INSTANCE.camera_bobbing_strength(VanillaOptions.CAMERA_BOBBING_STRENGTH.getValue());
        } else {
            ModConfig.INSTANCE.all_bobbing_strength(VanillaOptions.ALL_BOBBING_STRENGTH.getValue());
        }

        ModConfig.GSON.save();
    }

}

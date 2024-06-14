package me.gravityio.viewboboptions.mixin.mod;

import me.gravityio.viewboboptions.ModConfig;
import me.gravityio.viewboboptions.VanillaOptions;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.screen.option.VideoOptionsScreen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VideoOptionsScreen.class)
public abstract class VideoOptionsScreenMixin extends GameOptionsScreen {

    public VideoOptionsScreenMixin(Screen parent, GameOptions gameOptions, Text title) {
        super(parent, gameOptions, title);
    }

    @Inject(method = "addOptions", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {
        if (!ModConfig.INSTANCE.show_in_options) return;

        if (ModConfig.INSTANCE.separate_bobs) {
            this.body.addAll(VanillaOptions.HAND_BOBBING_STRENGTH, VanillaOptions.CAMERA_BOBBING_STRENGTH);
        } else {
            this.body.addSingleOptionEntry(VanillaOptions.ALL_BOBBING_STRENGTH);
        }

    }

}

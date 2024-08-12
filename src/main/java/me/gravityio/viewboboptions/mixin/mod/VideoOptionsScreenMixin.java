package me.gravityio.viewboboptions.mixin.mod;

import me.gravityio.viewboboptions.ModConfig;
import me.gravityio.viewboboptions.VanillaOptions;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if >=1.21 {
/*import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.client.gui.screens.options.VideoSettingsScreen;
*///?} else {
import net.minecraft.client.gui.screens.VideoSettingsScreen;
import net.minecraft.client.gui.screens.OptionsSubScreen;
//?}

@Mixin(VideoSettingsScreen.class)
public abstract class VideoOptionsScreenMixin extends OptionsSubScreen {

    //? if <=1.20.5 {
    @Shadow private OptionsList list;
    //?}


    public VideoOptionsScreenMixin(Screen parent, Options gameOptions, Component title) {
        super(parent, gameOptions, title);
    }

    //? if >=1.21 {
    /*@Inject(method = "addOptions", at = @At("TAIL"))
    *///?} else {
    @Inject(method = "init", at = @At("TAIL"))
    //?}
    private void onInit(CallbackInfo ci) {
        if (!ModConfig.INSTANCE.show_in_options) return;

        if (ModConfig.INSTANCE.separate_bobs) {
            this.list.addSmall(VanillaOptions.HAND_BOBBING_STRENGTH, VanillaOptions.CAMERA_BOBBING_STRENGTH);
        } else {
            this.list.addBig(VanillaOptions.ALL_BOBBING_STRENGTH);
        }

    }

}

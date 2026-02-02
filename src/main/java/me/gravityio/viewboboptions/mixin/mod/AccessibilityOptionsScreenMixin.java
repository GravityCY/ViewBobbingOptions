package me.gravityio.viewboboptions.mixin.mod;

import me.gravityio.viewboboptions.ModConfig;
import me.gravityio.viewboboptions.VanillaOptions;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.*;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


//? if >=1.21 {
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.client.gui.screens.options.AccessibilityOptionsScreen;
//?} else {
/*import net.minecraft.client.gui.screens.AccessibilityOptionsScreen;
import net.minecraft.client.gui.screens.SimpleOptionsSubScreen;
import net.minecraft.client.OptionInstance;
*///?}

@Mixin(AccessibilityOptionsScreen.class)
public abstract class AccessibilityOptionsScreenMixin extends /*? if >=1.21 {*/OptionsSubScreen /*?} else {*//*SimpleOptionsSubScreen*//*?}*/ {

    public AccessibilityOptionsScreenMixin(Screen screen, Options options, Component component /*? if <1.21 {*//*,OptionInstance<?>[] optionInstances *//*?}*/) {
        super(screen, options, component/*? if <1.21 {*//*, optionInstances *//*?}*/);
    }

    //? if >=1.21 {
    @Inject(method = "addOptions", at = @At("TAIL"))
    //?} else {
    /*@Inject(method = "init", at = @At("TAIL"))
    *///?}
    private void onInit(CallbackInfo ci) {
        if (!ModConfig.INSTANCE.show_in_options) return;

        if (ModConfig.INSTANCE.separate_bobs) {
            this.list.addSmall(VanillaOptions.HAND_BOBBING_STRENGTH, VanillaOptions.CAMERA_BOBBING_STRENGTH);
        } else {
            this.list.addBig(VanillaOptions.ALL_BOBBING_STRENGTH);
        }

        this.list.addSmall(VanillaOptions.HAND_SWAY_STRENGTH, null);

    }

}

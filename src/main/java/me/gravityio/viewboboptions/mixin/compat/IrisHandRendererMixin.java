package me.gravityio.viewboboptions.mixin.compat;

import com.mojang.blaze3d.vertex.PoseStack;
import me.gravityio.viewboboptions.mixin.TransientMixinData;
import me.gravityio.viewboboptions.mixin.TransientMixinData.BobType;
import net.irisshaders.iris.pathways.HandRenderer;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

//? if >=1.20.5 {
import org.joml.Matrix4fc;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//?} else {
/*import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
*///?}

@Pseudo
@Mixin(HandRenderer.class)
public class IrisHandRendererMixin {
    //? if >=1.20.5 {
    @Inject(method = "setupGlState", at = @At("HEAD"))
    private void setHandBobMode(GameRenderer gameRenderer, Camera camera, Matrix4fc modelMatrix, float tickDelta, CallbackInfoReturnable<PoseStack> cir) {
        TransientMixinData.CURRENT = BobType.HAND;
    }

    @Inject(method = "setupGlState", at = @At("TAIL"))
    private void setNoneBobMode(GameRenderer gameRenderer, Camera camera, Matrix4fc modelMatrix, float tickDelta, CallbackInfoReturnable<PoseStack> cir) {
        TransientMixinData.CURRENT = BobType.NONE;
    }
    //?} else {
    /*@Inject(method = "setupGlState", at = @At("HEAD"))
    private void setHandBobMode(GameRenderer gameRenderer, Camera camera, PoseStack poseStack, float tickDelta, CallbackInfo ci) {
        TransientMixinData.CURRENT = BobType.HAND;
    }

    @Inject(method = "setupGlState", at = @At("TAIL"))
    private void setNoneBobMode(GameRenderer gameRenderer, Camera camera, PoseStack poseStack, float tickDelta, CallbackInfo ci) {
        TransientMixinData.CURRENT = BobType.NONE;
    }
    *///?}
}

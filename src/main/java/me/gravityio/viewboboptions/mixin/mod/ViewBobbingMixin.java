package me.gravityio.viewboboptions.mixin.mod;

import me.gravityio.viewboboptions.ModConfig;
import me.gravityio.viewboboptions.ViewBobbingOptions;
import me.gravityio.viewboboptions.mixin.TransientMixinData;
import me.gravityio.viewboboptions.mixin.TransientMixinData.BobType;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if >=1.20.5 {
import org.joml.Matrix4f;
//?}

//? if >=1.21 {
import net.minecraft.client.DeltaTracker;
//?} elif <=1.20.3 {
/*import com.mojang.blaze3d.vertex.PoseStack;
*///?}

@Mixin(GameRenderer.class)
public abstract class ViewBobbingMixin {

    @Shadow @Final
    private Minecraft minecraft;

    @ModifyVariable(
            method = "bobView",
            at = @At(value = "STORE", ordinal = 0),
            ordinal = 3)
    private float onBobView(float value) {
        if (!ModConfig.INSTANCE.separate_bobs) {
            if (this.minecraft.player != null && ViewBobbingOptions.isStationary(this.minecraft.player))
                return ModConfig.INSTANCE.all_bobbing_strength != 0 ? value * 0.05f : 0f;
            return value * ModConfig.INSTANCE.all_bobbing_strength / 100f;
        }

        return switch(TransientMixinData.CURRENT) {
            case NONE -> value;
            case HAND -> {
                if (this.minecraft.player != null && ViewBobbingOptions.isStationary(this.minecraft.player))
                    yield ModConfig.INSTANCE.hand_bobbing_strength != 0 ? value * 0.05f : 0f;
                yield value * (ModConfig.INSTANCE.hand_bobbing_strength / 100f);
            }
            case CAMERA -> value * (ModConfig.INSTANCE.camera_bobbing_strength / 100f);
        };
    }

    //? if >=1.21.2 {
    @Inject(method = "renderItemInHand",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;bobView(Lcom/mojang/blaze3d/vertex/PoseStack;F)V"))
    private void setHandBobType(Camera camera, float tickDelta, Matrix4f matrix4f, CallbackInfo ci) {
        TransientMixinData.CURRENT = BobType.HAND;
    }
    @Inject(method = "renderItemInHand", at = @At("TAIL"))
    private void setFinishRenderHand(Camera camera, float tickDelta, Matrix4f matrix4f, CallbackInfo ci) {
        TransientMixinData.CURRENT = BobType.NONE;
    }
    //?} elif >=1.20.5 {
    /*@Inject(method = "renderItemInHand",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;resetProjectionMatrix(Lorg/joml/Matrix4f;)V"))
    private void setHandBobType(Camera camera, float tickDelta, Matrix4f matrix4f, CallbackInfo ci) {
        TransientMixinData.CURRENT = BobType.HAND;
    }
    @Inject(method = "renderItemInHand", at = @At("TAIL"))
    private void setFinishRenderHand(Camera camera, float tickDelta, Matrix4f matrix4f, CallbackInfo ci) {
        TransientMixinData.CURRENT = BobType.NONE;
    }
    *///?} elif >=1.20.3 {
    /*@Inject(method = "renderItemInHand",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;resetProjectionMatrix(Lorg/joml/Matrix4f;)V"))
    private void setHandBobType(PoseStack poseStack, Camera camera, float f, CallbackInfo ci) {
        TransientMixinData.CURRENT = BobType.HAND;
    }
    @Inject(method = "renderItemInHand", at = @At("TAIL"))
    private void setFinishRenderHand(PoseStack poseStack, Camera camera, float f, CallbackInfo ci) {
        TransientMixinData.CURRENT = BobType.NONE;
    }
    *///?} else {
    /*@Inject(method = "renderItemInHand",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;resetProjectionMatrix(Lorg/joml/Matrix4f;)V"))
    private void setHandBobType(PoseStack poseStack, Camera camera, float f, CallbackInfo ci) {
        TransientMixinData.CURRENT = BobType.HAND;
    }
    @Inject(method = "renderItemInHand", at = @At("TAIL"))
    private void setFinishRenderHand(PoseStack poseStack, Camera camera, float f, CallbackInfo ci) {
        TransientMixinData.CURRENT = BobType.NONE;
    }
    *///?}

    //? if >=1.21 {
    @Inject(method = "renderLevel", at = @At("HEAD"))
    private void setCameraBobType(DeltaTracker tickCounter, CallbackInfo ci) {
        TransientMixinData.CURRENT = BobType.CAMERA;
    }
    @Inject(method = "renderLevel", at = @At("TAIL"))
    private void setFinishRenderWorld(DeltaTracker tickCounter, CallbackInfo ci) {
        TransientMixinData.CURRENT = BobType.NONE;
    }
    //?} elif >=1.20.5 {
    /*@Inject(method = "renderLevel", at = @At("HEAD"))
    private void setCameraBobType(float f, long l, CallbackInfo ci) {
        TransientMixinData.CURRENT = BobType.CAMERA;
    }
    @Inject(method = "renderLevel", at = @At("TAIL"))
    private void setFinishRenderWorld(float f, long l, CallbackInfo ci) {
        TransientMixinData.CURRENT = BobType.NONE;
    }
    *///?} else {
    /*@Inject(method = "renderLevel", at = @At("HEAD"))
    private void setCameraBobType(float f, long l, PoseStack poseStack, CallbackInfo ci) {
        TransientMixinData.CURRENT = BobType.CAMERA;
    }
    @Inject(method = "renderLevel", at = @At("TAIL"))
    private void setFinishRenderWorld(float f, long l, PoseStack poseStack, CallbackInfo ci) {
        TransientMixinData.CURRENT = BobType.NONE;
    }
    *///?}

}

package me.gravityio.viewboboptions.mixin.mod;

import me.gravityio.viewboboptions.ModConfig;
import me.gravityio.viewboboptions.ViewBobbingOptions;
import me.gravityio.viewboboptions.mixin.TransientMixinData;
import me.gravityio.viewboboptions.mixin.TransientMixinData.BobType;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if >=1.21 {
import net.minecraft.client.DeltaTracker;
//?}

@Mixin(GameRenderer.class)
public abstract class ViewBobbingMixin {

    @Shadow @Final
    Minecraft minecraft;

    @ModifyVariable(
            method = "bobView",
            at = @At(value = "STORE", ordinal = 0),
            ordinal = 3)
    private float onBobView(float value) {
        if (!ModConfig.INSTANCE.separate_bobs)
            return value * ModConfig.INSTANCE.all_bobbing_strength / 100f;

        return switch(TransientMixinData.CURRENT) {
            case NONE -> value;
            case HAND -> {
                if (this.minecraft.player != null && ViewBobbingOptions.isStationary(this.minecraft.player))
                    yield value * 0.05f;
                yield value * (ModConfig.INSTANCE.hand_bobbing_strength / 100f);
            }
            case CAMERA -> value * (ModConfig.INSTANCE.camera_bobbing_strength / 100f);
        };
    }

    @Inject(method = "renderItemInHand",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;resetProjectionMatrix(Lorg/joml/Matrix4f;)V"))
    private void setHandBobType(Camera camera, float tickDelta, Matrix4f matrix4f, CallbackInfo ci) {
        TransientMixinData.CURRENT = BobType.HAND;
    }
    @Inject(method = "renderItemInHand", at = @At("TAIL"))
    private void setFinishRenderHand(Camera camera, float tickDelta, Matrix4f matrix4f, CallbackInfo ci) {
        TransientMixinData.CURRENT = BobType.NONE;
    }

    //? if >=1.21 {
    @Inject(method = "renderLevel", at = @At("HEAD"))
    private void setCameraBobType(DeltaTracker tickCounter, CallbackInfo ci) {
        TransientMixinData.CURRENT = BobType.CAMERA;
    }
    @Inject(method = "renderLevel", at = @At("TAIL"))
    private void setFinishRenderWorld(DeltaTracker tickCounter, CallbackInfo ci) {
        TransientMixinData.CURRENT = BobType.NONE;
    }
    //?} else {
    /*@Inject(method = "renderLevel", at = @At("HEAD"))
    private void setCameraBobType(float f, long l, CallbackInfo ci) {
        TransientMixinData.CURRENT = BobType.CAMERA;
    }
    @Inject(method = "renderLevel", at = @At("TAIL"))
    private void setFinishRenderWorld(float f, long l, CallbackInfo ci) {
        TransientMixinData.CURRENT = BobType.NONE;
    }
    *///?}

}

package me.gravityio.viewboboptions.mixin.mod;

import me.gravityio.viewboboptions.ModConfig;
import me.gravityio.viewboboptions.mixin.BobType;
import me.gravityio.viewboboptions.mixin.TransientMixinData;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(GameRenderer.class)
public class ViewBobbingMixin {

    @ModifyVariable(
            method = "bobView",
            at = @At(value = "STORE", ordinal = 0),
            ordinal = 3)
    private float onBobView(float value) {
        if (!ModConfig.INSTANCE.separate_bobs)
            return value * ModConfig.INSTANCE.all_bobbing_strength / 100f;

        return switch(TransientMixinData.CURRENT) {
            case NONE -> value;
            case HAND -> value * (ModConfig.INSTANCE.hand_bobbing_strength / 100f);
            case CAMERA -> value * (ModConfig.INSTANCE.camera_bobbing_strength / 100f);
        };
    }

    @Inject(method = "renderHand",at = @At(value = "HEAD"))
    private void setHandBobType(MatrixStack matrices, Camera camera, float tickDelta, CallbackInfo ci) {
        TransientMixinData.CURRENT = BobType.HAND;
    }
    @Inject(method = "renderHand", at = @At("TAIL"))
    private void setFinishRenderHand(MatrixStack matrices, Camera camera, float tickDelta, CallbackInfo ci) {
        TransientMixinData.CURRENT = BobType.NONE;
    }

    @Inject(method = "renderWorld", at = @At("HEAD"))
    private void setCameraBobType(float tickDelta, long limitTime, MatrixStack matrices, CallbackInfo ci) {
        TransientMixinData.CURRENT = BobType.CAMERA;
    }
    @Inject(method = "renderWorld", at = @At("TAIL"))
    private void setFinishRenderWorld(float tickDelta, long limitTime, MatrixStack matrices, CallbackInfo ci) {
        TransientMixinData.CURRENT = BobType.NONE;
    }

}

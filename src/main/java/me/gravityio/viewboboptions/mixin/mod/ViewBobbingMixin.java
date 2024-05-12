package me.gravityio.viewboboptions.mixin.mod;

import me.gravityio.viewboboptions.ModConfig;
import me.gravityio.viewboboptions.ViewBobbingOptions;
import me.gravityio.viewboboptions.mixin.TransientMixinData;
import me.gravityio.viewboboptions.mixin.TransientMixinData.BobType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(GameRenderer.class)
public abstract class ViewBobbingMixin {

    @Shadow @Final
    MinecraftClient client;

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
                if (this.client.player != null && ViewBobbingOptions.isStationary(this.client.player))
                    yield value * 0.05f;
                yield value * (ModConfig.INSTANCE.hand_bobbing_strength / 100f);
            }
            case CAMERA -> value * (ModConfig.INSTANCE.camera_bobbing_strength / 100f);
        };
    }

    @Inject(method = "renderHand",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;loadProjectionMatrix(Lorg/joml/Matrix4f;)V"))
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

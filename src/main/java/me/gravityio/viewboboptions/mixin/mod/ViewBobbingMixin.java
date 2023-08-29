package me.gravityio.viewboboptions.mixin.mod;

import me.gravityio.viewboboptions.Helper;
import me.gravityio.viewboboptions.mixin.GlobalMixinData;
import me.gravityio.viewboboptions.mixin.GlobalMixinData.BobType;
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
public class ViewBobbingMixin {

    @Shadow @Final
    MinecraftClient client;

    @ModifyVariable(
            method = "bobView",
            at = @At(value = "STORE", ordinal = 0),
            ordinal = 3)
    private float onBobView(float value) {
        return value * Helper.getBobStrength(this.client.player);
    }

    @Inject(method = "renderHand",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;loadProjectionMatrix(Lorg/joml/Matrix4f;)V"))
    private void setHandBobType(MatrixStack matrices, Camera camera, float tickDelta, CallbackInfo ci) {
        GlobalMixinData.CURRENT = BobType.HAND;
    }
    @Inject(method = "renderHand", at = @At("TAIL"))
    private void setFinishRenderHand(MatrixStack matrices, Camera camera, float tickDelta, CallbackInfo ci) {
        GlobalMixinData.CURRENT = BobType.NONE;
    }

    @Inject(method = "renderWorld", at = @At("HEAD"))
    private void setCameraBobType(float tickDelta, long limitTime, MatrixStack matrices, CallbackInfo ci) {
        GlobalMixinData.CURRENT = BobType.CAMERA;
    }
    @Inject(method = "renderWorld", at = @At("TAIL"))
    private void setFinishRenderWorld(float tickDelta, long limitTime, MatrixStack matrices, CallbackInfo ci) {
        GlobalMixinData.CURRENT = BobType.NONE;
    }

}

package me.gravityio.viewboboptions.mixin.compat;

import me.gravityio.viewboboptions.mixin.TransientMixinData;
import me.gravityio.viewboboptions.mixin.TransientMixinData.BobType;
import net.irisshaders.iris.pathways.HandRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4fc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(HandRenderer.class)
public class IrisHandRendererMixin {
    @Inject(method = "setupGlState", at = @At("HEAD"))
    private void setHandBobMode(GameRenderer gameRenderer, Camera camera, Matrix4fc modelMatrix, float tickDelta, CallbackInfoReturnable<MatrixStack> cir) {
        TransientMixinData.CURRENT = BobType.HAND;
    }

    @Inject(method = "setupGlState", at = @At("TAIL"))
    private void setNoneBobMode(GameRenderer gameRenderer, Camera camera, Matrix4fc modelMatrix, float tickDelta, CallbackInfoReturnable<MatrixStack> cir) {
        TransientMixinData.CURRENT = BobType.NONE;
    }
}

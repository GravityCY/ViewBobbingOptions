package me.gravityio.customviewbob.mixin.mod;

import me.gravityio.customviewbob.mixin.BobType;
import me.gravityio.customviewbob.mixin.TransientMixinData;
import net.coderbot.iris.pipeline.HandRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(HandRenderer.class)
public class IrisHandRendererMixin {
    @Inject(method = "setupGlState", at = @At("HEAD"))
    private void setHandBobMode(GameRenderer par1, Camera par2, MatrixStack par3, float par4, CallbackInfo ci) {
        TransientMixinData.CURRENT = BobType.HAND;
    }

    @Inject(method = "setupGlState", at = @At("TAIL"))
    private void setNoneBobMode(GameRenderer par1, Camera par2, MatrixStack par3, float par4, CallbackInfo ci) {
        TransientMixinData.CURRENT = BobType.NONE;
    }
}

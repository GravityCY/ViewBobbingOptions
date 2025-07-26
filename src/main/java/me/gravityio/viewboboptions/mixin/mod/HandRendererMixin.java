package me.gravityio.viewboboptions.mixin.mod;

import me.gravityio.viewboboptions.ModConfig;
import net.minecraft.client.renderer.ItemInHandRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ItemInHandRenderer.class)
public class HandRendererMixin {

    @ModifyArg(
            method = "renderHandsWithItems",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/math/Axis;rotationDegrees(F)Lorg/joml/Quaternionf;",
                    ordinal = 0
            ),
            index = 0
    )
    private float handSwayStrengthX(float f) {
        return f * ModConfig.INSTANCE.hand_sway_strength / 100f;
    }

    @ModifyArg(
            method = "renderHandsWithItems",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/math/Axis;rotationDegrees(F)Lorg/joml/Quaternionf;",
                    ordinal = 1
            ),
            index = 0
    )
    private float handSwayStrengthY(float f) {
        return f * ModConfig.INSTANCE.hand_sway_strength / 100f;
    }
}

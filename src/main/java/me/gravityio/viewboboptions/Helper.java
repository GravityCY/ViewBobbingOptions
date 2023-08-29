package me.gravityio.viewboboptions;

import me.gravityio.viewboboptions.mixin.GlobalMixinData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;

public class Helper {

    /**
     * Adds the current item held by the player as an override.
     **/
    public static void addCurrentItemAsOverride(PlayerEntity player) {
        for (var stack : player.getHandItems()) {
            if (stack.isEmpty()) continue;
            var id = Registries.ITEM.getId(stack.getItem()).toString();
            if (!ModConfig.INSTANCE.item_overrides.remove(id)) {
                ModConfig.INSTANCE.item_overrides.add(id);
                player.sendMessage(Text.translatable("messages.viewboboptions.added_override", id), true);
            } else {
                player.sendMessage(Text.translatable("messages.viewboboptions.removed_override", id), true);
            }
            ModConfig.GSON.save();
            break;
        }
    }

    /**
     * Gets the bobbing strength.
     */
    public static float getBobStrength(PlayerEntity player) {
        return switch (GlobalMixinData.CURRENT) {
            case NONE -> 1;
            case HAND -> {
                for (var stack : player.getHandItems()) {
                    if (!ModConfig.isItemOverride(stack)) continue;
                    yield 0;
                }

                if (!ModConfig.INSTANCE.separate_bobs)
                    yield ModConfig.INSTANCE.all_bobbing_strength / 100f;
                yield ModConfig.INSTANCE.hand_bobbing_strength / 100f;
            }
            case CAMERA -> {
                if (!ModConfig.INSTANCE.separate_bobs)
                    yield ModConfig.INSTANCE.all_bobbing_strength / 100f;
                yield ModConfig.INSTANCE.camera_bobbing_strength / 100f;
            }
        };
    }

}

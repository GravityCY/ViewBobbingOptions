package me.gravityio.viewboboptions;

import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ViewBobbingOptions implements ClientModInitializer, PreLaunchEntrypoint {
    public static final String MOD_ID = "viewboboptions";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static final KeyBinding ADD_ITEM_BIND = new KeyBinding("viewboboptions.add_item_bind", GLFW.GLFW_KEY_Z, "category.gravityio.name");

    public static boolean isStationary(ClientPlayerEntity player) {
        for (ItemStack handItem : player.getHandItems()) {
            if (handItem.isEmpty()) continue;
            String id = Registries.ITEM.getId(handItem.getItem()).toString();
            if (ModConfig.INSTANCE.stationary_items.contains(id))
                return true;
        }
        return false;
    }

    @Override
    public void onPreLaunch() {
        MixinExtrasBootstrap.init();
        ModConfig.GSON.load();
        ModConfig.INSTANCE = ModConfig.GSON.instance();
    }

    @Override
    public void onInitializeClient() {
        KeyBindingHelper.registerKeyBinding(ADD_ITEM_BIND);
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;
            while (ADD_ITEM_BIND.wasPressed()) {
                onAddItem(client.player);
            }
        });
        VanillaOptions.init();
    }

    private void onAddItem(ClientPlayerEntity player) {
        for (ItemStack handItem : player.getHandItems()) {
            if (handItem.isEmpty()) continue;
            String id = Registries.ITEM.getId(handItem.getItem()).toString();
            if (ModConfig.INSTANCE.stationary_items.contains(id)) {
                player.sendMessage(Text.translatable("messages.viewboboptions.remove_item", id), true);
                ModConfig.INSTANCE.stationary_items.remove(id);
            } else {
                player.sendMessage(Text.translatable("messages.viewboboptions.add_item", id), true);
                ModConfig.INSTANCE.stationary_items.add(id);
            }
            ModConfig.GSON.save();
            break;
        }
    }

}

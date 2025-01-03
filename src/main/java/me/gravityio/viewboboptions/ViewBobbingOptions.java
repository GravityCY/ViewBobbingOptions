package me.gravityio.viewboboptions;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ViewBobbingOptions implements ClientModInitializer {
    public static final String MOD_ID = "viewboboptions";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static final KeyMapping ADD_ITEM_BIND = new KeyMapping("viewboboptions.add_item_bind", GLFW.GLFW_KEY_Z, "category.viewboboptions.name");

    public static ResourceLocation id(String path) {
        //? if >=1.21 {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
        //?} else {
        /*return new ResourceLocation(MOD_ID, path);
        *///?}
    }

    public static boolean isStationary(LocalPlayer player) {
        for (ItemStack handItem : player.getHandSlots()) {
            if (handItem.isEmpty()) continue;
            String id = BuiltInRegistries.ITEM.getKey(handItem.getItem()).toString();
            if (ModConfig.INSTANCE.stationary_items.contains(id))
                return true;
        }
        return false;
    }

    @Override
    public void onInitializeClient() {
        ModConfig.GSON.load();
        ModConfig.INSTANCE = ModConfig.GSON.instance();
        KeyBindingHelper.registerKeyBinding(ADD_ITEM_BIND);
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;
            while (ADD_ITEM_BIND.consumeClick()) {
                this.onAddItem(client.player);
            }
        });
        VanillaOptions.init();
    }

    private Component getItemName(ItemStack stack) {
        //? if >=1.21.2 {
        return stack.getItemName();
        //?} else {
        /*return stack.getHoverName();        *///?}
    }

    private void onAddItem(LocalPlayer player) {
        for (ItemStack handItem : player.getHandSlots()) {
            if (handItem.isEmpty()) continue;
            String id = BuiltInRegistries.ITEM.getKey(handItem.getItem()).toString();
            if (ModConfig.INSTANCE.stationary_items.contains(id)) {
                player.displayClientMessage(Component.translatable("messages.viewboboptions.remove_item", this.getItemName(handItem)), true);
                ModConfig.INSTANCE.stationary_items.remove(id);
            } else {
                player.displayClientMessage(Component.translatable("messages.viewboboptions.add_item", this.getItemName(handItem)), true);
                ModConfig.INSTANCE.stationary_items.add(id);
            }
            ModConfig.GSON.save();
            break;
        }
    }

}

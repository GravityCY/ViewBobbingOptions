package me.gravityio.viewboboptions;

import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.option.KeybindsScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ViewBobbingOptions implements ClientModInitializer, PreLaunchEntrypoint {
    public static final String MOD_ID = "viewboboptions";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static final KeyBinding EXCLUDE_BIND = new KeyBinding("bind.category.viewboboptions.exclude", -1, "bind.category.viewboboptions");

    @Override
    public void onPreLaunch() {
        MixinExtrasBootstrap.init();
        ModConfig.GSON.load();
        ModConfig.INSTANCE = ModConfig.GSON.getConfig();
        ClientTickEvents.END_CLIENT_TICK.register(this::onClientTickEnd);
    }

    @Override
    public void onInitializeClient() {
        KeyBindingHelper.registerKeyBinding(EXCLUDE_BIND);
        VanillaOptions.init();
    }

    public void onClientTickEnd(MinecraftClient client) {
        if (client.player == null) return;
        while (EXCLUDE_BIND.wasPressed()) {
            Helper.addCurrentItemAsOverride(client.player);
        }
    }




}

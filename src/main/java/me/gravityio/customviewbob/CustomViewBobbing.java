package me.gravityio.customviewbob;

import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomViewBobbing implements ClientModInitializer, PreLaunchEntrypoint {
    public static final String MOD_ID = "customviewbob";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onPreLaunch() {
        MixinExtrasBootstrap.init();
        ModConfig.GSON.load();
        ModConfig.INSTANCE = ModConfig.GSON.getConfig();
    }

    @Override
    public void onInitializeClient() {

    }


}

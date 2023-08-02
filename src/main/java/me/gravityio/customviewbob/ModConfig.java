package me.gravityio.customviewbob;


import dev.isxander.yacl3.config.ConfigEntry;
import dev.isxander.yacl3.config.ConfigInstance;
import dev.isxander.yacl3.config.GsonConfigInstance;

import java.nio.file.Path;

public class ModConfig {
    public static ConfigInstance<ModConfig> GSON = GsonConfigInstance.createBuilder(ModConfig.class)
            .setPath(Path.of("./config/customviewbob.json"))
            .build();

    public static ModConfig INSTANCE;

    @ConfigEntry
    public boolean show_in_options = true;
    @ConfigEntry
    public boolean separate_bobs = true;
    @ConfigEntry
    public int hand_bobbing_strength = 100;
    @ConfigEntry
    public int camera_bobbing_strength = 100;
    @ConfigEntry
    public int all_bobbing_strength = 100;

    // Getters
    public boolean get_show_in_options() {
        return show_in_options;
    }
    public boolean get_separate_bobs() {
        return this.separate_bobs;
    }
    public int get_all_bobbing_strength() {
        return this.all_bobbing_strength;
    }
    public int get_hand_bobbing_strength() {
        return hand_bobbing_strength;
    }
    public int get_camera_bobbing_strength() {
        return camera_bobbing_strength;
    }

    // Setters
    public void set_show_in_options(boolean show_in_options){
        this.show_in_options = show_in_options;
    }
    public void set_separate_bobs(boolean separate_bobs) {
        this.separate_bobs = separate_bobs;
    }
    public void set_hand_bobbing_strength(int hand_bobbing_strength) {
        CustomViewBobbing.LOGGER.info("Setting hand bobbing strength to: " + hand_bobbing_strength);
        this.hand_bobbing_strength = hand_bobbing_strength;
        ModOptions.HAND_BOBBING_STRENGTH.setValue(hand_bobbing_strength);
    }
    public void set_camera_bobbing_strength(int camera_bobbing_strength){
        this.camera_bobbing_strength = camera_bobbing_strength;
        ModOptions.CAMERA_BOBBING_STRENGTH.setValue(camera_bobbing_strength);
    }
    public void set_all_bobbing_strength(int all_bobbing_strength) {
        this.all_bobbing_strength = all_bobbing_strength;
        ModOptions.ALL_BOBBING_STRENGTH.setValue(all_bobbing_strength);
    }
}

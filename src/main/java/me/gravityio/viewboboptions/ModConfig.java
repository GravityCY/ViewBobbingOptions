package me.gravityio.viewboboptions;

import dev.isxander.yacl.config.ConfigEntry;
import dev.isxander.yacl.config.GsonConfigInstance;
import me.gravityio.viewboboptions.lib.yacl.annotations.*;

import java.nio.file.Path;

public class ModConfig {
    public static GsonConfigInstance<ModConfig> GSON = new GsonConfigInstance<>(ModConfig.class, Path.of("config.json"));

    public static ModConfig INSTANCE;

    @ConfigEntry
    @ScreenOption(index = 0)
    @BooleanToggle
    public boolean show_in_options = true;
    @ConfigEntry
    @ScreenOption(index = 1)
    @BooleanToggle
    public boolean separate_bobs = true;
    @ConfigEntry
    @ScreenOption(index = 2)
    @WholeSlider
    public int all_bobbing_strength = 100;
    @ConfigEntry
    @ScreenOption(index = 3)
    @WholeSlider
    public int hand_bobbing_strength = 100;
    @ConfigEntry
    @ScreenOption(index = 4)
    @WholeSlider
    public int camera_bobbing_strength = 100;


    // Getters
    @Getter
    public boolean show_in_options() {
        return show_in_options;
    }
    @Getter
    public boolean separate_bobs() {
        return this.separate_bobs;
    }
    @Getter
    public int all_bobbing_strength() {
        return this.all_bobbing_strength;
    }
    @Getter
    public int hand_bobbing_strength() {
        return hand_bobbing_strength;
    }
    @Getter
    public int camera_bobbing_strength() {
        return camera_bobbing_strength;
    }

    // Setters
    @Setter
    public void show_in_options(boolean show_in_options){
        this.show_in_options = show_in_options;

    }
    @Setter
    public void separate_bobs(boolean separate_bobs) {
        this.separate_bobs = separate_bobs;

    }
    @Setter
    public void all_bobbing_strength(int all_bobbing_strength) {
        this.all_bobbing_strength = all_bobbing_strength;
        hand_bobbing_strength(all_bobbing_strength);
        camera_bobbing_strength(all_bobbing_strength);
        VanillaOptions.ALL_BOBBING_STRENGTH.setValue(all_bobbing_strength);
    }
    @Setter
    public void hand_bobbing_strength(int hand_bobbing_strength) {
        this.hand_bobbing_strength = hand_bobbing_strength;
        VanillaOptions.HAND_BOBBING_STRENGTH.setValue(hand_bobbing_strength);

    }
    @Setter
    public void camera_bobbing_strength(int camera_bobbing_strength){
        this.camera_bobbing_strength = camera_bobbing_strength;
        VanillaOptions.CAMERA_BOBBING_STRENGTH.setValue(camera_bobbing_strength);

    }

    public void save() {
        GSON.save();
    }
}

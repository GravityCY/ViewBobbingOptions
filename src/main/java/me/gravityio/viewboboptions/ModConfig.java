package me.gravityio.viewboboptions;

import dev.isxander.yacl.api.Option;
import dev.isxander.yacl.config.ConfigEntry;
import dev.isxander.yacl.config.GsonConfigInstance;
import me.gravityio.viewboboptions.lib.yacl.ConfigScreenFrame;
import me.gravityio.viewboboptions.lib.yacl.annotations.*;

import java.nio.file.Path;
import java.util.Map;

@Config(namespace = ViewBobbingOptions.MOD_ID)
public class ModConfig implements ConfigScreenFrame {
    public static GsonConfigInstance<ModConfig> GSON = new GsonConfigInstance<>(ModConfig.class, Path.of("config/viewboboptions.json"));

    public static ModConfig INSTANCE;

    @Override
    public void onAfterBuildOptions(Map<String, Option<?>> options) {
        Option<Boolean> separate = (Option<Boolean>) options.get("separate_bobs");
        Option<Integer> all_bobbing = (Option<Integer>) options.get("all_bobbing_strength");
        Option<Integer> hand_bobbing = (Option<Integer>) options.get("hand_bobbing_strength");
        Option<Integer> camera_bobbing = (Option<Integer>) options.get("camera_bobbing_strength");

        all_bobbing.setAvailable(!separate_bobs());
        all_bobbing.addListener((opt, v) -> {
            hand_bobbing.requestSet(v);
            camera_bobbing.requestSet(v);
        });
        hand_bobbing.setAvailable(separate_bobs());
        camera_bobbing.setAvailable(separate_bobs());

        separate.addListener((opt, v) -> {
            all_bobbing.setAvailable(!v);
            hand_bobbing.setAvailable(v);
            camera_bobbing.setAvailable(v);
        });

    }

    @ConfigEntry
    @ScreenOption(index = 0)
    @BooleanToggle(useCustomFormatter = true)
    public boolean show_in_options = true;
    @ConfigEntry
    @ScreenOption(index = 1)
    @BooleanToggle(useCustomFormatter = true)
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

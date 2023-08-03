package me.gravityio.viewboboptions;


import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.config.ConfigEntry;
import dev.isxander.yacl3.config.ConfigInstance;
import dev.isxander.yacl3.config.GsonConfigInstance;
import me.gravityio.viewboboptions.lib.yacl.ConfigScreenFrame;
import me.gravityio.viewboboptions.lib.yacl.annotations.*;

import java.nio.file.Path;
import java.util.Map;

@Config(namespace = "viewboboptions")
public class ModConfig implements ConfigScreenFrame {
    public static ConfigInstance<ModConfig> GSON = GsonConfigInstance.createBuilder(ModConfig.class)
            .setPath(Path.of("./config/viewboboptions.json"))
            .build();

    @Override
    public void onAfterBuildOptions(Map<String, Option<?>> options) {
        var opt_separate_bobs = (Option<Boolean>) options.get("separate_bobs");
        var opt_all_bob = (Option<Integer>) options.get("all_bobbing_strength");
        var opt_hand_bob = (Option<Integer>) options.get("hand_bobbing_strength");
        var opt_camera_bob = (Option<Integer>) options.get("camera_bobbing_strength");
        opt_all_bob.addListener((opt, v) -> {
            opt_hand_bob.requestSet(v);
            opt_camera_bob.requestSet(v);
        });
        opt_all_bob.setAvailable(!this.separate_bobs);
        opt_hand_bob.setAvailable(this.separate_bobs);
        opt_camera_bob.setAvailable(this.separate_bobs);
        opt_separate_bobs.addListener((opt, v) -> {
            opt_all_bob.setAvailable(!v);
            opt_hand_bob.setAvailable(v);
            opt_camera_bob.setAvailable(v);
        });
    }

    public static ModConfig INSTANCE;

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

}

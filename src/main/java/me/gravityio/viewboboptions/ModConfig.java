package me.gravityio.viewboboptions;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import dev.isxander.yacl3.config.ConfigEntry;
import dev.isxander.yacl3.config.ConfigInstance;
import dev.isxander.yacl3.config.GsonConfigInstance;
import me.gravityio.yaclutils.annotations.Config;
import me.gravityio.yaclutils.annotations.Setter;
import me.gravityio.yaclutils.annotations.elements.BooleanToggle;
import me.gravityio.yaclutils.annotations.elements.ScreenOption;
import me.gravityio.yaclutils.annotations.elements.nums.WholeSlider;
import me.gravityio.yaclutils.api.ConfigFrame;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Config(namespace = "viewboboptions")
public class ModConfig implements ConfigFrame<ModConfig> {

    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("viewboboptions.json");

    private static final GsonBuilder GSON_BUILDER = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .setFieldNamingStrategy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);

    public static ConfigInstance<ModConfig> GSON = GsonConfigInstance.createBuilder(ModConfig.class)
            .overrideGsonBuilder(GSON_BUILDER)
            .setPath(CONFIG_PATH)
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

    @Override
    public void onBeforeBuildCategory(String category, ModConfig defaults, ConfigCategory.Builder builder) {
        var excludedItemsList = ListOption.<String>createBuilder()
                .name(Text.translatable("yacl.viewboboptions.item_overrides.label"))
                .description(OptionDescription.of(Text.translatable("yacl.viewboboptions.item_overrides.description")))
                .controller(StringControllerBuilder::create)
                .initial("")
                .binding(defaults.item_overrides, () -> this.item_overrides, this::setItemOverrides);
        builder.group(excludedItemsList.build());
    }

    public static ModConfig INSTANCE;

    /**
     * Whether to show in the vanilla options.
     */
    @ConfigEntry
    @ScreenOption(index = 0)
    @BooleanToggle(useCustomFormatter = true)
    public boolean show_in_options = true;

    /**
     * Whether to separate the Camera, and Hand bobbing options.
     */
    @ConfigEntry
    @ScreenOption(index = 1)
    @BooleanToggle(useCustomFormatter = true)
    public boolean separate_bobs = true;

    /**
     * The Bobbing Strength for both the Camera, and the Hand.
     */
    @ConfigEntry
    @ScreenOption(index = 2)
    @WholeSlider(max = 200, interval = 5)
    public int all_bobbing_strength = 100;

    /**
     * The Bobbing Strength for the Hand.
     */
    @ConfigEntry
    @ScreenOption(index = 3)
    @WholeSlider(max = 200, interval = 5)
    public int hand_bobbing_strength = 100;

    /**
     * The Bobbing Strength for the Camera (aka the whole screen).
     */
    @ConfigEntry
    @ScreenOption(index = 4)
    @WholeSlider(max = 200, interval = 5)
    public int camera_bobbing_strength = 100;

    /**
     * A list of item IDs that should not bob when in the players hand.
     */
    @ConfigEntry
    public List<String> item_overrides = new ArrayList<>(
            List.of(
                    "minecraft:filled_map",
                    "minecraft:compass",
                    "minecraft:clock",
                    "minecraft:recovery_compass"
            )
    );


    public static boolean isItemOverride(ItemStack stack) {
        return INSTANCE.item_overrides.contains(Registries.ITEM.getId(stack.getItem()).toString());
    }

    public void setItemOverrides(List<String> item_overrides) {
        this.item_overrides.clear();
        this.item_overrides.addAll(item_overrides);
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

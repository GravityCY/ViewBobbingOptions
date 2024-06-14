package me.gravityio.viewboboptions;


import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.HashSet;
import java.util.Set;

public class ModConfig {
    public static ConfigClassHandler<ModConfig> GSON = ConfigClassHandler.createBuilder(ModConfig.class)
            .id(Identifier.of(ViewBobbingOptions.MOD_ID, "config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("viewboboptions.json5"))
                    .appendGsonBuilder(GsonBuilder::setPrettyPrinting)
                    .setJson5(true)
                    .build())
            .build();

    public static Screen getScreen(Screen parent) {
        return YetAnotherConfigLib.create(ModConfig.GSON, (defaults, config, builder) -> {
            builder.title(Text.translatable("yacl.viewboboptions.title"));
            var mainCategory = ConfigCategory.createBuilder();
            var showInOptions = Option.<Boolean>createBuilder()
                    .name(Text.translatable("yacl.viewboboptions.show_in_options.label"))
                    .description(OptionDescription.of(Text.translatable("yacl.viewboboptions.show_in_options.description")))
                    .controller(TickBoxControllerBuilder::create)
                    .binding(defaults.show_in_options, () -> config.show_in_options, (v) -> config.show_in_options = v)
                    .build();

            var viewBobbingStrength = Option.<Integer>createBuilder()
                    .name(Text.translatable("yacl.viewboboptions.all_bobbing_strength.label"))
                    .description(OptionDescription.of(Text.translatable("yacl.viewboboptions.all_bobbing_strength.description")))
                    .customController(opt -> IntegerSliderControllerBuilder.create(opt).range(0, 100).step(1).build())
                    .binding((int) defaults.all_bobbing_strength, () -> (int) config.all_bobbing_strength, v -> config.setAllBobbingStrength(v.shortValue()))
                    .available(!config.separate_bobs)
                    .build();

            var handBobbingStrength = Option.<Integer>createBuilder()
                    .name(Text.translatable("yacl.viewboboptions.hand_bobbing_strength.label"))
                    .description(OptionDescription.of(Text.translatable("yacl.viewboboptions.hand_bobbing_strength.description")))
                    .customController(opt -> IntegerSliderControllerBuilder.create(opt).range(0, 100).step(1).build())
                    .binding((int) defaults.hand_bobbing_strength, () -> (int) config.hand_bobbing_strength, v -> config.setHandBobbingStrength(v.shortValue()))
                    .available(config.separate_bobs)
                    .build();

            var cameraBobbingStrength = Option.<Integer>createBuilder()
                    .name(Text.translatable("yacl.viewboboptions.camera_bobbing_strength.label"))
                    .description(OptionDescription.of(Text.translatable("yacl.viewboboptions.camera_bobbing_strength.description")))
                    .customController(opt -> IntegerSliderControllerBuilder.create(opt).range(0, 100).step(1).build())
                    .binding((int) defaults.camera_bobbing_strength, () -> (int) config.camera_bobbing_strength, v -> config.setCameraBobbingStrength(v.shortValue()))
                    .available(config.separate_bobs)
                    .build();

            var seperateBobs = Option.<Boolean>createBuilder()
                    .name(Text.translatable("yacl.viewboboptions.separate_bobs.label"))
                    .description(OptionDescription.of(Text.translatable("yacl.viewboboptions.separate_bobs.description")))
                    .controller(TickBoxControllerBuilder::create)
                    .binding(defaults.separate_bobs, () -> config.separate_bobs, (v) -> config.separate_bobs = v)
                    .listener((opt, v) -> {
                        viewBobbingStrength.setAvailable(!v);
                        handBobbingStrength.setAvailable(v);
                        cameraBobbingStrength.setAvailable(v);
                    })
                    .build();

            mainCategory.name(Text.translatable("yacl.viewboboptions.title"))
                    .option(showInOptions)
                    .option(seperateBobs)
                    .option(viewBobbingStrength)
                    .option(handBobbingStrength)
                    .option(cameraBobbingStrength);

            builder.category(mainCategory.build());
            return builder;
        }).generateScreen(parent);
    }

    public static ModConfig INSTANCE;

    @SerialEntry
    public boolean show_in_options = true;
    @SerialEntry
    public boolean separate_bobs = true;
    @SerialEntry
    public short all_bobbing_strength = 100;
    @SerialEntry
    public short hand_bobbing_strength = 100;
    @SerialEntry
    public short camera_bobbing_strength = 100;
    @SerialEntry
    public Set<String> stationary_items = Util.make(() -> {
        var a = new HashSet<String>();
        a.add("minecraft:compass");
        a.add("minecraft:clock");
        a.add("minecraft:recovery_compass");
        a.add("minecraft:filled_map");
        return a;
    });

    public void setAllBobbingStrength(short all_bobbing_strength) {
        this.all_bobbing_strength = all_bobbing_strength;
        setHandBobbingStrength(all_bobbing_strength);
        setCameraBobbingStrength(all_bobbing_strength);
        VanillaOptions.ALL_BOBBING_STRENGTH.setValue((int) all_bobbing_strength);
    }

    public void setHandBobbingStrength(short hand_bobbing_strength) {
        this.hand_bobbing_strength = hand_bobbing_strength;
        VanillaOptions.HAND_BOBBING_STRENGTH.setValue((int) hand_bobbing_strength);
    }

    public void setCameraBobbingStrength(short camera_bobbing_strength){
        this.camera_bobbing_strength = camera_bobbing_strength;
        VanillaOptions.CAMERA_BOBBING_STRENGTH.setValue((int) camera_bobbing_strength);
    }

}

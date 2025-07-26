package me.gravityio.viewboboptions;


import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.Util;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.HashSet;
import java.util.Set;

public class ModConfig {
    public static ConfigClassHandler<ModConfig> GSON = ConfigClassHandler.createBuilder(ModConfig.class)
            .id(ViewBobbingOptions.id("config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("viewboboptions.json5"))
                    .appendGsonBuilder(GsonBuilder::setPrettyPrinting)
                    .setJson5(true)
                    .build())
            .build();

    public static Screen getScreen(Screen parent) {
        return YetAnotherConfigLib.create(ModConfig.GSON, (defaults, config, builder) -> {
            builder.title(Component.translatable("yacl.viewboboptions.title"));
            var mainCategory = ConfigCategory.createBuilder();
            var showInOptions = Option.<Boolean>createBuilder()
                    .name(Component.translatable("yacl.viewboboptions.show_in_options.label"))
                    .description(OptionDescription.of(Component.translatable("yacl.viewboboptions.show_in_options.description")))
                    .controller(TickBoxControllerBuilder::create)
                    .binding(defaults.show_in_options, () -> config.show_in_options, (v) -> config.show_in_options = v)
                    .build();

            var viewBobbingStrength = Option.<Integer>createBuilder()
                    .name(Component.translatable("yacl.viewboboptions.all_bobbing_strength.label"))
                    .description(OptionDescription.of(Component.translatable("yacl.viewboboptions.all_bobbing_strength.description")))
                    .customController(opt -> IntegerSliderControllerBuilder.create(opt).range(0, 100).step(1).build())
                    .binding((int) defaults.all_bobbing_strength, () -> (int) config.all_bobbing_strength, v -> config.setAllBobbingStrength(v.shortValue()))
                    .available(!config.separate_bobs)
                    .build();

            var handBobbingStrength = Option.<Integer>createBuilder()
                    .name(Component.translatable("yacl.viewboboptions.hand_bobbing_strength.label"))
                    .description(OptionDescription.of(Component.translatable("yacl.viewboboptions.hand_bobbing_strength.description")))
                    .customController(opt -> IntegerSliderControllerBuilder.create(opt).range(0, 100).step(1).build())
                    .binding((int) defaults.hand_bobbing_strength, () -> (int) config.hand_bobbing_strength, v -> config.setHandBobbingStrength(v.shortValue()))
                    .available(config.separate_bobs)
                    .build();

            var cameraBobbingStrength = Option.<Integer>createBuilder()
                    .name(Component.translatable("yacl.viewboboptions.camera_bobbing_strength.label"))
                    .description(OptionDescription.of(Component.translatable("yacl.viewboboptions.camera_bobbing_strength.description")))
                    .customController(opt -> IntegerSliderControllerBuilder.create(opt).range(0, 100).step(1).build())
                    .binding((int) defaults.camera_bobbing_strength, () -> (int) config.camera_bobbing_strength, v -> config.setCameraBobbingStrength(v.shortValue()))
                    .available(config.separate_bobs)
                    .build();

            var handSwayStrength = Option.<Integer>createBuilder()
                    .name(Component.translatable("yacl.viewboboptions.hand_sway_strength.label"))
                    .description(OptionDescription.of(Component.translatable("yacl.viewboboptions.hand_sway_strength.description")))
                    .customController(opt -> IntegerSliderControllerBuilder.create(opt).range(0, 100).step(1).build())
                    .binding((int) defaults.hand_sway_strength, () -> (int) config.hand_sway_strength, v -> config.setHandSwayStrength(v.shortValue()))
                    .available(config.separate_bobs)
                    .build();

            var seperateBobs = Option.<Boolean>createBuilder()
                    .name(Component.translatable("yacl.viewboboptions.separate_bobs.label"))
                    .description(OptionDescription.of(Component.translatable("yacl.viewboboptions.separate_bobs.description")))
                    .controller(TickBoxControllerBuilder::create)
                    .binding(defaults.separate_bobs, () -> config.separate_bobs, (v) -> config.separate_bobs = v)
                    .listener((opt, v) -> {
                        viewBobbingStrength.setAvailable(!v);
                        handBobbingStrength.setAvailable(v);
                        cameraBobbingStrength.setAvailable(v);
                    })
                    .build();

            mainCategory.name(Component.translatable("yacl.viewboboptions.title"))
                    .option(showInOptions)
                    .option(seperateBobs)
                    .option(viewBobbingStrength)
                    .option(handBobbingStrength)
                    .option(cameraBobbingStrength)
                    .option(handSwayStrength);

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
    public short hand_sway_strength = 100;
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
        VanillaOptions.ALL_BOBBING_STRENGTH.set((int) all_bobbing_strength);
    }

    public void setHandBobbingStrength(short hand_bobbing_strength) {
        this.hand_bobbing_strength = hand_bobbing_strength;
        VanillaOptions.HAND_BOBBING_STRENGTH.set((int) hand_bobbing_strength);
    }

    public void setCameraBobbingStrength(short camera_bobbing_strength){
        this.camera_bobbing_strength = camera_bobbing_strength;
        VanillaOptions.CAMERA_BOBBING_STRENGTH.set((int) camera_bobbing_strength);
    }

    public void setHandSwayStrength(short hand_sway_strength){
        this.hand_sway_strength = hand_sway_strength;
        VanillaOptions.HAND_SWAY_STRENGTH.set((int) hand_sway_strength);
    }

}

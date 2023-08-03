package me.gravityio.viewboboptions.lib.yacl;

import dev.isxander.yacl3.api.Option;

import java.util.Map;

/**
 * Needs to be implemented by a Config
 */
public interface ConfigScreenFrame {
    default void onBeforeBuildOptions(Map<String, Option.Builder<?>> options) {};
    default void onAfterBuildOptions(Map<String, Option<?>> options) {}

}

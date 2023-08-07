package me.gravityio.viewboboptions.compat;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.gravityio.viewboboptions.ModConfig;
import me.gravityio.viewboboptions.lib.yacl.ConfigScreenBuilder;

public class ModMenuApiImpl implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> ConfigScreenBuilder.getScreen(ModConfig.GSON, parent);
    }
}

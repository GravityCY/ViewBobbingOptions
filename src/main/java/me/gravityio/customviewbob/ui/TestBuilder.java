package me.gravityio.customviewbob.ui;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionGroup;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

public class TestBuilder {
        private final ConfigCategory.Builder builder;

        public TestBuilder() {
            this.builder = ConfigCategory.createBuilder();
        }

        public static TestBuilder create() {
            return new TestBuilder();
        }

        public TestBuilder name(@NotNull Text name) {
            this.builder.name(name);
            return this;
        }

        public TestBuilder option(@NotNull Option<?> ... options) {
            for (Option<?> option : options)
                this.builder.option(option);
            return this;
        }

        public TestBuilder group(@NotNull OptionGroup... groups) {
            for (OptionGroup group : groups)
                this.builder.group(group);
            return this;
        }

        public TestBuilder tooltip(@NotNull Text... tooltips) {
            this.builder.tooltip(tooltips);
            return this;
        }


        public ConfigCategory build() {
            return this.builder.build();
        }
    }
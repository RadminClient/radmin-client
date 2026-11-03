/*
 * Decompiled with CFR 0.152.
 */
package radmin.client.settings;

import java.util.function.Consumer;
import java.util.function.Supplier;
import radmin.client.gui.widgets.input.WTextBox;
import radmin.client.settings.IVisible;
import radmin.client.settings.Setting;
import radmin.client.settings.StringSetting;

public class ProvidedStringSetting
extends StringSetting {
    public final Supplier<String[]> supplier;

    public ProvidedStringSetting(String name, String description, String defaultValue, Consumer<String> onChanged, Consumer<Setting<String>> onModuleActivated, IVisible visible, Class<? extends WTextBox.Renderer> renderer, boolean wide, Supplier<String[]> supplier) {
        super(name, description, defaultValue, onChanged, onModuleActivated, visible, "", renderer, null, wide);
        this.supplier = supplier;
    }

    public static class Builder
    extends Setting.SettingBuilder<Builder, String, ProvidedStringSetting> {
        private Class<? extends WTextBox.Renderer> renderer;
        private Supplier<String[]> supplier;
        private boolean wide;

        public Builder() {
            super(null);
        }

        public Builder renderer(Class<? extends WTextBox.Renderer> renderer) {
            this.renderer = renderer;
            return this;
        }

        public Builder supplier(Supplier<String[]> supplier) {
            this.supplier = supplier;
            return this;
        }

        public Builder wide() {
            this.wide = true;
            return this;
        }

        @Override
        public ProvidedStringSetting build() {
            return new ProvidedStringSetting(this.name, this.description, (String)this.defaultValue, this.onChanged, this.onModuleActivated, this.visible, this.renderer, this.wide, this.supplier);
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 */
package radmin.client.settings;

import java.util.function.Consumer;
import net.minecraft.nbt.CompoundTag;
import radmin.client.gui.utils.CharFilter;
import radmin.client.gui.widgets.input.WTextBox;
import radmin.client.settings.IVisible;
import radmin.client.settings.Setting;

public class StringSetting
extends Setting<String> {
    public final String placeholder;
    public final Class<? extends WTextBox.Renderer> renderer;
    public final CharFilter filter;
    public final boolean wide;

    public StringSetting(String name, String description, String defaultValue, Consumer<String> onChanged, Consumer<Setting<String>> onModuleActivated, IVisible visible, String placeholder, Class<? extends WTextBox.Renderer> renderer, CharFilter filter, boolean wide) {
        super(name, description, defaultValue, onChanged, onModuleActivated, visible);
        this.placeholder = placeholder;
        this.renderer = renderer;
        this.filter = filter;
        this.wide = wide;
    }

    @Override
    protected String parseImpl(String str) {
        return str;
    }

    @Override
    protected boolean isValueValid(String value) {
        if (this.filter == null) {
            return true;
        }
        for (int i = 0; i < value.length(); ++i) {
            if (this.filter.filter(value, value.charAt(i))) continue;
            return false;
        }
        return true;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putString("value", (String)this.get());
        return tag;
    }

    @Override
    public String load(CompoundTag tag) {
        this.set(tag.getStringOr("value", ""));
        return (String)this.get();
    }

    public static class Builder
    extends Setting.SettingBuilder<Builder, String, StringSetting> {
        private String placeholder;
        private Class<? extends WTextBox.Renderer> renderer;
        private CharFilter filter;
        private boolean wide;

        public Builder() {
            super("");
        }

        public Builder placeholder(String placeholder) {
            this.placeholder = placeholder;
            return this;
        }

        public Builder renderer(Class<? extends WTextBox.Renderer> renderer) {
            this.renderer = renderer;
            return this;
        }

        public Builder filter(CharFilter filter) {
            this.filter = filter;
            return this;
        }

        public Builder wide() {
            this.wide = true;
            return this;
        }

        @Override
        public StringSetting build() {
            return new StringSetting(this.name, this.description, (String)this.defaultValue, this.onChanged, this.onModuleActivated, this.visible, this.placeholder, this.renderer, this.filter, this.wide);
        }
    }
}


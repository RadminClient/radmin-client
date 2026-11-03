/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.ListTag
 *  net.minecraft.nbt.StringTag
 *  net.minecraft.nbt.Tag
 */
package radmin.client.settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import radmin.client.gui.GuiTheme;
import radmin.client.gui.renderer.GuiRenderer;
import radmin.client.gui.utils.CharFilter;
import radmin.client.gui.widgets.containers.WTable;
import radmin.client.gui.widgets.input.WTextBox;
import radmin.client.gui.widgets.pressable.WButton;
import radmin.client.gui.widgets.pressable.WMinus;
import radmin.client.settings.IVisible;
import radmin.client.settings.Setting;

public class StringListSetting
extends Setting<List<String>> {
    public final Class<? extends WTextBox.Renderer> renderer;
    public final CharFilter filter;

    public StringListSetting(String name, String description, List<String> defaultValue, Consumer<List<String>> onChanged, Consumer<Setting<List<String>>> onModuleActivated, IVisible visible, Class<? extends WTextBox.Renderer> renderer, CharFilter filter) {
        super(name, description, defaultValue, onChanged, onModuleActivated, visible);
        this.renderer = renderer;
        this.filter = filter;
    }

    @Override
    protected List<String> parseImpl(String str) {
        return Arrays.asList(str.split(","));
    }

    @Override
    protected boolean isValueValid(List<String> value) {
        return true;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        ListTag valueTag = new ListTag();
        for (int i = 0; i < ((List)this.value).size(); ++i) {
            valueTag.add(i, (Tag)StringTag.valueOf((String)((String)((List)this.get()).get(i))));
        }
        tag.put("value", (Tag)valueTag);
        return tag;
    }

    @Override
    public List<String> load(CompoundTag tag) {
        ((List)this.get()).clear();
        ListTag valueTag = tag.getListOrEmpty("value");
        for (Tag tagI : valueTag) {
            ((List)this.get()).add(tagI.asString().orElse(""));
        }
        return (List)this.get();
    }

    @Override
    public void resetImpl() {
        this.value = new ArrayList((Collection)this.defaultValue);
    }

    public static void fillTable(GuiTheme theme, WTable table, StringListSetting setting) {
        table.clear();
        ArrayList strings = new ArrayList((Collection)setting.get());
        CharFilter filter = setting.filter == null ? (string, c) -> true : setting.filter;
        for (int i = 0; i < ((List)setting.get()).size(); ++i) {
            int msgI = i;
            String message = (String)((List)setting.get()).get(i);
            WTextBox textBox = table.add(theme.textBox(message, filter, setting.renderer)).expandX().widget();
            textBox.action = () -> strings.set(msgI, textBox.get());
            textBox.actionOnUnfocused = () -> setting.set(strings);
            WMinus delete = table.add(theme.minus()).widget();
            delete.action = () -> {
                strings.remove(msgI);
                setting.set(strings);
                StringListSetting.fillTable(theme, table, setting);
            };
            table.row();
        }
        if (!((List)setting.get()).isEmpty()) {
            table.add(theme.horizontalSeparator()).expandX();
            table.row();
        }
        WButton add = table.add(theme.button("Add")).expandX().widget();
        add.action = () -> {
            strings.add("");
            setting.set(strings);
            StringListSetting.fillTable(theme, table, setting);
        };
        WButton reset = table.add(theme.button(GuiRenderer.RESET)).widget();
        reset.action = () -> {
            setting.reset();
            StringListSetting.fillTable(theme, table, setting);
        };
        reset.tooltip = "Reset";
    }

    public static class Builder
    extends Setting.SettingBuilder<Builder, List<String>, StringListSetting> {
        private Class<? extends WTextBox.Renderer> renderer;
        private CharFilter filter;

        public Builder() {
            super(new ArrayList(0));
        }

        @Override
        public Builder defaultValue(String ... defaults) {
            return (Builder)this.defaultValue(defaults != null ? Arrays.asList(defaults) : new ArrayList());
        }

        public Builder renderer(Class<? extends WTextBox.Renderer> renderer) {
            this.renderer = renderer;
            return this;
        }

        public Builder filter(CharFilter filter) {
            this.filter = filter;
            return this;
        }

        @Override
        public StringListSetting build() {
            return new StringListSetting(this.name, this.description, (List)this.defaultValue, this.onChanged, this.onModuleActivated, this.visible, this.renderer, this.filter);
        }
    }
}


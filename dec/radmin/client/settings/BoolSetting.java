/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 */
package radmin.client.settings;

import java.util.List;
import java.util.function.Consumer;
import net.minecraft.nbt.CompoundTag;
import radmin.client.settings.IVisible;
import radmin.client.settings.Setting;

public class BoolSetting
extends Setting<Boolean> {
    private static final List<String> SUGGESTIONS = List.of("true", "false", "toggle");

    private BoolSetting(String name, String description, Boolean defaultValue, Consumer<Boolean> onChanged, Consumer<Setting<Boolean>> onModuleActivated, IVisible visible) {
        super(name, description, defaultValue, onChanged, onModuleActivated, visible);
    }

    @Override
    protected Boolean parseImpl(String str) {
        if (str.equalsIgnoreCase("true") || str.equalsIgnoreCase("1")) {
            return true;
        }
        if (str.equalsIgnoreCase("false") || str.equalsIgnoreCase("0")) {
            return false;
        }
        if (str.equalsIgnoreCase("toggle")) {
            return (Boolean)this.get() == false;
        }
        return null;
    }

    @Override
    protected boolean isValueValid(Boolean value) {
        return true;
    }

    @Override
    public List<String> getSuggestions() {
        return SUGGESTIONS;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putBoolean("value", ((Boolean)this.get()).booleanValue());
        return tag;
    }

    @Override
    public Boolean load(CompoundTag tag) {
        this.set(tag.getBooleanOr("value", false));
        return (Boolean)this.get();
    }

    public static class Builder
    extends Setting.SettingBuilder<Builder, Boolean, BoolSetting> {
        public Builder() {
            super(false);
        }

        @Override
        public BoolSetting build() {
            return new BoolSetting(this.name, this.description, (Boolean)this.defaultValue, this.onChanged, this.onModuleActivated, this.visible);
        }
    }
}


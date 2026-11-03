/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 *  org.lwjgl.PointerBuffer
 */
package radmin.client.settings;

import java.io.File;
import java.util.function.Consumer;
import net.minecraft.nbt.CompoundTag;
import org.lwjgl.PointerBuffer;
import radmin.client.settings.IVisible;
import radmin.client.settings.Setting;

public class FileSetting
extends Setting<File> {
    public final PointerBuffer filters;

    public FileSetting(String name, String description, File defaultValue, Consumer<File> onChanged, Consumer<Setting<File>> onModuleActivated, IVisible visible, PointerBuffer filters) {
        super(name, description, defaultValue, onChanged, onModuleActivated, visible);
        this.filters = filters;
    }

    @Override
    protected File parseImpl(String str) {
        File file = new File(str);
        return file.exists() && file.isFile() ? file : null;
    }

    @Override
    protected boolean isValueValid(File value) {
        return value.exists() && value.isFile();
    }

    @Override
    protected CompoundTag save(CompoundTag tag) {
        if (this.get() != null) {
            tag.putString("file", ((File)this.get()).getAbsolutePath());
        }
        return tag;
    }

    @Override
    protected File load(CompoundTag tag) {
        if (tag.contains("file")) {
            this.set(new File(tag.getStringOr("file", "")));
        }
        return (File)this.get();
    }

    public static class Builder
    extends Setting.SettingBuilder<Builder, File, FileSetting> {
        private PointerBuffer filter;

        public Builder() {
            super(null);
        }

        public Builder filter(PointerBuffer filter) {
            this.filter = filter;
            return this;
        }

        @Override
        public FileSetting build() {
            return new FileSetting(this.name, this.description, (File)this.defaultValue, this.onChanged, this.onModuleActivated, this.visible, this.filter);
        }
    }
}


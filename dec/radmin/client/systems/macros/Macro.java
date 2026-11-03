/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 *  org.meteordev.starscript.Script
 */
package radmin.client.systems.macros;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.meteordev.starscript.Script;
import radmin.client.MeteorClient;
import radmin.client.gui.utils.StarscriptTextBoxRenderer;
import radmin.client.settings.KeybindSetting;
import radmin.client.settings.Setting;
import radmin.client.settings.SettingGroup;
import radmin.client.settings.Settings;
import radmin.client.settings.StringListSetting;
import radmin.client.settings.StringSetting;
import radmin.client.utils.misc.ISerializable;
import radmin.client.utils.misc.Keybind;
import radmin.client.utils.misc.MeteorStarscript;
import radmin.client.utils.player.ChatUtils;

public class Macro
implements ISerializable<Macro> {
    public final Settings settings = new Settings();
    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();
    public Setting<String> name = this.sgGeneral.add(((StringSetting.Builder)((StringSetting.Builder)new StringSetting.Builder().name("name")).description("The name of the macro.")).build());
    public Setting<List<String>> messages = this.sgGeneral.add(((StringListSetting.Builder)((StringListSetting.Builder)((StringListSetting.Builder)new StringListSetting.Builder().name("messages")).description("The messages for the macro to send.")).onChanged(list -> {
        this.dirty = true;
    })).renderer(StarscriptTextBoxRenderer.class).build());
    public Setting<Keybind> keybind = this.sgGeneral.add(((KeybindSetting.Builder)((KeybindSetting.Builder)new KeybindSetting.Builder().name("keybind")).description("The bind to run the macro.")).build());
    private final List<Script> scripts = new ArrayList<Script>(1);
    private boolean dirty;

    public Macro() {
    }

    public Macro(Tag tag) {
        this.fromTag((CompoundTag)tag);
    }

    public boolean onAction(boolean isKey, int value, int modifiers) {
        if (!this.keybind.get().matches(isKey, value, modifiers) || MeteorClient.mc.screen != null) {
            return false;
        }
        return this.onAction();
    }

    public boolean onAction() {
        if (this.dirty) {
            this.scripts.clear();
            for (String message : this.messages.get()) {
                Script script = MeteorStarscript.compile(message);
                if (script == null) continue;
                this.scripts.add(script);
            }
            this.dirty = false;
        }
        for (Script script : this.scripts) {
            String message = MeteorStarscript.run(script);
            if (message == null) continue;
            ChatUtils.sendPlayerMsg(message, false);
        }
        return true;
    }

    @Override
    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();
        tag.put("settings", (Tag)this.settings.toTag());
        return tag;
    }

    @Override
    public Macro fromTag(CompoundTag tag) {
        if (tag.contains("settings")) {
            this.settings.fromTag(tag.getCompoundOrEmpty("settings"));
        }
        return this;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        Macro macro = (Macro)o;
        return Objects.equals(macro.name.get(), this.name.get());
    }
}


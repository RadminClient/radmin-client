/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.chat.Component
 *  org.jetbrains.annotations.NotNull
 */
package radmin.client.systems.modules;

import java.util.Objects;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import radmin.client.MeteorClient;
import radmin.client.addons.AddonManager;
import radmin.client.addons.MeteorAddon;
import radmin.client.gui.GuiTheme;
import radmin.client.gui.widgets.WWidget;
import radmin.client.settings.Settings;
import radmin.client.systems.config.Config;
import radmin.client.systems.modules.Category;
import radmin.client.systems.modules.Modules;
import radmin.client.utils.Utils;
import radmin.client.utils.misc.ISerializable;
import radmin.client.utils.misc.Keybind;
import radmin.client.utils.player.ChatUtils;
import radmin.client.utils.render.color.Color;

public abstract class Module
implements ISerializable<Module>,
Comparable<Module> {
    protected final Minecraft mc;
    public final Category category;
    public final String name;
    public final String title;
    public final String description;
    public final String[] aliases;
    public final Color color;
    public final MeteorAddon addon;
    public final Settings settings = new Settings();
    private boolean active;
    public boolean serialize = true;
    public boolean runInMainMenu = false;
    public boolean autoSubscribe = true;
    public final Keybind keybind = Keybind.none();
    public boolean toggleOnBindRelease = false;
    public boolean chatFeedback = true;
    public boolean favorite = false;

    public Module(Category category, String name, String description, String ... aliases) {
        if (name.contains(" ")) {
            MeteorClient.LOG.warn("Module '{}' contains invalid characters in its name making it incompatible with RadminVPN Client commands.", (Object)name);
        }
        this.mc = Minecraft.getInstance();
        this.category = category;
        this.name = name;
        this.title = Utils.nameToTitle(name);
        this.description = description;
        this.aliases = aliases;
        this.color = Color.fromHsv(Utils.random(0.0, 360.0), 0.35, 1.0);
        String classname = this.getClass().getName();
        for (MeteorAddon addon : AddonManager.ADDONS) {
            if (!classname.startsWith(addon.getPackage())) continue;
            this.addon = addon;
            return;
        }
        this.addon = null;
    }

    public Module(Category category, String name, String desc) {
        this(category, name, desc, new String[0]);
    }

    public WWidget getWidget(GuiTheme theme) {
        return null;
    }

    public void onActivate() {
    }

    public void onDeactivate() {
    }

    public void toggle() {
        if (!this.active) {
            this.active = true;
            Modules.get().addActive(this);
            this.settings.onActivated();
            if (this.runInMainMenu || Utils.canUpdate()) {
                if (this.autoSubscribe) {
                    MeteorClient.EVENT_BUS.subscribe((Object)this);
                }
                this.onActivate();
            }
        } else {
            if (this.runInMainMenu || Utils.canUpdate()) {
                if (this.autoSubscribe) {
                    MeteorClient.EVENT_BUS.unsubscribe((Object)this);
                }
                this.onDeactivate();
            }
            this.active = false;
            Modules.get().removeActive(this);
        }
    }

    public void enable() {
        if (!this.isActive()) {
            this.toggle();
        }
    }

    public void disable() {
        if (this.isActive()) {
            this.toggle();
        }
    }

    public void sendToggledMsg() {
        if (Config.get().chatFeedback.get().booleanValue() && this.chatFeedback) {
            ChatUtils.forceNextPrefixClass(this.getClass());
            ChatUtils.sendMsg(this.hashCode(), ChatFormatting.GRAY, "Toggled (highlight)%s(default) %s(default).", this.title, this.isActive() ? String.valueOf(ChatFormatting.GREEN) + "on" : String.valueOf(ChatFormatting.RED) + "off");
        }
    }

    public void info(Component message) {
        ChatUtils.forceNextPrefixClass(this.getClass());
        ChatUtils.sendMsg(this.title, message);
    }

    public void info(String message, Object ... args) {
        ChatUtils.forceNextPrefixClass(this.getClass());
        ChatUtils.infoPrefix(this.title, message, args);
    }

    public void warning(String message, Object ... args) {
        ChatUtils.forceNextPrefixClass(this.getClass());
        ChatUtils.warningPrefix(this.title, message, args);
    }

    public void error(String message, Object ... args) {
        ChatUtils.forceNextPrefixClass(this.getClass());
        ChatUtils.errorPrefix(this.title, message, args);
    }

    public boolean isActive() {
        return this.active;
    }

    public String getInfoString() {
        return null;
    }

    @Override
    public CompoundTag toTag() {
        if (!this.serialize) {
            return null;
        }
        CompoundTag tag = new CompoundTag();
        tag.putString("name", this.name);
        tag.put("keybind", (Tag)this.keybind.toTag());
        tag.putBoolean("toggleOnKeyRelease", this.toggleOnBindRelease);
        tag.putBoolean("chatFeedback", this.chatFeedback);
        tag.putBoolean("favorite", this.favorite);
        tag.put("settings", (Tag)this.settings.toTag());
        tag.putBoolean("active", this.active);
        return tag;
    }

    @Override
    public Module fromTag(CompoundTag tag) {
        boolean active;
        this.keybind.fromTag(tag.getCompoundOrEmpty("keybind"));
        this.toggleOnBindRelease = tag.getBooleanOr("toggleOnKeyRelease", false);
        this.chatFeedback = !tag.contains("chatFeedback") || tag.getBooleanOr("chatFeedback", false);
        this.favorite = tag.getBooleanOr("favorite", false);
        Tag settingsTag = tag.get("settings");
        if (settingsTag instanceof CompoundTag) {
            CompoundTag compoundTag = (CompoundTag)settingsTag;
            this.settings.fromTag(compoundTag);
        }
        if ((active = tag.getBooleanOr("active", false)) != this.isActive()) {
            this.toggle();
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
        Module module = (Module)o;
        return Objects.equals(this.name, module.name);
    }

    public int hashCode() {
        return Objects.hash(this.name);
    }

    @Override
    public int compareTo(@NotNull Module o) {
        return this.name.compareTo(o.name);
    }
}


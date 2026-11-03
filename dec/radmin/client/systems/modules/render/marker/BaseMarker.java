/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 */
package radmin.client.systems.modules.render.marker;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import radmin.client.events.render.Render3DEvent;
import radmin.client.gui.GuiTheme;
import radmin.client.gui.screens.MarkerScreen;
import radmin.client.gui.widgets.WWidget;
import radmin.client.settings.BoolSetting;
import radmin.client.settings.EnumSetting;
import radmin.client.settings.Setting;
import radmin.client.settings.SettingGroup;
import radmin.client.settings.Settings;
import radmin.client.settings.StringSetting;
import radmin.client.utils.misc.ISerializable;
import radmin.client.utils.player.PlayerUtils;
import radmin.client.utils.world.Dimension;

public abstract class BaseMarker
implements ISerializable<BaseMarker> {
    public final Settings settings = new Settings();
    protected final SettingGroup sgBase = this.settings.createGroup("Base");
    public final Setting<String> name = this.sgBase.add(((StringSetting.Builder)((StringSetting.Builder)new StringSetting.Builder().name("name")).description("Custom name for this marker.")).build());
    protected final Setting<String> description = this.sgBase.add(((StringSetting.Builder)((StringSetting.Builder)new StringSetting.Builder().name("description")).description("Custom description for this marker.")).build());
    private final Setting<Dimension> dimension = this.sgBase.add(((EnumSetting.Builder)((EnumSetting.Builder)((EnumSetting.Builder)new EnumSetting.Builder().name("dimension")).description("In which dimension this marker should be visible.")).defaultValue(Dimension.Overworld)).build());
    private final Setting<Boolean> active = this.sgBase.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("active")).description("Is this marker visible.")).defaultValue(false)).build());

    public BaseMarker(String name) {
        this.name.set(name);
        this.dimension.set(PlayerUtils.getDimension());
    }

    protected void render(Render3DEvent event) {
    }

    protected void tick() {
    }

    public Screen getScreen(GuiTheme theme) {
        return new MarkerScreen(theme, this);
    }

    public WWidget getWidget(GuiTheme theme) {
        return null;
    }

    public String getName() {
        return this.name.get();
    }

    public String getTypeName() {
        return null;
    }

    public boolean isActive() {
        return this.active.get();
    }

    public boolean isVisible() {
        return this.isActive() && PlayerUtils.getDimension() == this.dimension.get();
    }

    public Dimension getDimension() {
        return this.dimension.get();
    }

    public void toggle() {
        this.active.set(this.active.get() == false);
    }

    @Override
    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();
        tag.put("settings", (Tag)this.settings.toTag());
        return tag;
    }

    @Override
    public BaseMarker fromTag(CompoundTag tag) {
        CompoundTag settingsTag = (CompoundTag)tag.get("settings");
        if (settingsTag != null) {
            this.settings.fromTag(settingsTag);
        }
        return this;
    }
}


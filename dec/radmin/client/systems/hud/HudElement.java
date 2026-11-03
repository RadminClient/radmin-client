/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 */
package radmin.client.systems.hud;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import radmin.client.gui.GuiTheme;
import radmin.client.gui.widgets.WWidget;
import radmin.client.settings.Settings;
import radmin.client.systems.hud.Alignment;
import radmin.client.systems.hud.Hud;
import radmin.client.systems.hud.HudBox;
import radmin.client.systems.hud.HudElementInfo;
import radmin.client.systems.hud.HudRenderer;
import radmin.client.systems.hud.XAnchor;
import radmin.client.systems.hud.YAnchor;
import radmin.client.systems.hud.screens.HudEditorScreen;
import radmin.client.utils.Utils;
import radmin.client.utils.misc.ISerializable;
import radmin.client.utils.other.Snapper;

public abstract class HudElement
implements Snapper.Element,
ISerializable<HudElement> {
    public final HudElementInfo<?> info;
    private boolean active;
    public final Settings settings = new Settings();
    public final HudBox box = new HudBox(this);
    public boolean autoAnchors = true;
    public int x;
    public int y;

    public HudElement(HudElementInfo<?> info) {
        this.info = info;
        this.active = true;
    }

    public boolean isActive() {
        return this.active;
    }

    public void toggle() {
        this.active = !this.active;
    }

    public void setSize(double width, double height) {
        this.box.setSize(width, height);
    }

    @Override
    public void setPos(int x, int y) {
        if (this.autoAnchors) {
            this.box.setPos(x, y);
            this.box.xAnchor = XAnchor.Left;
            this.box.yAnchor = YAnchor.Top;
            this.box.updateAnchors();
        } else {
            this.box.setPos(this.box.x + (x - this.x), this.box.y + (y - this.y));
        }
        this.updatePos();
    }

    @Override
    public void move(int deltaX, int deltaY) {
        this.box.move(deltaX, deltaY);
        this.updatePos();
    }

    public void updatePos() {
        this.x = this.box.getRenderX();
        this.y = this.box.getRenderY();
    }

    protected double alignX(double width, Alignment alignment) {
        return this.box.alignX(this.getWidth(), width, alignment);
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public int getWidth() {
        return this.box.width;
    }

    @Override
    public int getHeight() {
        return this.box.height;
    }

    protected boolean isInEditor() {
        return !Utils.canUpdate() || HudEditorScreen.isOpen();
    }

    public void remove() {
        Hud.get().remove(this);
    }

    public void tick(HudRenderer renderer) {
    }

    public void render(HudRenderer renderer) {
    }

    public void onFontChanged() {
    }

    public WWidget getWidget(GuiTheme theme) {
        return null;
    }

    @Override
    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();
        tag.putString("name", this.info.name);
        tag.putBoolean("active", this.active);
        tag.put("settings", (Tag)this.settings.toTag());
        tag.put("box", (Tag)this.box.toTag());
        tag.putBoolean("autoAnchors", this.autoAnchors);
        return tag;
    }

    @Override
    public HudElement fromTag(CompoundTag tag) {
        this.settings.reset();
        tag.getBoolean("active").ifPresent(active1 -> {
            this.active = active1;
        });
        this.settings.fromTag(tag.getCompoundOrEmpty("settings"));
        this.box.fromTag(tag.getCompoundOrEmpty("box"));
        tag.getBoolean("autoAnchors").ifPresent(autoAnchors1 -> {
            this.autoAnchors = autoAnchors1;
        });
        this.x = this.box.getRenderX();
        this.y = this.box.getRenderY();
        return this;
    }
}


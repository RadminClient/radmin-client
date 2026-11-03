/*
 * Decompiled with CFR 0.152.
 */
package radmin.client.gui.themes.meteor.widgets.pressable;

import radmin.client.gui.renderer.GuiRenderer;
import radmin.client.gui.renderer.packer.GuiTexture;
import radmin.client.gui.themes.meteor.MeteorGuiTheme;
import radmin.client.gui.themes.meteor.MeteorWidget;
import radmin.client.gui.widgets.WWidget;
import radmin.client.gui.widgets.pressable.WButton;
import radmin.client.utils.render.color.Color;

public class WMeteorButton
extends WButton
implements MeteorWidget {
    public WMeteorButton(String text, GuiTexture texture) {
        super(text, texture);
    }

    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        MeteorGuiTheme theme = this.theme();
        double pad = this.pad();
        this.renderBackground(renderer, (WWidget)this, this.pressed, this.mouseOver);
        if (this.text != null) {
            renderer.text(this.text, this.x + this.width / 2.0 - this.textWidth / 2.0, this.y + pad, theme.textColor.get(), false);
        } else {
            double ts = theme.textHeight();
            renderer.quad(this.x + this.width / 2.0 - ts / 2.0, this.y + pad, ts, ts, this.texture, (Color)theme.textColor.get());
        }
    }
}


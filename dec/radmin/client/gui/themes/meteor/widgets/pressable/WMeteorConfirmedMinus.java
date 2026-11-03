/*
 * Decompiled with CFR 0.152.
 */
package radmin.client.gui.themes.meteor.widgets.pressable;

import radmin.client.gui.renderer.GuiRenderer;
import radmin.client.gui.themes.meteor.MeteorGuiTheme;
import radmin.client.gui.themes.meteor.MeteorWidget;
import radmin.client.gui.widgets.WWidget;
import radmin.client.gui.widgets.pressable.WConfirmedMinus;
import radmin.client.utils.render.color.Color;
import radmin.client.utils.render.color.SettingColor;

public class WMeteorConfirmedMinus
extends WConfirmedMinus
implements MeteorWidget {
    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        MeteorGuiTheme theme = this.theme();
        double pad = this.pad();
        double s = theme.scale(3.0);
        SettingColor outline = theme.outlineColor.get(this.pressed, this.mouseOver);
        Color fg = this.pressedOnce ? theme.backgroundColor.get(this.pressed, this.mouseOver) : (Color)this.theme().minusColor.get();
        Color bg = this.pressedOnce ? (Color)this.theme().minusColor.get() : theme.backgroundColor.get(this.pressed, this.mouseOver);
        this.renderBackground(renderer, (WWidget)this, outline, bg);
        renderer.quad(this.x + pad, this.y + this.height / 2.0 - s / 2.0, this.width - pad * 2.0, s, fg);
    }
}


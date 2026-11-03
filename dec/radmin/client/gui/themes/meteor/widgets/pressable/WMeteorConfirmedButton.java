/*
 * Decompiled with CFR 0.152.
 */
package radmin.client.gui.themes.meteor.widgets.pressable;

import radmin.client.gui.renderer.GuiRenderer;
import radmin.client.gui.renderer.packer.GuiTexture;
import radmin.client.gui.themes.meteor.MeteorGuiTheme;
import radmin.client.gui.themes.meteor.MeteorWidget;
import radmin.client.gui.widgets.WWidget;
import radmin.client.gui.widgets.pressable.WConfirmedButton;
import radmin.client.utils.render.color.Color;
import radmin.client.utils.render.color.SettingColor;

public class WMeteorConfirmedButton
extends WConfirmedButton
implements MeteorWidget {
    public WMeteorConfirmedButton(String text, String confirmText, GuiTexture texture) {
        super(text, confirmText, texture);
    }

    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        MeteorGuiTheme theme = this.theme();
        double pad = this.pad();
        SettingColor outline = theme.outlineColor.get(this.pressed, this.mouseOver);
        Color fg = this.pressedOnce ? theme.backgroundColor.get(this.pressed, this.mouseOver) : (Color)theme.textColor.get();
        Color bg = this.pressedOnce ? (Color)theme.textColor.get() : theme.backgroundColor.get(this.pressed, this.mouseOver);
        this.renderBackground(renderer, (WWidget)this, outline, bg);
        String text = this.getText();
        if (text != null) {
            renderer.text(text, this.x + this.width / 2.0 - this.textWidth / 2.0, this.y + pad, fg, false);
        } else {
            double ts = theme.textHeight();
            renderer.quad(this.x + this.width / 2.0 - ts / 2.0, this.y + pad, ts, ts, this.texture, fg);
        }
    }
}


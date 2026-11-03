/*
 * Decompiled with CFR 0.152.
 */
package radmin.client.gui.themes.meteor;

import radmin.client.gui.renderer.GuiRenderer;
import radmin.client.gui.themes.meteor.MeteorGuiTheme;
import radmin.client.gui.utils.BaseWidget;
import radmin.client.gui.widgets.WWidget;
import radmin.client.utils.render.color.Color;

public interface MeteorWidget
extends BaseWidget {
    default public MeteorGuiTheme theme() {
        return (MeteorGuiTheme)this.getTheme();
    }

    default public void renderBackground(GuiRenderer renderer, WWidget widget, Color outlineColor, Color backgroundColor) {
        MeteorGuiTheme theme = this.theme();
        double s = theme.scale(2.0);
        renderer.quad(widget.x + s, widget.y + s, widget.width - s * 2.0, widget.height - s * 2.0, backgroundColor);
        renderer.quad(widget.x, widget.y, widget.width, s, outlineColor);
        renderer.quad(widget.x, widget.y + widget.height - s, widget.width, s, outlineColor);
        renderer.quad(widget.x, widget.y + s, s, widget.height - s * 2.0, outlineColor);
        renderer.quad(widget.x + widget.width - s, widget.y + s, s, widget.height - s * 2.0, outlineColor);
    }

    default public void renderBackground(GuiRenderer renderer, WWidget widget, boolean pressed, boolean mouseOver) {
        MeteorGuiTheme theme = this.theme();
        this.renderBackground(renderer, widget, theme.outlineColor.get(pressed, mouseOver), theme.backgroundColor.get(pressed, mouseOver));
    }
}


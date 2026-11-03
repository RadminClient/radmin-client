/*
 * Decompiled with CFR 0.152.
 */
package radmin.client.gui.themes.meteor.widgets;

import radmin.client.gui.renderer.GuiRenderer;
import radmin.client.gui.themes.meteor.MeteorGuiTheme;
import radmin.client.gui.themes.meteor.MeteorWidget;
import radmin.client.gui.widgets.WVerticalSeparator;
import radmin.client.utils.render.color.Color;

public class WMeteorVerticalSeparator
extends WVerticalSeparator
implements MeteorWidget {
    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        MeteorGuiTheme theme = this.theme();
        Color colorEdges = theme.separatorEdges.get();
        Color colorCenter = theme.separatorCenter.get();
        double s = theme.scale(1.0);
        double offsetX = Math.round(this.width / 2.0);
        renderer.quad(this.x + offsetX, this.y, s, this.height / 2.0, colorEdges, colorEdges, colorCenter, colorCenter);
        renderer.quad(this.x + offsetX, this.y + this.height / 2.0, s, this.height / 2.0, colorCenter, colorCenter, colorEdges, colorEdges);
    }
}


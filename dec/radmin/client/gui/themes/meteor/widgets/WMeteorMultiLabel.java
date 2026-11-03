/*
 * Decompiled with CFR 0.152.
 */
package radmin.client.gui.themes.meteor.widgets;

import radmin.client.gui.renderer.GuiRenderer;
import radmin.client.gui.themes.meteor.MeteorWidget;
import radmin.client.gui.widgets.WMultiLabel;
import radmin.client.utils.render.color.Color;

public class WMeteorMultiLabel
extends WMultiLabel
implements MeteorWidget {
    public WMeteorMultiLabel(String text, boolean title, double maxWidth) {
        super(text, title, maxWidth);
    }

    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        double h = this.theme.textHeight(this.title);
        Color defaultColor = this.theme().textColor.get();
        for (int i = 0; i < this.lines.size(); ++i) {
            renderer.text((String)this.lines.get(i), this.x, this.y + h * (double)i, this.color != null ? this.color : defaultColor, false);
        }
    }
}


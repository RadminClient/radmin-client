/*
 * Decompiled with CFR 0.152.
 */
package radmin.client.gui.themes.meteor.widgets;

import radmin.client.gui.renderer.GuiRenderer;
import radmin.client.gui.themes.meteor.MeteorWidget;
import radmin.client.gui.widgets.WLabel;
import radmin.client.utils.render.color.Color;

public class WMeteorLabel
extends WLabel
implements MeteorWidget {
    public WMeteorLabel(String text, boolean title) {
        super(text, title);
    }

    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        if (!this.text.isEmpty()) {
            renderer.text(this.text, this.x, this.y, this.color != null ? this.color : (this.title ? (Color)this.theme().titleTextColor.get() : (Color)this.theme().textColor.get()), this.title);
        }
    }
}


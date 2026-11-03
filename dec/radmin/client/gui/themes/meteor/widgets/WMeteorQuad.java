/*
 * Decompiled with CFR 0.152.
 */
package radmin.client.gui.themes.meteor.widgets;

import radmin.client.gui.renderer.GuiRenderer;
import radmin.client.gui.widgets.WQuad;
import radmin.client.utils.render.color.Color;

public class WMeteorQuad
extends WQuad {
    public WMeteorQuad(Color color) {
        super(color);
    }

    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        renderer.quad(this.x, this.y, this.width, this.height, this.color);
    }
}


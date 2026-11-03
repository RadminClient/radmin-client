/*
 * Decompiled with CFR 0.152.
 */
package radmin.client.gui.themes.meteor.widgets;

import radmin.client.gui.renderer.GuiRenderer;
import radmin.client.gui.themes.meteor.MeteorWidget;
import radmin.client.gui.widgets.WTooltip;

public class WMeteorTooltip
extends WTooltip
implements MeteorWidget {
    public WMeteorTooltip(String text) {
        super(text);
    }

    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        renderer.quad(this, this.theme().backgroundColor.get());
    }
}


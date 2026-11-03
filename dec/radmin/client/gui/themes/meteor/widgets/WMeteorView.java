/*
 * Decompiled with CFR 0.152.
 */
package radmin.client.gui.themes.meteor.widgets;

import radmin.client.gui.renderer.GuiRenderer;
import radmin.client.gui.themes.meteor.MeteorWidget;
import radmin.client.gui.widgets.containers.WView;

public class WMeteorView
extends WView
implements MeteorWidget {
    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        if (this.canScroll && this.hasScrollBar) {
            renderer.quad(this.handleX(), this.handleY(), this.handleWidth(), this.handleHeight(), this.theme().scrollbarColor.get(this.focused, this.handleMouseOver));
        }
    }
}


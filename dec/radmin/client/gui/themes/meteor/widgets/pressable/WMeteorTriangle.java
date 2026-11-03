/*
 * Decompiled with CFR 0.152.
 */
package radmin.client.gui.themes.meteor.widgets.pressable;

import radmin.client.gui.renderer.GuiRenderer;
import radmin.client.gui.themes.meteor.MeteorWidget;
import radmin.client.gui.widgets.pressable.WTriangle;

public class WMeteorTriangle
extends WTriangle
implements MeteorWidget {
    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        renderer.rotatedQuad(this.x, this.y, this.width, this.height, this.rotation, GuiRenderer.TRIANGLE, this.theme().backgroundColor.get(this.pressed, this.mouseOver));
    }
}


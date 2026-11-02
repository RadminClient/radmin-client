/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package radmin.client.gui.themes.meteor.widgets;

import radmin.client.gui.renderer.GuiRenderer;
import radmin.client.gui.themes.meteor.MeteorWidget;
import radmin.client.gui.widgets.WTooltip;
import radmin.client.renderer.text.TextRenderer;
import radmin.client.renderer.text.VanillaTextRenderer;

public class WMeteorTooltip extends WTooltip implements MeteorWidget {
    public WMeteorTooltip(String text) {
        super(text);
    }

    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        if (TextRenderer.get() == VanillaTextRenderer.INSTANCE) {
            renderer.fill(this, theme().backgroundColor.get());
        } else {
            renderer.quad(this, theme().backgroundColor.get());
        }
    }
}

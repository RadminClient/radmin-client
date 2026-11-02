/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package radmin.client.gui.themes.meteor.widgets;

import radmin.client.gui.themes.meteor.MeteorWidget;
import radmin.client.gui.widgets.WTopBar;
import radmin.client.utils.render.color.Color;

public class WMeteorTopBar extends WTopBar implements MeteorWidget {
    @Override
    protected Color getButtonColor(boolean pressed, boolean hovered) {
        return theme().backgroundColor.get(pressed, hovered);
    }

    @Override
    protected Color getNameColor() {
        return theme().textColor.get();
    }
}

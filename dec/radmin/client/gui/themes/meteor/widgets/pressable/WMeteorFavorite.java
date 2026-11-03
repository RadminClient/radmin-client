/*
 * Decompiled with CFR 0.152.
 */
package radmin.client.gui.themes.meteor.widgets.pressable;

import radmin.client.gui.themes.meteor.MeteorWidget;
import radmin.client.gui.widgets.pressable.WFavorite;
import radmin.client.utils.render.color.Color;

public class WMeteorFavorite
extends WFavorite
implements MeteorWidget {
    public WMeteorFavorite(boolean checked) {
        super(checked);
    }

    @Override
    protected Color getColor() {
        return this.theme().favoriteColor.get();
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package radmin.client.utils.render;

import radmin.client.gui.GuiTheme;
import radmin.client.gui.GuiThemes;
import radmin.client.gui.themes.meteor.MeteorGuiTheme;
import radmin.client.utils.render.color.Color;

public class ThemeAccent {
    private ThemeAccent() {
    }

    public static Color get() {
        GuiTheme guiTheme = GuiThemes.get();
        if (guiTheme instanceof MeteorGuiTheme) {
            MeteorGuiTheme theme = (MeteorGuiTheme)guiTheme;
            return theme.accentColor.get();
        }
        return new Color(0, 153, 255, 255);
    }

    public static Color get(double alpha) {
        Color c = ThemeAccent.get();
        return new Color(c.r, c.g, c.b, (int)Math.max(0.0, Math.min(255.0, 255.0 * alpha)));
    }

    public static Color get(double alpha, double multiplier) {
        Color c = ThemeAccent.get();
        int r = (int)Math.max(0.0, Math.min(255.0, (double)c.r * multiplier));
        int g = (int)Math.max(0.0, Math.min(255.0, (double)c.g * multiplier));
        int b = (int)Math.max(0.0, Math.min(255.0, (double)c.b * multiplier));
        return new Color(r, g, b, (int)Math.max(0.0, Math.min(255.0, 255.0 * alpha)));
    }
}


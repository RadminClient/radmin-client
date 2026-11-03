/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Mth
 */
package radmin.client.utils.render;

import net.minecraft.util.Mth;
import radmin.client.utils.render.ThemeAccent;
import radmin.client.utils.render.color.Color;
import radmin.client.utils.render.color.SettingColor;

public final class HudThemeUtils {
    private HudThemeUtils() {
    }

    public static Color themeColor() {
        return HudThemeUtils.themeColor(0);
    }

    public static Color themeColor(int index) {
        Color accent = ThemeAccent.get();
        if (accent instanceof SettingColor) {
            SettingColor settingColor = (SettingColor)accent;
            if (settingColor.rainbow) {
                return HudThemeUtils.rainbow(10, index, 0.6f, 1.0f, 1.0f);
            }
        }
        return new Color(accent.r, accent.g, accent.b, accent.a);
    }

    public static Color darken(Color color, float factor) {
        float[] hsb = java.awt.Color.RGBtoHSB(color.r, color.g, color.b, null);
        hsb[2] = Mth.clamp((float)(hsb[2] * factor), (float)0.0f, (float)1.0f);
        int rgb = java.awt.Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
        return new Color(Color.toRGBAR(rgb), Color.toRGBAG(rgb), Color.toRGBAB(rgb), color.a);
    }

    public static Color applyAlpha(Color color, float alphaMul) {
        int a = color.a;
        int na = (int)((float)a * Mth.clamp((float)alphaMul, (float)0.0f, (float)1.0f));
        return new Color(color.r, color.g, color.b, na);
    }

    public static int setAlpha(int color, int alpha) {
        return color & 0xFFFFFF | (alpha & 0xFF) << 24;
    }

    public static int gradient(int color1, int color2, float amount) {
        amount = Mth.clamp((float)amount, (float)0.0f, (float)1.0f);
        int r = (int)Mth.lerp((float)amount, (float)Color.toRGBAR(color1), (float)Color.toRGBAR(color2));
        int g = (int)Mth.lerp((float)amount, (float)Color.toRGBAG(color1), (float)Color.toRGBAG(color2));
        int b = (int)Mth.lerp((float)amount, (float)Color.toRGBAB(color1), (float)Color.toRGBAB(color2));
        int a = (int)Mth.lerp((float)amount, (float)Color.toRGBAA(color1), (float)Color.toRGBAA(color2));
        return Color.fromRGBA(r, g, b, a);
    }

    public static Color interpolate(Color color1, Color color2, float amount) {
        amount = Mth.clamp((float)amount, (float)0.0f, (float)1.0f);
        return new Color((int)Mth.lerp((float)amount, (float)color1.r, (float)color2.r), (int)Mth.lerp((float)amount, (float)color1.g, (float)color2.g), (int)Mth.lerp((float)amount, (float)color1.b, (float)color2.b), (int)Mth.lerp((float)amount, (float)color1.a, (float)color2.a));
    }

    public static Color rainbow(int speed, int index, float saturation, float brightness, float opacity) {
        int angle = (int)((System.currentTimeMillis() / (long)speed + (long)index) % 360L);
        float hue = (float)angle / 360.0f;
        int color = java.awt.Color.HSBtoRGB(hue, saturation, brightness);
        return new Color(Color.toRGBAR(color), Color.toRGBAG(color), Color.toRGBAB(color), Math.max(0, Math.min(255, (int)(opacity * 255.0f))));
    }
}


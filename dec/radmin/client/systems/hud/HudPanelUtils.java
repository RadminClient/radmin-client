/*
 * Decompiled with CFR 0.152.
 */
package radmin.client.systems.hud;

import radmin.client.systems.hud.HudRenderer;
import radmin.client.utils.render.HudThemeUtils;
import radmin.client.utils.render.color.Color;

public final class HudPanelUtils {
    private HudPanelUtils() {
    }

    public static void defaultPanel(HudRenderer renderer, double x, double y, double width, double height, double gradientRadius, double borderRadius, Color themeColor) {
        Color border = new Color(50, 50, 50, 255);
        Color top = HudThemeUtils.darken(themeColor, 0.15f);
        Color bottom = HudThemeUtils.darken(themeColor, 0.05f);
        renderer.rounded(x - 0.5, y - 0.5, width + 1.0, height + 1.0, borderRadius, border);
        renderer.roundedGradient(x, y, width, height, gradientRadius, top, bottom);
    }

    public static void defaultPanel(HudRenderer renderer, double x, double y, double width, double height, Color themeColor) {
        HudPanelUtils.defaultPanel(renderer, x, y, width, height, 3.0, 3.5, themeColor);
    }

    public static void waveHeader(HudRenderer renderer, double x, double y, double width, double height, double radius, Color leftTop, Color leftBottom, Color centerTop, Color centerBottom, Color rightTop, Color rightBottom) {
        renderer.roundedGradient(x, y, width, height, radius, leftTop, rightTop, rightBottom, leftBottom);
    }

    public static void wavePanel(HudRenderer renderer, double x, double y, double width, double height, Color bgColor, double headerHeight, double headerRadius, Color leftTop, Color leftBottom, Color centerTop, Color centerBottom, Color rightTop, Color rightBottom) {
        renderer.quad(x, y, width, height, bgColor);
        HudPanelUtils.waveHeader(renderer, x, y, width, headerHeight, headerRadius, leftTop, leftBottom, centerTop, centerBottom, rightTop, rightBottom);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void squarePattern(HudRenderer renderer, double x, double y, double width, double height, Color themeColor) {
        if (width <= 6.0 || height <= 6.0) {
            return;
        }
        double clipX = x - 1.0;
        double clipY = y + 1.0;
        double clipW = Math.max(1.0, width - 2.0);
        double clipH = Math.max(1.0, height - 2.0);
        float themeAlphaMul = (float)themeColor.a / 255.0f;
        if (themeAlphaMul <= 0.001f) {
            return;
        }
        Color outlineBase = HudThemeUtils.darken(themeColor, clipH <= 20.0 ? 0.62f : 0.72f);
        renderer.enableScissor(clipX, clipY, clipW, clipH);
        try {
            if (clipH <= 20.0) {
                double[][] slots = new double[][]{{0.05, 0.08, 8.6}, {0.92, 0.1, 8.8}, {0.16, 0.78, 6.3}, {0.77, 0.8, 6.5}, {0.31, 0.18, 6.0}, {0.58, 0.74, 5.8}, {0.45, 0.45, 5.1}, {0.86, 0.46, 5.3}, {0.23, 0.52, 4.9}, {0.67, 0.3, 5.0}, {0.11, 0.34, 5.5}, {0.38, 0.7, 5.2}, {0.72, 0.16, 5.7}, {0.95, 0.68, 5.1}};
                double desiredCount = Math.min((double)slots.length, 3.7 + Math.max(0.0, (clipW - 84.0) / 32.0));
                HudPanelUtils.drawSlots(renderer, slots, desiredCount, clipX, clipY, clipW, clipH, outlineBase, themeAlphaMul, 82.0f, 86.0f, true);
            } else {
                double[][] slots = new double[][]{{0.05, 4.0, 9.6}, {0.87, 4.0, 9.2}, {0.5, 8.0, 7.4}, {0.18, 13.0, 6.2}, {0.72, 13.0, 6.0}, {0.07, 21.0, 5.6}, {0.91, 21.0, 5.8}, {0.24, 30.0, 5.4}, {0.66, 30.0, 5.5}, {0.04, 38.0, 6.8}, {0.9, 38.0, 7.0}, {0.15, 47.0, 5.4}, {0.78, 47.0, 5.5}, {0.08, 56.0, 5.1}, {0.92, 56.0, 5.2}, {0.23, 65.0, 5.8}, {0.69, 65.0, 5.9}, {0.52, 71.0, 7.2}, {0.06, 74.0, 7.6}, {0.88, 74.0, 7.4}, {0.14, 85.0, 5.7}, {0.82, 85.0, 5.8}, {0.09, 97.0, 6.5}, {0.9, 98.0, 6.6}};
                double baseCount = 10.0;
                double extraHeight = Math.max(0.0, clipH - 24.0);
                double desiredCount = Math.min((double)slots.length, baseCount + extraHeight / 10.0);
                HudPanelUtils.drawSlots(renderer, slots, desiredCount, clipX, clipY, clipW, clipH, outlineBase, themeAlphaMul, 40.0f, 58.0f, false);
            }
        }
        finally {
            renderer.disableScissor();
        }
    }

    private static void drawSlots(HudRenderer renderer, double[][] slots, double desiredCount, double clipX, double clipY, double clipW, double clipH, Color outlineBase, float themeAlphaMul, float baseAlpha, float outlineAlphaMul, boolean compact) {
        double pa;
        double panelAlpha = 1.0;
        if (!compact && (panelAlpha = (pa = Math.max(0.0, Math.min(1.0, (clipH - 10.0) / 16.0))) * pa * (3.0 - 2.0 * pa)) <= 0.015) {
            return;
        }
        for (int i = 0; i < slots.length; ++i) {
            double py;
            double px;
            double reveal = desiredCount - (double)i;
            if (reveal <= 0.0) continue;
            double alphaMul = Math.max(0.0, Math.min(1.0, reveal));
            alphaMul = alphaMul * alphaMul * (3.0 - 2.0 * alphaMul);
            if ((alphaMul *= panelAlpha) <= 0.015) continue;
            double size = slots[i][2];
            if (compact) {
                px = clipX + 0.8 + slots[i][0] * Math.max(1.0, clipW - size + 1.6);
                py = clipY - 1.2 + slots[i][1] * Math.max(1.0, clipH - size + 2.4);
            } else {
                px = clipX + 2.0 + slots[i][0] * Math.max(1.0, clipW - size - 4.0);
                py = clipY + slots[i][1];
                double bottomLimit = clipY + clipH - 1.0;
                if (py >= bottomLimit) continue;
                if (py + size > bottomLimit) {
                    double visible = Math.max(0.0, Math.min(1.0, (bottomLimit - py) / Math.max(1.0, size)));
                    if ((alphaMul *= (visible = visible * visible * (3.0 - 2.0 * visible))) <= 0.015) continue;
                }
            }
            int outlineAlpha = Math.max(0, Math.min(255, (int)((double)outlineAlphaMul * alphaMul * (double)themeAlphaMul)));
            if (outlineAlpha <= 0) continue;
            Color outline = new Color(outlineBase.r, outlineBase.g, outlineBase.b, outlineAlpha);
            renderer.boxLines(px, py, size, size, outline);
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.screens.Screen
 */
package radmin.client.renderer;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.minecraft.client.gui.screens.Screen;
import radmin.client.MeteorClient;
import radmin.client.events.meteor.CustomFontChangedEvent;
import radmin.client.gui.WidgetScreen;
import radmin.client.renderer.text.CustomTextRenderer;
import radmin.client.renderer.text.FontFace;
import radmin.client.renderer.text.FontFamily;
import radmin.client.renderer.text.FontInfo;
import radmin.client.systems.config.Config;
import radmin.client.utils.PreInit;
import radmin.client.utils.render.FontUtils;

public class Fonts {
    public static final String[] BUILTIN_FONTS = new String[]{"JetBrains Mono", "Comfortaa", "Tw Cen MT", "Pixelation"};
    public static String DEFAULT_FONT_FAMILY;
    public static FontFace DEFAULT_FONT;
    public static final List<FontFamily> FONT_FAMILIES;
    public static CustomTextRenderer RENDERER;

    private Fonts() {
    }

    @PreInit
    public static void refresh() {
        FONT_FAMILIES.clear();
        for (String builtinFont : BUILTIN_FONTS) {
            FontUtils.loadBuiltin(FONT_FAMILIES, builtinFont);
        }
        for (String fontPath : FontUtils.getSearchPaths()) {
            FontUtils.loadSystem(FONT_FAMILIES, new File(fontPath));
        }
        FONT_FAMILIES.sort(Comparator.comparing(FontFamily::getName));
        MeteorClient.LOG.info("Found {} font families.", (Object)FONT_FAMILIES.size());
        FontFamily defaultFamily = Fonts.getFamily("Calibri");
        if (defaultFamily == null) {
            defaultFamily = Fonts.getFamily(FontUtils.getBuiltinFontInfo(BUILTIN_FONTS[1]).family());
        }
        DEFAULT_FONT_FAMILY = defaultFamily.getName();
        DEFAULT_FONT = defaultFamily.get(FontInfo.Type.Regular);
        Config config = Config.get();
        Fonts.load(config != null ? config.font.get() : DEFAULT_FONT);
    }

    public static void load(FontFace fontFace) {
        if (RENDERER != null) {
            if (Fonts.RENDERER.fontFace.equals(fontFace)) {
                return;
            }
            RENDERER.destroy();
        }
        try {
            RENDERER = new CustomTextRenderer(fontFace);
            MeteorClient.EVENT_BUS.post((Object)CustomFontChangedEvent.get());
        }
        catch (Exception e) {
            if (fontFace.equals(DEFAULT_FONT)) {
                throw new RuntimeException("Failed to load default font: " + String.valueOf(fontFace), e);
            }
            MeteorClient.LOG.error("Failed to load font: {}", (Object)fontFace, (Object)e);
            Fonts.load(DEFAULT_FONT);
        }
        Screen screen = MeteorClient.mc.screen;
        if (screen instanceof WidgetScreen) {
            WidgetScreen widgetScreen = (WidgetScreen)screen;
            if (Config.get().customFont.get().booleanValue()) {
                widgetScreen.invalidate();
            }
        }
    }

    public static FontFamily getFamily(String name) {
        for (FontFamily fontFamily : FONT_FAMILIES) {
            if (!fontFamily.getName().equalsIgnoreCase(name)) continue;
            return fontFamily;
        }
        return null;
    }

    static {
        FONT_FAMILIES = new ArrayList<FontFamily>();
    }
}


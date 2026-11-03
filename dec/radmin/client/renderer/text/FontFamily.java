/*
 * Decompiled with CFR 0.152.
 */
package radmin.client.renderer.text;

import java.util.ArrayList;
import java.util.List;
import radmin.client.renderer.text.FontFace;
import radmin.client.renderer.text.FontInfo;

public class FontFamily {
    private final String name;
    private final List<FontFace> fonts = new ArrayList<FontFace>();

    public FontFamily(String name) {
        this.name = name;
    }

    public boolean addFont(FontFace font) {
        return this.fonts.add(font);
    }

    public boolean hasType(FontInfo.Type type) {
        return this.get(type) != null;
    }

    public FontFace get(FontInfo.Type type) {
        if (type == null) {
            return null;
        }
        for (FontFace font : this.fonts) {
            if (!font.info.type().equals((Object)type)) continue;
            return font;
        }
        return null;
    }

    public String getName() {
        return this.name;
    }
}


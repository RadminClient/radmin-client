/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 */
package radmin.client.renderer.text;

import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import org.jspecify.annotations.NullMarked;
import radmin.client.renderer.text.FontFace;
import radmin.client.renderer.text.FontInfo;
import radmin.client.utils.render.FontUtils;

@NullMarked
public non-sealed class BuiltinFontFace
extends FontFace {
    private final String name;

    public BuiltinFontFace(FontInfo info, String name) {
        super(info);
        this.name = name;
    }

    @Override
    public ReadableByteChannel byteChannelForRead() {
        InputStream inputStream = FontUtils.builtinFontStream(this.name);
        if (inputStream == null) {
            throw new IllegalArgumentException("Builtin font '" + this.name + "' not found");
        }
        return Channels.newChannel(inputStream);
    }

    @Override
    public String toString() {
        return super.toString() + " (builtin)";
    }
}


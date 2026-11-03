/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.NullMarked
 *  org.lwjgl.BufferUtils
 */
package radmin.client.renderer.text;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import org.jspecify.annotations.NullMarked;
import org.lwjgl.BufferUtils;
import radmin.client.renderer.text.BuiltinFontFace;
import radmin.client.renderer.text.FontInfo;
import radmin.client.renderer.text.SystemFontFace;
import radmin.client.utils.files.ByteBufferUtils;

@NullMarked
public abstract sealed class FontFace
permits BuiltinFontFace, SystemFontFace {
    public final FontInfo info;

    protected FontFace(FontInfo info) {
        this.info = info;
    }

    public abstract ReadableByteChannel byteChannelForRead() throws IOException;

    public final ByteBuffer readToDirectByteBuffer() throws IOException {
        try (ReadableByteChannel channel = this.byteChannelForRead();){
            ByteBuffer byteBuffer = ByteBufferUtils.readFully(channel, BufferUtils::createByteBuffer);
            return byteBuffer;
        }
    }

    public String toString() {
        return this.info.toString();
    }
}


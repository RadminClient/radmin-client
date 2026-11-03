/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.VertexFormat
 *  com.mojang.blaze3d.vertex.VertexFormatElement
 */
package radmin.client.renderer;

import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import radmin.client.renderer.MeteorVertexFormatElements;

public abstract class MeteorVertexFormats {
    public static final VertexFormat POS2 = VertexFormat.builder().add("Position", MeteorVertexFormatElements.POS2).build();
    public static final VertexFormat POS2_COLOR = VertexFormat.builder().add("Position", MeteorVertexFormatElements.POS2).add("Color", VertexFormatElement.COLOR).build();
    public static final VertexFormat POS2_TEXTURE_COLOR = VertexFormat.builder().add("Position", MeteorVertexFormatElements.POS2).add("Texture", VertexFormatElement.UV).add("Color", VertexFormatElement.COLOR).build();
    public static final VertexFormat POS3_TEXTURE_COLOR = VertexFormat.builder().add("Position", VertexFormatElement.POSITION).add("Texture", VertexFormatElement.UV).add("Color", VertexFormatElement.COLOR).build();

    private MeteorVertexFormats() {
    }
}


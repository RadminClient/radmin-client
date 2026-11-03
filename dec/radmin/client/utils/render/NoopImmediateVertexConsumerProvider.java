/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  net.minecraft.client.renderer.MultiBufferSource$BufferSource
 *  net.minecraft.client.renderer.rendertype.RenderType
 */
package radmin.client.utils.render;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderType;
import radmin.client.utils.render.NoopVertexConsumer;

public class NoopImmediateVertexConsumerProvider
extends MultiBufferSource.BufferSource {
    public static final NoopImmediateVertexConsumerProvider INSTANCE = new NoopImmediateVertexConsumerProvider();

    private NoopImmediateVertexConsumerProvider() {
        super(null, null);
    }

    public VertexConsumer getBuffer(RenderType layer) {
        return NoopVertexConsumer.INSTANCE;
    }

    public void endBatch() {
    }

    public void endBatch(RenderType layer) {
    }
}


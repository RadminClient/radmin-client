/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.MultiBufferSource$BufferSource
 *  net.minecraft.client.renderer.rendertype.RenderType
 */
package radmin.client.utils.render;

import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.function.Supplier;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderType;

public class WrapperImmediateVertexConsumerProvider
extends MultiBufferSource.BufferSource {
    private final Supplier<MultiBufferSource> supplier;

    public WrapperImmediateVertexConsumerProvider(Supplier<MultiBufferSource> supplier) {
        super(null, null);
        this.supplier = supplier;
    }

    public VertexConsumer getBuffer(RenderType layer) {
        return this.supplier.get().getBuffer(layer);
    }

    public void endBatch() {
    }

    public void endBatch(RenderType layer) {
    }
}


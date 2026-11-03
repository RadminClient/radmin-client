/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.blaze3d.vertex.VertexFormat
 *  com.mojang.blaze3d.vertex.VertexFormatElement
 *  net.caffeinemc.mods.sodium.api.vertex.buffer.VertexBufferWriter
 *  org.lwjgl.system.MemoryStack
 *  org.lwjgl.system.MemoryUtil
 *  org.spongepowered.asm.mixin.Mixin
 */
package radmin.client.mixin.sodium;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import net.caffeinemc.mods.sodium.api.vertex.buffer.VertexBufferWriter;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.spongepowered.asm.mixin.Mixin;
import radmin.client.utils.render.MeshBuilderVertexConsumerProvider;

@Mixin(value={MeshBuilderVertexConsumerProvider.MeshBuilderVertexConsumer.class}, remap=false)
public abstract class MeshVertexConsumerMixin
implements VertexConsumer,
VertexBufferWriter {
    public void push(MemoryStack stack, long ptr, int count, VertexFormat format) {
        int positionOffset = format.getOffset(VertexFormatElement.POSITION);
        if (positionOffset == -1) {
            return;
        }
        for (int i = 0; i < count; ++i) {
            long positionPtr = ptr + (long)format.getVertexSize() * (long)i + (long)positionOffset;
            float x = MemoryUtil.memGetFloat((long)positionPtr);
            float y = MemoryUtil.memGetFloat((long)(positionPtr + 4L));
            float z = MemoryUtil.memGetFloat((long)(positionPtr + 8L));
            this.addVertex(x, y, z);
        }
    }
}


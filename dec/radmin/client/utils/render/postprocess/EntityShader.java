/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.pipeline.RenderPipeline
 *  net.minecraft.world.entity.Entity
 */
package radmin.client.utils.render.postprocess;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.world.entity.Entity;
import radmin.client.MeteorClient;
import radmin.client.mixininterface.ILevelRenderer;
import radmin.client.utils.render.CustomOutlineVertexConsumerProvider;
import radmin.client.utils.render.postprocess.PostProcessShader;

public abstract class EntityShader
extends PostProcessShader {
    public final CustomOutlineVertexConsumerProvider vertexConsumerProvider = new CustomOutlineVertexConsumerProvider();

    protected EntityShader(RenderPipeline pipeline) {
        super(pipeline);
    }

    public abstract boolean shouldDraw(Entity var1);

    @Override
    protected void preDraw() {
        ((ILevelRenderer)MeteorClient.mc.levelRenderer).meteor$pushEntityOutlineFramebuffer(this.framebuffer);
    }

    @Override
    protected void postDraw() {
        ((ILevelRenderer)MeteorClient.mc.levelRenderer).meteor$popEntityOutlineFramebuffer();
    }

    public void submitVertices() {
        this.submitVertices(this.vertexConsumerProvider::draw);
    }
}


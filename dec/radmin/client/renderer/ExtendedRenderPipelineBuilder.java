/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.pipeline.RenderPipeline
 *  com.mojang.blaze3d.pipeline.RenderPipeline$Builder
 *  com.mojang.blaze3d.pipeline.RenderPipeline$Snippet
 */
package radmin.client.renderer;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import radmin.client.mixininterface.IRenderPipeline;

public class ExtendedRenderPipelineBuilder
extends RenderPipeline.Builder {
    private boolean lineSmooth;

    public ExtendedRenderPipelineBuilder(RenderPipeline.Snippet ... snippets) {
        for (RenderPipeline.Snippet snippet : snippets) {
            this.withSnippet(snippet);
        }
    }

    public ExtendedRenderPipelineBuilder withLineSmooth() {
        this.lineSmooth = true;
        return this;
    }

    public RenderPipeline build() {
        RenderPipeline pipeline = super.build();
        ((IRenderPipeline)pipeline).meteor$setLineSmooth(this.lineSmooth);
        return pipeline;
    }
}


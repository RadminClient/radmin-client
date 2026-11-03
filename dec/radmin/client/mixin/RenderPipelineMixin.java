/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.pipeline.RenderPipeline
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 */
package radmin.client.mixin;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import radmin.client.mixininterface.IRenderPipeline;

@Mixin(value={RenderPipeline.class})
public abstract class RenderPipelineMixin
implements IRenderPipeline {
    @Unique
    private boolean lineSmooth;

    @Override
    public void meteor$setLineSmooth(boolean lineSmooth) {
        this.lineSmooth = lineSmooth;
    }

    @Override
    public boolean meteor$getLineSmooth() {
        return this.lineSmooth;
    }
}


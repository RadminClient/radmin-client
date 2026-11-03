/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.pipeline.RenderTarget
 */
package radmin.client.mixininterface;

import com.mojang.blaze3d.pipeline.RenderTarget;

public interface ILevelRenderer {
    public void meteor$pushEntityOutlineFramebuffer(RenderTarget var1);

    public void meteor$popEntityOutlineFramebuffer();
}


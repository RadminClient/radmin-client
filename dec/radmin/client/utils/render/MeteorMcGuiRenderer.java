/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.render.GuiRenderer
 *  net.minecraft.client.gui.render.pip.PictureInPictureRenderer
 *  net.minecraft.client.renderer.MultiBufferSource$BufferSource
 *  net.minecraft.client.renderer.SubmitNodeCollector
 *  net.minecraft.client.renderer.feature.FeatureRenderDispatcher
 *  net.minecraft.client.renderer.state.gui.GuiRenderState
 */
package radmin.client.utils.render;

import java.util.List;
import net.minecraft.client.gui.render.GuiRenderer;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
import net.minecraft.client.renderer.state.gui.GuiRenderState;

public class MeteorMcGuiRenderer
extends GuiRenderer {
    public MeteorMcGuiRenderer(GuiRenderState renderState, MultiBufferSource.BufferSource bufferSource, SubmitNodeCollector submitNodeCollector, FeatureRenderDispatcher featureRenderDispatcher, List<PictureInPictureRenderer<?>> pictureInPictureRenderers) {
        super(renderState, bufferSource, submitNodeCollector, featureRenderDispatcher, pictureInPictureRenderers);
    }
}


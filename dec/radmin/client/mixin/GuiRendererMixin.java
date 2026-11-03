/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphicsExtractor
 *  net.minecraft.client.gui.render.GuiRenderer
 *  net.minecraft.client.gui.render.pip.PictureInPictureRenderer
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.renderer.MultiBufferSource$BufferSource
 *  net.minecraft.client.renderer.SubmitNodeCollector
 *  net.minecraft.client.renderer.feature.FeatureRenderDispatcher
 *  net.minecraft.client.renderer.fog.FogRenderer
 *  net.minecraft.client.renderer.fog.FogRenderer$FogMode
 *  net.minecraft.client.renderer.state.gui.GuiRenderState
 *  net.minecraft.util.profiling.Profiler
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package radmin.client.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.render.GuiRenderer;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
import net.minecraft.client.renderer.fog.FogRenderer;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import net.minecraft.util.profiling.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import radmin.client.MeteorClient;
import radmin.client.events.render.Render2DEvent;
import radmin.client.gui.WidgetScreen;
import radmin.client.mixin.GameRendererAccessor;
import radmin.client.systems.hud.screens.HudEditorScreen;
import radmin.client.utils.Utils;
import radmin.client.utils.render.MeteorMcGuiRenderer;

@Mixin(value={GuiRenderer.class})
public abstract class GuiRendererMixin {
    @Unique
    private GuiRenderState renderState;
    @Unique
    private MeteorMcGuiRenderer guiRenderer;

    @Inject(method={"<init>"}, at={@At(value="RETURN")})
    private void init$meteor(GuiRenderState renderState, MultiBufferSource.BufferSource bufferSource, SubmitNodeCollector submitNodeCollector, FeatureRenderDispatcher featureRenderDispatcher, List<PictureInPictureRenderer<?>> pictureInPictureRenderers, CallbackInfo ci) {
        if ((GuiRenderer)this instanceof MeteorMcGuiRenderer) {
            return;
        }
        this.renderState = new GuiRenderState();
        this.guiRenderer = new MeteorMcGuiRenderer(this.renderState, bufferSource, submitNodeCollector, featureRenderDispatcher, pictureInPictureRenderers);
    }

    @Inject(method={"render"}, at={@At(value="HEAD")})
    private void render$preGui(CallbackInfo ci) {
        if ((GuiRenderer)this instanceof MeteorMcGuiRenderer) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc.screen == null || mc.screen instanceof WidgetScreen) {
            return;
        }
        this.meteor$render2D(mc);
    }

    @Inject(method={"render"}, at={@At(value="TAIL")})
    private void render$postGui(CallbackInfo ci) {
        if ((GuiRenderer)this instanceof MeteorMcGuiRenderer) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        RenderSystem.getDevice().createCommandEncoder().clearDepthTexture(mc.getMainRenderTarget().getDepthTexture(), 1.0);
        if (mc.screen == null || mc.screen instanceof WidgetScreen) {
            this.meteor$render2D(mc);
        }
        this.guiRenderer.endFrame();
    }

    @Unique
    private void meteor$render2D(Minecraft mc) {
        Screen tickDelta2;
        int mouseX = (int)mc.mouseHandler.getScaledXPos(mc.getWindow());
        int mouseY = (int)mc.mouseHandler.getScaledYPos(mc.getWindow());
        FogRenderer fogRenderer = ((GameRendererAccessor)mc.gameRenderer).meteor$fogRenderer();
        if (Utils.canUpdate() || HudEditorScreen.isOpen()) {
            Profiler.get().push("radmin-client_render_2d");
            Utils.unscaledProjection();
            GuiGraphicsExtractor graphics = new GuiGraphicsExtractor(mc, this.renderState, mouseX, mouseY);
            float tickDelta2 = mc.getDeltaTracker().getGameTimeDeltaPartialTick(true);
            MeteorClient.EVENT_BUS.post((Object)Render2DEvent.get(graphics, graphics.guiWidth(), graphics.guiHeight(), tickDelta2));
            this.guiRenderer.render(fogRenderer.getBuffer(FogRenderer.FogMode.NONE));
            Utils.scaledProjection();
            Profiler.get().pop();
        }
        if ((tickDelta2 = mc.screen) instanceof WidgetScreen) {
            WidgetScreen widgetScreen = (WidgetScreen)tickDelta2;
            GuiGraphicsExtractor graphics = new GuiGraphicsExtractor(mc, this.renderState, mouseX, mouseY);
            float guiDelta = mc.getDeltaTracker().getGameTimeDeltaTicks();
            widgetScreen.renderCustom(graphics, mouseX, mouseY, guiDelta);
            this.guiRenderer.render(fogRenderer.getBuffer(FogRenderer.FogMode.NONE));
        }
    }
}


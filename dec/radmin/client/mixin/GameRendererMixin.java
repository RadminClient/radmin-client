/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.ModifyExpressionValue
 *  com.llamalad7.mixinextras.sugar.Local
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.Camera
 *  net.minecraft.client.DeltaTracker
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.render.pip.PictureInPictureRenderer
 *  net.minecraft.client.renderer.GameRenderer
 *  net.minecraft.client.renderer.RenderBuffers
 *  net.minecraft.client.renderer.state.GameRenderState
 *  net.minecraft.client.renderer.state.level.CameraRenderState
 *  net.minecraft.client.resources.model.sprite.SpriteGetter
 *  net.minecraft.util.profiling.Profiler
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  org.joml.Matrix4f
 *  org.joml.Matrix4fc
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.ModifyArg
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package radmin.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.state.GameRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import radmin.client.MeteorClient;
import radmin.client.MixinPlugin;
import radmin.client.events.render.Render3DEvent;
import radmin.client.events.render.RenderAfterWorldEvent;
import radmin.client.renderer.MeteorRenderPipelines;
import radmin.client.renderer.Renderer3D;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.render.Freecam;
import radmin.client.systems.modules.render.NoRender;
import radmin.client.systems.modules.render.Zoom;
import radmin.client.utils.Utils;
import radmin.client.utils.render.CustomBannerGuiElementRenderer;
import radmin.client.utils.render.NametagUtils;
import radmin.client.utils.render.RenderUtils;

@Mixin(value={GameRenderer.class})
public abstract class GameRendererMixin {
    @Shadow
    @Final
    private Minecraft minecraft;
    @Shadow
    @Final
    private Camera mainCamera;
    @Unique
    private Renderer3D renderer;
    @Unique
    private Renderer3D depthRenderer;
    @Unique
    private final PoseStack matrices = new PoseStack();
    @Shadow
    @Final
    private RenderBuffers renderBuffers;
    @Shadow
    @Final
    private GameRenderState gameRenderState;

    @Shadow
    protected abstract void bobView(CameraRenderState var1, PoseStack var2);

    @Shadow
    protected abstract void bobHurt(CameraRenderState var1, PoseStack var2);

    @ModifyArg(method={"<init>"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/gui/render/GuiRenderer;<init>(Lnet/minecraft/client/renderer/state/gui/GuiRenderState;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/feature/FeatureRenderDispatcher;Ljava/util/List;)V"))
    private List<PictureInPictureRenderer<?>> meteor$addSpecialRenderers(List<PictureInPictureRenderer<?>> list) {
        ArrayList result = new ArrayList(list.size() + 1);
        result.addAll(list);
        result.add(new CustomBannerGuiElementRenderer(this.renderBuffers.bufferSource(), (SpriteGetter)this.minecraft.getAtlasManager()));
        return result;
    }

    @Inject(method={"renderLevel"}, at={@At(value="INVOKE_STRING", target="Lnet/minecraft/util/profiling/ProfilerFiller;popPush(Ljava/lang/String;)V", args={"ldc=hand"})})
    private void onRenderLevel(DeltaTracker deltaTracker, CallbackInfo ci, @Local(name={"projectionMatrix"}) Matrix4f projectionMatrix, @Local(name={"modelViewMatrix"}) Matrix4fc modelViewMatrix, @Local(name={"worldPartialTicks"}) float worldPartialTicks, @Local(name={"bobStack"}) PoseStack bobStack) {
        if (!Utils.canUpdate()) {
            return;
        }
        Profiler.get().push("radmin-client_render");
        if (this.renderer == null) {
            this.renderer = new Renderer3D(MeteorRenderPipelines.WORLD_COLORED_LINES, MeteorRenderPipelines.WORLD_COLORED);
        }
        if (this.depthRenderer == null) {
            this.depthRenderer = new Renderer3D(MeteorRenderPipelines.WORLD_COLORED_LINES_DEPTH, MeteorRenderPipelines.WORLD_COLORED_DEPTH);
        }
        Render3DEvent event = Render3DEvent.get(bobStack, this.renderer, this.depthRenderer, worldPartialTicks, this.mainCamera.position().x, this.mainCamera.position().y, this.mainCamera.position().z);
        RenderSystem.getModelViewStack().pushMatrix().mul(modelViewMatrix);
        this.matrices.pushPose();
        this.bobHurt(this.gameRenderState.levelRenderState.cameraRenderState, this.matrices);
        if (((Boolean)this.minecraft.options.bobView().get()).booleanValue()) {
            this.bobView(this.gameRenderState.levelRenderState.cameraRenderState, this.matrices);
        }
        Matrix4f inverseBob = new Matrix4f((Matrix4fc)this.matrices.last().pose()).invert();
        RenderSystem.getModelViewStack().mul((Matrix4fc)inverseBob);
        this.matrices.popPose();
        Matrix4fc correctedPosition = MixinPlugin.isIrisPresent && RenderUtils.isShaderPackInUse() ? new Matrix4f(modelViewMatrix).mul((Matrix4fc)inverseBob) : modelViewMatrix;
        RenderUtils.updateScreenCenter((Matrix4fc)projectionMatrix, correctedPosition);
        NametagUtils.onRender(modelViewMatrix);
        this.renderer.begin();
        this.depthRenderer.begin();
        MeteorClient.EVENT_BUS.post((Object)event);
        this.renderer.render(bobStack);
        this.depthRenderer.render(bobStack);
        RenderSystem.getModelViewStack().popMatrix();
        Profiler.get().pop();
    }

    @Inject(method={"renderLevel"}, at={@At(value="TAIL")})
    private void onRenderLevelTail(CallbackInfo ci) {
        MeteorClient.EVENT_BUS.post((Object)RenderAfterWorldEvent.get());
    }

    @Inject(method={"displayItemActivation"}, at={@At(value="HEAD")}, cancellable=true)
    private void onDisplayItemActivation(ItemStack itemStack, CallbackInfo ci) {
        if (itemStack.getItem() == Items.TOTEM_OF_UNDYING && Modules.get().get(NoRender.class).noTotemAnimation()) {
            ci.cancel();
        }
    }

    @ModifyExpressionValue(method={"renderLevel"}, at={@At(value="INVOKE", target="Ljava/lang/Math;max(FF)F", ordinal=0)})
    private float applyCameraTransformationsMathHelperLerpProxy(float original) {
        return Modules.get().get(NoRender.class).noNausea() ? 0.0f : original;
    }

    @Inject(method={"renderItemInHand"}, at={@At(value="HEAD")}, cancellable=true)
    private void renderItemInHand(CameraRenderState cameraState, float deltaPartialTick, Matrix4fc modelViewMatrix, CallbackInfo ci) {
        if (!Modules.get().get(Freecam.class).renderHands() || !Modules.get().get(Zoom.class).renderHands()) {
            ci.cancel();
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.model.Model
 *  net.minecraft.client.model.object.banner.BannerModel
 *  net.minecraft.client.renderer.Sheets
 *  net.minecraft.client.renderer.SubmitNodeCollector
 *  net.minecraft.client.renderer.blockentity.BannerRenderer
 *  net.minecraft.client.renderer.blockentity.state.BannerRenderState
 *  net.minecraft.client.renderer.feature.ModelFeatureRenderer$CrumblingOverlay
 *  net.minecraft.client.renderer.state.level.CameraRenderState
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.client.resources.model.sprite.SpriteGetter
 *  net.minecraft.client.resources.model.sprite.SpriteId
 *  net.minecraft.util.Unit
 *  net.minecraft.world.level.block.BannerBlock$AttachmentType
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package radmin.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.object.banner.BannerModel;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.blockentity.state.BannerRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.util.Unit;
import net.minecraft.world.level.block.BannerBlock;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.render.NoRender;

@Mixin(value={BannerRenderer.class})
public abstract class BannerRendererMixin {
    @Shadow
    @Final
    private SpriteGetter sprites;

    @Shadow
    protected abstract BannerModel bannerModel(BannerBlock.AttachmentType var1);

    @Inject(method={"submit(Lnet/minecraft/client/renderer/blockentity/state/BannerRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void injectRender1(BannerRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera, CallbackInfo ci) {
        if (Modules.get().get(NoRender.class).getBannerRenderMode() == NoRender.BannerRenderMode.None) {
            ci.cancel();
        }
    }

    @Inject(method={"submit(Lnet/minecraft/client/renderer/blockentity/state/BannerRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/renderer/blockentity/BannerRenderer;submitBanner(Lnet/minecraft/client/resources/model/sprite/SpriteGetter;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;IILnet/minecraft/client/model/object/banner/BannerModel;Lnet/minecraft/client/model/object/banner/BannerFlagModel;FLnet/minecraft/world/item/DyeColor;Lnet/minecraft/world/level/block/entity/BannerPatternLayers;Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;I)V")}, cancellable=true)
    private void injectRender2(BannerRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera, CallbackInfo ci) {
        if (Modules.get().get(NoRender.class).getBannerRenderMode() == NoRender.BannerRenderMode.Pillar) {
            BannerRendererMixin.renderPillar(poseStack, submitNodeCollector, state.lightCoords, this.bannerModel(state.attachmentType), this.sprites, state.breakProgress);
            ci.cancel();
        }
    }

    @Unique
    private static void renderPillar(PoseStack matrices, SubmitNodeCollector submitNodeCollector, int light, BannerModel model, SpriteGetter sprites, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        SpriteId sprite = Sheets.BANNER_BASE;
        submitNodeCollector.submitModel((Model)model, (Object)Unit.INSTANCE, matrices, light, OverlayTexture.NO_OVERLAY, -1, sprite, sprites, 0, breakProgress);
    }
}


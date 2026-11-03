/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.v2.WrapWithCondition
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.model.Model
 *  net.minecraft.client.renderer.SubmitNodeCollector
 *  net.minecraft.client.renderer.blockentity.EnchantTableRenderer
 *  net.minecraft.client.renderer.feature.ModelFeatureRenderer$CrumblingOverlay
 *  net.minecraft.client.resources.model.sprite.SpriteGetter
 *  net.minecraft.client.resources.model.sprite.SpriteId
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 */
package radmin.client.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.EnchantTableRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.render.NoRender;

@Mixin(value={EnchantTableRenderer.class})
public abstract class EnchantTableRendererMixin {
    @WrapWithCondition(method={"submit(Lnet/minecraft/client/renderer/blockentity/state/EnchantTableRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;IIILnet/minecraft/client/resources/model/sprite/SpriteId;Lnet/minecraft/client/resources/model/sprite/SpriteGetter;ILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V")})
    private <S> boolean onRenderBookModelRenderProxy(SubmitNodeCollector instance, Model<? super S> model, Object state, PoseStack poseStack, int lightCoords, int overlayCoords, int tintedColor, SpriteId spriteId, SpriteGetter spriteGetter, int outlineColor, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        return !Modules.get().get(NoRender.class).noEnchTableBook();
    }
}


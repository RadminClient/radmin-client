/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.ModifyExpressionValue
 *  com.llamalad7.mixinextras.injector.v2.WrapWithCondition
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.model.EntityModel
 *  net.minecraft.client.model.geom.ModelPart
 *  net.minecraft.client.model.player.PlayerModel
 *  net.minecraft.client.renderer.SubmitNodeCollector
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.client.renderer.entity.LivingEntityRenderer
 *  net.minecraft.client.renderer.entity.player.AvatarRenderer
 *  net.minecraft.client.renderer.entity.state.AvatarRenderState
 *  net.minecraft.client.renderer.rendertype.RenderType
 *  net.minecraft.client.renderer.rendertype.RenderTypes
 *  net.minecraft.client.renderer.texture.TextureAtlasSprite
 *  net.minecraft.resources.Identifier
 *  net.minecraft.world.entity.Avatar
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package radmin.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Avatar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import radmin.client.MeteorClient;
import radmin.client.mixininterface.IVec3;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.render.Chams;
import radmin.client.utils.player.Rotations;

@Mixin(value={AvatarRenderer.class})
public abstract class AvatarRendererMixin
extends LivingEntityRenderer<Avatar, AvatarRenderState, PlayerModel> {
    @Unique
    private Chams chams;

    public AvatarRendererMixin(EntityRendererProvider.Context ctx, PlayerModel model, float shadowRadius) {
        super(ctx, (EntityModel)model, shadowRadius);
    }

    @Inject(method={"<init>"}, at={@At(value="RETURN")})
    private void init$chams(CallbackInfo ci) {
        this.chams = Modules.get().get(Chams.class);
    }

    @Inject(method={"extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V"}, at={@At(value="RETURN")})
    private void updateRenderState$scale(Avatar entity, AvatarRenderState state, float partialTicks, CallbackInfo ci) {
        if (!this.chams.isActive() || !this.chams.players.get().booleanValue()) {
            return;
        }
        if (this.chams.ignoreSelf.get().booleanValue() && entity == MeteorClient.mc.player) {
            return;
        }
        float v = this.chams.playersScale.get().floatValue();
        state.scale *= v;
        if (state.nameTagAttachment != null) {
            ((IVec3)state.nameTagAttachment).meteor$setY(state.nameTagAttachment.y + (double)(entity.getBbHeight() * v - entity.getBbHeight()));
        }
    }

    @ModifyExpressionValue(method={"renderHand"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/renderer/rendertype/RenderTypes;entityTranslucent(Lnet/minecraft/resources/Identifier;)Lnet/minecraft/client/renderer/rendertype/RenderType;")})
    private RenderType renderArm$texture(RenderType original, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, Identifier skinTexture, ModelPart arm, boolean hasSleeve) {
        if (this.chams.isActive() && this.chams.hand.get().booleanValue()) {
            Identifier texture = this.chams.handTexture.get() != false ? skinTexture : Chams.BLANK;
            return RenderTypes.entityTranslucent((Identifier)texture);
        }
        return original;
    }

    @WrapWithCondition(method={"renderHand"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModelPart(Lnet/minecraft/client/model/geom/ModelPart;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/rendertype/RenderType;IILnet/minecraft/client/renderer/texture/TextureAtlasSprite;)V")})
    private boolean renderArm$color(SubmitNodeCollector instance, ModelPart modelPart, PoseStack matrixStack, RenderType renderLayer, int light, int uv, TextureAtlasSprite sprite) {
        if (this.chams.isActive() && this.chams.hand.get().booleanValue()) {
            instance.submitModelPart(modelPart, matrixStack, renderLayer, light, uv, null, this.chams.handColor.get().getPacked(), null);
            return false;
        }
        return true;
    }

    @Inject(method={"extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V"}, at={@At(value="RETURN")})
    private void extractRenderState$rotations(Avatar entity, AvatarRenderState state, float partialTicks, CallbackInfo ci) {
        if (Rotations.rotating && entity == MeteorClient.mc.player) {
            state.yRot = 0.0f;
            state.bodyRot = Rotations.serverYaw;
            state.xRot = Rotations.serverPitch;
        }
    }
}


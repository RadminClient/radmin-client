/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.ModifyExpressionValue
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.renderer.SubmitNodeCollector
 *  net.minecraft.client.renderer.entity.layers.CapeLayer
 *  net.minecraft.client.renderer.entity.state.AvatarRenderState
 *  net.minecraft.core.ClientAsset$ResourceTexture
 *  net.minecraft.core.ClientAsset$Texture
 *  net.minecraft.resources.Identifier
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 */
package radmin.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.CapeLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.core.ClientAsset;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import radmin.client.mixininterface.IEntityRenderState;
import radmin.client.utils.network.Capes;

@Mixin(value={CapeLayer.class})
public abstract class CapeLayerMixin {
    @ModifyExpressionValue(method={"submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/AvatarRenderState;FF)V"}, at={@At(value="INVOKE", target="Lnet/minecraft/world/entity/player/PlayerSkin;cape()Lnet/minecraft/core/ClientAsset$Texture;")})
    private ClientAsset.Texture modifyCapeTexture(ClientAsset.Texture original, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, AvatarRenderState state, float yRot, float xRot) {
        Entity entity = ((IEntityRenderState)state).meteor$getEntity();
        if (entity instanceof Player) {
            Player player = (Player)entity;
            Identifier id = Capes.get(player);
            return id == null ? original : new ClientAsset.ResourceTexture(id, id);
        }
        return original;
    }
}


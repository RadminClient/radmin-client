/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.model.EntityModel
 *  net.minecraft.client.renderer.SubmitNodeCollector
 *  net.minecraft.client.renderer.entity.layers.CustomHeadLayer
 *  net.minecraft.client.renderer.entity.state.AvatarRenderState
 *  net.minecraft.client.renderer.entity.state.LivingEntityRenderState
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package radmin.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.render.NoRender;

@Mixin(value={CustomHeadLayer.class})
public abstract class CustomHeadLayerMixin<S extends LivingEntityRenderState, M extends EntityModel<S>> {
    @Inject(method={"submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;FF)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void onRender(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, S state, float yRot, float xRot, CallbackInfo ci) {
        if (state instanceof AvatarRenderState && Modules.get().get(NoRender.class).noArmor()) {
            ci.cancel();
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  meteordevelopment.orbit.ICancellable
 *  net.minecraft.client.renderer.SubmitNodeCollector
 *  net.minecraft.client.renderer.entity.ItemEntityRenderer
 *  net.minecraft.client.renderer.entity.state.ItemEntityRenderState
 *  net.minecraft.client.renderer.item.ItemModelResolver
 *  net.minecraft.client.renderer.state.level.CameraRenderState
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package radmin.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import meteordevelopment.orbit.ICancellable;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.state.ItemEntityRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import radmin.client.MeteorClient;
import radmin.client.events.render.RenderItemEntityEvent;

@Mixin(value={ItemEntityRenderer.class})
public abstract class ItemEntityRendererMixin {
    @Shadow
    @Final
    private ItemModelResolver itemModelResolver;

    @Inject(method={"submit(Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void renderStack(ItemEntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera, CallbackInfo ci) {
        RenderItemEntityEvent event = (RenderItemEntityEvent)MeteorClient.EVENT_BUS.post((ICancellable)RenderItemEntityEvent.get(state, MeteorClient.mc.getDeltaTracker().getGameTimeDeltaPartialTick(true), poseStack, null, state.lightCoords, this.itemModelResolver, submitNodeCollector));
        if (event.isCancelled()) {
            ci.cancel();
        }
    }
}


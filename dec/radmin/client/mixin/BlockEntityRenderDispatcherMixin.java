/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  meteordevelopment.orbit.ICancellable
 *  net.minecraft.client.renderer.SubmitNodeCollector
 *  net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher
 *  net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState
 *  net.minecraft.client.renderer.state.level.CameraRenderState
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package radmin.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import meteordevelopment.orbit.ICancellable;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import radmin.client.MeteorClient;
import radmin.client.events.render.RenderBlockEntityEvent;

@Mixin(value={BlockEntityRenderDispatcher.class})
public abstract class BlockEntityRenderDispatcherMixin {
    @Inject(method={"submit"}, at={@At(value="HEAD")}, cancellable=true)
    private <S extends BlockEntityRenderState> void onRenderEntity(S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera, CallbackInfo ci) {
        RenderBlockEntityEvent event = (RenderBlockEntityEvent)MeteorClient.EVENT_BUS.post((ICancellable)RenderBlockEntityEvent.get(state));
        if (event.isCancelled()) {
            ci.cancel();
        }
    }
}


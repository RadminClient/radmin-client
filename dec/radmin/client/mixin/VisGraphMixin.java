/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  meteordevelopment.orbit.ICancellable
 *  net.minecraft.client.renderer.chunk.VisGraph
 *  net.minecraft.core.BlockPos
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package radmin.client.mixin;

import meteordevelopment.orbit.ICancellable;
import net.minecraft.client.renderer.chunk.VisGraph;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import radmin.client.MeteorClient;
import radmin.client.events.world.ChunkOcclusionEvent;

@Mixin(value={VisGraph.class})
public abstract class VisGraphMixin {
    @Inject(method={"setOpaque"}, at={@At(value="HEAD")}, cancellable=true)
    private void onMarkClosed(BlockPos pos, CallbackInfo ci) {
        ChunkOcclusionEvent event = (ChunkOcclusionEvent)MeteorClient.EVENT_BUS.post((ICancellable)ChunkOcclusionEvent.get());
        if (event.isCancelled()) {
            ci.cancel();
        }
    }
}


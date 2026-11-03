/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState
 */
package radmin.client.events.render;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import radmin.client.events.Cancellable;

public class RenderBlockEntityEvent
extends Cancellable {
    private static final RenderBlockEntityEvent INSTANCE = new RenderBlockEntityEvent();
    public BlockEntityRenderState blockEntityState;

    public static RenderBlockEntityEvent get(BlockEntityRenderState blockEntityState) {
        INSTANCE.setCancelled(false);
        RenderBlockEntityEvent.INSTANCE.blockEntityState = blockEntityState;
        return INSTANCE;
    }
}


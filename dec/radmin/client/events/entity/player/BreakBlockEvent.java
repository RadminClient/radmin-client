/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 */
package radmin.client.events.entity.player;

import net.minecraft.core.BlockPos;
import radmin.client.events.Cancellable;

public class BreakBlockEvent
extends Cancellable {
    private static final BreakBlockEvent INSTANCE = new BreakBlockEvent();
    public BlockPos blockPos;

    public static BreakBlockEvent get(BlockPos blockPos) {
        INSTANCE.setCancelled(false);
        BreakBlockEvent.INSTANCE.blockPos = blockPos;
        return INSTANCE;
    }
}


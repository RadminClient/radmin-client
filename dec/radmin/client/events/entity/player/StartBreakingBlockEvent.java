/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 */
package radmin.client.events.entity.player;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import radmin.client.events.Cancellable;

public class StartBreakingBlockEvent
extends Cancellable {
    private static final StartBreakingBlockEvent INSTANCE = new StartBreakingBlockEvent();
    public BlockPos blockPos;
    public Direction direction;

    public static StartBreakingBlockEvent get(BlockPos blockPos, Direction direction) {
        INSTANCE.setCancelled(false);
        StartBreakingBlockEvent.INSTANCE.blockPos = blockPos;
        StartBreakingBlockEvent.INSTANCE.direction = direction;
        return INSTANCE;
    }
}


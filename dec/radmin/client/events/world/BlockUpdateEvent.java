/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.world.level.block.state.BlockState
 */
package radmin.client.events.world;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockUpdateEvent {
    private static final BlockUpdateEvent INSTANCE = new BlockUpdateEvent();
    public BlockPos pos;
    public BlockState oldState;
    public BlockState newState;

    public static BlockUpdateEvent get(BlockPos pos, BlockState oldState, BlockState newState) {
        BlockUpdateEvent.INSTANCE.pos = pos;
        BlockUpdateEvent.INSTANCE.oldState = oldState;
        BlockUpdateEvent.INSTANCE.newState = newState;
        return INSTANCE;
    }
}


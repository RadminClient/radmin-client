/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.level.block.state.BlockState
 */
package radmin.client.events.world;

import net.minecraft.world.level.block.state.BlockState;

public class BlockActivateEvent {
    private static final BlockActivateEvent INSTANCE = new BlockActivateEvent();
    public BlockState blockState;

    public static BlockActivateEvent get(BlockState blockState) {
        BlockActivateEvent.INSTANCE.blockState = blockState;
        return INSTANCE;
    }
}


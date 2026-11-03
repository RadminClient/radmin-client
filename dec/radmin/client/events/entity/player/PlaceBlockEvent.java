/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.world.level.block.Block
 */
package radmin.client.events.entity.player;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import radmin.client.events.Cancellable;

public class PlaceBlockEvent
extends Cancellable {
    private static final PlaceBlockEvent INSTANCE = new PlaceBlockEvent();
    public BlockPos blockPos;
    public Block block;

    public static PlaceBlockEvent get(BlockPos blockPos, Block block) {
        INSTANCE.setCancelled(false);
        PlaceBlockEvent.INSTANCE.blockPos = blockPos;
        PlaceBlockEvent.INSTANCE.block = block;
        return INSTANCE;
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.core.BlockPos
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.phys.shapes.VoxelShape
 */
package radmin.client.events.world;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import radmin.client.events.Cancellable;

public class CollisionShapeEvent
extends Cancellable {
    private static final CollisionShapeEvent INSTANCE = new CollisionShapeEvent();
    public BlockState state;
    public BlockPos pos;
    public VoxelShape shape;

    public static CollisionShapeEvent get(BlockState state, BlockPos pos, VoxelShape shape) {
        CollisionShapeEvent event = INSTANCE;
        if (!RenderSystem.isOnRenderThread()) {
            event = new CollisionShapeEvent();
        }
        event.setCancelled(false);
        event.state = state;
        event.pos = pos;
        event.shape = shape;
        return event;
    }
}


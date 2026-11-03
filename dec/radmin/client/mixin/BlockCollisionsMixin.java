/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.wrapoperation.Operation
 *  com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation
 *  meteordevelopment.orbit.ICancellable
 *  net.minecraft.client.Minecraft
 *  net.minecraft.core.BlockPos
 *  net.minecraft.world.level.BlockCollisions
 *  net.minecraft.world.level.CollisionGetter
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.phys.shapes.CollisionContext
 *  net.minecraft.world.phys.shapes.Shapes
 *  net.minecraft.world.phys.shapes.VoxelShape
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 */
package radmin.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import meteordevelopment.orbit.ICancellable;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockCollisions;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import radmin.client.MeteorClient;
import radmin.client.events.world.CollisionShapeEvent;

@Mixin(value={BlockCollisions.class})
public abstract class BlockCollisionsMixin {
    @WrapOperation(method={"computeNext"}, at={@At(value="INVOKE", target="Lnet/minecraft/world/phys/shapes/CollisionContext;getCollisionShape(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/CollisionGetter;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/phys/shapes/VoxelShape;")})
    private VoxelShape onComputeNextCollisionBox(CollisionContext instance, BlockState blockState, CollisionGetter collisionView, BlockPos blockPos, Operation<VoxelShape> original) {
        VoxelShape shape = (VoxelShape)original.call(new Object[]{instance, blockState, collisionView, blockPos});
        if (collisionView != Minecraft.getInstance().level) {
            return shape;
        }
        CollisionShapeEvent event = (CollisionShapeEvent)MeteorClient.EVENT_BUS.post((ICancellable)CollisionShapeEvent.get(blockState, blockPos, shape));
        return event.isCancelled() ? Shapes.empty() : event.shape;
    }
}


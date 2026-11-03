/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.ModifyExpressionValue
 *  com.llamalad7.mixinextras.sugar.Local
 *  meteordevelopment.orbit.ICancellable
 *  net.caffeinemc.mods.lithium.common.entity.movement.ChunkAwareBlockCollisionSweeper
 *  net.caffeinemc.mods.lithium.common.entity.movement.ChunkAwareBlockCollisionSweeperVoxelShape
 *  net.minecraft.client.Minecraft
 *  net.minecraft.core.BlockPos
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.shapes.Shapes
 *  net.minecraft.world.phys.shapes.VoxelShape
 *  org.jetbrains.annotations.Nullable
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 */
package radmin.client.mixin.lithium;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import meteordevelopment.orbit.ICancellable;
import net.caffeinemc.mods.lithium.common.entity.movement.ChunkAwareBlockCollisionSweeper;
import net.caffeinemc.mods.lithium.common.entity.movement.ChunkAwareBlockCollisionSweeperVoxelShape;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import radmin.client.MeteorClient;
import radmin.client.events.world.CollisionShapeEvent;

@Mixin(value={ChunkAwareBlockCollisionSweeperVoxelShape.class})
public abstract class ChunkAwareBlockCollisionSweeperMixin
extends ChunkAwareBlockCollisionSweeper<VoxelShape> {
    public ChunkAwareBlockCollisionSweeperMixin(Level world, @Nullable Entity entity, AABB box, boolean hideLastCollision) {
        super(world, entity, box, hideLastCollision);
    }

    @ModifyExpressionValue(method={"computeNext()Lnet/minecraft/world/phys/shapes/VoxelShape;"}, at={@At(value="INVOKE", target="Lnet/minecraft/world/phys/shapes/CollisionContext;getCollisionShape(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/CollisionGetter;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/phys/shapes/VoxelShape;")})
    private VoxelShape modifyCollisionShape(VoxelShape original, @Local(name={"state"}) BlockState state) {
        if (this.world != Minecraft.getInstance().level) {
            return original;
        }
        CollisionShapeEvent event = (CollisionShapeEvent)MeteorClient.EVENT_BUS.post((ICancellable)CollisionShapeEvent.get(state, (BlockPos)this.pos, original));
        return event.isCancelled() ? Shapes.empty() : event.shape;
    }
}


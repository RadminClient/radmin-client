/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.state.BlockBehaviour$BlockStateBase
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.phys.BlockHitResult
 *  org.jspecify.annotations.NonNull
 *  org.spongepowered.asm.mixin.Mixin
 */
package radmin.client.mixin;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Mixin;
import radmin.client.MeteorClient;
import radmin.client.events.world.BlockActivateEvent;

@Mixin(value={BlockState.class})
public abstract class BlockStateMixin
extends BlockBehaviour.BlockStateBase {
    protected BlockStateMixin(Block owner, Property<?>[] propertyKeys, Comparable<?>[] propertyValues) {
        super(owner, propertyKeys, (Comparable[])propertyValues);
    }

    public @NonNull InteractionResult useWithoutItem(@NonNull Level level, @NonNull Player player, @NonNull BlockHitResult hitResult) {
        MeteorClient.EVENT_BUS.post((Object)BlockActivateEvent.get((BlockState)this));
        return super.useWithoutItem(level, player, hitResult);
    }
}


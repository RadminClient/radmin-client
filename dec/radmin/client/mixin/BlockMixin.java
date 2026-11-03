/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.ModifyReturnValue
 *  net.minecraft.core.Direction
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.state.BlockBehaviour
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package radmin.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.core.Direction;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.movement.NoSlow;
import radmin.client.systems.modules.movement.Slippy;
import radmin.client.systems.modules.render.Xray;

@Mixin(value={Block.class})
public abstract class BlockMixin
extends BlockBehaviour
implements ItemLike {
    public BlockMixin(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @ModifyReturnValue(method={"getFriction"}, at={@At(value="RETURN")})
    public float getFriction(float original) {
        if (Modules.get() == null) {
            return original;
        }
        Slippy slippy = Modules.get().get(Slippy.class);
        Block block = (Block)this;
        if (slippy.isActive() && (slippy.listMode.get() == Slippy.ListMode.Whitelist ? slippy.allowedBlocks.get().contains(block) : !slippy.ignoredBlocks.get().contains(block))) {
            return slippy.friction.get().floatValue();
        }
        if (block == Blocks.SLIME_BLOCK && Modules.get().get(NoSlow.class).slimeBlock()) {
            return 0.6f;
        }
        return original;
    }

    @Inject(method={"shouldRenderFace"}, at={@At(value="HEAD")}, cancellable=true)
    private static void meteor$forceXrayFace(BlockState state, BlockState neighborState, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        Modules modules = Modules.get();
        if (modules == null) {
            return;
        }
        Xray xray = modules.get(Xray.class);
        if (xray.isActive() && !xray.isBlocked(state.getBlock(), null)) {
            cir.setReturnValue((Object)true);
        }
    }
}


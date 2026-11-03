/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
 *  meteordevelopment.orbit.EventHandler
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.Position
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.InteractionResult$Fail
 *  net.minecraft.world.InteractionResult$Success
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.Vec3
 */
package radmin.client.systems.modules.player;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Set;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import radmin.client.events.entity.player.DoItemUseEvent;
import radmin.client.systems.modules.Categories;
import radmin.client.systems.modules.Module;

public class GhostHand
extends Module {
    private final Set<BlockPos> posList = new ObjectOpenHashSet();

    public GhostHand() {
        super(Categories.Player, "ghost-hand", "Opens containers through walls.");
    }

    @EventHandler
    private void onTick(DoItemUseEvent event) {
        if (!this.mc.options.keyUse.isDown() || this.mc.player.isShiftKeyDown()) {
            return;
        }
        if (this.mc.level.getBlockState(BlockPos.containing((Position)this.mc.player.pick(this.mc.player.blockInteractionRange(), this.mc.getDeltaTracker().getGameTimeDeltaPartialTick(true), false).getLocation())).hasBlockEntity()) {
            return;
        }
        Vec3 direction = new Vec3(0.0, 0.0, 0.1).xRot(-((float)Math.toRadians(this.mc.player.getXRot()))).yRot(-((float)Math.toRadians(this.mc.player.getYRot())));
        this.posList.clear();
        int i = 1;
        while ((double)i < this.mc.player.blockInteractionRange() * 10.0) {
            BlockPos pos = BlockPos.containing((Position)this.mc.player.getEyePosition(this.mc.getDeltaTracker().getGameTimeDeltaPartialTick(true)).add(direction.scale((double)i)));
            if (!this.posList.contains(pos)) {
                this.posList.add(pos);
                if (this.mc.level.getBlockState(pos).hasBlockEntity()) {
                    for (InteractionHand hand : InteractionHand.values()) {
                        InteractionResult result = this.mc.gameMode.useItemOn(this.mc.player, hand, new BlockHitResult(new Vec3((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5), Direction.UP, pos, true));
                        if (!(result instanceof InteractionResult.Success) && !(result instanceof InteractionResult.Fail)) continue;
                        this.mc.player.swing(hand);
                        event.cancel();
                        return;
                    }
                }
            }
            ++i;
        }
    }
}


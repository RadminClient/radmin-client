/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  meteordevelopment.orbit.EventHandler
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ServerboundPlayerActionPacket
 *  net.minecraft.network.protocol.game.ServerboundPlayerActionPacket$Action
 *  net.minecraft.network.protocol.game.ServerboundSwingPacket
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.level.block.BedBlock
 */
package radmin.client.systems.modules.combat;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.BedBlock;
import radmin.client.events.world.TickEvent;
import radmin.client.settings.BoolSetting;
import radmin.client.settings.Setting;
import radmin.client.settings.SettingGroup;
import radmin.client.systems.modules.Categories;
import radmin.client.systems.modules.Module;
import radmin.client.utils.player.InvUtils;
import radmin.client.utils.player.PlayerUtils;
import radmin.client.utils.player.Rotations;
import radmin.client.utils.world.BlockUtils;

public class AntiBed
extends Module {
    private final SettingGroup sgGeneral;
    private final Setting<Boolean> placeStringTop;
    private final Setting<Boolean> placeStringMiddle;
    private final Setting<Boolean> placeStringBottom;
    private final Setting<Boolean> onlyInHole;
    private boolean breaking;

    public AntiBed() {
        super(Categories.Combat, "anti-bed", "Places string to prevent beds being placed on you.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.placeStringTop = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("place-string-top")).description("Places string above you.")).defaultValue(false)).build());
        this.placeStringMiddle = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("place-string-middle")).description("Places string in your upper hitbox.")).defaultValue(true)).build());
        this.placeStringBottom = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("place-string-bottom")).description("Places string at your feet.")).defaultValue(false)).build());
        this.onlyInHole = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("only-in-hole")).description("Only functions when you are standing in a hole.")).defaultValue(true)).build());
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (this.onlyInHole.get().booleanValue() && !PlayerUtils.isInHole(true)) {
            return;
        }
        BlockPos head = this.mc.player.blockPosition().above();
        if (this.mc.level.getBlockState(head).getBlock() instanceof BedBlock && !this.breaking) {
            Rotations.rotate(Rotations.getYaw(head), Rotations.getPitch(head), 50, () -> this.sendMinePackets(head));
            this.breaking = true;
        } else if (this.breaking) {
            Rotations.rotate(Rotations.getYaw(head), Rotations.getPitch(head), 50, () -> this.sendStopPackets(head));
            this.breaking = false;
        }
        if (this.placeStringTop.get().booleanValue()) {
            this.place(this.mc.player.blockPosition().above(2));
        }
        if (this.placeStringMiddle.get().booleanValue()) {
            this.place(this.mc.player.blockPosition().above(1));
        }
        if (this.placeStringBottom.get().booleanValue()) {
            this.place(this.mc.player.blockPosition());
        }
    }

    private void place(BlockPos blockPos) {
        if (this.mc.level.getBlockState(blockPos).getBlock().asItem() != Items.STRING) {
            BlockUtils.place(blockPos, InvUtils.findInHotbar(Items.STRING), 50, false);
        }
    }

    private void sendMinePackets(BlockPos blockPos) {
        this.mc.gameMode.startPrediction(this.mc.level, sequence -> new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK, blockPos, Direction.UP, sequence));
        this.mc.gameMode.startPrediction(this.mc.level, sequence -> new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.STOP_DESTROY_BLOCK, blockPos, Direction.UP, sequence));
    }

    private void sendStopPackets(BlockPos blockPos) {
        this.mc.getConnection().send((Packet)new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.ABORT_DESTROY_BLOCK, blockPos, Direction.UP));
        this.mc.getConnection().send((Packet)new ServerboundSwingPacket(InteractionHand.MAIN_HAND));
    }
}


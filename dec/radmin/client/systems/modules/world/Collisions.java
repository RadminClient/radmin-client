/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  meteordevelopment.orbit.EventHandler
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ServerboundMovePlayerPacket
 *  net.minecraft.network.protocol.game.ServerboundMoveVehiclePacket
 *  net.minecraft.world.level.block.AbstractCauldronBlock
 *  net.minecraft.world.level.block.BaseFireBlock
 *  net.minecraft.world.level.block.BasePressurePlateBlock
 *  net.minecraft.world.level.block.BaseRailBlock
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.CactusBlock
 *  net.minecraft.world.level.block.CampfireBlock
 *  net.minecraft.world.level.block.HoneyBlock
 *  net.minecraft.world.level.block.PowderSnowBlock
 *  net.minecraft.world.level.block.SweetBerryBushBlock
 *  net.minecraft.world.level.block.TrapDoorBlock
 *  net.minecraft.world.level.block.TripWireBlock
 *  net.minecraft.world.level.block.TripWireHookBlock
 *  net.minecraft.world.level.block.WebBlock
 *  net.minecraft.world.phys.shapes.Shapes
 */
package radmin.client.systems.modules.world;

import java.util.List;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundMoveVehiclePacket;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.BasePressurePlateBlock;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.HoneyBlock;
import net.minecraft.world.level.block.PowderSnowBlock;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.TripWireBlock;
import net.minecraft.world.level.block.TripWireHookBlock;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.phys.shapes.Shapes;
import radmin.client.events.entity.player.PlayerMoveEvent;
import radmin.client.events.packets.PacketEvent;
import radmin.client.events.world.CollisionShapeEvent;
import radmin.client.mixininterface.IVec3;
import radmin.client.settings.BlockListSetting;
import radmin.client.settings.BoolSetting;
import radmin.client.settings.Setting;
import radmin.client.settings.SettingGroup;
import radmin.client.systems.modules.Categories;
import radmin.client.systems.modules.Module;

public class Collisions
extends Module {
    private final SettingGroup sgGeneral;
    public final Setting<List<Block>> blocks;
    private final Setting<Boolean> magma;
    private final Setting<Boolean> unloadedChunks;
    private final Setting<Boolean> ignoreBorder;

    public Collisions() {
        super(Categories.World, "collisions", "Adds collision boxes to certain blocks/areas.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.blocks = this.sgGeneral.add(((BlockListSetting.Builder)((BlockListSetting.Builder)new BlockListSetting.Builder().name("blocks")).description("What blocks should be added collision box.")).filter(this::blockFilter).build());
        this.magma = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("magma")).description("Prevents you from walking over magma blocks.")).defaultValue(false)).build());
        this.unloadedChunks = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("unloaded-chunks")).description("Stops you from going into unloaded chunks.")).defaultValue(false)).build());
        this.ignoreBorder = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("ignore-border")).description("Removes world border collision.")).defaultValue(false)).build());
    }

    @EventHandler
    private void onCollisionShape(CollisionShapeEvent event) {
        if (this.mc.level == null || this.mc.player == null) {
            return;
        }
        if (!event.state.getFluidState().isEmpty()) {
            return;
        }
        if (this.blocks.get().contains(event.state.getBlock())) {
            event.shape = Shapes.block();
        } else if (this.magma.get().booleanValue() && !this.mc.player.isShiftKeyDown() && event.state.isAir() && this.mc.level.getBlockState(event.pos.below()).getBlock() == Blocks.MAGMA_BLOCK) {
            event.shape = Shapes.block();
        }
    }

    @EventHandler
    private void onPlayerMove(PlayerMoveEvent event) {
        int x = (int)(this.mc.player.getX() + event.movement.x) >> 4;
        int z = (int)(this.mc.player.getZ() + event.movement.z) >> 4;
        if (this.unloadedChunks.get().booleanValue() && !this.mc.level.getChunkSource().hasChunk(x, z)) {
            ((IVec3)event.movement).meteor$set(0.0, event.movement.y, 0.0);
        }
    }

    @EventHandler
    private void onPacketSend(PacketEvent.Send event) {
        if (!this.unloadedChunks.get().booleanValue()) {
            return;
        }
        Packet<?> packet = event.packet;
        if (packet instanceof ServerboundMoveVehiclePacket) {
            ServerboundMoveVehiclePacket packet2 = (ServerboundMoveVehiclePacket)packet;
            if (!this.mc.level.getChunkSource().hasChunk((int)packet2.position().x() >> 4, (int)packet2.position().z() >> 4)) {
                this.mc.player.getVehicle().absSnapTo(this.mc.player.getVehicle().xo, this.mc.player.getVehicle().yo, this.mc.player.getVehicle().zo);
                event.cancel();
            }
        } else {
            packet = event.packet;
            if (packet instanceof ServerboundMovePlayerPacket) {
                ServerboundMovePlayerPacket packet3 = (ServerboundMovePlayerPacket)packet;
                if (!this.mc.level.getChunkSource().hasChunk((int)packet3.getX(this.mc.player.getX()) >> 4, (int)packet3.getZ(this.mc.player.getZ()) >> 4)) {
                    event.cancel();
                }
            }
        }
    }

    private boolean blockFilter(Block block) {
        return block instanceof BaseFireBlock || block instanceof BasePressurePlateBlock || block instanceof TripWireBlock || block instanceof TripWireHookBlock || block instanceof WebBlock || block instanceof CampfireBlock || block instanceof SweetBerryBushBlock || block instanceof CactusBlock || block instanceof BaseRailBlock || block instanceof TrapDoorBlock || block instanceof PowderSnowBlock || block instanceof AbstractCauldronBlock || block instanceof HoneyBlock;
    }

    public boolean ignoreBorder() {
        return this.isActive() && this.ignoreBorder.get() != false;
    }
}


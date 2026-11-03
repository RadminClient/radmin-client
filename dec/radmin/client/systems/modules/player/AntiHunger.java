/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  meteordevelopment.orbit.EventHandler
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ServerboundMovePlayerPacket
 *  net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket
 *  net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket$Action
 */
package radmin.client.systems.modules.player;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import radmin.client.events.entity.player.SendMovementPacketsEvent;
import radmin.client.events.packets.PacketEvent;
import radmin.client.mixin.ServerboundMovePlayerPacketAccessor;
import radmin.client.settings.BoolSetting;
import radmin.client.settings.Setting;
import radmin.client.settings.SettingGroup;
import radmin.client.systems.modules.Categories;
import radmin.client.systems.modules.Module;

public class AntiHunger
extends Module {
    private final SettingGroup sgGeneral;
    private final Setting<Boolean> sprint;
    private final Setting<Boolean> onGround;
    private boolean lastOnGround;
    private boolean ignorePacket;

    public AntiHunger() {
        super(Categories.Player, "anti-hunger", "Reduces (does NOT remove) hunger consumption.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.sprint = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("sprint")).description("Spoofs sprinting packets.")).defaultValue(true)).build());
        this.onGround = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("on-ground")).description("Spoofs the onGround flag.")).defaultValue(true)).build());
    }

    @Override
    public void onActivate() {
        this.lastOnGround = this.mc.player.onGround();
    }

    @EventHandler
    private void onSendPacket(PacketEvent.Send event) {
        ServerboundPlayerCommandPacket packet;
        if (this.ignorePacket && event.packet instanceof ServerboundMovePlayerPacket) {
            this.ignorePacket = false;
            return;
        }
        if (this.mc.player.isPassenger() || this.mc.player.isInWater() || this.mc.player.isUnderWater()) {
            return;
        }
        Packet<?> packet2 = event.packet;
        if (packet2 instanceof ServerboundPlayerCommandPacket) {
            packet = (ServerboundPlayerCommandPacket)packet2;
            if (this.sprint.get().booleanValue() && packet.getAction() == ServerboundPlayerCommandPacket.Action.START_SPRINTING) {
                event.cancel();
            }
        }
        if ((packet2 = event.packet) instanceof ServerboundMovePlayerPacket) {
            packet = (ServerboundMovePlayerPacket)packet2;
            if (this.onGround.get().booleanValue() && this.mc.player.onGround() && this.mc.player.fallDistance <= 0.0 && !this.mc.gameMode.isDestroying()) {
                ((ServerboundMovePlayerPacketAccessor)packet).meteor$setOnGround(false);
            }
        }
    }

    @EventHandler
    private void onTick(SendMovementPacketsEvent.Pre event) {
        if (this.mc.player.onGround() && !this.lastOnGround && this.onGround.get().booleanValue()) {
            this.ignorePacket = true;
        }
        this.lastOnGround = this.mc.player.onGround();
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  meteordevelopment.orbit.EventHandler
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ClientboundSetTimePacket
 */
package radmin.client.systems.modules.render;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetTimePacket;
import radmin.client.events.packets.PacketEvent;
import radmin.client.events.world.TickEvent;
import radmin.client.settings.DoubleSetting;
import radmin.client.settings.Setting;
import radmin.client.settings.SettingGroup;
import radmin.client.systems.modules.Categories;
import radmin.client.systems.modules.Module;

public class TimeChanger
extends Module {
    private final SettingGroup sgGeneral;
    private final Setting<Double> time;
    long oldTime;

    public TimeChanger() {
        super(Categories.Render, "time-changer", "Makes you able to set a custom time.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.time = this.sgGeneral.add(((DoubleSetting.Builder)((DoubleSetting.Builder)new DoubleSetting.Builder().name("time")).description("The specified time to be set.")).defaultValue(0.0).sliderRange(-20000.0, 20000.0).build());
    }

    @Override
    public void onActivate() {
        this.oldTime = this.mc.level.getGameTime();
    }

    @Override
    public void onDeactivate() {
        this.mc.level.getLevelData().setGameTime(this.oldTime);
    }

    @EventHandler
    private void onPacketReceive(PacketEvent.Receive event) {
        Packet<?> packet = event.packet;
        if (packet instanceof ClientboundSetTimePacket) {
            ClientboundSetTimePacket packet2 = (ClientboundSetTimePacket)packet;
            this.oldTime = packet2.gameTime();
            event.cancel();
        }
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        this.mc.level.getLevelData().setGameTime(this.time.get().longValue());
    }
}


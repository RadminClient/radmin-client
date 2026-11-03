/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  meteordevelopment.orbit.EventHandler
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ClientboundGameEventPacket
 *  net.minecraft.network.protocol.game.ClientboundGameEventPacket$Type
 */
package radmin.client.systems.modules.render;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import radmin.client.events.packets.PacketEvent;
import radmin.client.events.world.TickEvent;
import radmin.client.settings.DoubleSetting;
import radmin.client.settings.Setting;
import radmin.client.settings.SettingGroup;
import radmin.client.systems.modules.Categories;
import radmin.client.systems.modules.Module;

public class WeatherChanger
extends Module {
    private final SettingGroup sgGeneral;
    private final Setting<Double> rainLevel;
    private final Setting<Double> thunderLevel;
    private float oldThunderLevel;
    private float oldRainLevel;

    public WeatherChanger() {
        super(Categories.Render, "weather-changer", "Allows you to override the world's current weather.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.rainLevel = this.sgGeneral.add(((DoubleSetting.Builder)((DoubleSetting.Builder)new DoubleSetting.Builder().name("rain-level")).description("The specified rain level to be set.")).defaultValue(0.0).sliderRange(0.0, 1.0).build());
        this.thunderLevel = this.sgGeneral.add(((DoubleSetting.Builder)((DoubleSetting.Builder)new DoubleSetting.Builder().name("thunder-level")).description("The specified thunder level to be set.")).defaultValue(0.0).sliderRange(0.0, 1.0).build());
    }

    @Override
    public void onActivate() {
        if (this.mc.level == null) {
            return;
        }
        this.oldThunderLevel = this.mc.level.getThunderLevel(1.0f);
        this.oldRainLevel = this.mc.level.getRainLevel(1.0f);
    }

    @Override
    public void onDeactivate() {
        if (this.mc.level == null) {
            return;
        }
        this.mc.level.setRainLevel(this.oldRainLevel);
        this.mc.level.setThunderLevel(this.oldThunderLevel);
    }

    @EventHandler
    private void onPacketReceive(PacketEvent.Receive event) {
        Packet<?> packet = event.packet;
        if (!(packet instanceof ClientboundGameEventPacket)) {
            return;
        }
        ClientboundGameEventPacket packet2 = (ClientboundGameEventPacket)packet;
        ClientboundGameEventPacket.Type type = packet2.getEvent();
        if (!this.isWeatherPacket(type)) {
            return;
        }
        if (type == ClientboundGameEventPacket.THUNDER_LEVEL_CHANGE) {
            this.oldThunderLevel = packet2.getParam();
        } else if (type == ClientboundGameEventPacket.RAIN_LEVEL_CHANGE) {
            this.oldRainLevel = packet2.getParam();
        }
        event.cancel();
    }

    private boolean isWeatherPacket(ClientboundGameEventPacket.Type type) {
        return type == ClientboundGameEventPacket.START_RAINING || type == ClientboundGameEventPacket.STOP_RAINING || type == ClientboundGameEventPacket.THUNDER_LEVEL_CHANGE || type == ClientboundGameEventPacket.RAIN_LEVEL_CHANGE;
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (this.mc.level == null) {
            return;
        }
        this.mc.level.setRainLevel(this.rainLevel.get().floatValue());
        this.mc.level.setThunderLevel(this.thunderLevel.get().floatValue());
    }
}


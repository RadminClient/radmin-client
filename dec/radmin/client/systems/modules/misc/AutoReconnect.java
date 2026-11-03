/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.Pair
 *  it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair
 *  meteordevelopment.orbit.EventHandler
 *  net.minecraft.client.multiplayer.ServerData
 *  net.minecraft.client.multiplayer.resolver.ServerAddress
 */
package radmin.client.systems.modules.misc;

import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import java.util.Objects;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import radmin.client.MeteorClient;
import radmin.client.events.world.ServerConnectBeginEvent;
import radmin.client.settings.BoolSetting;
import radmin.client.settings.DoubleSetting;
import radmin.client.settings.Setting;
import radmin.client.settings.SettingGroup;
import radmin.client.systems.modules.Categories;
import radmin.client.systems.modules.Module;

public class AutoReconnect
extends Module {
    private final SettingGroup sgGeneral;
    public final Setting<Double> time;
    public final Setting<Boolean> button;
    public Pair<ServerAddress, ServerData> lastServerConnection;

    public AutoReconnect() {
        super(Categories.Misc, "auto-reconnect", "Automatically reconnects when disconnected from a server.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.time = this.sgGeneral.add(((DoubleSetting.Builder)((DoubleSetting.Builder)new DoubleSetting.Builder().name("delay")).description("The amount of seconds to wait before reconnecting to the server.")).defaultValue(3.5).min(0.0).decimalPlaces(1).build());
        this.button = this.sgGeneral.add(((BoolSetting.Builder)((BoolSetting.Builder)((BoolSetting.Builder)new BoolSetting.Builder().name("hide-buttons")).description("Will hide the buttons related to Auto Reconnect.")).defaultValue(false)).build());
        MeteorClient.EVENT_BUS.subscribe((Object)new StaticListener(this));
    }

    private class StaticListener {
        final /* synthetic */ AutoReconnect this$0;

        private StaticListener(AutoReconnect autoReconnect) {
            AutoReconnect autoReconnect2 = autoReconnect;
            Objects.requireNonNull(autoReconnect2);
            this.this$0 = autoReconnect2;
        }

        @EventHandler
        private void onGameJoined(ServerConnectBeginEvent event) {
            this.this$0.lastServerConnection = new ObjectObjectImmutablePair((Object)event.address, (Object)event.info);
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  meteordevelopment.orbit.EventHandler
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.PacketType
 *  org.jetbrains.annotations.NotNull
 */
package radmin.client.systems.modules.misc;

import java.util.Set;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketType;
import org.jetbrains.annotations.NotNull;
import radmin.client.events.packets.PacketEvent;
import radmin.client.settings.PacketListSetting;
import radmin.client.settings.Setting;
import radmin.client.settings.SettingGroup;
import radmin.client.systems.modules.Categories;
import radmin.client.systems.modules.Module;

public class PacketCanceller
extends Module {
    private final SettingGroup sgGeneral;
    private final Setting<Set<PacketType<? extends @NotNull Packet<?>>>> s2cPackets;
    private final Setting<Set<PacketType<? extends @NotNull Packet<?>>>> c2sPackets;

    public PacketCanceller() {
        super(Categories.Misc, "packet-canceller", "Allows you to cancel certain packets.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.s2cPackets = this.sgGeneral.add(((PacketListSetting.Builder)((PacketListSetting.Builder)new PacketListSetting.Builder().name("S2C-packets")).description("Server-to-client packets to cancel.")).clientbound().build());
        this.c2sPackets = this.sgGeneral.add(((PacketListSetting.Builder)((PacketListSetting.Builder)new PacketListSetting.Builder().name("C2S-packets")).description("Client-to-server packets to cancel.")).serverbound().build());
        this.runInMainMenu = true;
    }

    @EventHandler(priority=201)
    private void onReceivePacket(PacketEvent.Receive event) {
        if (this.s2cPackets.get().contains(event.packet.type())) {
            event.cancel();
        }
    }

    @EventHandler(priority=201)
    private void onSendPacket(PacketEvent.Send event) {
        if (this.c2sPackets.get().contains(event.packet.type())) {
            event.cancel();
        }
    }
}


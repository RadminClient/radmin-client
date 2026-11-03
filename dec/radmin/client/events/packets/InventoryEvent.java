/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket
 */
package radmin.client.events.packets;

import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;

public class InventoryEvent {
    private static final InventoryEvent INSTANCE = new InventoryEvent();
    public ClientboundContainerSetContentPacket packet;

    public static InventoryEvent get(ClientboundContainerSetContentPacket packet) {
        InventoryEvent.INSTANCE.packet = packet;
        return INSTANCE;
    }
}


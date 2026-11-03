/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket
 */
package radmin.client.events.packets;

import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;

public class ContainerSlotUpdateEvent {
    private static final ContainerSlotUpdateEvent INSTANCE = new ContainerSlotUpdateEvent();
    public ClientboundContainerSetSlotPacket packet;

    public static ContainerSlotUpdateEvent get(ClientboundContainerSetSlotPacket packet) {
        ContainerSlotUpdateEvent.INSTANCE.packet = packet;
        return INSTANCE;
    }
}


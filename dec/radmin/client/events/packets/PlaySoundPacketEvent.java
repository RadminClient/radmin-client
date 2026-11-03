/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.protocol.game.ClientboundSoundPacket
 */
package radmin.client.events.packets;

import net.minecraft.network.protocol.game.ClientboundSoundPacket;

public class PlaySoundPacketEvent {
    private static final PlaySoundPacketEvent INSTANCE = new PlaySoundPacketEvent();
    public ClientboundSoundPacket packet;

    public static PlaySoundPacketEvent get(ClientboundSoundPacket packet) {
        PlaySoundPacketEvent.INSTANCE.packet = packet;
        return INSTANCE;
    }
}


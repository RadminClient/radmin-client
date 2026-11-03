/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Connection
 *  net.minecraft.network.protocol.Packet
 */
package radmin.client.events.packets;

import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import radmin.client.events.Cancellable;

public class PacketEvent {

    public static class Sent {
        public Packet<?> packet;
        public Connection connection;

        public Sent(Packet<?> packet, Connection connection) {
            this.packet = packet;
            this.connection = connection;
        }

        public void sendSilently(Packet<?> packet) {
            this.connection.send(packet, null, true);
        }
    }

    public static class Send
    extends Cancellable {
        public Packet<?> packet;
        public Connection connection;

        public Send(Packet<?> packet, Connection connection) {
            this.setCancelled(false);
            this.packet = packet;
            this.connection = connection;
        }

        public void sendSilently(Packet<?> packet) {
            this.connection.send(packet, null, true);
        }
    }

    public static class Receive
    extends Cancellable {
        public Packet<?> packet;
        public Connection connection;

        public Receive(Packet<?> packet, Connection connection) {
            this.setCancelled(false);
            this.packet = packet;
            this.connection = connection;
        }
    }
}


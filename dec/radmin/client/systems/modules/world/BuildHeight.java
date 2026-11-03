/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  meteordevelopment.orbit.EventHandler
 *  net.minecraft.core.Direction
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ServerboundUseItemOnPacket
 */
package radmin.client.systems.modules.world;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import radmin.client.events.packets.PacketEvent;
import radmin.client.mixin.BlockHitResultAccessor;
import radmin.client.systems.modules.Categories;
import radmin.client.systems.modules.Module;

public class BuildHeight
extends Module {
    public BuildHeight() {
        super(Categories.World, "build-height", "Allows you to interact with objects at the build limit.");
    }

    @EventHandler
    private void onSendPacket(PacketEvent.Send event) {
        Packet<?> packet = event.packet;
        if (!(packet instanceof ServerboundUseItemOnPacket)) {
            return;
        }
        ServerboundUseItemOnPacket p = (ServerboundUseItemOnPacket)packet;
        if (this.mc.level == null) {
            return;
        }
        if (p.getHitResult().getLocation().y >= (double)this.mc.level.getHeight() && p.getHitResult().getDirection() == Direction.UP) {
            ((BlockHitResultAccessor)p.getHitResult()).meteor$setDirection(Direction.DOWN);
        }
    }
}


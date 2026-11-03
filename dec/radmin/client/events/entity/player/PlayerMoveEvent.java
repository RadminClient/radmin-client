/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.MoverType
 *  net.minecraft.world.phys.Vec3
 */
package radmin.client.events.entity.player;

import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;

public class PlayerMoveEvent {
    private static final PlayerMoveEvent INSTANCE = new PlayerMoveEvent();
    public MoverType type;
    public Vec3 movement;

    public static PlayerMoveEvent get(MoverType type, Vec3 movement) {
        PlayerMoveEvent.INSTANCE.type = type;
        PlayerMoveEvent.INSTANCE.movement = movement;
        return INSTANCE;
    }
}


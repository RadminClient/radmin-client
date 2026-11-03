/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.vehicle.boat.AbstractBoat
 */
package radmin.client.events.entity;

import net.minecraft.world.entity.vehicle.boat.AbstractBoat;

public class BoatMoveEvent {
    private static final BoatMoveEvent INSTANCE = new BoatMoveEvent();
    public AbstractBoat boat;

    public static BoatMoveEvent get(AbstractBoat entity) {
        BoatMoveEvent.INSTANCE.boat = entity;
        return INSTANCE;
    }
}


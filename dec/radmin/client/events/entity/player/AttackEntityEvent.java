/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 */
package radmin.client.events.entity.player;

import net.minecraft.world.entity.Entity;
import radmin.client.events.Cancellable;

public class AttackEntityEvent
extends Cancellable {
    private static final AttackEntityEvent INSTANCE = new AttackEntityEvent();
    public Entity entity;

    public static AttackEntityEvent get(Entity entity) {
        INSTANCE.setCancelled(false);
        AttackEntityEvent.INSTANCE.entity = entity;
        return INSTANCE;
    }
}


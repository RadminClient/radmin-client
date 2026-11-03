/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 */
package radmin.client.events.entity;

import net.minecraft.world.entity.Entity;

public class EntityAddedEvent {
    private static final EntityAddedEvent INSTANCE = new EntityAddedEvent();
    public Entity entity;

    public static EntityAddedEvent get(Entity entity) {
        EntityAddedEvent.INSTANCE.entity = entity;
        return INSTANCE;
    }
}


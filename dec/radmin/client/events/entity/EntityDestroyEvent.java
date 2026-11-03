/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 */
package radmin.client.events.entity;

import net.minecraft.world.entity.Entity;

public class EntityDestroyEvent {
    private static final EntityDestroyEvent INSTANCE = new EntityDestroyEvent();
    public Entity entity;

    public static EntityDestroyEvent get(Entity entity) {
        EntityDestroyEvent.INSTANCE.entity = entity;
        return INSTANCE;
    }
}


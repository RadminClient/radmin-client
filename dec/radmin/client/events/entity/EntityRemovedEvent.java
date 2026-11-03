/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 */
package radmin.client.events.entity;

import net.minecraft.world.entity.Entity;

public class EntityRemovedEvent {
    private static final EntityRemovedEvent INSTANCE = new EntityRemovedEvent();
    public Entity entity;

    public static EntityRemovedEvent get(Entity entity) {
        EntityRemovedEvent.INSTANCE.entity = entity;
        return INSTANCE;
    }
}


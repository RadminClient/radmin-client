/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.entity.Entity
 */
package radmin.client.events.entity.player;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import radmin.client.events.Cancellable;

public class InteractEntityEvent
extends Cancellable {
    private static final InteractEntityEvent INSTANCE = new InteractEntityEvent();
    public Entity entity;
    public InteractionHand hand;

    public static InteractEntityEvent get(Entity entity, InteractionHand hand) {
        INSTANCE.setCancelled(false);
        InteractEntityEvent.INSTANCE.entity = entity;
        InteractEntityEvent.INSTANCE.hand = hand;
        return INSTANCE;
    }
}


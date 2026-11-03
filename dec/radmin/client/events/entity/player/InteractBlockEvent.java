/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.phys.BlockHitResult
 */
package radmin.client.events.entity.player;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import radmin.client.events.Cancellable;

public class InteractBlockEvent
extends Cancellable {
    private static final InteractBlockEvent INSTANCE = new InteractBlockEvent();
    public InteractionHand hand;
    public BlockHitResult result;

    public static InteractBlockEvent get(InteractionHand hand, BlockHitResult result) {
        INSTANCE.setCancelled(false);
        InteractBlockEvent.INSTANCE.hand = hand;
        InteractBlockEvent.INSTANCE.result = result;
        return INSTANCE;
    }
}


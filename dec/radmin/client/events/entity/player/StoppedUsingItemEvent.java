/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.item.ItemStack
 */
package radmin.client.events.entity.player;

import net.minecraft.world.item.ItemStack;

public class StoppedUsingItemEvent {
    private static final StoppedUsingItemEvent INSTANCE = new StoppedUsingItemEvent();
    public ItemStack itemStack;

    public static StoppedUsingItemEvent get(ItemStack itemStack) {
        StoppedUsingItemEvent.INSTANCE.itemStack = itemStack;
        return INSTANCE;
    }
}


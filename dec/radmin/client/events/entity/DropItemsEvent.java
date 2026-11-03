/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.item.ItemStack
 */
package radmin.client.events.entity;

import net.minecraft.world.item.ItemStack;
import radmin.client.events.Cancellable;

public class DropItemsEvent
extends Cancellable {
    private static final DropItemsEvent INSTANCE = new DropItemsEvent();
    public ItemStack itemStack;

    public static DropItemsEvent get(ItemStack itemStack) {
        INSTANCE.setCancelled(false);
        DropItemsEvent.INSTANCE.itemStack = itemStack;
        return INSTANCE;
    }
}


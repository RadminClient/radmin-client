/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.item.ItemStack
 */
package radmin.client.events.entity.player;

import net.minecraft.world.item.ItemStack;

public class FinishUsingItemEvent {
    private static final FinishUsingItemEvent INSTANCE = new FinishUsingItemEvent();
    public ItemStack itemStack;

    public static FinishUsingItemEvent get(ItemStack itemStack) {
        FinishUsingItemEvent.INSTANCE.itemStack = itemStack;
        return INSTANCE;
    }
}


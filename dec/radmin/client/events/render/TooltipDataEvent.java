/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.inventory.tooltip.TooltipComponent
 *  net.minecraft.world.item.ItemStack
 */
package radmin.client.events.render;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public class TooltipDataEvent {
    private static final TooltipDataEvent INSTANCE = new TooltipDataEvent();
    public TooltipComponent tooltipData;
    public ItemStack itemStack;

    public static TooltipDataEvent get(ItemStack itemStack) {
        TooltipDataEvent.INSTANCE.tooltipData = null;
        TooltipDataEvent.INSTANCE.itemStack = itemStack;
        return INSTANCE;
    }
}


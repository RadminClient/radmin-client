/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.inventory.tooltip.TooltipComponent
 *  net.minecraft.world.item.BundleItem
 *  net.minecraft.world.item.ItemStack
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package radmin.client.mixin;

import java.util.Optional;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import radmin.client.MeteorClient;
import radmin.client.events.render.TooltipDataEvent;

@Mixin(value={BundleItem.class})
public abstract class BundleItemMixin {
    @Inject(method={"getTooltipImage"}, at={@At(value="HEAD")}, cancellable=true)
    private void onGetTooltipImage(ItemStack bundle, CallbackInfoReturnable<Optional<TooltipComponent>> cir) {
        TooltipDataEvent event = (TooltipDataEvent)MeteorClient.EVENT_BUS.post((Object)TooltipDataEvent.get(bundle));
        if (event.tooltipData != null) {
            cir.setReturnValue(Optional.of(event.tooltipData));
        }
    }
}


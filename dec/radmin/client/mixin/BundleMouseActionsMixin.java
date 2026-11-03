/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.ModifyExpressionValue
 *  net.minecraft.client.gui.BundleMouseActions
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.component.BundleContents
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 */
package radmin.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.BundleMouseActions;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.misc.InventoryTweaks;

@Mixin(value={BundleMouseActions.class})
public abstract class BundleMouseActionsMixin {
    @ModifyExpressionValue(method={"toggleSelectedBundleItem"}, at={@At(value="INVOKE", target="Lnet/minecraft/world/item/BundleItem;getNumberOfItemsToShow(Lnet/minecraft/world/item/ItemStack;)I")})
    private int uncapBundleScrolling1(int original, ItemStack bundleItem, int slotIndex, int selectedItem) {
        if (Modules.get().get(InventoryTweaks.class).uncapBundleScrolling()) {
            return ((BundleContents)bundleItem.getOrDefault(DataComponents.BUNDLE_CONTENTS, (Object)BundleContents.EMPTY)).size();
        }
        return original;
    }

    @ModifyExpressionValue(method={"onMouseScrolled"}, at={@At(value="INVOKE", target="Lnet/minecraft/world/item/BundleItem;getNumberOfItemsToShow(Lnet/minecraft/world/item/ItemStack;)I")})
    private int uncapBundleScrolling2(int original, double scrollX, double scrollY, int slotIndex, ItemStack itemStack) {
        if (Modules.get().get(InventoryTweaks.class).uncapBundleScrolling()) {
            return ((BundleContents)itemStack.getOrDefault(DataComponents.BUNDLE_CONTENTS, (Object)BundleContents.EMPTY)).size();
        }
        return original;
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.inventory.AbstractFurnaceMenu
 *  net.minecraft.world.item.ItemStack
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 */
package radmin.client.mixin;

import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import radmin.client.mixininterface.IAbstractFurnaceMenu;

@Mixin(value={AbstractFurnaceMenu.class})
public abstract class AbstractFurnaceMenuMixin
implements IAbstractFurnaceMenu {
    @Shadow
    protected abstract boolean canSmelt(ItemStack var1);

    @Override
    public boolean meteor$canSmelt(ItemStack itemStack) {
        return this.canSmelt(itemStack);
    }
}


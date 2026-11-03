/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.inventory.Slot
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 */
package radmin.client.mixin;

import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import radmin.client.mixininterface.ISlot;

@Mixin(targets={"net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen$SlotWrapper"})
public abstract class CreativeSlotMixin
implements ISlot {
    @Shadow
    @Final
    private Slot target;

    @Override
    public int meteor$getIndex() {
        return this.target.index;
    }

    @Override
    public int meteor$getSlot() {
        return this.target.getContainerSlot();
    }
}


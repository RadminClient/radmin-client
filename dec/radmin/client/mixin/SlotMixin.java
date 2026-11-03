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

@Mixin(value={Slot.class})
public abstract class SlotMixin
implements ISlot {
    @Shadow
    public int index;
    @Shadow
    @Final
    private int slot;

    @Override
    public int meteor$getIndex() {
        return this.index;
    }

    @Override
    public int meteor$getSlot() {
        return this.slot;
    }
}


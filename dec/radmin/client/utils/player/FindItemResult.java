/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.InteractionHand
 */
package radmin.client.utils.player;

import net.minecraft.world.InteractionHand;
import radmin.client.MeteorClient;

public record FindItemResult(int slot, int count) {
    public boolean found() {
        return this.slot != -1;
    }

    public InteractionHand getHand() {
        if (this.slot == 40) {
            return InteractionHand.OFF_HAND;
        }
        if (this.slot == MeteorClient.mc.player.getInventory().getSelectedSlot()) {
            return InteractionHand.MAIN_HAND;
        }
        return null;
    }

    public boolean isMainHand() {
        return this.getHand() == InteractionHand.MAIN_HAND;
    }

    public boolean isOffhand() {
        return this.getHand() == InteractionHand.OFF_HAND;
    }

    public boolean isHotbar() {
        return this.slot >= 0 && this.slot <= 8;
    }

    public boolean isMain() {
        return this.slot >= 9 && this.slot <= 35;
    }

    public boolean isArmor() {
        return this.slot >= 36 && this.slot <= 39;
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  meteordevelopment.orbit.EventHandler
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Items
 */
package radmin.client.systems.modules.player;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import radmin.client.events.world.TickEvent;
import radmin.client.systems.modules.Categories;
import radmin.client.systems.modules.Module;
import radmin.client.utils.player.FindItemResult;
import radmin.client.utils.player.InvUtils;
import radmin.client.utils.player.Rotations;

public class EXPThrower
extends Module {
    public EXPThrower() {
        super(Categories.Player, "exp-thrower", "Automatically throws XP bottles from your hotbar.");
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        FindItemResult exp = InvUtils.findInHotbar(Items.EXPERIENCE_BOTTLE);
        if (!exp.found()) {
            return;
        }
        Rotations.rotate((double)this.mc.player.getYRot(), 90.0, () -> {
            if (exp.getHand() != null) {
                this.mc.gameMode.useItem((Player)this.mc.player, exp.getHand());
            } else {
                InvUtils.swap(exp.slot(), true);
                this.mc.gameMode.useItem((Player)this.mc.player, exp.getHand());
                InvUtils.swapBack();
            }
        });
    }
}


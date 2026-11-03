/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  meteordevelopment.orbit.EventHandler
 *  net.minecraft.client.gui.screens.inventory.AnvilScreen
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.level.block.AnvilBlock
 *  net.minecraft.world.level.block.Block
 */
package radmin.client.systems.modules.combat;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Block;
import radmin.client.events.game.OpenScreenEvent;
import radmin.client.events.world.TickEvent;
import radmin.client.systems.modules.Categories;
import radmin.client.systems.modules.Module;
import radmin.client.utils.player.InvUtils;
import radmin.client.utils.world.BlockUtils;

public class SelfAnvil
extends Module {
    public SelfAnvil() {
        super(Categories.Combat, "self-anvil", "Automatically places an anvil on you to prevent other players from going into your hole.");
    }

    @EventHandler
    private void onOpenScreen(OpenScreenEvent event) {
        if (event.screen instanceof AnvilScreen) {
            event.cancel();
        }
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (BlockUtils.place(this.mc.player.blockPosition().offset(0, 2, 0), InvUtils.findInHotbar(itemStack -> Block.byItem((Item)itemStack.getItem()) instanceof AnvilBlock), 0)) {
            this.toggle();
        }
    }
}


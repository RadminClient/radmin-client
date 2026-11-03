/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  meteordevelopment.orbit.EventHandler
 *  net.minecraft.client.gui.screens.inventory.ContainerScreen
 *  net.minecraft.core.NonNullList
 *  net.minecraft.world.Container
 *  net.minecraft.world.inventory.ChestMenu
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.block.EnderChestBlock
 */
package radmin.client.utils.player;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.EnderChestBlock;
import radmin.client.MeteorClient;
import radmin.client.events.game.GameLeftEvent;
import radmin.client.events.game.OpenScreenEvent;
import radmin.client.events.world.BlockActivateEvent;
import radmin.client.utils.PreInit;

public class EChestMemory {
    public static final NonNullList<ItemStack> ITEMS = NonNullList.withSize((int)27, (Object)ItemStack.EMPTY);
    private static int echestOpenedState;
    private static boolean isKnown;

    private EChestMemory() {
    }

    @PreInit
    public static void init() {
        MeteorClient.EVENT_BUS.subscribe(EChestMemory.class);
    }

    @EventHandler
    private static void onBlockActivate(BlockActivateEvent event) {
        if (event.blockState.getBlock() instanceof EnderChestBlock && echestOpenedState == 0) {
            echestOpenedState = 1;
        }
    }

    @EventHandler
    private static void onOpenScreenEvent(OpenScreenEvent event) {
        if (echestOpenedState == 1 && event.screen instanceof ContainerScreen) {
            echestOpenedState = 2;
            return;
        }
        if (echestOpenedState == 0) {
            return;
        }
        if (!(MeteorClient.mc.screen instanceof ContainerScreen)) {
            return;
        }
        ChestMenu container = (ChestMenu)((ContainerScreen)MeteorClient.mc.screen).getMenu();
        if (container == null) {
            return;
        }
        Container inv = container.getContainer();
        for (int i = 0; i < 27; ++i) {
            ITEMS.set(i, (Object)inv.getItem(i));
        }
        isKnown = true;
        echestOpenedState = 0;
    }

    @EventHandler
    private static void onLeaveEvent(GameLeftEvent event) {
        ITEMS.clear();
        isKnown = false;
    }

    public static boolean isKnown() {
        return isKnown;
    }

    static {
        isKnown = false;
    }
}


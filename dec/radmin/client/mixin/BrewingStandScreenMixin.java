/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
 *  net.minecraft.client.gui.screens.inventory.BrewingStandScreen
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.inventory.BrewingStandMenu
 *  org.spongepowered.asm.mixin.Mixin
 */
package radmin.client.mixin;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.BrewingStandScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.BrewingStandMenu;
import org.spongepowered.asm.mixin.Mixin;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.world.AutoBrewer;

@Mixin(value={BrewingStandScreen.class})
public abstract class BrewingStandScreenMixin
extends AbstractContainerScreen<BrewingStandMenu> {
    public BrewingStandScreenMixin(BrewingStandMenu container, Inventory playerInventory, Component name) {
        super((AbstractContainerMenu)container, playerInventory, name);
    }

    public void containerTick() {
        super.containerTick();
        if (Modules.get().isActive(AutoBrewer.class)) {
            Modules.get().get(AutoBrewer.class).tick((BrewingStandMenu)this.menu);
        }
    }

    public void onClose() {
        if (Modules.get().isActive(AutoBrewer.class)) {
            Modules.get().get(AutoBrewer.class).onBrewingStandClose();
        }
        super.onClose();
    }
}


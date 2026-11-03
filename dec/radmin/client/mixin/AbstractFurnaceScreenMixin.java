/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
 *  net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen
 *  net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.inventory.AbstractFurnaceMenu
 *  org.spongepowered.asm.mixin.Mixin
 */
package radmin.client.mixin;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import org.spongepowered.asm.mixin.Mixin;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.world.AutoSmelter;

@Mixin(value={AbstractFurnaceScreen.class})
public abstract class AbstractFurnaceScreenMixin<T extends AbstractFurnaceMenu>
extends AbstractContainerScreen<T>
implements RecipeUpdateListener {
    public AbstractFurnaceScreenMixin(T container, Inventory playerInventory, Component name) {
        super(container, playerInventory, name);
    }

    public void containerTick() {
        super.containerTick();
        if (Modules.get().isActive(AutoSmelter.class)) {
            Modules.get().get(AutoSmelter.class).tick((AbstractFurnaceMenu)this.menu);
        }
    }
}


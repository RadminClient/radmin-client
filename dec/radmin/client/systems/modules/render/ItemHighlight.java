/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 */
package radmin.client.systems.modules.render;

import java.util.List;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import radmin.client.settings.ColorSetting;
import radmin.client.settings.ItemListSetting;
import radmin.client.settings.Setting;
import radmin.client.settings.SettingGroup;
import radmin.client.systems.modules.Categories;
import radmin.client.systems.modules.Module;
import radmin.client.utils.render.color.SettingColor;

public class ItemHighlight
extends Module {
    private final SettingGroup sgGeneral;
    private final Setting<List<Item>> items;
    private final Setting<SettingColor> color;

    public ItemHighlight() {
        super(Categories.Render, "item-highlight", "Highlights selected items when in guis");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.items = this.sgGeneral.add(((ItemListSetting.Builder)((ItemListSetting.Builder)new ItemListSetting.Builder().name("items")).description("Items to highlight.")).build());
        this.color = this.sgGeneral.add(((ColorSetting.Builder)((ColorSetting.Builder)new ColorSetting.Builder().name("color")).description("The color to highlight the items with.")).defaultValue(new SettingColor(225, 25, 255, 50)).build());
    }

    public int getColor(ItemStack stack) {
        if (stack != null && this.items.get().contains(stack.getItem()) && this.isActive()) {
            return this.color.get().getPacked();
        }
        return -1;
    }
}


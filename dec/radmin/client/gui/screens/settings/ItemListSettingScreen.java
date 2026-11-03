/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Items
 */
package radmin.client.gui.screens.settings;

import java.util.Collection;
import java.util.function.Predicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import radmin.client.gui.GuiTheme;
import radmin.client.gui.screens.settings.base.CollectionListSettingScreen;
import radmin.client.gui.widgets.WWidget;
import radmin.client.settings.ItemListSetting;
import radmin.client.utils.misc.Names;
import radmin.client.utils.render.DisplayItemUtils;

public class ItemListSettingScreen
extends CollectionListSettingScreen<Item> {
    public ItemListSettingScreen(GuiTheme theme, ItemListSetting setting) {
        super(theme, "Select Items", setting, (Collection)setting.get(), BuiltInRegistries.ITEM);
    }

    @Override
    protected boolean includeValue(Item value) {
        Predicate<Item> filter = ((ItemListSetting)this.setting).filter;
        if (filter != null && !filter.test(value)) {
            return false;
        }
        return value != Items.AIR;
    }

    @Override
    protected WWidget getValueWidget(Item value) {
        return this.theme.itemWithLabel(DisplayItemUtils.toStack(value));
    }

    @Override
    protected String[] getValueNames(Item value) {
        return new String[]{Names.get(value), BuiltInRegistries.ITEM.getKey((Object)value).toString()};
    }
}


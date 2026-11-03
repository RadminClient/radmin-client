/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.world.inventory.MenuType
 */
package radmin.client.gui.screens.settings;

import java.util.Collection;
import java.util.List;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import radmin.client.gui.GuiTheme;
import radmin.client.gui.screens.settings.base.CollectionListSettingScreen;
import radmin.client.gui.widgets.WWidget;
import radmin.client.settings.Setting;

public class ScreenHandlerSettingScreen
extends CollectionListSettingScreen<MenuType<?>> {
    public ScreenHandlerSettingScreen(GuiTheme theme, Setting<List<MenuType<?>>> setting) {
        super(theme, "Select Screen Handlers", setting, (Collection)setting.get(), BuiltInRegistries.MENU);
    }

    @Override
    protected WWidget getValueWidget(MenuType<?> value) {
        return this.theme.label(ScreenHandlerSettingScreen.getName(value));
    }

    @Override
    protected String[] getValueNames(MenuType<?> type) {
        return new String[]{ScreenHandlerSettingScreen.getName(type)};
    }

    private static String getName(MenuType<?> type) {
        return BuiltInRegistries.MENU.getKey(type).toString();
    }
}


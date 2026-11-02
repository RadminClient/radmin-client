/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package radmin.client.gui.tabs.builtin;

import radmin.client.gui.GuiTheme;
import radmin.client.gui.GuiThemes;
import radmin.client.gui.tabs.Tab;
import radmin.client.gui.tabs.TabScreen;
import net.minecraft.client.gui.screens.Screen;

public class ModulesTab extends Tab {
    public ModulesTab() {
        super("Modules");
    }

    @Override
    public TabScreen createScreen(GuiTheme theme) {
        return theme.modulesScreen();
    }

    @Override
    public boolean isScreen(Screen screen) {
        return GuiThemes.get().isModulesScreen(screen);
    }
}

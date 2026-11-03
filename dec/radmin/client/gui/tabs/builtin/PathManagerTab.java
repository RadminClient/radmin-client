/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.screens.Screen
 */
package radmin.client.gui.tabs.builtin;

import net.minecraft.client.gui.screens.Screen;
import radmin.client.gui.GuiTheme;
import radmin.client.gui.tabs.Tab;
import radmin.client.gui.tabs.TabScreen;
import radmin.client.gui.tabs.WindowTabScreen;
import radmin.client.gui.widgets.input.WTextBox;
import radmin.client.pathing.PathManagers;

public class PathManagerTab
extends Tab {
    public PathManagerTab() {
        super(PathManagers.get().getName());
    }

    @Override
    public TabScreen createScreen(GuiTheme theme) {
        return new PathManagerScreen(theme, this);
    }

    @Override
    public boolean isScreen(Screen screen) {
        return screen instanceof PathManagerScreen;
    }

    private static class PathManagerScreen
    extends WindowTabScreen {
        public PathManagerScreen(GuiTheme theme, Tab tab) {
            super(theme, tab);
            PathManagers.get().getSettings().get().onActivated();
        }

        @Override
        public void initWidgets() {
            WTextBox filter = this.add(this.theme.textBox("")).minWidth(400.0).expandX().widget();
            filter.setFocused(true);
            filter.action = () -> {
                this.clear();
                this.add(filter);
                this.add(this.theme.settings(PathManagers.get().getSettings().get(), filter.get().trim())).expandX();
            };
            this.add(this.theme.settings(PathManagers.get().getSettings().get(), filter.get().trim())).expandX();
        }

        @Override
        protected void onClosed() {
            PathManagers.get().getSettings().save();
        }
    }
}


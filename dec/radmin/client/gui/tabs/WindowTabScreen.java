/*
 * Decompiled with CFR 0.152.
 */
package radmin.client.gui.tabs;

import radmin.client.gui.GuiTheme;
import radmin.client.gui.tabs.Tab;
import radmin.client.gui.tabs.TabScreen;
import radmin.client.gui.utils.Cell;
import radmin.client.gui.widgets.WWidget;
import radmin.client.gui.widgets.containers.WWindow;

public abstract class WindowTabScreen
extends TabScreen {
    protected final WWindow window;

    public WindowTabScreen(GuiTheme theme, Tab tab) {
        super(theme, tab);
        this.window = super.add(theme.window(tab.name)).center().widget();
    }

    @Override
    public <W extends WWidget> Cell<W> add(W widget) {
        return this.window.add(widget);
    }

    @Override
    public void clear() {
        this.window.clear();
    }
}


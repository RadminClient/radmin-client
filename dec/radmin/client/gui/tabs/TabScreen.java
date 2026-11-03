/*
 * Decompiled with CFR 0.152.
 */
package radmin.client.gui.tabs;

import radmin.client.gui.GuiTheme;
import radmin.client.gui.WidgetScreen;
import radmin.client.gui.tabs.Tab;
import radmin.client.gui.utils.Cell;
import radmin.client.gui.widgets.WWidget;

public abstract class TabScreen
extends WidgetScreen {
    public final Tab tab;

    public TabScreen(GuiTheme theme, Tab tab) {
        super(theme, tab.name);
        this.tab = tab;
    }

    public <T extends WWidget> Cell<T> addDirect(T widget) {
        return super.add(widget);
    }
}


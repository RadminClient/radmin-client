/*
 * Decompiled with CFR 0.152.
 */
package radmin.client.gui.screens.settings;

import java.util.Collection;
import java.util.List;
import radmin.client.gui.GuiTheme;
import radmin.client.gui.screens.settings.base.CollectionListSettingScreen;
import radmin.client.gui.widgets.WWidget;
import radmin.client.settings.Setting;
import radmin.client.systems.modules.Module;
import radmin.client.systems.modules.Modules;

public class ModuleListSettingScreen
extends CollectionListSettingScreen<Module> {
    public ModuleListSettingScreen(GuiTheme theme, Setting<List<Module>> setting) {
        super(theme, "Select Modules", setting, (Collection)setting.get(), Modules.get().getAll());
    }

    @Override
    protected WWidget getValueWidget(Module value) {
        return this.theme.label(value.title);
    }

    @Override
    protected String[] getValueNames(Module value) {
        String[] names = new String[value.aliases.length + 1];
        System.arraycopy(value.aliases, 0, names, 1, value.aliases.length);
        names[0] = value.title;
        return names;
    }
}


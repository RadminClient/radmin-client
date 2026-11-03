/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package radmin.client.systems.hud.screens;

import org.jetbrains.annotations.Nullable;
import radmin.client.gui.GuiTheme;
import radmin.client.gui.WindowScreen;
import radmin.client.gui.widgets.containers.WHorizontalList;
import radmin.client.gui.widgets.input.WTextBox;
import radmin.client.gui.widgets.pressable.WPlus;
import radmin.client.systems.hud.Hud;
import radmin.client.systems.hud.HudElementInfo;
import radmin.client.utils.Utils;

public class HudElementPresetsScreen
extends WindowScreen {
    private final HudElementInfo<?> info;
    private final int x;
    private final int y;
    private final WTextBox searchBar;
    @Nullable
    private HudElementInfo.Preset firstPreset;

    public HudElementPresetsScreen(GuiTheme theme, HudElementInfo<?> info, int x, int y) {
        super(theme, "Select preset for " + info.title);
        this.info = info;
        this.x = x + 9;
        this.y = y;
        this.searchBar = theme.textBox("");
        this.searchBar.action = () -> {
            this.clear();
            this.initWidgets();
        };
        this.enterAction = () -> {
            if (this.firstPreset == null) {
                return;
            }
            Hud.get().add(this.firstPreset, x, y);
            this.onClose();
        };
    }

    @Override
    public void initWidgets() {
        this.firstPreset = null;
        this.add(this.searchBar).expandX();
        this.searchBar.setFocused(true);
        for (HudElementInfo.Preset preset : this.info.presets) {
            if (!Utils.searchTextDefault(preset.title, this.searchBar.get(), false)) continue;
            WHorizontalList l = this.add(this.theme.horizontalList()).expandX().widget();
            l.add(this.theme.label(preset.title));
            WPlus add = l.add(this.theme.plus()).expandCellX().right().widget();
            add.action = () -> {
                Hud.get().add(preset, this.x, this.y);
                this.onClose();
            };
            if (this.firstPreset != null) continue;
            this.firstPreset = preset;
        }
    }
}


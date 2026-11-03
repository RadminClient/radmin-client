/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.screens.Screen
 */
package radmin.client.gui.tabs.builtin;

import net.minecraft.client.gui.screens.Screen;
import radmin.client.MeteorClient;
import radmin.client.gui.GuiTheme;
import radmin.client.gui.renderer.GuiRenderer;
import radmin.client.gui.tabs.Tab;
import radmin.client.gui.tabs.TabScreen;
import radmin.client.gui.tabs.WindowTabScreen;
import radmin.client.gui.widgets.containers.WContainer;
import radmin.client.gui.widgets.containers.WHorizontalList;
import radmin.client.gui.widgets.pressable.WButton;
import radmin.client.gui.widgets.pressable.WCheckbox;
import radmin.client.systems.hud.Hud;
import radmin.client.systems.hud.screens.HudEditorScreen;
import radmin.client.utils.misc.NbtUtils;

public class HudTab
extends Tab {
    public HudTab() {
        super("HUD");
    }

    @Override
    public TabScreen createScreen(GuiTheme theme) {
        return new HudScreen(theme, this);
    }

    @Override
    public boolean isScreen(Screen screen) {
        return screen instanceof HudScreen;
    }

    public static class HudScreen
    extends WindowTabScreen {
        private WContainer settingsContainer;
        private final Hud hud = Hud.get();

        public HudScreen(GuiTheme theme, Tab tab) {
            super(theme, tab);
            this.hud.settings.onActivated();
        }

        @Override
        public void initWidgets() {
            this.settingsContainer = this.add(this.theme.verticalList()).expandX().widget();
            this.settingsContainer.add(this.theme.settings(this.hud.settings)).expandX().widget();
            this.add(this.theme.horizontalSeparator()).expandX();
            WButton openEditor = this.add(this.theme.button("Edit")).expandX().widget();
            openEditor.action = () -> MeteorClient.mc.setScreen((Screen)new HudEditorScreen(this.theme));
            WHorizontalList buttons = this.add(this.theme.horizontalList()).expandX().widget();
            buttons.add(this.theme.confirmedButton((String)"Clear", (String)"Confirm")).expandX().widget().action = this.hud::clear;
            buttons.add(this.theme.confirmedButton((String)"Reset to default elements", (String)"Confirm")).expandX().widget().action = this.hud::resetToDefaultElements;
            this.add(this.theme.horizontalSeparator()).expandX();
            WHorizontalList bottom = this.add(this.theme.horizontalList()).expandX().widget();
            bottom.add(this.theme.label("Active: "));
            WCheckbox active = bottom.add(this.theme.checkbox(this.hud.active)).expandCellX().widget();
            active.action = () -> {
                this.hud.active = active.checked;
            };
            WButton resetSettings = bottom.add(this.theme.button(GuiRenderer.RESET)).widget();
            resetSettings.action = this.hud.settings::reset;
            resetSettings.tooltip = "Reset";
        }

        public void tick() {
            super.tick();
            this.hud.settings.tick(this.settingsContainer, this.theme);
        }

        @Override
        public boolean toClipboard() {
            return NbtUtils.toClipboard(this.hud);
        }

        @Override
        public boolean fromClipboard() {
            return NbtUtils.fromClipboard(this.hud);
        }
    }
}


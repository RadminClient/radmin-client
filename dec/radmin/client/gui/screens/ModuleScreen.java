/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  meteordevelopment.orbit.EventHandler
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 */
package radmin.client.gui.screens;

import java.util.Optional;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import radmin.client.MeteorClient;
import radmin.client.events.meteor.ActiveModulesChangedEvent;
import radmin.client.events.meteor.ModuleBindChangedEvent;
import radmin.client.gui.GuiTheme;
import radmin.client.gui.WidgetScreen;
import radmin.client.gui.WindowScreen;
import radmin.client.gui.renderer.GuiRenderer;
import radmin.client.gui.utils.Cell;
import radmin.client.gui.widgets.WKeybind;
import radmin.client.gui.widgets.WWidget;
import radmin.client.gui.widgets.containers.WContainer;
import radmin.client.gui.widgets.containers.WHorizontalList;
import radmin.client.gui.widgets.containers.WSection;
import radmin.client.gui.widgets.pressable.WButton;
import radmin.client.gui.widgets.pressable.WCheckbox;
import radmin.client.gui.widgets.pressable.WFavorite;
import radmin.client.systems.modules.Module;
import radmin.client.systems.modules.Modules;
import radmin.client.utils.Utils;
import radmin.client.utils.misc.NbtUtils;
import radmin.client.utils.render.prompts.OkPrompt;

public class ModuleScreen
extends WindowScreen {
    private final Module module;
    private WContainer settingsContainer;
    private WKeybind keybind;
    private WCheckbox active;

    public ModuleScreen(GuiTheme theme, Module module) {
        super(theme, theme.favorite(module.favorite), module.title);
        ((WFavorite)this.window.icon).action = () -> {
            module.favorite = ((WFavorite)this.window.icon).checked;
        };
        this.module = module;
    }

    @Override
    public void initWidgets() {
        WWidget widget;
        this.add(this.theme.label(this.module.description, (double)Utils.getWindowWidth() / 2.0));
        if (this.module.addon != null && this.module.addon != MeteorClient.ADDON) {
            WHorizontalList addon = this.add(this.theme.horizontalList()).expandX().widget();
            addon.add(this.theme.label("From: ").color(this.theme.textSecondaryColor())).widget();
            addon.add(this.theme.label(this.module.addon.name).color(this.module.addon.color)).widget();
        }
        if (!this.module.settings.groups.isEmpty()) {
            this.settingsContainer = this.add(this.theme.verticalList()).expandX().widget();
            this.settingsContainer.add(this.theme.settings(this.module.settings)).expandX();
        }
        if ((widget = this.module.getWidget(this.theme)) != null) {
            this.add(this.theme.horizontalSeparator()).expandX();
            Cell<WWidget> cell = this.add(widget);
            if (widget instanceof WContainer) {
                cell.expandX();
            }
        }
        WSection section = this.add(this.theme.section("Bind", true)).expandX().widget();
        WHorizontalList bind = section.add(this.theme.horizontalList()).expandX().widget();
        bind.add(this.theme.label("Bind: "));
        this.keybind = bind.add(this.theme.keybind(this.module.keybind)).expandX().widget();
        this.keybind.actionOnSet = () -> Modules.get().setModuleToBind(this.module);
        WButton reset = bind.add(this.theme.button(GuiRenderer.RESET)).expandCellX().right().widget();
        reset.action = this.keybind::resetBind;
        reset.tooltip = "Reset";
        WHorizontalList tobr = section.add(this.theme.horizontalList()).widget();
        tobr.add(this.theme.label("Toggle on bind release: "));
        WCheckbox tobrC = tobr.add(this.theme.checkbox(this.module.toggleOnBindRelease)).widget();
        tobrC.action = () -> {
            this.module.toggleOnBindRelease = tobrC.checked;
        };
        WHorizontalList cf = section.add(this.theme.horizontalList()).widget();
        cf.add(this.theme.label("Chat Feedback: "));
        WCheckbox cfC = cf.add(this.theme.checkbox(this.module.chatFeedback)).widget();
        cfC.action = () -> {
            this.module.chatFeedback = cfC.checked;
        };
        this.add(this.theme.horizontalSeparator()).expandX();
        WHorizontalList bottom = this.add(this.theme.horizontalList()).expandX().widget();
        bottom.add(this.theme.label("Active: "));
        this.active = bottom.add(this.theme.checkbox(this.module.isActive())).expandCellX().widget();
        this.active.action = () -> {
            if (this.module.isActive() != this.active.checked) {
                this.module.toggle();
            }
        };
        WHorizontalList sharing = bottom.add(this.theme.horizontalList()).right().widget();
        WButton copy = sharing.add(this.theme.button(GuiRenderer.COPY)).widget();
        copy.action = () -> {
            if (this.toClipboard()) {
                ((OkPrompt)((OkPrompt)((OkPrompt)((OkPrompt)((OkPrompt)OkPrompt.create().title("Module copied!")).message("The settings for this module are now in your clipboard.")).message("You can also copy settings using Ctrl+C.")).message("Settings can be imported using Ctrl+V or the paste button.")).id("config-sharing-guide")).show();
            }
        };
        copy.tooltip = "Copy config";
        WButton paste = sharing.add(this.theme.button(GuiRenderer.PASTE)).widget();
        paste.action = this::fromClipboard;
        paste.tooltip = "Paste config";
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return !Modules.get().isBinding();
    }

    public void tick() {
        super.tick();
        this.module.settings.tick(this.settingsContainer, this.theme);
    }

    @EventHandler
    private void onModuleBindChanged(ModuleBindChangedEvent event) {
        this.keybind.reset();
    }

    @EventHandler
    private void onActiveModulesChanged(ActiveModulesChangedEvent event) {
        this.active.checked = this.module.isActive();
    }

    @Override
    public boolean toClipboard() {
        CompoundTag tag = new CompoundTag();
        tag.putString("name", this.module.name);
        CompoundTag settingsTag = this.module.settings.toTag();
        if (!settingsTag.isEmpty()) {
            tag.put("settings", (Tag)settingsTag);
        }
        return NbtUtils.toClipboard(tag);
    }

    @Override
    public boolean fromClipboard() {
        CompoundTag tag = NbtUtils.fromClipboard();
        if (tag == null) {
            return false;
        }
        if (!tag.getStringOr("name", "").equals(this.module.name)) {
            return false;
        }
        Optional settings = tag.getCompound("settings");
        if (settings.isPresent()) {
            this.module.settings.fromTag((CompoundTag)settings.get());
        } else {
            this.module.settings.reset();
        }
        Screen screen = this.parent;
        if (screen instanceof WidgetScreen) {
            WidgetScreen p = (WidgetScreen)screen;
            p.reload();
        }
        this.reload();
        return true;
    }
}


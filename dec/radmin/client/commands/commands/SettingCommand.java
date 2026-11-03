/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.builder.ArgumentBuilder
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.builder.RequiredArgumentBuilder
 *  net.minecraft.client.multiplayer.ClientSuggestionProvider
 */
package radmin.client.commands.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import radmin.client.commands.Command;
import radmin.client.commands.arguments.ModuleArgumentType;
import radmin.client.commands.arguments.SettingArgumentType;
import radmin.client.commands.arguments.SettingValueArgumentType;
import radmin.client.gui.GuiThemes;
import radmin.client.gui.WidgetScreen;
import radmin.client.gui.tabs.TabScreen;
import radmin.client.gui.tabs.Tabs;
import radmin.client.gui.tabs.builtin.ConfigTab;
import radmin.client.gui.tabs.builtin.HudTab;
import radmin.client.settings.Setting;
import radmin.client.systems.config.Config;
import radmin.client.systems.modules.Module;
import radmin.client.utils.Utils;
import radmin.client.utils.player.ChatUtils;

public class SettingCommand
extends Command {
    public SettingCommand() {
        super("settings", "Allows you to view and change module settings.", "s");
    }

    @Override
    public void build(LiteralArgumentBuilder<ClientSuggestionProvider> builder) {
        builder.then(SettingCommand.literal("hud").executes(commandContext -> {
            TabScreen screen = Tabs.get(HudTab.class).createScreen(GuiThemes.get());
            screen.parent = null;
            Utils.screenToOpen = screen;
            return 1;
        }));
        builder.then(SettingCommand.literal("config").executes(commandContext -> {
            TabScreen screen = Tabs.get(ConfigTab.class).createScreen(GuiThemes.get());
            screen.parent = null;
            Utils.screenToOpen = screen;
            return 1;
        }));
        builder.then(SettingCommand.literal("config").then(((RequiredArgumentBuilder)SettingCommand.argument("setting", SettingArgumentType.create()).executes(context -> {
            Setting<?> setting = SettingArgumentType.get(context, Config.get().settings);
            ChatUtils.infoPrefix("Config", "Setting (highlight)%s(default) is (highlight)%s(default).", setting.title, setting.get());
            return 1;
        })).suggests((commandContext, suggestionsBuilder) -> SettingArgumentType.listSuggestions(suggestionsBuilder, Config.get().settings)).then((ArgumentBuilder)((RequiredArgumentBuilder)SettingCommand.argument("value", SettingValueArgumentType.create()).executes(context -> {
            String value;
            Setting<?> setting = SettingArgumentType.get(context, Config.get().settings);
            if (setting.parse(value = SettingValueArgumentType.get(context))) {
                ChatUtils.infoPrefix("Config", "Setting (highlight)%s(default) changed to (highlight)%s(default).", setting.title, value);
            }
            return 1;
        })).suggests((context, suggestionsBuilder) -> SettingValueArgumentType.listSuggestions(context, suggestionsBuilder, Config.get().settings)))));
        builder.then(SettingCommand.argument("module", ModuleArgumentType.create()).executes(context -> {
            Module module = (Module)context.getArgument("module", Module.class);
            WidgetScreen screen = GuiThemes.get().moduleScreen(module);
            screen.parent = null;
            Utils.screenToOpen = screen;
            return 1;
        }));
        builder.then(SettingCommand.argument("module", ModuleArgumentType.create()).then(((RequiredArgumentBuilder)SettingCommand.argument("setting", SettingArgumentType.create()).executes(context -> {
            Setting<?> setting = SettingArgumentType.get(context);
            ModuleArgumentType.get(context).info("Setting (highlight)%s(default) is (highlight)%s(default).", setting.title, setting.get());
            return 1;
        })).then(SettingCommand.argument("value", SettingValueArgumentType.create()).executes(context -> {
            String value;
            Setting<?> setting = SettingArgumentType.get(context);
            if (setting.parse(value = SettingValueArgumentType.get(context))) {
                ModuleArgumentType.get(context).info("Setting (highlight)%s(default) changed to (highlight)%s(default).", setting.title, value);
            }
            return 1;
        }))));
    }
}


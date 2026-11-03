/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  net.minecraft.client.multiplayer.ClientSuggestionProvider
 */
package radmin.client.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import radmin.client.commands.Command;
import radmin.client.commands.arguments.ModuleArgumentType;
import radmin.client.gui.GuiThemes;
import radmin.client.settings.Setting;
import radmin.client.systems.hud.Hud;
import radmin.client.systems.modules.Module;
import radmin.client.systems.modules.Modules;
import radmin.client.utils.player.ChatUtils;

public class ResetCommand
extends Command {
    public ResetCommand() {
        super("reset", "Resets specified settings.", new String[0]);
    }

    @Override
    public void build(LiteralArgumentBuilder<ClientSuggestionProvider> builder) {
        ((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)builder.then(((LiteralArgumentBuilder)ResetCommand.literal("settings").then(ResetCommand.argument("module", ModuleArgumentType.create()).executes(context -> {
            Module module = (Module)context.getArgument("module", Module.class);
            module.settings.forEach(group -> group.forEach(Setting::reset));
            module.info("Reset all settings.", new Object[0]);
            return 1;
        }))).then(ResetCommand.literal("all").executes(commandContext -> {
            Modules.get().getAll().forEach(module -> module.settings.forEach(group -> group.forEach(Setting::reset)));
            ChatUtils.infoPrefix("Modules", "Reset all module settings", new Object[0]);
            return 1;
        })))).then(ResetCommand.literal("gui").executes(commandContext -> {
            GuiThemes.get().clearWindowConfigs();
            GuiThemes.get().settings.reset();
            ChatUtils.info("Reset all GUI settings.", new Object[0]);
            return 1;
        }))).then(((LiteralArgumentBuilder)ResetCommand.literal("bind").then(ResetCommand.argument("module", ModuleArgumentType.create()).executes(context -> {
            Module module = (Module)context.getArgument("module", Module.class);
            module.keybind.reset();
            module.info("Reset bind.", new Object[0]);
            return 1;
        }))).then(ResetCommand.literal("all").executes(commandContext -> {
            Modules.get().getAll().forEach(module -> module.keybind.reset());
            ChatUtils.infoPrefix("Modules", "Reset all binds.", new Object[0]);
            return 1;
        })))).then(ResetCommand.literal("hud").executes(commandContext -> {
            Hud.get().resetToDefaultElements();
            ChatUtils.infoPrefix("HUD", "Reset all elements.", new Object[0]);
            return 1;
        }));
    }
}


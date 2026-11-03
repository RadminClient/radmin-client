/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.builder.RequiredArgumentBuilder
 *  net.minecraft.client.multiplayer.ClientSuggestionProvider
 */
package radmin.client.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import java.util.ArrayList;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import radmin.client.commands.Command;
import radmin.client.commands.arguments.ModuleArgumentType;
import radmin.client.systems.hud.Hud;
import radmin.client.systems.modules.Module;
import radmin.client.systems.modules.Modules;

public class ToggleCommand
extends Command {
    public ToggleCommand() {
        super("toggle", "Toggles a module.", "t");
    }

    @Override
    public void build(LiteralArgumentBuilder<ClientSuggestionProvider> builder) {
        ((LiteralArgumentBuilder)((LiteralArgumentBuilder)builder.then(((LiteralArgumentBuilder)ToggleCommand.literal("all").then(ToggleCommand.literal("on").executes(commandContext -> {
            new ArrayList<Module>(Modules.get().getAll()).forEach(Module::enable);
            Hud.get().active = true;
            return 1;
        }))).then(ToggleCommand.literal("off").executes(commandContext -> {
            new ArrayList<Module>(Modules.get().getActive()).forEach(Module::toggle);
            Hud.get().active = false;
            return 1;
        })))).then(((RequiredArgumentBuilder)((RequiredArgumentBuilder)ToggleCommand.argument("module", ModuleArgumentType.create()).executes(context -> {
            Module m = ModuleArgumentType.get(context);
            m.toggle();
            m.sendToggledMsg();
            return 1;
        })).then(ToggleCommand.literal("on").executes(context -> {
            Module m = ModuleArgumentType.get(context);
            m.enable();
            return 1;
        }))).then(ToggleCommand.literal("off").executes(context -> {
            Module m = ModuleArgumentType.get(context);
            m.disable();
            return 1;
        })))).then(((LiteralArgumentBuilder)((LiteralArgumentBuilder)ToggleCommand.literal("hud").executes(commandContext -> {
            Hud.get().active = !Hud.get().active;
            return 1;
        })).then(ToggleCommand.literal("on").executes(commandContext -> {
            Hud.get().active = true;
            return 1;
        }))).then(ToggleCommand.literal("off").executes(commandContext -> {
            Hud.get().active = false;
            return 1;
        })));
    }
}


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
import radmin.client.systems.modules.Module;
import radmin.client.systems.modules.Modules;

public class BindCommand
extends Command {
    public BindCommand() {
        super("bind", "Binds a specified module to the next pressed key.", new String[0]);
    }

    @Override
    public void build(LiteralArgumentBuilder<ClientSuggestionProvider> builder) {
        builder.then(BindCommand.argument("module", ModuleArgumentType.create()).executes(context -> {
            Module module = (Module)context.getArgument("module", Module.class);
            Modules.get().setModuleToBind(module);
            Modules.get().awaitKeyRelease();
            module.info("Press a key to bind the module to.", new Object[0]);
            return 1;
        }));
    }
}


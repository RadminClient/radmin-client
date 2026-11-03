/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.arguments.IntegerArgumentType
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  net.minecraft.client.multiplayer.ClientSuggestionProvider
 */
package radmin.client.commands.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import radmin.client.commands.Command;
import radmin.client.mixininterface.IOptionInstance;

public class FovCommand
extends Command {
    public FovCommand() {
        super("fov", "Changes your fov.", new String[0]);
    }

    @Override
    public void build(LiteralArgumentBuilder<ClientSuggestionProvider> builder) {
        builder.then(FovCommand.argument("fov", IntegerArgumentType.integer((int)1, (int)180)).executes(context -> {
            ((IOptionInstance)FovCommand.mc.options.fov()).meteor$set(context.getArgument("fov", Integer.class));
            return 1;
        }));
    }
}


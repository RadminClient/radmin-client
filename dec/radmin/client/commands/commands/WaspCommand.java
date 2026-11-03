/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.Message
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.exceptions.SimpleCommandExceptionType
 *  net.minecraft.client.multiplayer.ClientSuggestionProvider
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.entity.player.Player
 */
package radmin.client.commands.commands;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import radmin.client.commands.Command;
import radmin.client.commands.arguments.PlayerArgumentType;
import radmin.client.systems.modules.Modules;
import radmin.client.systems.modules.movement.AutoWasp;

public class WaspCommand
extends Command {
    private static final SimpleCommandExceptionType CANT_WASP_SELF = new SimpleCommandExceptionType((Message)Component.literal((String)"You cannot target yourself!"));

    public WaspCommand() {
        super("wasp", "Sets the auto wasp target.", new String[0]);
    }

    @Override
    public void build(LiteralArgumentBuilder<ClientSuggestionProvider> builder) {
        AutoWasp wasp = Modules.get().get(AutoWasp.class);
        builder.then(WaspCommand.literal("reset").executes(commandContext -> {
            wasp.disable();
            return 1;
        }));
        builder.then(WaspCommand.argument("player", PlayerArgumentType.create()).executes(context -> {
            Player player = PlayerArgumentType.get(context);
            if (player == WaspCommand.mc.player) {
                throw CANT_WASP_SELF.create();
            }
            wasp.target = player;
            wasp.enable();
            this.info(player.getName().getString() + " set as target.", new Object[0]);
            return 1;
        }));
    }
}


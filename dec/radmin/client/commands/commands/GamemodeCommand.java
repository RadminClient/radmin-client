/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  net.minecraft.client.multiplayer.ClientSuggestionProvider
 *  net.minecraft.world.level.GameType
 */
package radmin.client.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import net.minecraft.world.level.GameType;
import radmin.client.commands.Command;

public class GamemodeCommand
extends Command {
    public GamemodeCommand() {
        super("gamemode", "Changes your gamemode client-side.", "gm");
    }

    @Override
    public void build(LiteralArgumentBuilder<ClientSuggestionProvider> builder) {
        for (GameType gameMode : GameType.values()) {
            builder.then(GamemodeCommand.literal(gameMode.getName()).executes(commandContext -> {
                GamemodeCommand.mc.gameMode.setLocalMode(gameMode);
                return 1;
            }));
        }
    }
}


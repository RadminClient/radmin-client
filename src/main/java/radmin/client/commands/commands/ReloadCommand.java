/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package radmin.client.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import radmin.client.commands.Command;
import radmin.client.renderer.Fonts;
import radmin.client.systems.Systems;
import radmin.client.systems.friends.Friend;
import radmin.client.systems.friends.Friends;
import radmin.client.utils.network.Capes;
import radmin.client.utils.network.MeteorExecutor;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;

public class ReloadCommand extends Command {
    public ReloadCommand() {
        super("reload", "Reloads many systems.");
    }

    @Override
    public void build(LiteralArgumentBuilder<ClientSuggestionProvider> builder) {
        builder.executes(_ -> {
            warning("Reloading systems, this may take a while.");

            Systems.load();
            Capes.init();
            Fonts.refresh();
            MeteorExecutor.execute(() -> Friends.get().forEach(Friend::updateInfo));

            return SINGLE_SUCCESS;
        });
    }
}

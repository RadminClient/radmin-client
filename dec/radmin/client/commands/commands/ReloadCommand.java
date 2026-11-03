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
import radmin.client.renderer.Fonts;
import radmin.client.systems.Systems;
import radmin.client.systems.friends.Friend;
import radmin.client.systems.friends.Friends;
import radmin.client.utils.network.Capes;
import radmin.client.utils.network.MeteorExecutor;

public class ReloadCommand
extends Command {
    public ReloadCommand() {
        super("reload", "Reloads many systems.", new String[0]);
    }

    @Override
    public void build(LiteralArgumentBuilder<ClientSuggestionProvider> builder) {
        builder.executes(commandContext -> {
            this.warning("Reloading systems, this may take a while.", new Object[0]);
            Systems.load();
            Capes.init();
            Fonts.refresh();
            MeteorExecutor.execute(() -> Friends.get().forEach(Friend::updateInfo));
            return 1;
        });
    }
}


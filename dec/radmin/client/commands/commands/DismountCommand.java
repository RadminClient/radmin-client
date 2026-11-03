/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  net.minecraft.client.multiplayer.ClientSuggestionProvider
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ServerboundPlayerInputPacket
 *  net.minecraft.world.entity.player.Input
 */
package radmin.client.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundPlayerInputPacket;
import net.minecraft.world.entity.player.Input;
import radmin.client.commands.Command;

public class DismountCommand
extends Command {
    public DismountCommand() {
        super("dismount", "Dismounts you from entity you are riding.", new String[0]);
    }

    @Override
    public void build(LiteralArgumentBuilder<ClientSuggestionProvider> builder) {
        builder.executes(commandContext -> {
            Input sneak = new Input(false, false, false, false, false, true, false);
            mc.getConnection().send((Packet)new ServerboundPlayerInputPacket(sneak));
            return 1;
        });
    }
}


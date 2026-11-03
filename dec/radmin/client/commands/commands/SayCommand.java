/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.arguments.StringArgumentType
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  net.minecraft.client.multiplayer.ClientPacketListener
 *  net.minecraft.client.multiplayer.ClientSuggestionProvider
 *  net.minecraft.network.chat.LastSeenMessagesTracker$Update
 *  net.minecraft.network.chat.MessageSignature
 *  net.minecraft.network.chat.SignedMessageBody
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ServerboundChatPacket
 *  net.minecraft.util.Crypt$SaltSupplier
 *  org.meteordev.starscript.Script
 */
package radmin.client.commands.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.time.Instant;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import net.minecraft.network.chat.LastSeenMessagesTracker;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.network.chat.SignedMessageBody;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundChatPacket;
import net.minecraft.util.Crypt;
import org.meteordev.starscript.Script;
import radmin.client.commands.Command;
import radmin.client.mixin.ClientPacketListenerAccessor;
import radmin.client.utils.misc.MeteorStarscript;

public class SayCommand
extends Command {
    public SayCommand() {
        super("say", "Sends messages in chat.", new String[0]);
    }

    @Override
    public void build(LiteralArgumentBuilder<ClientSuggestionProvider> builder) {
        builder.then(SayCommand.argument("message", StringArgumentType.greedyString()).executes(context -> {
            String message;
            String msg = (String)context.getArgument("message", String.class);
            Script script = MeteorStarscript.compile(msg);
            if (script != null && (message = MeteorStarscript.run(script)) != null) {
                Instant instant = Instant.now();
                long l = Crypt.SaltSupplier.getLong();
                ClientPacketListener handler = mc.getConnection();
                LastSeenMessagesTracker.Update lastSeenMessages = ((ClientPacketListenerAccessor)handler).meteor$getLastSeenMessages().generateAndApplyUpdate();
                MessageSignature messageSignatureData = ((ClientPacketListenerAccessor)handler).meteor$getSignedMessageEncoder().pack(new SignedMessageBody(message, instant, l, lastSeenMessages.lastSeen()));
                handler.send((Packet)new ServerboundChatPacket(message, instant, l, messageSignatureData, lastSeenMessages.update()));
            }
            return 1;
        }));
    }
}


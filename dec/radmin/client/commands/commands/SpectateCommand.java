/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  meteordevelopment.orbit.EventHandler
 *  net.minecraft.client.multiplayer.ClientSuggestionProvider
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.entity.Entity
 */
package radmin.client.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import radmin.client.MeteorClient;
import radmin.client.commands.Command;
import radmin.client.commands.arguments.PlayerArgumentType;
import radmin.client.events.meteor.KeyInputEvent;
import radmin.client.events.meteor.MouseClickEvent;
import radmin.client.utils.misc.input.Input;

public class SpectateCommand
extends Command {
    private final StaticListener shiftListener = new StaticListener();

    public SpectateCommand() {
        super("spectate", "Allows you to spectate nearby players", new String[0]);
    }

    @Override
    public void build(LiteralArgumentBuilder<ClientSuggestionProvider> builder) {
        builder.then(SpectateCommand.literal("reset").executes(commandContext -> {
            mc.setCameraEntity((Entity)SpectateCommand.mc.player);
            return 1;
        }));
        builder.then(SpectateCommand.argument("player", PlayerArgumentType.create()).executes(context -> {
            mc.setCameraEntity((Entity)PlayerArgumentType.get(context));
            SpectateCommand.mc.player.sendSystemMessage((Component)Component.literal((String)"Sneak to un-spectate."));
            MeteorClient.EVENT_BUS.subscribe((Object)this.shiftListener);
            return 1;
        }));
    }

    private static class StaticListener {
        private StaticListener() {
        }

        @EventHandler
        private void onKey(KeyInputEvent event) {
            if (Input.isPressed(mc.options.keyShift)) {
                mc.setCameraEntity((Entity)mc.player);
                event.cancel();
                MeteorClient.EVENT_BUS.unsubscribe((Object)this);
            }
        }

        @EventHandler
        private void onMouse(MouseClickEvent event) {
            if (Input.isPressed(mc.options.keyShift)) {
                mc.setCameraEntity((Entity)mc.player);
                event.cancel();
                MeteorClient.EVENT_BUS.unsubscribe((Object)this);
            }
        }
    }
}


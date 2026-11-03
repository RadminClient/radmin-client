/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.multiplayer.ClientSuggestionProvider
 *  net.minecraft.client.multiplayer.PlayerInfo
 *  net.minecraft.network.chat.ClickEvent
 *  net.minecraft.network.chat.ClickEvent$OpenUrl
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.HoverEvent
 *  net.minecraft.network.chat.HoverEvent$ShowText
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.network.chat.TextColor
 */
package radmin.client.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.lang.reflect.Type;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import radmin.client.commands.Command;
import radmin.client.commands.arguments.PlayerListEntryArgumentType;
import radmin.client.utils.Utils;
import radmin.client.utils.network.Http;
import radmin.client.utils.network.MeteorExecutor;
import radmin.client.utils.player.ChatUtils;
import radmin.client.utils.player.PlayerUtils;
import radmin.client.utils.render.color.Color;

public class NameHistoryCommand
extends Command {
    public NameHistoryCommand() {
        super("name-history", "Provides a list of a players previous names from the laby.net api.", "history", "names");
    }

    @Override
    public void build(LiteralArgumentBuilder<ClientSuggestionProvider> builder) {
        builder.then(NameHistoryCommand.argument("player", PlayerListEntryArgumentType.create()).executes(context -> {
            MeteorExecutor.execute(() -> {
                PlayerInfo lookUpTarget = PlayerListEntryArgumentType.get(context);
                UUID uuid = lookUpTarget.getProfile().id();
                NameHistory history = (NameHistory)Http.get("https://laby.net/api/v2/user/" + String.valueOf(uuid) + "/get-profile").exceptionHandler(exception -> this.error("There was an error fetching that users name history.", new Object[0])).sendJson((Type)((Object)NameHistory.class));
                if (history == null) {
                    return;
                }
                if (history.username_history == null || history.username_history.length == 0) {
                    this.error("There was an error fetching that users name history.", new Object[0]);
                }
                String name = lookUpTarget.getProfile().name();
                MutableComponent initial = Component.literal((String)name);
                initial.append((Component)Component.literal((String)(name.endsWith("s") ? "'" : "'s")));
                Color nameColor = PlayerUtils.getPlayerColor(NameHistoryCommand.mc.level.getPlayerByUUID(uuid), Utils.WHITE);
                initial.setStyle(initial.getStyle().withColor(TextColor.fromRgb((int)nameColor.getPacked())).withClickEvent((ClickEvent)new ClickEvent.OpenUrl(URI.create("https://laby.net/@" + name))).withHoverEvent((HoverEvent)new HoverEvent.ShowText((Component)Component.literal((String)"View on laby.net").withStyle(ChatFormatting.YELLOW).withStyle(ChatFormatting.ITALIC))));
                this.info((Component)initial.append((Component)Component.literal((String)" Username History:").withStyle(ChatFormatting.GRAY)));
                for (Name entry : history.username_history) {
                    MutableComponent nameText = Component.literal((String)entry.name);
                    nameText.withStyle(ChatFormatting.AQUA);
                    if (entry.changed_at != null && entry.changed_at.getTime() != 0L) {
                        MutableComponent changed = Component.literal((String)"Changed at: ");
                        changed.withStyle(ChatFormatting.GRAY);
                        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss, dd/MM/yyyy");
                        changed.append((Component)Component.literal((String)formatter.format(entry.changed_at)).withStyle(ChatFormatting.WHITE));
                        nameText.setStyle(nameText.getStyle().withHoverEvent((HoverEvent)new HoverEvent.ShowText((Component)changed)));
                    }
                    if (!entry.accurate) {
                        MutableComponent text = Component.literal((String)"*").withStyle(ChatFormatting.WHITE);
                        text.setStyle(text.getStyle().withHoverEvent((HoverEvent)new HoverEvent.ShowText((Component)Component.literal((String)"This name history entry is not accurate according to laby.net"))));
                        nameText.append((Component)text);
                    }
                    ChatUtils.sendMsg((Component)nameText);
                }
            });
            return 1;
        }));
    }

    private static class NameHistory {
        public Name[] username_history;

        private NameHistory() {
        }
    }

    private static class Name {
        public String name;
        public Date changed_at;
        public boolean accurate;

        private Name() {
        }
    }
}


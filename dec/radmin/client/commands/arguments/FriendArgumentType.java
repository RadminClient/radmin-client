/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Streams
 *  com.mojang.brigadier.StringReader
 *  com.mojang.brigadier.arguments.ArgumentType
 *  com.mojang.brigadier.context.CommandContext
 *  com.mojang.brigadier.exceptions.CommandSyntaxException
 *  com.mojang.brigadier.suggestion.Suggestions
 *  com.mojang.brigadier.suggestion.SuggestionsBuilder
 *  net.minecraft.commands.SharedSuggestionProvider
 */
package radmin.client.commands.arguments;

import com.google.common.collect.Streams;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.minecraft.commands.SharedSuggestionProvider;
import radmin.client.systems.friends.Friend;
import radmin.client.systems.friends.Friends;

public class FriendArgumentType
implements ArgumentType<String> {
    private static final FriendArgumentType INSTANCE = new FriendArgumentType();
    private static final Collection<String> EXAMPLES = List.of("seasnail8169", "MineGame159");

    public static FriendArgumentType create() {
        return INSTANCE;
    }

    public static Friend get(CommandContext<?> context) {
        return Friends.get().get((String)context.getArgument("friend", String.class));
    }

    private FriendArgumentType() {
    }

    public String parse(StringReader reader) throws CommandSyntaxException {
        return reader.readString();
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(Streams.stream((Iterable)Friends.get()).map(Friend::getName), (SuggestionsBuilder)builder);
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.StringReader
 *  com.mojang.brigadier.arguments.ArgumentType
 *  com.mojang.brigadier.context.CommandContext
 *  com.mojang.brigadier.exceptions.CommandSyntaxException
 *  com.mojang.brigadier.suggestion.Suggestions
 *  com.mojang.brigadier.suggestion.SuggestionsBuilder
 *  net.minecraft.commands.SharedSuggestionProvider
 */
package radmin.client.commands.arguments;

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
import radmin.client.utils.entity.fakeplayer.FakePlayerEntity;
import radmin.client.utils.entity.fakeplayer.FakePlayerManager;

public class FakePlayerArgumentType
implements ArgumentType<String> {
    private static final FakePlayerArgumentType INSTANCE = new FakePlayerArgumentType();
    private static final Collection<String> EXAMPLES = List.of("seasnail8169", "MineGame159");

    public static FakePlayerArgumentType create() {
        return INSTANCE;
    }

    public static FakePlayerEntity get(CommandContext<?> context) {
        return FakePlayerManager.get((String)context.getArgument("fp", String.class));
    }

    private FakePlayerArgumentType() {
    }

    public String parse(StringReader reader) throws CommandSyntaxException {
        return reader.readString();
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(FakePlayerManager.stream().map(fakePlayerEntity -> fakePlayerEntity.getName().getString()), (SuggestionsBuilder)builder);
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}


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
 *  net.minecraft.commands.CommandBuildContext
 *  net.minecraft.commands.SharedSuggestionProvider
 *  net.minecraft.core.component.DataComponentMap
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
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.core.component.DataComponentMap;
import radmin.client.utils.misc.ComponentMapReader;

public class ComponentMapArgumentType
implements ArgumentType<DataComponentMap> {
    private static final Collection<String> EXAMPLES = List.of("{foo=bar}");
    private final ComponentMapReader reader;

    public ComponentMapArgumentType(CommandBuildContext commandRegistryAccess) {
        this.reader = new ComponentMapReader(commandRegistryAccess);
    }

    public static ComponentMapArgumentType componentMap(CommandBuildContext commandRegistryAccess) {
        return new ComponentMapArgumentType(commandRegistryAccess);
    }

    public static <S extends SharedSuggestionProvider> DataComponentMap getComponentMap(CommandContext<S> context, String name) {
        return (DataComponentMap)context.getArgument(name, DataComponentMap.class);
    }

    public DataComponentMap parse(StringReader reader) throws CommandSyntaxException {
        return this.reader.consume(reader);
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return this.reader.getSuggestions(builder);
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}


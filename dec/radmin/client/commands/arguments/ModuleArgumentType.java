/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.StringReader
 *  com.mojang.brigadier.arguments.ArgumentType
 *  com.mojang.brigadier.context.CommandContext
 *  com.mojang.brigadier.exceptions.CommandSyntaxException
 *  com.mojang.brigadier.exceptions.DynamicCommandExceptionType
 *  com.mojang.brigadier.suggestion.Suggestions
 *  com.mojang.brigadier.suggestion.SuggestionsBuilder
 *  net.minecraft.commands.SharedSuggestionProvider
 *  net.minecraft.network.chat.Component
 */
package radmin.client.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import radmin.client.systems.modules.Module;
import radmin.client.systems.modules.Modules;

public class ModuleArgumentType
implements ArgumentType<Module> {
    private static final ModuleArgumentType INSTANCE = new ModuleArgumentType();
    private static final DynamicCommandExceptionType NO_SUCH_MODULE = new DynamicCommandExceptionType(name -> Component.literal((String)("Module with name " + String.valueOf(name) + " doesn't exist.")));
    private static final Collection<String> EXAMPLES = Modules.get().getAll().stream().limit(3L).map(module -> module.name).toList();

    public static ModuleArgumentType create() {
        return INSTANCE;
    }

    public static Module get(CommandContext<?> context) {
        return (Module)context.getArgument("module", Module.class);
    }

    private ModuleArgumentType() {
    }

    public Module parse(StringReader reader) throws CommandSyntaxException {
        String argument = reader.readString();
        Module module = Modules.get().get(argument);
        if (module == null) {
            throw NO_SUCH_MODULE.create((Object)argument);
        }
        return module;
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(Modules.get().getAll().stream().map(module -> module.name), (SuggestionsBuilder)builder);
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}


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
 *  net.minecraft.resources.Identifier
 *  org.jetbrains.annotations.NotNull
 */
package radmin.client.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.concurrent.CompletableFuture;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import radmin.client.commands.arguments.SettingArgumentType;
import radmin.client.settings.Setting;
import radmin.client.settings.Settings;

public class SettingValueArgumentType
implements ArgumentType<String> {
    private static final SettingValueArgumentType INSTANCE = new SettingValueArgumentType();

    public static SettingValueArgumentType create() {
        return INSTANCE;
    }

    public static String get(CommandContext<?> context) {
        return (String)context.getArgument("value", String.class);
    }

    private SettingValueArgumentType() {
    }

    public String parse(StringReader reader) throws CommandSyntaxException {
        String text = reader.getRemaining();
        reader.setCursor(reader.getTotalLength());
        return text;
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        Setting<?> setting;
        try {
            setting = SettingArgumentType.get(context);
        }
        catch (CommandSyntaxException commandSyntaxException) {
            return Suggestions.empty();
        }
        return SettingValueArgumentType.suggest(builder, setting);
    }

    public static <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder, Settings settings) {
        Setting<?> setting;
        try {
            setting = SettingArgumentType.get(context, settings);
        }
        catch (CommandSyntaxException commandSyntaxException) {
            return Suggestions.empty();
        }
        return SettingValueArgumentType.suggest(builder, setting);
    }

    public static CompletableFuture<Suggestions> suggest(SuggestionsBuilder builder, @NotNull Setting<?> setting) {
        Iterable<Identifier> identifiers = setting.getIdentifierSuggestions();
        if (identifiers != null) {
            return SharedSuggestionProvider.suggestResource(identifiers, (SuggestionsBuilder)builder);
        }
        return SharedSuggestionProvider.suggest(setting.getSuggestions(), (SuggestionsBuilder)builder);
    }
}


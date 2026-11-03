/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.arguments.IntegerArgumentType
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.builder.RequiredArgumentBuilder
 *  com.mojang.brigadier.context.CommandContext
 *  meteordevelopment.orbit.EventHandler
 *  net.minecraft.client.multiplayer.ClientSuggestionProvider
 *  net.minecraft.commands.arguments.TimeArgument
 */
package radmin.client.commands.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import java.util.ArrayList;
import java.util.List;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import net.minecraft.commands.arguments.TimeArgument;
import radmin.client.MeteorClient;
import radmin.client.commands.Command;
import radmin.client.commands.arguments.MacroArgumentType;
import radmin.client.commands.commands.ScheduledMacro;
import radmin.client.events.world.TickEvent;
import radmin.client.systems.macros.Macro;

public class MacroCommand
extends Command {
    List<ScheduledMacro> scheduleQueue = new ArrayList<ScheduledMacro>();
    List<ScheduledMacro> scheduledMacros = new ArrayList<ScheduledMacro>();

    public MacroCommand() {
        super("macro", "Allows you to execute macros.", new String[0]);
        MeteorClient.EVENT_BUS.subscribe((Object)this);
    }

    @Override
    public void build(LiteralArgumentBuilder<ClientSuggestionProvider> builder) {
        ((LiteralArgumentBuilder)builder.then(((LiteralArgumentBuilder)MacroCommand.literal("clear").executes(commandContext -> {
            if (this.scheduleQueue.isEmpty() && this.scheduledMacros.isEmpty()) {
                this.error("No macros are currently scheduled.", new Object[0]);
                return 1;
            }
            this.clearAll();
            this.info("Cleared all scheduled macros.", new Object[0]);
            return 1;
        })).then(MacroCommand.argument("macro", MacroArgumentType.create()).executes(context -> {
            Macro macro = MacroArgumentType.get(context);
            if (!this.isScheduled(macro)) {
                this.error("This macro is not currently scheduled.", new Object[0]);
                return 1;
            }
            this.clear(macro);
            this.info("Cleared scheduled macro.", new Object[0]);
            return 1;
        })))).then(((RequiredArgumentBuilder)MacroCommand.argument("macro", MacroArgumentType.create()).executes(context -> {
            Macro macro = MacroArgumentType.get(context);
            this.scheduleQueue.add(new ScheduledMacro(0, macro));
            return 1;
        })).then(MacroCommand.argument("delay", TimeArgument.time()).executes(context -> {
            Macro macro = MacroArgumentType.get(context);
            this.scheduleQueue.add(new ScheduledMacro(IntegerArgumentType.getInteger((CommandContext)context, (String)"delay"), macro));
            return 1;
        })));
    }

    public void clearAll() {
        this.scheduleQueue.clear();
        this.scheduledMacros.clear();
    }

    public boolean isScheduled(Macro macro) {
        return this.scheduleQueue.stream().anyMatch(element -> element.macro == macro) || this.scheduledMacros.stream().anyMatch(element -> element.macro == macro);
    }

    public void clear(Macro macro) {
        this.scheduleQueue.removeIf(scheduledMacro -> scheduledMacro.macro == macro);
        this.scheduledMacros.removeIf(scheduledMacro -> scheduledMacro.macro == macro);
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (!this.scheduleQueue.isEmpty()) {
            this.scheduledMacros.addAll(this.scheduleQueue);
            this.scheduleQueue.clear();
        }
        if (!this.scheduledMacros.isEmpty()) {
            this.runMacros();
        }
        this.scheduledMacros.forEach(ScheduledMacro::tick);
    }

    private void runMacros() {
        this.scheduledMacros.removeIf(ScheduledMacro::run);
    }
}


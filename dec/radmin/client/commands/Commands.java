/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.CommandDispatcher
 *  com.mojang.brigadier.exceptions.CommandSyntaxException
 *  meteordevelopment.orbit.EventHandler
 *  net.minecraft.client.multiplayer.ClientPacketListener
 *  net.minecraft.client.multiplayer.ClientSuggestionProvider
 *  net.minecraft.commands.CommandBuildContext
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.world.flag.FeatureFlagSet
 */
package radmin.client.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.flag.FeatureFlagSet;
import radmin.client.MeteorClient;
import radmin.client.commands.Command;
import radmin.client.commands.commands.BindCommand;
import radmin.client.commands.commands.BindsCommand;
import radmin.client.commands.commands.CommandsCommand;
import radmin.client.commands.commands.DamageCommand;
import radmin.client.commands.commands.DisconnectCommand;
import radmin.client.commands.commands.DismountCommand;
import radmin.client.commands.commands.DropCommand;
import radmin.client.commands.commands.EnchantCommand;
import radmin.client.commands.commands.EnderChestCommand;
import radmin.client.commands.commands.FakePlayerCommand;
import radmin.client.commands.commands.FovCommand;
import radmin.client.commands.commands.FriendsCommand;
import radmin.client.commands.commands.GamemodeCommand;
import radmin.client.commands.commands.GiveCommand;
import radmin.client.commands.commands.HClipCommand;
import radmin.client.commands.commands.HelpCommand;
import radmin.client.commands.commands.InputCommand;
import radmin.client.commands.commands.InventoryCommand;
import radmin.client.commands.commands.LocateCommand;
import radmin.client.commands.commands.MacroCommand;
import radmin.client.commands.commands.ModulesCommand;
import radmin.client.commands.commands.NameHistoryCommand;
import radmin.client.commands.commands.NbtCommand;
import radmin.client.commands.commands.NotebotCommand;
import radmin.client.commands.commands.PeekCommand;
import radmin.client.commands.commands.ProfilesCommand;
import radmin.client.commands.commands.ReloadCommand;
import radmin.client.commands.commands.ResetCommand;
import radmin.client.commands.commands.RotationCommand;
import radmin.client.commands.commands.SaveMapCommand;
import radmin.client.commands.commands.SayCommand;
import radmin.client.commands.commands.ServerCommand;
import radmin.client.commands.commands.SettingCommand;
import radmin.client.commands.commands.SpectateCommand;
import radmin.client.commands.commands.SwarmCommand;
import radmin.client.commands.commands.ToggleCommand;
import radmin.client.commands.commands.VClipCommand;
import radmin.client.commands.commands.WaspCommand;
import radmin.client.commands.commands.WaypointCommand;
import radmin.client.events.game.GameJoinedEvent;
import radmin.client.pathing.PathManagers;
import radmin.client.utils.PostInit;

public class Commands {
    public static final List<Command> COMMANDS = new ArrayList<Command>();
    public static CommandDispatcher<ClientSuggestionProvider> DISPATCHER = new CommandDispatcher();

    @PostInit(dependencies={PathManagers.class})
    public static void init() {
        Commands.add(new VClipCommand());
        Commands.add(new HClipCommand());
        Commands.add(new DismountCommand());
        Commands.add(new DisconnectCommand());
        Commands.add(new DamageCommand());
        Commands.add(new DropCommand());
        Commands.add(new EnchantCommand());
        Commands.add(new FakePlayerCommand());
        Commands.add(new FriendsCommand());
        Commands.add(new CommandsCommand());
        Commands.add(new InventoryCommand());
        Commands.add(new NbtCommand());
        Commands.add(new NotebotCommand());
        Commands.add(new PeekCommand());
        Commands.add(new EnderChestCommand());
        Commands.add(new ProfilesCommand());
        Commands.add(new ReloadCommand());
        Commands.add(new ResetCommand());
        Commands.add(new SayCommand());
        Commands.add(new ServerCommand());
        Commands.add(new SwarmCommand());
        Commands.add(new ToggleCommand());
        Commands.add(new SettingCommand());
        Commands.add(new SpectateCommand());
        Commands.add(new GamemodeCommand());
        Commands.add(new SaveMapCommand());
        Commands.add(new MacroCommand());
        Commands.add(new ModulesCommand());
        Commands.add(new BindsCommand());
        Commands.add(new GiveCommand());
        Commands.add(new NameHistoryCommand());
        Commands.add(new BindCommand());
        Commands.add(new FovCommand());
        Commands.add(new RotationCommand());
        Commands.add(new WaypointCommand());
        Commands.add(new InputCommand());
        Commands.add(new WaspCommand());
        Commands.add(new LocateCommand());
        Commands.add(new HelpCommand());
        COMMANDS.sort(Comparator.comparing(Command::getName));
        MeteorClient.EVENT_BUS.subscribe(Commands.class);
    }

    public static void add(Command command) {
        COMMANDS.removeIf(existing -> existing.getName().equals(command.getName()));
        COMMANDS.add(command);
    }

    public static void dispatch(String message) throws CommandSyntaxException {
        DISPATCHER.execute(message, (Object)MeteorClient.mc.getConnection().getSuggestionsProvider());
    }

    public static Command get(String name) {
        for (Command command : COMMANDS) {
            if (!command.getName().equals(name)) continue;
            return command;
        }
        return null;
    }

    @EventHandler
    private static void onJoin(GameJoinedEvent event) {
        ClientPacketListener networkHandler = MeteorClient.mc.getConnection();
        Command.REGISTRY_ACCESS = CommandBuildContext.simple((HolderLookup.Provider)networkHandler.registryAccess(), (FeatureFlagSet)networkHandler.enabledFeatures());
        DISPATCHER = new CommandDispatcher();
        for (Command command : COMMANDS) {
            command.registerTo(DISPATCHER);
        }
    }
}


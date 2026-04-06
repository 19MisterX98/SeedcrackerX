package kaptainwutax.seedcrackerX.init;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import kaptainwutax.seedcrackerX.command.ClientCommand;
import kaptainwutax.seedcrackerX.command.CrackerCommand;
import kaptainwutax.seedcrackerX.command.DataCommand;
import kaptainwutax.seedcrackerX.command.DatabaseCommand;
import kaptainwutax.seedcrackerX.command.FinderCommand;
import kaptainwutax.seedcrackerX.command.GuiCommand;
import kaptainwutax.seedcrackerX.command.RenderCommand;
import kaptainwutax.seedcrackerX.command.VersionCommand;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.*;
import kaptainwutax.seedcrackerX.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ClientCommands {

    public static final String PREFIX = "seedcracker";
    public static final List<ClientCommand> COMMANDS = new ArrayList<>();

    public static RenderCommand RENDER;
    public static FinderCommand FINDER;
    public static DataCommand DATA;
    public static CrackerCommand CRACKER;
    public static VersionCommand VERSION;
    public static GuiCommand GUI;
    public static DatabaseCommand DATABASE;

    static {
        COMMANDS.add(RENDER = new RenderCommand());
        COMMANDS.add(FINDER = new FinderCommand());
        COMMANDS.add(DATA = new DataCommand());
        COMMANDS.add(CRACKER = new CrackerCommand());
        COMMANDS.add(VERSION = new VersionCommand());
        COMMANDS.add(GUI = new GuiCommand());
        COMMANDS.add(DATABASE = new DatabaseCommand());
    }

    public static void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        LiteralArgumentBuilder<FabricClientCommandSource> root = literal(PREFIX)
                .executes(context -> {
                    Log.error("Error: please enter a valid seedcracker command");
                    return 1;
                });
     
            for (ClientCommand command : COMMANDS) {
                root.then(command.buildSubCommand());
            }
     
            dispatcher.register(root);
        }
    }

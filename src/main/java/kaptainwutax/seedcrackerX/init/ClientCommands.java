package kaptainwutax.seedcrackerX.init;

import com.mojang.brigadier.CommandDispatcher;
import kaptainwutax.seedcrackerX.command.*;
import net.minecraft.commands.CommandSourceStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;

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

    @SubscribeEvent
    public void onRegisterClientCommands(RegisterClientCommandsEvent event) {
        registerCommands(event.getDispatcher());
    }

    public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        COMMANDS.forEach(clientCommand -> clientCommand.register(dispatcher));
    }
}

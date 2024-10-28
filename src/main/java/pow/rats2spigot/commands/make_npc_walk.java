package pow.rats2spigot.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pow.rats2spigot.MainManager;
import pow.rats2spigot.util.Utility;

import java.util.Objects;


public class make_npc_walk implements CommandExecutor {
    private final MainManager mainManager;

    public make_npc_walk(MainManager mainManager) {
        this.mainManager = mainManager;
        Objects.requireNonNull(mainManager.getCommand("make_npc_walk")).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        Utility.sendMessageToAllAdmins("Make NPC Walk");
        mainManager.getCitizensManipulate().startNPCWalk();

        return true;
    }
}


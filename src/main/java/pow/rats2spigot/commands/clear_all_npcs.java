package pow.rats2spigot.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pow.rats2spigot.MainManager;
import pow.rats2spigot.util.Utility;


import java.util.Objects;


public class clear_all_npcs implements CommandExecutor {
    private final MainManager mainManager;

    public clear_all_npcs(MainManager mainManager) {
        this.mainManager = mainManager;
        Objects.requireNonNull(mainManager.getCommand("clear_all_npcs")).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Utility.sendMessageToAllAdmins("Clearing all npcs");

        mainManager.getCitizensManipulate().clearAllNPCs(true);

        return true;
    }
}


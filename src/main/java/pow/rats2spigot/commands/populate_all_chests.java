package pow.rats2spigot.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pow.rats2spigot.MainManager;
import pow.rats2spigot.util.Utility;

import java.util.Objects;


public class populate_all_chests implements CommandExecutor {
    private final MainManager mainManager;

    public populate_all_chests(MainManager mainManager) {
        this.mainManager = mainManager;
        Objects.requireNonNull(mainManager.getCommand("populate_all_chests")).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        Utility.sendMessageToAllAdmins("Populating all chests!");

        mainManager.populateAllChests();

        return true;
    }
}


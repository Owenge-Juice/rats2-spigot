package pow.rats2spigot.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pow.rats2spigot.MainManager;
import pow.rats2spigot.util.Utility;

import java.util.Objects;


public class hard_clear_all_npcs implements CommandExecutor {
    private final MainManager mainManager;

    public hard_clear_all_npcs(MainManager mainManager) {
        this.mainManager = mainManager;
        Objects.requireNonNull(mainManager.getCommand("hard_clear_all_npcs")).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Utility.sendMessageToAllAdmins("Hard clearing ALL npcs");

        mainManager.getCitizensManipulate().hardClearAllNPCs();
        return true;
    }
}


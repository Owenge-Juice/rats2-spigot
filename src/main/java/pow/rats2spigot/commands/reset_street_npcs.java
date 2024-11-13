package pow.rats2spigot.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pow.rats2spigot.MainManager;
import pow.rats2spigot.util.Utility;

import java.util.Objects;


public class reset_street_npcs implements CommandExecutor {
    private final MainManager mainManager;

    public reset_street_npcs(MainManager mainManager) {
        this.mainManager = mainManager;
        Objects.requireNonNull(mainManager.getCommand("reset_street_npcs")).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Utility.sendMessageToAllAdmins("Resetting npcs");
        commandSender.sendMessage("Resetting ncps");

        mainManager.getCitizensManipulate().hardClearAllNPCs();
        mainManager.getCitizensManipulate().spawnCitizens();
        mainManager.getCitizensManipulate().startCitizensWalking(50);

        return true;
    }
}


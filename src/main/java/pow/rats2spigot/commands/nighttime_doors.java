package pow.rats2spigot.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pow.rats2spigot.MainManager;

import java.util.Objects;


public class nighttime_doors implements CommandExecutor {
    private final MainManager mainManager;

    public nighttime_doors(MainManager mainManager) {
        this.mainManager = mainManager;
        Objects.requireNonNull(mainManager.getCommand("nighttime_doors")).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        mainManager.loadDoorConfigFromFile("nighttime");
        return true;
    }
}


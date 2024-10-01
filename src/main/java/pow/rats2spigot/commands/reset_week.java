package pow.rats2spigot.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pow.rats2spigot.MainManager;

import java.util.Objects;


public class reset_week implements CommandExecutor {
    private final MainManager mainManager;

    public reset_week(MainManager mainManager) {
        this.mainManager = mainManager;
        Objects.requireNonNull(mainManager.getCommand("reset_week")).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {


        mainManager.updateCurrentIRLDay(0);

        return true;
    }
}


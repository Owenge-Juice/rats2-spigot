package pow.rats2spigot.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pow.rats2spigot.MainManager;

import java.util.Objects;


public class advance_day implements CommandExecutor {
    private final MainManager mainManager;

    public advance_day(MainManager mainManager) {
        this.mainManager = mainManager;
        Objects.requireNonNull(mainManager.getCommand("advance_day")).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {


        mainManager.advanceDay();

        return true;
    }
}


package pow.rats2spigot.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pow.rats2spigot.MainManager;

import java.util.Objects;


public class test implements CommandExecutor {
    private final MainManager mainManager;

    public test(MainManager mainManager) {
        this.mainManager = mainManager;
        Objects.requireNonNull(mainManager.getCommand("test")).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        return true;
    }
}


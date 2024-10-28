package pow.rats2spigot.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pow.rats2spigot.MainManager;
import pow.rats2spigot.util.Utility;

import java.util.Objects;


public class give_up implements CommandExecutor {
    private final MainManager mainManager;

    public give_up(MainManager mainManager) {
        this.mainManager = mainManager;
        Objects.requireNonNull(mainManager.getCommand("give_up")).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player)commandSender;
        player.setHealth(0);
        return true;
    }
}


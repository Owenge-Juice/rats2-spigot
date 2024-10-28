package pow.rats2spigot.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import pow.rats2spigot.MainManager;

import java.util.Objects;


public class cancel_cat_disc implements CommandExecutor {
    private final MainManager mainManager;

    public cancel_cat_disc(MainManager mainManager) {
        this.mainManager = mainManager;
        Objects.requireNonNull(mainManager.getCommand("cancel_cat_disc")).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(mainManager.getCatDiscEvent()!=null){
            mainManager.getCatDiscEvent().cancel();
        }
        return true;
    }


}


package pow.rats2spigot.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Score;
import pow.rats2spigot.MainManager;

import java.util.Objects;


public class slow_mode implements CommandExecutor {
    private final MainManager mainManager;

    public slow_mode(MainManager mainManager) {
        this.mainManager = mainManager;
        Objects.requireNonNull(mainManager.getCommand("slow_mode")).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player)commandSender;

        if(strings.length!=1){
            player.sendMessage("Command requires argument after it, either 'on' or 'off'");
            return false;
        }

        if(Objects.equals(strings[0], "on")){
            player.sendMessage("Slow mode has been turned on");
            Score slowmodeScore = Bukkit.getScoreboardManager().getMainScoreboard().getObjective("config").getScore("slowmode");
            slowmodeScore.setScore(1);
            return true;
        }else if(Objects.equals(strings[0], "off")){
            player.sendMessage("Slow mode has been turned off");
            Score slowmodeScore = Bukkit.getScoreboardManager().getMainScoreboard().getObjective("config").getScore("slowmode");
            slowmodeScore.setScore(0);

            return true;
        }

        return true;
    }
}


package pow.rats2spigot.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import pow.rats2spigot.MainManager;

import java.util.Objects;


public class little_tiles_chunk implements CommandExecutor {
    private final MainManager mainManager;

    public little_tiles_chunk(MainManager mainManager) {
        this.mainManager = mainManager;
        Objects.requireNonNull(mainManager.getCommand("little_tiles_chunk")).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player)commandSender;
        if(player.getScoreboardTags().contains("TilingChunk")){
            player.getScoreboardTags().remove("TilingChunk");
            player.sendMessage("removing you from tiling chunk");
        }else{
            player.sendMessage("Adding you to tiling chunk");
            player.getScoreboardTags().add("TilingChunk");
            player.setGameMode(GameMode.CREATIVE);
            player.teleport(new Location(mainManager.getWorld(),(player.getLocation().getChunk().getX()*16)+0.5,-63,(player.getLocation().getChunk().getZ()*16)+0.5));
        }


        return true;
    }


}


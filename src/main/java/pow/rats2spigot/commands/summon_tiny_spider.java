package pow.rats2spigot.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import pow.rats2spigot.MainManager;

import java.util.Objects;


public class summon_tiny_spider implements CommandExecutor {
    private final MainManager mainManager;

    public summon_tiny_spider(MainManager mainManager) {
        this.mainManager = mainManager;
        Objects.requireNonNull(mainManager.getCommand("summon_tiny_spider")).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        Player player = (Player)commandSender;

        int numberOfSpiders;

        if(strings.length==1){
            try {
                numberOfSpiders = Integer.parseInt(strings[0]);
            } catch (NumberFormatException e) {
                numberOfSpiders = 1;
            }
        }else{
            numberOfSpiders = 1;
        }


        mainManager.summonTinySpider(player.getLocation(),numberOfSpiders);

        return true;
    }


}


package pow.rats2spigot.commands;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import pow.rats2spigot.MainManager;

import java.util.Objects;


public class summonBossSpider implements CommandExecutor {
    private final MainManager mainManager;

    public summonBossSpider(MainManager mainManager) {
        this.mainManager = mainManager;
        Objects.requireNonNull(mainManager.getCommand("summonBossSpider")).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player)commandSender;

        CaveSpider caveSpider = (CaveSpider) mainManager.getWorld().spawnEntity(player.getLocation(), EntityType.CAVE_SPIDER);
        caveSpider.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(100);
        caveSpider.setHealth(100);

        return true;
    }


}


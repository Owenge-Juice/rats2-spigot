package pow.rats2spigot.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import pow.rats2spigot.MainManager;

import java.util.Objects;


public class quest implements CommandExecutor {
    private final MainManager mainManager;

    public quest(MainManager mainManager) {
        this.mainManager = mainManager;
        Objects.requireNonNull(mainManager.getCommand("quest")).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player)commandSender;

        Location location = player.getLocation().clone().getBlock().getLocation().add(0.5,0,0.5);

        ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);


        BukkitTask bukkitTask = new BukkitRunnable() {
            @Override
            public void run() {
                armorStand.addScoreboardTag("QuestMarker");
            }
        }.runTaskLater(mainManager, 300); // 100 ticks = 5 seconds
        armorStand.setInvulnerable(true);
        armorStand.setInvisible(true);

        mainManager.getQuestMarkers().add(armorStand);


        return true;
    }


}


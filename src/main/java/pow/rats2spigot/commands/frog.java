package pow.rats2spigot.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Frog;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import pow.rats2spigot.MainManager;

import java.util.Objects;
import java.util.Random;


public class frog implements CommandExecutor {
    private final MainManager mainManager;

    public frog(MainManager mainManager) {
        this.mainManager = mainManager;
        Objects.requireNonNull(mainManager.getCommand("frog")).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player)commandSender;


        if (strings.length == 0) {
            player.sendMessage("ERROR: Command requires a name like /frog <name>");
            return false;
        }

        // Join all arguments with spaces to form the name
        String name = String.join(" ", strings);

        // Spawn the frog
        Frog frog = (Frog) player.getWorld().spawnEntity(player.getLocation(), EntityType.FROG);

        frog.setPersistent(true);
        frog.setSilent(true);
        frog.setInvulnerable(true);
        frog.setCustomName(name); // Set the custom name
        frog.setCustomNameVisible(true);

        // Set a random variant
        Frog.Variant randomVariant = getRandomVariant();
        frog.setVariant(randomVariant);

        Random random = new Random();
        float size = 0.25f + random.nextFloat() * 0.25f; // Scale and shift

        BukkitTask bukkitTask = new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"scale set pehkui:base "+ size + " " + frog.getUniqueId());
            }
        }.runTaskLater(mainManager, 40); // 2 seconds

        return true;
    }

    private Frog.Variant getRandomVariant() {
        Random random = new Random();
        Frog.Variant[] variants = Frog.Variant.values();
        return variants[random.nextInt(variants.length)];
    }


}


package pow.rats2spigot.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.inventory.ItemStack;
import pow.rats2spigot.MainManager;

import java.util.Objects;

public class summon_tiny_horses implements CommandExecutor {

    private final MainManager mainManager;

    public summon_tiny_horses(MainManager mainManager) {
        this.mainManager = mainManager;
        Objects.requireNonNull(mainManager.getCommand("summon_tiny_horses")).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        Horse Gnawser = (Horse)mainManager.getWorld().spawnEntity(new Location(mainManager.getWorld(),-42, -45, -63), EntityType.HORSE);
        Horse Scamper = (Horse)mainManager.getWorld().spawnEntity(new Location(mainManager.getWorld(),-1, -46, -136), EntityType.HORSE);
        Horse Pouncer = (Horse)mainManager.getWorld().spawnEntity(new Location(mainManager.getWorld(),-57, -49, -133), EntityType.HORSE);
        Horse Nibblen = (Horse)mainManager.getWorld().spawnEntity(new Location(mainManager.getWorld(),37, -45, -35), EntityType.HORSE);
        Horse Chewet = (Horse)mainManager.getWorld().spawnEntity(new Location(mainManager.getWorld(),27, -24, -168), EntityType.HORSE);
        Horse Skitter = (Horse)mainManager.getWorld().spawnEntity(new Location(mainManager.getWorld(),91, -24, -126), EntityType.HORSE);
        Horse Squeaker = (Horse)mainManager.getWorld().spawnEntity(new Location(mainManager.getWorld(),79, -10, -122), EntityType.HORSE);
        Horse Whiskers = (Horse)mainManager.getWorld().spawnEntity(new Location(mainManager.getWorld(),84, -24, -78), EntityType.HORSE);
        Horse Ratdolph = (Horse)mainManager.getWorld().spawnEntity(new Location(mainManager.getWorld(),93, -45, -131), EntityType.HORSE);

        Gnawser.setTamed(true);
        Gnawser.getInventory().setSaddle(new ItemStack(Material.SADDLE));
        Gnawser.setAdult();
        Gnawser.setCustomName("Gnawser");
        Gnawser.setCustomNameVisible(true);
        Gnawser.getScoreboardTags().add("Horse");
        Gnawser.setPersistent(true);
        Gnawser.setInvulnerable(true);

        Scamper.setTamed(true);
        Scamper.getInventory().setSaddle(new ItemStack(Material.SADDLE));
        Scamper.setAdult();
        Scamper.setCustomName("Scamper");
        Scamper.setCustomNameVisible(true);
        Scamper.getScoreboardTags().add("Horse");
        Scamper.setPersistent(true);
        Scamper.setInvulnerable(true);

        Pouncer.setTamed(true);
        Pouncer.getInventory().setSaddle(new ItemStack(Material.SADDLE));
        Pouncer.setAdult();
        Pouncer.setCustomName("Pouncer");
        Pouncer.setCustomNameVisible(true);
        Pouncer.getScoreboardTags().add("Horse");
        Pouncer.setPersistent(true);
        Pouncer.setInvulnerable(true);

        Nibblen.setTamed(true);
        Nibblen.getInventory().setSaddle(new ItemStack(Material.SADDLE));
        Nibblen.setAdult();
        Nibblen.setCustomName("Nibblen");
        Nibblen.setCustomNameVisible(true);
        Nibblen.getScoreboardTags().add("Horse");
        Nibblen.setPersistent(true);
        Nibblen.setInvulnerable(true);

        Chewet.setTamed(true);
        Chewet.getInventory().setSaddle(new ItemStack(Material.SADDLE));
        Chewet.setAdult();
        Chewet.setCustomName("Chewet");
        Chewet.setCustomNameVisible(true);
        Chewet.getScoreboardTags().add("Horse");
        Chewet.setPersistent(true);
        Chewet.setInvulnerable(true);

        Skitter.setTamed(true);
        Skitter.getInventory().setSaddle(new ItemStack(Material.SADDLE));
        Skitter.setAdult();
        Skitter.setCustomName("Skitter");
        Skitter.setCustomNameVisible(true);
        Skitter.getScoreboardTags().add("Horse");
        Skitter.setPersistent(true);
        Skitter.setInvulnerable(true);

        Squeaker.setTamed(true);
        Squeaker.getInventory().setSaddle(new ItemStack(Material.SADDLE));
        Squeaker.setAdult();
        Squeaker.setCustomName("Squeaker");
        Squeaker.setCustomNameVisible(true);
        Squeaker.getScoreboardTags().add("Horse");
        Squeaker.setPersistent(true);
        Squeaker.setInvulnerable(true);

        Whiskers.setTamed(true);
        Whiskers.getInventory().setSaddle(new ItemStack(Material.SADDLE));
        Whiskers.setAdult();
        Whiskers.setCustomName("Whiskers");
        Whiskers.setCustomNameVisible(true);
        Whiskers.getScoreboardTags().add("Horse");
        Whiskers.setPersistent(true);
        Whiskers.setInvulnerable(true);

        Ratdolph.setTamed(true);
        Ratdolph.getInventory().setSaddle(new ItemStack(Material.SADDLE));
        Ratdolph.setAdult();
        Ratdolph.setCustomName("Ratdolph");
        Ratdolph.setCustomNameVisible(true);
        Ratdolph.getScoreboardTags().add("Horse");
        Ratdolph.setPersistent(true);
        Ratdolph.setInvulnerable(true);

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"execute as @e[tag=Horse] run scale set pehkui:base 0.3 @s");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"execute as @e[tag=Horse] run scale set pehkui:motion 2 @s");

        return true;
    }
}

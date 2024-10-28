package pow.rats2spigot.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import pow.rats2spigot.MainManager;

import java.util.Objects;
import java.util.Random;


public class halloween_completed implements CommandExecutor {
    private final MainManager mainManager;

    public halloween_completed(MainManager mainManager) {
        this.mainManager = mainManager;
        Objects.requireNonNull(mainManager.getCommand("halloween_completed")).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Location summonLocation = new Location(mainManager.getWorld(), 273, -59, -103);
        summonLocation.getWorld().playSound(summonLocation,"rats2:powevents.sound.halloween_completed", SoundCategory.AMBIENT,1,1);

        return true;
    }


}


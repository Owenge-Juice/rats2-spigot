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
import pow.rats2spigot.MainManager;

import java.util.Objects;
import java.util.Random;


public class halloween_summon_brawl_3 implements CommandExecutor {
    private final MainManager mainManager;

    public halloween_summon_brawl_3(MainManager mainManager) {
        this.mainManager = mainManager;
        Objects.requireNonNull(mainManager.getCommand("halloween_summon_brawl_3")).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Location summonLocation = new Location(mainManager.getWorld(), 183, -60, -176);
        Random random = new Random();
        Location randomSummonLocation;
        for (int i = 0; i < 4; i++) {
            randomSummonLocation = summonLocation.clone().add(random.nextFloat(-2,2),0,random.nextFloat(-2,2));

            randomSummonLocation.getWorld().spawnEntity(randomSummonLocation, EntityType.SILVERFISH);
        }

        for (int i = 0; i < 7; i++) {
            randomSummonLocation = summonLocation.clone().add(random.nextFloat(-2,2),0,random.nextFloat(-2,2));
            Entity entity = randomSummonLocation.getWorld().spawnEntity(randomSummonLocation, EntityType.SPIDER);

            ((LivingEntity)entity).addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,20,1,false,false));
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"execute as "+ entity.getUniqueId() +" run scale set pehkui:base 0.2 @s");
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"execute as "+ entity.getUniqueId() +" run scale set pehkui:hitbox_width 2 @s");
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"execute as "+ entity.getUniqueId() +" run scale set pehkui:hitbox_height 2 @s");
        }


        return true;
    }


}


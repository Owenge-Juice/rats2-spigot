package pow.rats2spigot.runnables;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import pow.rats2spigot.MainManager;

public class HalloweenRunnable extends BukkitRunnable {

    MainManager mainManager;
    private BukkitTask bukkitTask;

    public HalloweenRunnable(MainManager mainManager) {
        this.mainManager = mainManager;
        startTaskTimer();
    }

    public BukkitTask getBukkitTask() {
        return bukkitTask;
    }

    //ran every second, and depending on how many seconds has passed, prints text accordingly.
    @Override
    public void run() {
        //System.out.println("scanning!");
        for(Entity entity : mainManager.getWorld().getEntities()){
            if(entity.getType()== EntityType.CAT){
                //System.out.println("Found cat!");
                mainManager.getMainTeam().addEntry(entity.getName());
                assert entity instanceof LivingEntity;
                ((LivingEntity)entity).addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,-1,1,false,false));
                //System.out.println("Done");
            }
        }

        for(Player player : mainManager.getWorld().getPlayers()){
            if(player.getInventory().contains(Material.MUSIC_DISC_CAT)){
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,150,1,false,false));
            }
        }

    }

    public BukkitTask startTaskTimer() {
        bukkitTask = runTaskTimer(mainManager, 0, 100);
        return bukkitTask;
    }
}

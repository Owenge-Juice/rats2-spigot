package pow.rats2spigot;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import pow.rats2spigot.util.Utility;

import java.util.*;

public class StoveRunnable extends BukkitRunnable {

    MainManager mainManager;
    StoveManager stoveManager;
    private BukkitTask bukkitTask;

    public StoveRunnable(StoveManager stoveManager, MainManager mainManager) {
        this.mainManager = mainManager;
        this.stoveManager = stoveManager;
        startTaskTimer();
    }

    public BukkitTask getBukkitTask() {
        return bukkitTask;
    }

    //ran every 2 ticks
    @Override
    public void run() {
        ArrayList<UUID> objectsToRemove = new ArrayList<>();
        for(Map.Entry<UUID,Integer> entry : stoveManager.getCookedFoods().entrySet()){
            Entity entity = Bukkit.getEntity(entry.getKey());
            stoveManager.getCookedFoods().replace(entry.getKey(),entry.getValue()+1);
            if(entry.getValue()>100){
                objectsToRemove.add(entry.getKey());
            }
            if(entry.getValue()%40==0){
                entity.getWorld().playSound(entity.getLocation(),"farmersdelight:block.skillet.sizzle",SoundCategory.BLOCKS,1,1);
            }
            if(entry.getValue()%5==0){
                Random random = new Random();
                int randomCount = random.nextInt(5);
                for (int i = 0; i < randomCount; i++) {
                    int randomAlpha = random.nextInt(50);
                    float randomX = random.nextFloat(0.2f);
                    float randomZ = random.nextFloat(0.2f);
                    float randomSize = random.nextFloat(0.5f)+0.5f;
                    int randomZAxis = random.nextBoolean() ? 1 : -1;
                    int randomXAxis = random.nextBoolean() ? 1 : -1;

                    // Define the color and size for the particle
                    Color color = Color.fromRGB(randomAlpha, randomAlpha, randomAlpha);  // Black color (0, 0, 0)

                    // Create the DustOptions for the particle
                    Particle.DustOptions dustOptions = new Particle.DustOptions(color, randomSize);
                    Location location = entity.getLocation().clone().add(0.5,0.2,0.5);
                    location.add(randomX*randomXAxis,0,randomZ*randomZAxis);

                    mainManager.getWorld().spawnParticle(Particle.REDSTONE, location, 1, 0, 0, 0, 0, dustOptions);
                }
            }
        }

        for(UUID uuid : objectsToRemove){
            Entity entityToRemove = Bukkit.getEntity(uuid);
            stoveManager.getCookedFoods().remove(uuid);
            ItemStack itemStack = new ItemStack(stoveManager.getCookingConversion().get(((ItemDisplay)entityToRemove).getItemStack().getType()));
            mainManager.getWorld().dropItemNaturally(Bukkit.getEntity(uuid).getLocation().clone().add(0.5,0.1,0.5),itemStack);
            entityToRemove.remove();
        }
    }

    public BukkitTask startTaskTimer() {
        bukkitTask = runTaskTimer(mainManager, 0, 2);
        return bukkitTask;
    }
}

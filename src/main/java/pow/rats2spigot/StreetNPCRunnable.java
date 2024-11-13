package pow.rats2spigot;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class StreetNPCRunnable extends BukkitRunnable {

    MainManager mainManager;
    StreetNPC streetNPC;
    private BukkitTask bukkitTask;

    public StreetNPCRunnable(StreetNPC streetNPC, MainManager mainManager) {
        this.streetNPC = streetNPC;
        this.mainManager = mainManager;
    }

    public BukkitTask getBukkitTask() {
        return bukkitTask;
    }

    //ran every 5 seconds
    @Override
    public void run() {
        //If the NPC is currently walking
        if(streetNPC.isWalking()){
            //Get the location that they are headed
            Location destinationLocation = streetNPC.getDestinationStreetCorner();
            //If they are within 3 blocks of that location
            if(destinationLocation.distance(streetNPC.getNpc().getStoredLocation())<3){
                //Make them start walking again
                if(!mainManager.getCitizensManipulate().makeNPCWalk(streetNPC)){
                    bukkitTask.cancel();
                }

            }
        }

        //Despawn checker
        for (NPC npc : CitizensAPI.getNPCRegistry()) {
            // Check if the NPC is spawned and in the specified world
            if (npc.isSpawned() && npc.getEntity().getWorld().equals(mainManager.getWorld())) {
                if(!mainManager.getCitizensManipulate().getAllValidStreetNPCEntities().contains(npc.getEntity())){
                    npc.despawn();
                }
            }
        }
    }

    public BukkitTask startTaskTimer() {
        bukkitTask = runTaskTimer(mainManager, 0, 20*5);
        return bukkitTask;
    }
}

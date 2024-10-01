package pow.rats2spigot.runnables;

import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import pow.rats2spigot.MainManager;
import pow.rats2spigot.util.AmbientLocation;

import java.util.ArrayList;

public class AmbientRunnable extends BukkitRunnable {

    MainManager mainManager;
    private BukkitTask bukkitTask;
    private ArrayList<AmbientLocation> ambientLocations = new ArrayList<>();


    public AmbientRunnable(MainManager mainManager,int lengthOfTrack, String sound,int numberOfVariations, ArrayList<Location> soundLocations) {
        this.mainManager = mainManager;
        for(Location location : soundLocations){
            ambientLocations.add(new AmbientLocation(location,lengthOfTrack,sound,numberOfVariations,mainManager));
        }
        startTaskTimer(0,20);
    }

    //ran every second, and depending on how many seconds has passed, prints text accordingly.
    @Override
    public void run() {
        for(AmbientLocation ambientLocation : ambientLocations){
            ambientLocation.scanPlayers();
        }
    }

    public BukkitTask startTaskTimer(int delay, int period) {
        bukkitTask = runTaskTimer(mainManager, delay, period);
        return bukkitTask;
    }
}

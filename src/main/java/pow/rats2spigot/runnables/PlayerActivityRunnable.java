package pow.rats2spigot.runnables;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import pow.rats2spigot.MainManager;

public class PlayerActivityRunnable extends BukkitRunnable {

    MainManager mainManager;
    private BukkitTask bukkitTask;

    public PlayerActivityRunnable(MainManager mainManager) {
        this.mainManager = mainManager;
        startTaskTimer(0,100);
    }

    public BukkitTask getBukkitTask() {
        return bukkitTask;
    }

    //ran every second, and depending on how many seconds has passed, prints text accordingly.
    @Override
    public void run() {
        if(mainManager.getWorld().getPlayers().isEmpty()){
            mainManager.checkIfCitizensCleared();
        }
    }

    public BukkitTask startTaskTimer(int delay, int period) {
        bukkitTask = runTaskTimer(mainManager, delay, period);
        return bukkitTask;
    }
}

package pow.rats2spigot.runnables;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import pow.rats2spigot.MainManager;

public class TickRunnable extends BukkitRunnable {

    MainManager mainManager;
    private BukkitTask bukkitTask;

    public TickRunnable(MainManager mainManager) {
        this.mainManager = mainManager;
        startTaskTimer();
    }

    public BukkitTask getBukkitTask() {
        return bukkitTask;
    }

    //ran every second, and depending on how many seconds has passed, prints text accordingly.
    @Override
    public void run() {
        mainManager.tickRunnableTrigger();
    }

    public BukkitTask startTaskTimer() {
        bukkitTask = runTaskTimer(mainManager, 0, 1);
        return bukkitTask;
    }
}

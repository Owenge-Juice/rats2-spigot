package pow.rats2spigot.runnables;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import pow.rats2spigot.MainManager;

public class TemplateRunnable extends BukkitRunnable {

    MainManager mainManager;
    private BukkitTask bukkitTask;

    public TemplateRunnable(MainManager mainManager) {
        this.mainManager = mainManager;
        startTaskTimer(0,20);
    }

    public BukkitTask getBukkitTask() {
        return bukkitTask;
    }

    //ran every second, and depending on how many seconds has passed, prints text accordingly.
    @Override
    public void run() {
    }

    public BukkitTask startTaskTimer(int delay, int period) {
        bukkitTask = runTaskTimer(mainManager, delay, period);
        return bukkitTask;
    }
}

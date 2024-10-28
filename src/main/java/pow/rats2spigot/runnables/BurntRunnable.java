package pow.rats2spigot.runnables;

import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import pow.rats2spigot.MainManager;

public class BurntRunnable extends BukkitRunnable {

    MainManager mainManager;
    private BukkitTask bukkitTask;
    int timeLeft = 20;
    Player player;

    public BurntRunnable(MainManager mainManager, Player player) {
        this.mainManager = mainManager;
        this.player=player;
        startTaskTimer();
    }

    public BukkitTask getBukkitTask() {
        return bukkitTask;
    }

    //ran every second, and depending on how many seconds has passed, prints text accordingly.
    @Override
    public void run() {
        timeLeft-=1;
        if(timeLeft%2==0){
            player.getWorld().spawnParticle(Particle.SMOKE_LARGE,player.getLocation(),0,0,0,0,0);
        }

        if(timeLeft<=0){
            this.cancel();
        }
    }

    public BukkitTask startTaskTimer() {
        bukkitTask = runTaskTimer(mainManager, 0, 1);
        return bukkitTask;
    }
}

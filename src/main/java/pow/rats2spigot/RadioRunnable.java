package pow.rats2spigot;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import pow.rats2spigot.interactables.Radio;
import pow.rats2spigot.util.Utility;

public class RadioRunnable extends BukkitRunnable {

    Radio radio;
    MainManager mainManager;
    private BukkitTask bukkitTask;

    public RadioRunnable(Radio radio, MainManager mainManager) {
        this.radio = radio;
        this.mainManager = mainManager;
        startTaskTimer();
    }

    public BukkitTask getBukkitTask() {
        return bukkitTask;
    }

    //ran every 5 seconds
    @Override
    public void run() {
        if(radio.isPlayingSong()){
            radio.getLocation().getWorld().spawnParticle(Particle.NOTE,radio.getLocation().clone().add(0.5,0.5,0.5),1);
        }
    }

    public BukkitTask startTaskTimer() {
        bukkitTask = runTaskTimer(mainManager, 0, 20);
        return bukkitTask;
    }
}

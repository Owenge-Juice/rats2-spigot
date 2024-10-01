package pow.rats2spigot.util;

import jdk.tools.jmod.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import pow.rats2spigot.MainManager;

import java.util.HashMap;
import java.util.Random;

public class AmbientLocation {

    Location locationOfAmbience;
    HashMap<String,Boolean> playingMap = new HashMap<>();
    int lengthOfTrack;
    String sound;
    MainManager mainManager;
    int numberOfVariations;

    public AmbientLocation(Location locationOfAmbience, int lengthOfTrack, String sound, int numberOfVariations,MainManager mainManager) {
        this.locationOfAmbience = locationOfAmbience;
        this.numberOfVariations=numberOfVariations;
        this.sound=sound;
        this.mainManager = mainManager;
        this.lengthOfTrack = lengthOfTrack;
    }

    public void scanPlayers(){
        for(Player player : Bukkit.getOnlinePlayers()){
            if(!playingMap.containsKey(player.getName())){
                playingMap.put(player.getName(),false);
            }
            if(player.getLocation().distance(locationOfAmbience)<15 && !playingMap.get(player.getName())){
                playAmbience(player);
            }
        }
    }

    private void playAmbience(Player player) {

        Random random = new Random();
        String randomSoundVariance = sound + random.nextInt(numberOfVariations);
        Utility.sendMessageToAllAdmins("playing ambience for " + player.getName() +": " + randomSoundVariance);
        player.getWorld().playSound(locationOfAmbience,randomSoundVariance, SoundCategory.AMBIENT,1,1);
        playingMap.replace(player.getName(),true);
        BukkitTask bukkitTask = new BukkitRunnable() {
            @Override
            public void run() {
                playingMap.replace(player.getName(),false);
            }
        }.runTaskLater(mainManager, lengthOfTrack);
    }

}

package pow.rats2spigot;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import pow.rats2spigot.runnables.AmbientRunnable;
import pow.rats2spigot.util.Utility;

import java.util.ArrayList;
import java.util.HashMap;

public class AmbientManager {

    MainManager mainManager;
    Location streetAmbientLocation;
    ArrayList<AmbientRunnable> ambientRunnables = new ArrayList<>();

    HashMap<String,Boolean> basementPlayerMap = new HashMap<>();
    HashMap<String,Boolean> boilerroomPlayerMap = new HashMap<>();

    public AmbientManager(MainManager mainManager) {
        this.mainManager = mainManager;
        streetAmbientLocation= new Location(mainManager.getWorld(), 59, 68, -148);

        setupStreetAmbientLocations();
    }

    public HashMap<String, Boolean> getBasementPlayerMap() {
        return basementPlayerMap;
    }

    public void checkBasementAmbience(Player player){
        if(!basementPlayerMap.containsKey(player.getName())){
            basementPlayerMap.put(player.getName(),false);
        }

        if(!basementPlayerMap.get(player.getName())){
            player.playSound(player,"rats2:powevents.sound.ambient_basement",SoundCategory.AMBIENT,0.6f,1);
            basementPlayerMap.replace(player.getName(),true);

            BukkitTask bukkitTask = new BukkitRunnable(){
                @Override
                public void run(){
                    basementPlayerMap.replace(player.getName(),false);
                }
            }.runTaskLater(mainManager,2*60*20);
        }
    }

    public void checkBoilerroomAmbience(Player player){
        if(!boilerroomPlayerMap.containsKey(player.getName())){
            boilerroomPlayerMap.put(player.getName(),false);
        }

        if(!boilerroomPlayerMap.get(player.getName())){
            player.playSound(new Location(mainManager.getWorld(), 39, -55, -153),"rats2:powevents.sound.ambient_boilerroom0",SoundCategory.AMBIENT,1,1);
            player.playSound(new Location(mainManager.getWorld(),72, -55, -111),"rats2:powevents.sound.ambient_boilerroom0",SoundCategory.AMBIENT,1,1);
            boilerroomPlayerMap.replace(player.getName(),true);

            BukkitTask bukkitTask = new BukkitRunnable(){
                @Override
                public void run(){
                    boilerroomPlayerMap.replace(player.getName(),false);
                }
            }.runTaskLater(mainManager,1*15*20);
        }
    }

    public void stopAllAmbientRunnables(){
        for(AmbientRunnable ambientRunnable : ambientRunnables){
            ambientRunnable.cancel();
        }
        for(Player player : Bukkit.getOnlinePlayers()){
            player.stopSound(SoundCategory.AMBIENT);
        }
    }

    public void setupStreetAmbientLocations(){
        ArrayList<Location> locations = new ArrayList<>();
        locations.add(new Location(mainManager.getWorld(), 14, -48, -21));
        locations.add(new Location(mainManager.getWorld(),-11, -48, -21));
        locations.add(new Location(mainManager.getWorld(),-28, -49, -21));
        locations.add(new Location(mainManager.getWorld(),-53, -50, -21));
        locations.add(new Location(mainManager.getWorld(),-72, -50, -21));
        locations.add(new Location(mainManager.getWorld(),-90, -51, -21));
        locations.add(new Location(mainManager.getWorld(),-110, -52, -22));
        locations.add(new Location(mainManager.getWorld(),-110, -52, -41));
        locations.add(new Location(mainManager.getWorld(),-110, -52, -61));
        locations.add(new Location(mainManager.getWorld(),-109, -52, -82));
        locations.add(new Location(mainManager.getWorld(),-109, -52, -101));
        locations.add(new Location(mainManager.getWorld(),-107, -52, -120));
        locations.add(new Location(mainManager.getWorld(),-109, -52, -142));
        locations.add(new Location(mainManager.getWorld(),-109, -52, -163));
        locations.add(new Location(mainManager.getWorld(),-109, -52, -181));
        locations.add(new Location(mainManager.getWorld(),-109, -52, -192));
        locations.add(new Location(mainManager.getWorld(),-109, -52, -198));
        locations.add(new Location(mainManager.getWorld(),-108, -52, -223));
        locations.add(new Location(mainManager.getWorld(),-108, -52, -243));
        locations.add(new Location(mainManager.getWorld(),-108, -52, -281));
        locations.add(new Location(mainManager.getWorld(),-108, -52, -303));
        locations.add(new Location(mainManager.getWorld(),-108, -52, -324));
        locations.add(new Location(mainManager.getWorld(),-108, -52, -341));
        locations.add(new Location(mainManager.getWorld(),-108, -52, -353));
        locations.add(new Location(mainManager.getWorld(),-90, -51, -353));
        locations.add(new Location(mainManager.getWorld(),-70, -50, -353));
        locations.add(new Location(mainManager.getWorld(),-47, -50, -353));
        locations.add(new Location(mainManager.getWorld(),-29, -49, -353));
        locations.add(new Location(mainManager.getWorld(),-8, -48, -354));
        locations.add(new Location(mainManager.getWorld(),12, -48, -354));
        locations.add(new Location(mainManager.getWorld(),29, -47, -353));
        locations.add(new Location(mainManager.getWorld(),52, -46, -354));
        locations.add(new Location(mainManager.getWorld(),71, -46, -354));
        locations.add(new Location(mainManager.getWorld(),90, -45, -354));
        locations.add(new Location(mainManager.getWorld(),102, -45, -353));
        locations.add(new Location(mainManager.getWorld(),102, -45, -337));
        locations.add(new Location(mainManager.getWorld(),102, -45, -319));
        locations.add(new Location(mainManager.getWorld(),102, -45, -297));
        locations.add(new Location(mainManager.getWorld(),101, -45, -275));
        locations.add(new Location(mainManager.getWorld(),101, -45, -262));
        locations.add(new Location(mainManager.getWorld(),100, -45, -240));
        locations.add(new Location(mainManager.getWorld(),100, -45, -222));
        locations.add(new Location(mainManager.getWorld(),100, -45, -206));
        locations.add(new Location(mainManager.getWorld(),101, -45, -186));
        locations.add(new Location(mainManager.getWorld(),105, -45, -163));
        locations.add(new Location(mainManager.getWorld(),105, -45, -139));
        locations.add(new Location(mainManager.getWorld(),106, -45, -115));
        locations.add(new Location(mainManager.getWorld(),106, -45, -101));
        locations.add(new Location(mainManager.getWorld(),105, -45, -84));
        locations.add(new Location(mainManager.getWorld(),105, -45, -60));
        locations.add(new Location(mainManager.getWorld(),105, -45, -39));
        locations.add(new Location(mainManager.getWorld(),105, -45, -23));
        locations.add(new Location(mainManager.getWorld(),88, -45, -21));
        locations.add(new Location(mainManager.getWorld(),69, -46, -21));
        locations.add(new Location(mainManager.getWorld(),50, -47, -21));
        locations.add(new Location(mainManager.getWorld(),31, -47, -21));

        ambientRunnables.add(new AmbientRunnable(mainManager,2*60*20,"rats2:powevents.sound.ambient_street",10,locations));
    }

    public void setupBoilerRoomAmbientLocations(){
        ArrayList<Location> locations = new ArrayList<>();
        locations.add(new Location(mainManager.getWorld(), 39, -55, -153));
        locations.add(new Location(mainManager.getWorld(),72, -55, -111));

        ambientRunnables.add(new AmbientRunnable(mainManager,1*15*20,"rats2:powevents.sound.ambient_boilerroom",1,locations));
    }


}

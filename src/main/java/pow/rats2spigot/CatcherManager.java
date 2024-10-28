package pow.rats2spigot;

import net.citizensnpcs.api.trait.trait.Owner;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;


public class CatcherManager {
    MainManager mainManager;
    CatcherRunnable catcherRunnable;
    ArrayList<String> namesOfCatchers = new ArrayList<>();
    HashMap<String, String> catcherSpawnDayLocations = new HashMap<>();
    HashMap<String, String> catcherSpawnNightLocations = new HashMap<>();

    public CatcherManager(MainManager mainManager) {
        this.mainManager = mainManager;

        namesOfCatchers.add("boilerman");
        namesOfCatchers.add("bach");
        namesOfCatchers.add("evil_brother");
        namesOfCatchers.add("evil_sister");
        namesOfCatchers.add("gom");
        namesOfCatchers.add("husband");
        namesOfCatchers.add("owner");
        namesOfCatchers.add("owners_son");
        namesOfCatchers.add("safety_inspector");
        namesOfCatchers.add("scientist");
        namesOfCatchers.add("widow");
        namesOfCatchers.add("widow_oldest_child");
        namesOfCatchers.add("youngest_child");
        namesOfCatchers.add("wife");

        catcherSpawnDayLocations.clear();
        catcherSpawnNightLocations.clear();


        catcherSpawnDayLocations.put("boilerman", "53 -55 -134");
        catcherSpawnNightLocations.put("boilerman", "53 -55 -134");
        catcherSpawnDayLocations.put("owner", "-65 -55 -40");
        catcherSpawnNightLocations.put("owner", "-65 -55 -40");
        catcherSpawnDayLocations.put("gom", "51 -45 -148");
        catcherSpawnNightLocations.put("gom", "-62 -24 -75");
        catcherSpawnDayLocations.put("youngest_child", "-49 -45 -41");
        catcherSpawnNightLocations.put("youngest_child", "66 -24 -115");
        catcherSpawnDayLocations.put("owners_son", "42 -45 -46");
        catcherSpawnNightLocations.put("owners_son", "42 -45 -46");
        catcherSpawnDayLocations.put("bach", "-35 -24 -168");
        catcherSpawnNightLocations.put("bach", "-2 -55 -55");
        catcherSpawnDayLocations.put("widow_oldest_child", "63 -35 -156");
        catcherSpawnNightLocations.put("widow_oldest_child", "-25 -45 -109");
        catcherSpawnDayLocations.put("evil_brother", "89 -45 -64");
        catcherSpawnNightLocations.put("evil_brother", "92 -45 -45");
        catcherSpawnDayLocations.put("husband", "45 -23 -89");
        catcherSpawnNightLocations.put("husband", "-41 -24 -137");

        catcherSpawnDayLocations.put("widow", "92 -24 -136");
        catcherSpawnNightLocations.put("widow", "65 -24 -140");

        catcherSpawnDayLocations.put("scientist", "48 -45 -164");
        catcherSpawnNightLocations.put("scientist", "-92 -52 -92");

        catcherSpawnDayLocations.put("safety_inspector", "-36 -54 13");
        catcherSpawnNightLocations.put("safety_inspector", "14 -47 -349");

        catcherSpawnDayLocations.put("evil_sister", "90 -45 -78");
        catcherSpawnNightLocations.put("evil_sister", "90 -45 -78");

        catcherSpawnNightLocations.put("wife", "-36 -24 -140");
        catcherSpawnDayLocations.put("wife", "46 -23 -88");


        BukkitTask bukkitTask = new BukkitRunnable() {
            @Override
            public void run() {
                clearAllCatchers();
                System.out.println("Spawning Catchers");
                catcherRunnable = new CatcherRunnable(mainManager);

                summonAllCatchers();
            }
        }.runTaskLater(mainManager, 400); // 100 ticks = 5 seconds

    }

    public ArrayList<String> getNamesOfCatchers() {
        return namesOfCatchers;
    }

    public void clearAllCatchers() {
        ArrayList<Entity> catchersDay = getAllCatchers();
        for(Entity entity : catchersDay){
            entity.remove();
        }
    }

    public HashMap<String, String> getCatcherSpawnDayLocations() {
        return catcherSpawnDayLocations;
    }

    public HashMap<String, String> getCatcherSpawnNightLocations() {
        return catcherSpawnNightLocations;
    }

    public ArrayList<Entity> getCatchersFromName(String name){
        String type = "RATSMP2_FORGENPC_"+name.toUpperCase()+"_CATCHER";

        ArrayList<Entity> catchers = new ArrayList<>();
        for(Entity entity : mainManager.getWorld().getEntities()){
            if(type.equals(entity.getType().toString())){
                catchers.add(entity);
            }
        }
        return catchers;
    }

    public ArrayList<Entity> getAllCatchers(){

        ArrayList<Entity> catchers = new ArrayList<>();
        for(Entity entity : mainManager.getWorld().getEntities()){
            if(entity.getScoreboardTags().contains("catcher") || entity.getScoreboardTags().contains("UnprocessedCatcher")){
                catchers.add(entity);
            }
        }
        return catchers;
    }

    public void summonCatcher(String name){
        String stringToRun;
        if(mainManager.isNight()){
            stringToRun = "ratsmp2_forgenpc:" + name + "_catcher " + catcherSpawnNightLocations.get(name);
        }else{
            stringToRun = "ratsmp2_forgenpc:" + name + "_catcher " + catcherSpawnDayLocations.get(name);
        }

        //System.out.println(stringToRun);
        String[] parts = stringToRun.split(" ");
        // Parse the last three parts as x, y, z coordinates
        String type = parts[0].replace(":","_");
        int x = Integer.parseInt(parts[1]);
        int y = Integer.parseInt(parts[2]);
        int z = Integer.parseInt(parts[3]);
        Location location = new Location(mainManager.getWorld(),x,y,z);


        //mainManager.getWorld().setGameRule(GameRule.SEND_COMMAND_FEEDBACK,false);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"summon " + stringToRun);
        //mainManager.getWorld().setGameRule(GameRule.SEND_COMMAND_FEEDBACK,true);
        Entity summonedCatcher = null;
        for(Entity entity : mainManager.getWorld().getEntities()){
            if(entity.getScoreboardTags().contains("UnprocessedCatcher")){
                summonedCatcher = entity;
                break;
            }
        }

        if(summonedCatcher!=null){
            //TODO: Height of catcher set here
            //mainManager.getWorld().setGameRule(GameRule.SEND_COMMAND_FEEDBACK,false);
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "scale set pehkui:height 2 "+summonedCatcher.getUniqueId());
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "scale set pehkui:width 2 "+summonedCatcher.getUniqueId());
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "scale set pehkui:motion 0.8 "+summonedCatcher.getUniqueId());
            setHeightOfCatcher(name,summonedCatcher.getUniqueId());
            //mainManager.getWorld().setGameRule(GameRule.SEND_COMMAND_FEEDBACK,true);

            summonedCatcher.getScoreboardTags().add("catcher");
            summonedCatcher.getScoreboardTags().remove("UnprocessedCatcher");
            ((LivingEntity)summonedCatcher).addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING,-1,1,false,false));
            summonedCatcher.setInvulnerable(true);
            summonedCatcher.getScoreboardTags().add(name);
            summonedCatcher.setPersistent(true);
            switch (name){
                case "owner":
                    summonedCatcher.setCustomName("Delroy LaRue");
                    break;
                case "owners_son":
                    summonedCatcher.setCustomName("Quincy LaRue");
                    break;
                case "bach":
                    summonedCatcher.setCustomName("Carlos Serrano");
                    break;
                case "widow":
                    summonedCatcher.setCustomName("Nadine Baudet");
                    break;
                case "youngest_child":
                    summonedCatcher.setCustomName("Rosalie Baudet");
                    break;
                case "widow_oldest_child":
                    summonedCatcher.setCustomName("Vivienne Baudet");
                    break;
                case "safety_inspector":
                    summonedCatcher.setCustomName("Quain");
                    break;
                case "scientist":
                    summonedCatcher.setCustomName("Mila Marrero");
                    break;
                case "evil_brother":
                    summonedCatcher.setCustomName("Sharav");
                    break;
                case "evil_sister":
                    summonedCatcher.setCustomName("Amala");
                    break;
                case "gom":
                    summonedCatcher.setCustomName("Audric Duvall");
                    break;
                case "wife":
                    summonedCatcher.setCustomName("Ái Shikongo");
                    break;
                case "husband":
                    summonedCatcher.setCustomName("Destin Shikongo");
                    break;
                case "boilerman":
                    summonedCatcher.setCustomName("Faron");
                    break;
            }
        }else{
            System.out.println("No catcher found :(");
        }


    }

    private void setHeightOfCatcher(String name, UUID uniqueId) {
        switch (name){
            case "owners_son":
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"scale multiply pehkui:height 1.15 "+uniqueId);
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"scale multiply pehkui:width 0.95 "+uniqueId);
                break;
            case "widow":
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"scale multiply pehkui:width 0.9 "+uniqueId);
                break;
            case "youngest_child":
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"scale multiply pehkui:height 0.6 "+uniqueId);
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"scale multiply pehkui:width 0.6 "+uniqueId);
                break;
            case "widow_oldest_child":
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"scale multiply pehkui:height 0.8 "+uniqueId);
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"scale multiply pehkui:width 0.75 "+uniqueId);
                break;
            case "safety_inspector":
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"scale multiply pehkui:width 1.1 "+uniqueId);
                break;
            case "scientist":
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"scale multiply pehkui:height 0.85 "+uniqueId);
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"scale multiply pehkui:width 0.85 "+uniqueId);
                break;
            case "evil_brother":
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"scale multiply pehkui:height 1.2 "+uniqueId);
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"scale multiply pehkui:width 1.2 "+uniqueId);
                break;
            case "evil_sister":
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"scale multiply pehkui:height 1.2 "+uniqueId);
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"scale multiply pehkui:width 1.1 "+uniqueId);
                break;
            case "gom":
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"scale multiply pehkui:height 1.3 "+uniqueId);
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"scale multiply pehkui:width 1.4 "+uniqueId);
                break;
            case "wife":
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"scale multiply pehkui:height 0.9 "+uniqueId);
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"scale multiply pehkui:width 0.8 "+uniqueId);
                break;
            case "husband":
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"scale multiply pehkui:height 1.05  "+uniqueId);
                break;
            case "boilerman":
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"scale multiply pehkui:height 0.8 "+uniqueId);
                break;
        }
    }

    public void summonAllCatchers(){
        clearAllCatchers();
        for(Map.Entry<String, String> entry : catcherSpawnDayLocations.entrySet()){
            summonCatcher(entry.getKey());
        }
    }

}

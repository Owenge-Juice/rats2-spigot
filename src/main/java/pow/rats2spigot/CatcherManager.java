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
import pow.rats2spigot.util.Utility;

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
        namesOfCatchers.add("dev");
        namesOfCatchers.add("dv8fromthecode");
        namesOfCatchers.add("crissy");
        namesOfCatchers.add("candy");
        namesOfCatchers.add("dev_copy");
        namesOfCatchers.add("dv8fromthecode_copy");
        namesOfCatchers.add("crissy_copy");
        namesOfCatchers.add("candy_copy");

        catcherSpawnDayLocations.clear();
        catcherSpawnNightLocations.clear();


        catcherSpawnDayLocations.put("boilerman", "53 -55 -134");
        catcherSpawnNightLocations.put("boilerman", "53 -55 -134");
        catcherSpawnDayLocations.put("owner", "-65 -55 -40");
        catcherSpawnNightLocations.put("owner", "-65 -55 -40");
        catcherSpawnDayLocations.put("gom", "-53 -24 -36");
        catcherSpawnNightLocations.put("gom", "-58 -24 -72");
        catcherSpawnDayLocations.put("youngest_child", "73 -24 -105");
        catcherSpawnNightLocations.put("youngest_child", "65 -24 -116");
        catcherSpawnDayLocations.put("owners_son", "42 -45 -46");
        catcherSpawnNightLocations.put("owners_son", "64 -24 -45");
        catcherSpawnDayLocations.put("bach", "-35 -24 -168");
        catcherSpawnNightLocations.put("bach", "-3 -55 -46");
        catcherSpawnDayLocations.put("widow_oldest_child", "63 -35 -156");
        catcherSpawnNightLocations.put("widow_oldest_child", "63 -24 -129");
        catcherSpawnDayLocations.put("evil_brother", "89 -45 -64");
        catcherSpawnNightLocations.put("evil_brother", "82 -45 -121");
        catcherSpawnDayLocations.put("husband", "-42 -24 -76");
        catcherSpawnNightLocations.put("husband", "-41 -24 -139");

        catcherSpawnDayLocations.put("widow", "85 -24 -112");
        catcherSpawnNightLocations.put("widow", "66 -24 -140");

        catcherSpawnDayLocations.put("scientist", "-58 -24 -164");
        catcherSpawnNightLocations.put("scientist", "69 -45 -164");

        catcherSpawnDayLocations.put("safety_inspector", "-99 -52 -275");
        catcherSpawnNightLocations.put("safety_inspector", "14 -47 -349");

        catcherSpawnDayLocations.put("evil_sister", "90 -45 -78");
        catcherSpawnNightLocations.put("evil_sister", "90 -45 -78");

        catcherSpawnNightLocations.put("wife", "-39 -24 -140");
        catcherSpawnDayLocations.put("wife", "-26 -24 -83");


        catcherSpawnNightLocations.put("dev", "7 -45 -59");
        catcherSpawnDayLocations.put("dev", "-37 -44 -130");

        catcherSpawnNightLocations.put("dv8fromthecode", "-47 -24 -154");
        catcherSpawnDayLocations.put("dv8fromthecode", "63 -44 -120");

        catcherSpawnNightLocations.put("crissy", "94 -24 -153");
        catcherSpawnDayLocations.put("crissy", "13 -46 -131");

        catcherSpawnNightLocations.put("candy", "-5 -24 -43");
        catcherSpawnDayLocations.put("candy", "70 -45 -88");

        catcherSpawnNightLocations.put("dev_copy", "113 -55 -104");
        catcherSpawnDayLocations.put("dev_copy", "113 -55 -104");

        catcherSpawnNightLocations.put("dv8fromthecode_copy", "40 -55 -162");
        catcherSpawnDayLocations.put("dv8fromthecode_copy", "40 -55 -162");

        catcherSpawnNightLocations.put("crissy_copy", "-67 -55 -72");
        catcherSpawnDayLocations.put("crissy_copy", "-67 -55 -72");

        catcherSpawnNightLocations.put("candy_copy", "40 -55 -76");
        catcherSpawnDayLocations.put("candy_copy", "40 -55 -76");


        BukkitTask bukkitTask = new BukkitRunnable() {
            @Override
            public void run() {
                clearAllCatchers();
                Utility.sendMessageToAllAdmins("Spawning Catchers");
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
        String nameAfterCut = name;

        if (name.endsWith("_copy")) {
             nameAfterCut = name.substring(0, name.length() - "_copy".length());
        }

        if(mainManager.isNight()){
            stringToRun = "ratsmp2_forgenpc:" + nameAfterCut + "_catcher " + catcherSpawnNightLocations.get(name);
        }else{
            stringToRun = "ratsmp2_forgenpc:" + nameAfterCut + "_catcher " + catcherSpawnDayLocations.get(name);
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
            setHeightOfCatcher(nameAfterCut,summonedCatcher.getUniqueId());
            //mainManager.getWorld().setGameRule(GameRule.SEND_COMMAND_FEEDBACK,true);

            summonedCatcher.getScoreboardTags().add("catcher");
            summonedCatcher.getScoreboardTags().remove("UnprocessedCatcher");
            ((LivingEntity)summonedCatcher).addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING,-1,1,false,false));
            summonedCatcher.setInvulnerable(true);
            summonedCatcher.getScoreboardTags().add(nameAfterCut);
            summonedCatcher.setPersistent(true);
            switch (nameAfterCut){
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
                case "dev":
                    summonedCatcher.setCustomName("Dev");
                    break;
                case "dv8fromthecode":
                    summonedCatcher.setCustomName("Dv8FromTheCode");
                    break;
                case "crissy":
                    summonedCatcher.setCustomName("Crissy");
                    break;
                case "candy":
                    summonedCatcher.setCustomName("Candy");
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
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"scale multiply pehkui:height 1.05 "+uniqueId);
                break;
            case "boilerman":
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"scale multiply pehkui:height 0.8 "+uniqueId);
                break;
            default:
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

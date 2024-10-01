package pow.rats2spigot;

import com.google.gson.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.Powerable;
import org.bukkit.block.data.Rail;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;
import pow.rats2spigot.build.BasementDoor;
import pow.rats2spigot.build.Door;
import pow.rats2spigot.build.NarrowDoor;
import pow.rats2spigot.build.ThinDoor;
import pow.rats2spigot.commands.*;
import pow.rats2spigot.interactables.Tap;
import pow.rats2spigot.runnables.BurntRunnable;
import pow.rats2spigot.runnables.TickRunnable;
import pow.rats2spigot.util.LootChest;
import pow.rats2spigot.util.Utility;


import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public final class MainManager extends JavaPlugin implements Listener {

    private Location narrowDoorTemplate;
    private Location wideDoorTemplate;
    private Location thinDoorTemplate;
    private Location basementDoorTemplate;
    private ArrayList<Tap> taps = new ArrayList<>();
    private TickRunnable tickRunnable;
    private ArrayList<Location> stoveTops = new ArrayList<>();
    private final HashMap<Integer,Door> doorIndex = new HashMap<>();
    private final HashMap<Integer,Boolean> openIndex = new HashMap<>();
    private AmbientManager ambientManager;
    private ArrayList<LootChest> lootChests = new ArrayList<>();
    private HashMap<String,Location> chestCopyLocations = new HashMap<>();
    private HashMap<Location,String> chestPasteLocations = new HashMap<>();
    private HashMap<String,String> lastMessageSent;


    @Override
    public void onDisable() {
        ambientManager.stopAllAmbientRunnables();
    }

    @Override
    public void onEnable() {
        lastMessageSent = new HashMap<>();

        getServer().getPluginManager().registerEvents(this, this);
        // Plugin startup logic
        new test(this);
        new create_all_doors(this);
        new clear_all_doors(this);
        new open_all_doors(this);
        new close_all_doors(this);
        new toggle_all_doors(this);
        new advance_day(this);
        new reset_week(this);
        new nighttime_doors(this);
        new stove_tops_off(this);
        new stove_tops_on(this);
        new populate_all_chests(this);

        ambientManager = new AmbientManager(this);

        narrowDoorTemplate= new Location(getWorld(),26, -48, 41);
        wideDoorTemplate = new Location(getWorld(),20, -48, 41);
        thinDoorTemplate = new Location(getWorld(),17, -48, 41);
        basementDoorTemplate = new Location(getWorld(),13, -48, 41);

        tickRunnable=new TickRunnable(this);

        setUpScoreboard();
        init_doors();
        init_chests();
        init_stove_tops();
        init_taps();

        //createAllDoors();
        //turnAllStoveTopsOn();

        Utility.sendMessageToAllAdmins("Code initialised successfully");
    }

    private void init_chests() {


        chestCopyLocations.clear();

        chestCopyLocations.put("main_lobby",new Location(getWorld(),44, -63, 54));
        chestCopyLocations.put("theatre_backstage",new Location(getWorld(),44, -63, 52));
        chestCopyLocations.put("mirror_vanity",new Location(getWorld(),42, -63, 52));
        chestCopyLocations.put("pool_changing_rooms",new Location(getWorld(),44, -63, 50));
        chestCopyLocations.put("staff_room",new Location(getWorld(),44, -63, 48));
        chestCopyLocations.put("library",new Location(getWorld(),44, -63, 46));
        chestCopyLocations.put("restaurant",new Location(getWorld(),44, -63, 44));
        chestCopyLocations.put("bar",new Location(getWorld(),42, -63, 44));
        chestCopyLocations.put("kitchen",new Location(getWorld(),40, -63, 44));
        chestCopyLocations.put("fridge",new Location(getWorld(),38, -63, 44));
        chestCopyLocations.put("gom_kitchen",new Location(getWorld(),44, -63, 42));
        chestCopyLocations.put("gom_living",new Location(getWorld(),42, -63, 42));
        chestCopyLocations.put("gom_bedroom",new Location(getWorld(),40, -63, 42));
        chestCopyLocations.put("gom_bathroom",new Location(getWorld(),38, -63, 42));
        chestCopyLocations.put("love_bathroom",new Location(getWorld(),42, -63, 40));
        chestCopyLocations.put("love_bedroom",new Location(getWorld(),36, -63, 40));
        chestCopyLocations.put("scientist_bedroom",new Location(getWorld(),44, -63, 36));
        chestCopyLocations.put("scientist_bathroom",new Location(getWorld(),42, -63, 36));
        chestCopyLocations.put("scientist_living_room",new Location(getWorld(),40, -63, 36));
        chestCopyLocations.put("widow_kitchen",new Location(getWorld(),44, -63, 30));
        chestCopyLocations.put("widow_lounge",new Location(getWorld(),42, -63, 30));
        chestCopyLocations.put("widow_child",new Location(getWorld(),40, -63, 30));
        chestCopyLocations.put("widow_girl",new Location(getWorld(),38, -63, 30));
        chestCopyLocations.put("widow_mum",new Location(getWorld(),36, -63, 30));
        chestCopyLocations.put("boilerroom",new Location(getWorld(),44, -63, 20));
        chestCopyLocations.put("laundry_room",new Location(getWorld(),44, -63, 18));
        chestCopyLocations.put("bachelor_kitchen",new Location(getWorld(),44, -63, 12));
        chestCopyLocations.put("bachelor_bedroom",new Location(getWorld(),42, -63, 12));
        chestCopyLocations.put("basement_crockery",new Location(getWorld(),44, -63, 10));
        chestCopyLocations.put("basement_winery",new Location(getWorld(),44, -63, 8));
        chestCopyLocations.put("abandoned_bathroom",new Location(getWorld(),26, -63, 43));
        chestCopyLocations.put("abandoned_bedroom",new Location(getWorld(),30, -63, 43));
        chestCopyLocations.put("abandoned_living_room",new Location(getWorld(),28, -63, 43));
        chestCopyLocations.put("abandoned_kitchen",new Location(getWorld(),24, -63, 43));
        chestCopyLocations.put("food_scraps",new Location(getWorld(),30, -63, 41));
        chestCopyLocations.put("wool_chest",new Location(getWorld(),28, -63, 41));
        chestCopyLocations.put("misc",new Location(getWorld(),26, -63, 41));
        chestCopyLocations.put("basic_bathroom",new Location(getWorld(),24, -63, 41));

        lootChests.clear();
        lootChests.add(new LootChest("Kitchen1","kitchen",new Location(getWorld(),96, -40, -87),BlockFace.WEST,this));
        lootChests.add(new LootChest("Kitchen2","kitchen",new Location(getWorld(),96, -40, -89),BlockFace.WEST,this));
        lootChests.add(new LootChest("Kitchen3","kitchen",new Location(getWorld(),84, -37, -61),BlockFace.EAST,this));
        lootChests.add(new LootChest("Kitchen4","kitchen",new Location(getWorld(),-45, -33, -174) ,BlockFace.WEST,this));
        lootChests.add(new LootChest("Fridge1","fridge",new Location(getWorld(),96, -37, -47),BlockFace.WEST,this));
        lootChests.add(new LootChest("Fridge2","fridge",new Location(getWorld(),87, -39, -46) ,BlockFace.EAST,this));
        lootChests.add(new LootChest("Restaurant1","restaurant",new Location(getWorld(),87, -43, -116) ,BlockFace.WEST,this));
        lootChests.add(new LootChest("Restaurant2","restaurant",new Location(getWorld(),78, -43, -126) ,BlockFace.WEST,this));
        lootChests.add(new LootChest("Bar1","bar",new Location(getWorld(),82, -43, -144) ,BlockFace.SOUTH,this));
        lootChests.add(new LootChest("Library1","library",new Location(getWorld(),97, -39, -159) ,BlockFace.WEST,this));
        lootChests.add(new LootChest("Library2","library",new Location(getWorld(),41, -33, -175) ,BlockFace.SOUTH,this));
        lootChests.add(new LootChest("Library3","library",new Location(getWorld(),33, -43, -153) ,BlockFace.NORTH,this));
        lootChests.add(new LootChest("StaffRoom1","staff_room",new Location(getWorld(),0, -41, -162) ,BlockFace.WEST,this));
        lootChests.add(new LootChest("StaffRoom2","staff_room",new Location(getWorld(),0, -31, -168) ,BlockFace.WEST,this));
        lootChests.add(new LootChest("StaffRoom3","staff_room",new Location(getWorld(),-58, -35, -174) ,BlockFace.SOUTH,this));
        lootChests.add(new LootChest("Gom Kitchen","gom_kitchen",new Location(getWorld(),-68, -22, -44) ,BlockFace.EAST,this));
        lootChests.add(new LootChest("Pool Changing Rooms 1","pool_changing_rooms",new Location(getWorld(),-68, -42, -167) ,BlockFace.EAST,this));
        lootChests.add(new LootChest("Gom Living","gom_living",new Location(getWorld(),-40, -24, -35) ,BlockFace.WEST,this));
        lootChests.add(new LootChest("Mirror Vanity 1","mirror_vanity",new Location(getWorld(),-49, -39, -94) ,BlockFace.SOUTH,this));
        lootChests.add(new LootChest("TheaterBackstage1","theatre_backstage",new Location(getWorld(),-77, -37, -65),BlockFace.EAST,this));
        lootChests.add(new LootChest("TheaterBackstage2","theatre_backstage",new Location(getWorld(),-62, -43, -56) ,BlockFace.WEST,this));
        lootChests.add(new LootChest("TheaterBackstage3","theatre_backstage",new Location(getWorld(),-78, -41, -72) ,BlockFace.NORTH,this));
        lootChests.add(new LootChest("GomBedroom1","gom_bedroom",new Location(getWorld(),-56, -22, -84) ,BlockFace.WEST,this));
        lootChests.add(new LootChest("GomBathroom1","gom_bathroom",new Location(getWorld(),-68, -24, -65) ,BlockFace.EAST,this));
        lootChests.add(new LootChest("MainLobby1","main_lobby",new Location(getWorld(),-14, -38, -49) ,BlockFace.SOUTH,this));
        lootChests.add(new LootChest("MainLobby2","main_lobby",new Location(getWorld(),42, -44, -50) ,BlockFace.SOUTH,this));
        lootChests.add(new LootChest("Love Kitchen","food_scraps",new Location(getWorld(),-34, -22, -103),BlockFace.WEST,this));
        lootChests.add(new LootChest("Love Bathroom 1","love_bathroom",new Location(getWorld(),-36, -24, -111) ,BlockFace.EAST,this));
        lootChests.add(new LootChest("Love Bathroom 2","love_bathroom",new Location(getWorld(),-39, -24, -150) ,BlockFace.SOUTH,this));
        lootChests.add(new LootChest("Love Bathroom 3","love_bathroom",new Location(getWorld(),-24, -24, -74) ,BlockFace.WEST,this));
        lootChests.add(new LootChest("Love Bedroom 1","love_bedroom",new Location(getWorld(),-39, -23, -142) ,BlockFace.SOUTH,this));

        lootChests.add(new LootChest("Abandoned Room 1 Bathroom 1","abandoned_bathroom",new Location(getWorld(),-58, -23, -151) ,BlockFace.SOUTH,this));
        lootChests.add(new LootChest("Basement Winery 1","basement_winery",new Location(getWorld(),107, -51, -77),BlockFace.EAST,this));
        lootChests.add(new LootChest("Basement Winery 2","basement_winery",new Location(getWorld(),112, -53, -136) ,BlockFace.SOUTH,this));
        lootChests.add(new LootChest("Abandoned Room 1 Bedroom 1","abandoned_bedroom",new Location(getWorld(),-66, -22, -113) ,BlockFace.NORTH,this));
        lootChests.add(new LootChest("Basement Crockery 1","basement_crockery",new Location(getWorld(),93, -50, -83) ,BlockFace.WEST,this));
        lootChests.add(new LootChest("Abandoned Room 1 Bedroom 2","abandoned_bedroom",new Location(getWorld(),-66, -18, -131) ,BlockFace.NORTH,this));
        lootChests.add(new LootChest("Basement Crockery 2","basement_crockery",new Location(getWorld(),84, -53, -98) ,BlockFace.EAST,this));
        lootChests.add(new LootChest("Bachelor Bathroom 1","basic_bathroom",new Location(getWorld(),-28, -55, -58),BlockFace.EAST,this));
        lootChests.add(new LootChest("Bachelor Kitchen 1","bachelor_kitchen",new Location(getWorld(),-13, -53, -37) ,BlockFace.EAST,this));
        lootChests.add(new LootChest("Bachelor Bedroom 1","bachelor_bedroom",new Location(getWorld(),-28, -53, -45) ,BlockFace.EAST,this));
        lootChests.add(new LootChest("Abandoned Room 1 Living Room","abandoned_living_room",new Location(getWorld(),-66, -22, -98) ,BlockFace.NORTH,this));
        lootChests.add(new LootChest("Abandoned Room 1 Kitchen","abandoned_kitchen",new Location(getWorld(),-64, -22, -90) ,BlockFace.NORTH,this));
        lootChests.add(new LootChest("Scientist Bedroom 1","scientist_bedroom",new Location(getWorld(),-44, -19, -171) ,BlockFace.WEST,this));
        lootChests.add(new LootChest("Owners Bedroom 1","wool_chest",new Location(getWorld(),-69, -53, -32),BlockFace.NORTH,this));
        lootChests.add(new LootChest("Scientist Bedroom 2 ","scientist_bedroom",new Location(getWorld(),-47, -22, -162) ,BlockFace.NORTH,this));
        lootChests.add(new LootChest("Owners Bedroom 2 ","wool_chest",new Location(getWorld(),-48, -53, -32) ,BlockFace.WEST,this));
        lootChests.add(new LootChest("Scientist Living Room 1","scientist_living_room",new Location(getWorld(),-69, -22, -173) ,BlockFace.EAST,this));
        lootChests.add(new LootChest("Scientist Living Room 2","scientist_living_room",new Location(getWorld(),-55, -22, -171),BlockFace.WEST,this));
        lootChests.add(new LootChest("Scientist Bathroom 1","scientist_bathroom",new Location(getWorld(),-69, -24, -157),BlockFace.EAST,this));
        lootChests.add(new LootChest("Owners Bathroom 1 ","basic_bathroom",new Location(getWorld(),-69, -55, -60) ,BlockFace.EAST,this));
        lootChests.add(new LootChest("Owners Bathroom 2","basic_bathroom",new Location(getWorld(),-48, -55, -60) ,BlockFace.SOUTH,this));
        lootChests.add(new LootChest("Owners Kitchen 1","food_scraps",new Location(getWorld(),-36, -53, -32),BlockFace.NORTH,this));
        lootChests.add(new LootChest("Storage Rack 1","misc",new Location(getWorld(),-17, -52, -69) ,BlockFace.SOUTH,this));
        lootChests.add(new LootChest("Storage Rack 2","misc",new Location(getWorld(),34, -52, -72) ,BlockFace.NORTH,this));
        lootChests.add(new LootChest("Abandoned Room 2 Kitchen 1","abandoned_kitchen",new Location(getWorld(),56, -22, -162) ,BlockFace.WEST,this));
        lootChests.add(new LootChest("Storage Rack 3 ","misc",new Location(getWorld(),-68, -50, -155) ,BlockFace.SOUTH,this));
        lootChests.add(new LootChest("Abadoned Room 2 Bedroom 1","abandoned_bedroom",new Location(getWorld(),-12, -23, -160) ,BlockFace.EAST,this));
        lootChests.add(new LootChest("Laundry Room 1 ","laundry_room",new Location(getWorld(),78, -55, -90) ,BlockFace.WEST ,this));
        lootChests.add(new LootChest("Abandoned Room 2 Bathroom 1","abandoned_bathroom",new Location(getWorld(),-25, -24, -162) ,BlockFace.NORTH,this));
        lootChests.add(new LootChest("Laundry Room 1","laundry_room",new Location(getWorld(),47, -55, -98) ,BlockFace.SOUTH,this));
        lootChests.add(new LootChest("Laundry Room 1","laundry_room",new Location(getWorld(),78, -49, -76) ,BlockFace.WEST,this));
        lootChests.add(new LootChest("Abandoned Room 3 Bathroom 1","abandoned_bathroom",new Location(getWorld(),68, -24, -171) ,BlockFace.EAST,this));
        lootChests.add(new LootChest("Abandoned Room 3 Bedroom 1","abandoned_bedroom",new Location(getWorld(),88, -22, -169) ,BlockFace.WEST,this));
        lootChests.add(new LootChest("ES Kitchen 1","food_scraps",new Location(getWorld(),68, -53, -173) ,BlockFace.EAST,this));
        lootChests.add(new LootChest("Abandoned Room 3 Kitchen 1","abandoned_kitchen",new Location(getWorld(),91, -22, -171) ,BlockFace.EAST ,this));
        lootChests.add(new LootChest("ES Bathroom 1","basic_bathroom",new Location(getWorld(),80, -55, -171) ,BlockFace.SOUTH,this));
        lootChests.add(new LootChest("ES Bedroom 1","wool_chest",new Location(getWorld(),51, -53, -177),BlockFace.SOUTH,this));
        lootChests.add(new LootChest("ES Bedroom 2","wool_chest",new Location(getWorld(),65, -53, -177) ,BlockFace.SOUTH,this));
        lootChests.add(new LootChest("Widow Kitchen","widow_kitchen",new Location(getWorld(),78, -22, -110) ,BlockFace.EAST,this));
        lootChests.add(new LootChest("Basement Storage 1","misc",new Location(getWorld(),27, -53, -121) ,BlockFace.WEST,this));
        lootChests.add(new LootChest("Basement Storage 2","misc",new Location(getWorld(),6, -52, -72) ,BlockFace.NORTH,this));
        lootChests.add(new LootChest("Widow Lounge 1","widow_lounge",new Location(getWorld(),87, -21, -99) ,BlockFace.WEST,this));
        lootChests.add(new LootChest("Widow Lounge 2","widow_lounge",new Location(getWorld(),61, -22, -96) ,BlockFace.NORTH,this));
        lootChests.add(new LootChest("Dumpster Shed 1 ","misc",new Location(getWorld(),-86, -49, -137) ,BlockFace.NORTH,this));
        lootChests.add(new LootChest("Green Kitchen 1","food_scraps",new Location(getWorld(),80, -18, -93) ,BlockFace.EAST,this));
        lootChests.add(new LootChest("Green Bedroom 1","wool_chest",new Location(getWorld(),66, -23, -85) ,BlockFace.NORTH,this));
        lootChests.add(new LootChest("Widow Girl Bedroom 1","widow_girl",new Location(getWorld(),61, -24, -118) ,BlockFace.SOUTH,this));
        lootChests.add(new LootChest("Widow Girl Bedroom 2","widow_girl",new Location(getWorld(),62, -22, -105),BlockFace.NORTH,this));
        lootChests.add(new LootChest("Widow Older Child Bedroom 1","widow_child",new Location(getWorld(),63, -22, -121) ,BlockFace.NORTH,this));
        lootChests.add(new LootChest("Widow Mum Bedroom 1","widow_mum",new Location(getWorld(),61, -22, -149),BlockFace.EAST,this));
        lootChests.add(new LootChest("Main Lobby 1","main_lobby",new Location(getWorld(),-2, -43, -71) ,BlockFace.EAST,this));
        lootChests.add(new LootChest("Main Lobby 2","main_lobby",new Location(getWorld(),30, -43, -53) ,BlockFace.NORTH,this));

        chestPasteLocations.clear();
        /*
        chestPasteLocations.put(new Location(getWorld(),96, -40, -87),"kitchen");
        chestPasteLocations.put(new Location(getWorld(),96, -40, -89),"kitchen");
        chestPasteLocations.put(new Location(getWorld(),84, -37, -61),"kitchen");
        chestPasteLocations.put(new Location(getWorld(),-45, -33, -174) ,"kitchen");
        chestPasteLocations.put(new Location(getWorld(),96, -37, -47),"fridge");
        chestPasteLocations.put(new Location(getWorld(),96, -37, -47),"fridge");
        chestPasteLocations.put(new Location(getWorld(),87, -43, -116) ,"restaurant");
        chestPasteLocations.put(new Location(getWorld(),78, -43, -126) ,"restaurant");
        chestPasteLocations.put(new Location(getWorld(),82, -43, -144) ,"bar");
        chestPasteLocations.put(new Location(getWorld(),97, -39, -159) ,"library");
        chestPasteLocations.put(new Location(getWorld(),41, -33, -175) ,"library");
        chestPasteLocations.put(new Location(getWorld(),33, -43, -153) ,"library");
        chestPasteLocations.put(new Location(getWorld(),0, -41, -162) ,"staff_room");
        chestPasteLocations.put(new Location(getWorld(),0, -31, -168) ,"staff_room");
        chestPasteLocations.put(new Location(getWorld(),-58, -35, -174) ,"staff_room");
        chestPasteLocations.put(new Location(getWorld(),-68, -22, -44) ,"gom_kitchen");
        chestPasteLocations.put(new Location(getWorld(),-40, -24, -35) ,"gom_living");
        chestPasteLocations.put(new Location(getWorld(),-56, -22, -84) ,"gom_bedroom");
        chestPasteLocations.put(new Location(getWorld(),-68, -24, -65) ,"gom_bathroom");
        chestPasteLocations.put(new Location(getWorld(),-49, -39, -94) ,"mirror_vanity");
        chestPasteLocations.put(new Location(getWorld(),-77, -37, -65) ,"theatre_backstage");
        chestPasteLocations.put(new Location(getWorld(),-62, -43, -56) ,"theatre_backstage");
        chestPasteLocations.put(new Location(getWorld(),-78, -41, -72) ,"theatre_backstage");
        chestPasteLocations.put(new Location(getWorld(),-68, -42, -167) ,"pool_changing_rooms");
        chestPasteLocations.put(new Location(getWorld(),-14, -38, -49) ,"main_lobby");
        chestPasteLocations.put(new Location(getWorld(),42, -44, -50)  ,"main_lobby");
        chestPasteLocations.put(new Location(getWorld(),-34, -22, -103) ,"food_scraps");
        chestPasteLocations.put(new Location(getWorld(),-36, -24, -111) ,"love_bathroom");
        chestPasteLocations.put(new Location(getWorld(),-39, -24, -150) ,"love_bathroom");
        chestPasteLocations.put(new Location(getWorld(),-24, -24, -74) ,"love_bathroom");
        chestPasteLocations.put(new Location(getWorld(),-39, -23, -142) ,"love_bedroom");
        chestPasteLocations.put(new Location(getWorld(),-58, -23, -151) ,"abandoned_bathroom");
        chestPasteLocations.put(new Location(getWorld(),107, -51, -77) ,"basement_winery");
        chestPasteLocations.put(new Location(getWorld(),112, -53, -136) ,"basement_winery");
        chestPasteLocations.put(new Location(getWorld(),-66, -22, -113) ,"abandoned_bedroom");
        chestPasteLocations.put(new Location(getWorld(),-66, -18, -131)  ,"abandoned_bedroom");
        chestPasteLocations.put(new Location(getWorld(),93, -50, -83) ,"basement_crockery");
        chestPasteLocations.put(new Location(getWorld(),84, -53, -98) ,"basement_crockery");
        chestPasteLocations.put(new Location(getWorld(),-28, -55, -58) ,"basic_bathroom");
        chestPasteLocations.put(new Location(getWorld(),-13, -53, -37) ,"bachelor_kitchen");
        chestPasteLocations.put(new Location(getWorld(),-28, -53, -45) ,"bachelor_bedroom");
        chestPasteLocations.put(new Location(getWorld(),-66, -22, -98) ,"abandoned_living_room");
        chestPasteLocations.put(new Location(getWorld(),-64, -22, -90) ,"abandoned_kitchen");
        chestPasteLocations.put(new Location(getWorld(),-44, -19, -171) ,"scientist_bedroom");
        chestPasteLocations.put(new Location(getWorld(),-69, -53, -32) ,"wool_chest");
        chestPasteLocations.put(new Location(getWorld(),-48, -53, -32) ,"wool_chest");
        chestPasteLocations.put(new Location(getWorld(),-47, -22, -162) ,"scientist_bedroom");
        chestPasteLocations.put(new Location(getWorld(),-69, -22, -173) ,"scientist_living_room");
        chestPasteLocations.put(new Location(getWorld(),-55, -22, -171) ,"scientist_living_room");
        chestPasteLocations.put(new Location(getWorld(),-69, -24, -157) ,"scientist_bathroom");
        chestPasteLocations.put(new Location(getWorld(),-69, -55, -60) ,"basic_bathroom");
        chestPasteLocations.put(new Location(getWorld(),-48, -55, -60) ,"basic_bathroom");
        chestPasteLocations.put(new Location(getWorld(),-36, -53, -32) ,"food_scraps");
        chestPasteLocations.put(new Location(getWorld(),-17, -52, -69) ,"misc");
        chestPasteLocations.put(new Location(getWorld(),34, -52, -72) ,"misc");
        chestPasteLocations.put(new Location(getWorld(),-68, -50, -155) ,"misc");
        chestPasteLocations.put(new Location(getWorld(),56, -22, -162) ,"abandoned_kitchen");
        chestPasteLocations.put(new Location(getWorld(),-12, -23, -160) ,"abandoned_bedroom");
        chestPasteLocations.put(new Location(getWorld(),-25, -24, -162) ,"abandoned_bathroom");
        chestPasteLocations.put(new Location(getWorld(),78, -55, -90) ,"laundry_room");
        chestPasteLocations.put(new Location(getWorld(),47, -55, -98) ,"laundry_room");
        chestPasteLocations.put(new Location(getWorld(),78, -49, -76) ,"laundry_room");
        chestPasteLocations.put(new Location(getWorld(),68, -24, -171) ,"abandoned_bathroom");
        chestPasteLocations.put(new Location(getWorld(),88, -22, -169) ,"abandoned_bedroom");
        chestPasteLocations.put(new Location(getWorld(),68, -53, -173) ,"food_scraps");
        chestPasteLocations.put(new Location(getWorld(),91, -22, -171) ,"abandoned_kitchen");
        chestPasteLocations.put(new Location(getWorld(),80, -55, -171) ,"basic_bathroom");
        chestPasteLocations.put(new Location(getWorld(),51, -53, -177) ,"wool_chest");
        chestPasteLocations.put(new Location(getWorld(),65, -53, -177) ,"wool_chest");
        chestPasteLocations.put(new Location(getWorld(),78, -22, -110) ,"widow_kitchen");
        chestPasteLocations.put(new Location(getWorld(),27, -53, -121) ,"misc");
        chestPasteLocations.put(new Location(getWorld(),6, -52, -72) ,"misc");
        chestPasteLocations.put(new Location(getWorld(),87, -21, -99) ,"widow_lounge");
        chestPasteLocations.put(new Location(getWorld(),61, -22, -96) ,"widow_lounge");
        chestPasteLocations.put(new Location(getWorld(),-86, -49, -137) ,"misc");
        chestPasteLocations.put(new Location(getWorld(),80, -18, -93) ,"food_scraps");
        chestPasteLocations.put(new Location(getWorld(),66, -23, -85) ,"wool_chest");
        chestPasteLocations.put(new Location(getWorld(),61, -24, -118) ,"widow_girl");
        chestPasteLocations.put(new Location(getWorld(),62, -22, -105) ,"widow_girl");
        chestPasteLocations.put(new Location(getWorld(),63, -22, -121) ,"widow_child");
        chestPasteLocations.put(new Location(getWorld(),61, -22, -149) ,"widow_mum");
        chestPasteLocations.put(new Location(getWorld(),-2, -43, -71) ,"main_lobby");
        chestPasteLocations.put(new Location(getWorld(),30, -43, -53) ,"main_lobby");*/
    }

    public HashMap<String, Location> getChestCopyLocations() {
        return chestCopyLocations;
    }

    public void populateAllChests(){
        for(LootChest lootChest : lootChests){
            lootChest.populateChest();
        }

    }

    public ItemStack[] getItemStackFromStack(Location copyLocation) {
        ArrayList<Location> stackLocations = new ArrayList<>();
        Location tempLocation = copyLocation.clone();
        stackLocations.add(tempLocation);
        Location newLocation = tempLocation.clone();
        newLocation.add(0,1,0);

        while(newLocation.getBlock().getType().equals(Material.CHEST)){
            stackLocations.add(newLocation);
            newLocation=newLocation.clone();
            newLocation.add(0,1,0);
        }

        Random random = new Random();
        int randomIndex = random.nextInt(stackLocations.size());
        Block block = stackLocations.get(randomIndex).getBlock();

        Container container = (Container) block.getState();
        return container.getInventory().getContents();
    }


    private void setUpScoreboard() {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        if(scoreboard.getObjective("config")==null){
            scoreboard.registerNewObjective("config",Criteria.DUMMY,"config");
            Objective objective = scoreboard.getObjective("config");
            objective.getScore("day").setScore(0);
            objective.getScore("week1").setScore(0);
            objective.getScore("nighttime").setScore(0);
        }
        if(scoreboard.getObjective("taps")==null){
            scoreboard.registerNewObjective("taps",Criteria.DUMMY,"taps");

        }
    }

    private void init_taps() {
        for(Entity entity : getWorld().getEntities()){
            if(entity instanceof Interaction){
                entity.remove();
            }else if(entity instanceof BlockDisplay){
                entity.remove();
            }else if(entity instanceof ItemDisplay){
                entity.remove();
            }
        }


        taps.add(new Tap(new Location(getWorld(),71, -23, -87),BlockFace.WEST,new Location(getWorld(),72, -20, -87),this));
        taps.add(new Tap(new Location(getWorld(),71, -23, -90),BlockFace.WEST,new Location(getWorld(),72, -20, -90),this));
        taps.add(new Tap(new Location(getWorld(),71, -23, -93),BlockFace.WEST,new Location(getWorld(),72, -20, -93),this));
        taps.add(new Tap(new Location(getWorld(),77, -23, -93),BlockFace.EAST,new Location(getWorld(),76, -20, -93),this));
        taps.add(new Tap(new Location(getWorld(),77, -23, -90),BlockFace.EAST,new Location(getWorld(),76, -20, -90),this));
        taps.add(new Tap(new Location(getWorld(),77, -23, -87),BlockFace.EAST,new Location(getWorld(),76, -20, -87),this));




        taps.add(new Tap(new Location(getWorld(),43, 69, -152),BlockFace.NORTH,new Location(getWorld(),43, 71, -152),this));
        taps.add(new Tap(new Location(getWorld(),44, 69, -151),BlockFace.EAST,new Location(getWorld(),44, 71,-151),this));
        taps.add(new Tap(new Location(getWorld(),43, 69, -150),BlockFace.SOUTH,new Location(getWorld(),43, 71, -150),this));
        taps.add(new Tap(new Location(getWorld(),42, 69, -151),BlockFace.WEST,new Location(getWorld(),42, 71, -151),this));

    }

    public Tap getTapFromInteraction(Interaction interaction){
        for(Tap tap : taps){
            if(tap.getInteraction()==interaction){
                return tap;
            }
        }
        return null;
    }

    public void turnAllStoveTopsOn() {
        for(Location location : stoveTops){
            Block block = location.getBlock();
            block.setType(Material.DETECTOR_RAIL);
            Rail rail = (Rail) block.getBlockData();
            rail.setShape(Rail.Shape.NORTH_SOUTH);
            Powerable powerable = (Powerable) rail;
            powerable.setPowered(true);
            block.setBlockData(rail);
        }
    }

    public void turnAllStoveTopsOff() {
        for(Location location : stoveTops){
            Block block = location.getBlock();
            block.setType(Material.DETECTOR_RAIL);
            Rail rail = (Rail) block.getBlockData();
            rail.setShape(Rail.Shape.NORTH_SOUTH);
            Powerable powerable = (Powerable) rail;
            powerable.setPowered(false);
            block.setBlockData(rail);
        }
    }



    public World getWorld() {
        if(Bukkit.getServer().getWorld("Rats2")!=null){
            return Bukkit.getServer().getWorld("Rats2");
        }else{
            return Bukkit.getServer().getWorld("world");
        }
    }

    private void init_stove_tops(){
        stoveTops.add(new Location(getWorld(),92, -43, -87));
        stoveTops.add(new Location(getWorld(),91, -43, -88));
        stoveTops.add(new Location(getWorld(),92, -43, -88));
        stoveTops.add(new Location(getWorld(),88, -43, -88));
        stoveTops.add(new Location(getWorld(),89, -43, -88));
        stoveTops.add(new Location(getWorld(),89, -43, -87));
        stoveTops.add(new Location(getWorld(),88, -43, -87));
        stoveTops.add(new Location(getWorld(),89, -43, -82));
        stoveTops.add(new Location(getWorld(),88, -43, -82));
        stoveTops.add(new Location(getWorld(),89, -43, -83));
        stoveTops.add(new Location(getWorld(),88, -43, -83));
        stoveTops.add(new Location(getWorld(),92, -43, -82));
        stoveTops.add(new Location(getWorld(),91, -43, -82));
        stoveTops.add(new Location(getWorld(),91, -43, -83));
        stoveTops.add(new Location(getWorld(),92, -43, -83));
        stoveTops.add(new Location(getWorld(),91, -43, -87));
    }

    private void init_doors(){
        doorIndex.clear();
        doorIndex.put(0,new NarrowDoor(new Location(getWorld(),-22, -45, -41),BlockFace.WEST,true,false,this));
        doorIndex.put(1,new NarrowDoor(new Location(getWorld(),-19, -45, -57),BlockFace.WEST,true,true,this));
        doorIndex.put(2,new NarrowDoor(new Location(getWorld(),-19, -45, -73),BlockFace.WEST,true,false,this));
        doorIndex.put(3,new NarrowDoor(new Location(getWorld(),-11, -45, -88),BlockFace.NORTH,true,false,this));
        doorIndex.put(4,new NarrowDoor(new Location(getWorld(),5, -45, -88),BlockFace.NORTH,true,false,this));
        doorIndex.put(5,new NarrowDoor(new Location(getWorld(),21, -45, -88),BlockFace.NORTH,true,false,this));
        doorIndex.put(6,new NarrowDoor(new Location(getWorld(),37, -45, -88),BlockFace.NORTH,true,false,this));
        doorIndex.put(7,new NarrowDoor(new Location(getWorld(),45, -45, -75),BlockFace.EAST,true,false,this));
        doorIndex.put(8,new NarrowDoor(new Location(getWorld(),45, -45, -59),BlockFace.EAST,true,false,this));
        doorIndex.put(9,new NarrowDoor(new Location(getWorld(),95, -45, -97),BlockFace.SOUTH,true,false,this));
        doorIndex.put(10,new NarrowDoor(new Location(getWorld(),87, -45, -97),BlockFace.SOUTH,true,true,this));
        doorIndex.put(11,new NarrowDoor(new Location(getWorld(),69, -45, -95),BlockFace.NORTH,true,false,this));
        doorIndex.put(12,new NarrowDoor(new Location(getWorld(),91, -45, -144),BlockFace.NORTH,true,false,this));
        doorIndex.put(13,new NarrowDoor(new Location(getWorld(),49, -45, -91),BlockFace.EAST,true,false,this));
        doorIndex.put(14,new NarrowDoor(new Location(getWorld(),53, -45, -94),BlockFace.NORTH,true,false,this));
        doorIndex.put(15,new NarrowDoor(new Location(getWorld(),60, -45, -123),BlockFace.WEST,true,false,this));
        doorIndex.put(16,new NarrowDoor(new Location(getWorld(),61, -45, -152),BlockFace.NORTH,true,false,this));
        doorIndex.put(17,new NarrowDoor(new Location(getWorld(),53, -45, -152),BlockFace.NORTH,true,false,this));
        doorIndex.put(18,new NarrowDoor(new Location(getWorld(),-3, -45, -95),BlockFace.NORTH,true,false,this));
        doorIndex.put(19,new NarrowDoor(new Location(getWorld(),13, -45, -95),BlockFace.NORTH,true,false,this));
        doorIndex.put(20,new NarrowDoor(new Location(getWorld(),29, -45, -95),BlockFace.NORTH,true,false,this));
        doorIndex.put(21,new NarrowDoor(new Location(getWorld(),-20, -45, -89),BlockFace.WEST,true,false,this));
        doorIndex.put(22,new NarrowDoor(new Location(getWorld(),-42, -45, -91),BlockFace.WEST,true,false,this));
        doorIndex.put(23,new NarrowDoor(new Location(getWorld(),-63, -45, -91),BlockFace.WEST,true,false,this));
        doorIndex.put(24,new NarrowDoor(new Location(getWorld(),-42, -45, -99),BlockFace.WEST,true,false,this));
        doorIndex.put(25,new NarrowDoor(new Location(getWorld(),-42, -45, -147),BlockFace.WEST,true,false,this));
        doorIndex.put(26,new NarrowDoor(new Location(getWorld(),-48, -45, -144),BlockFace.NORTH,true,false,this));
        doorIndex.put(27,new NarrowDoor(new Location(getWorld(),-48, -45, -160),BlockFace.NORTH,true,false,this));
        doorIndex.put(28,new NarrowDoor(new Location(getWorld(),-55, -45, -38),BlockFace.WEST,true,false,this));
        doorIndex.put(29,new NarrowDoor(new Location(getWorld(),-55, -45, -76),BlockFace.WEST,true,true,this));
        doorIndex.put(30,new NarrowDoor(new Location(getWorld(),-31, -45, -157),BlockFace.EAST,true,false,this));
        doorIndex.put(31,new NarrowDoor(new Location(getWorld(),-21, -45, -155),BlockFace.WEST,true,false,this));
        doorIndex.put(32,new NarrowDoor(new Location(getWorld(),-23, -45, -168),BlockFace.SOUTH,true,true,this));
        doorIndex.put(33,new NarrowDoor(new Location(getWorld(),-31, -45, -163),BlockFace.WEST,true,true,this));

        doorIndex.put(34,new NarrowDoor(new Location(getWorld(),-41, -24, -53),BlockFace.SOUTH,true,false,this));
        doorIndex.put(35,new NarrowDoor(new Location(getWorld(),-49, -24, -46),BlockFace.SOUTH,true,false,this));
        doorIndex.put(36,new NarrowDoor(new Location(getWorld(),-59, -24, -54),BlockFace.NORTH,true,false,this));
        doorIndex.put(37,new NarrowDoor(new Location(getWorld(),-62, -24, -67),BlockFace.WEST,true,true,this));
        doorIndex.put(38,new NarrowDoor(new Location(getWorld(),-59, -24, -72),BlockFace.NORTH,true,false,this));
        doorIndex.put(39,new NarrowDoor(new Location(getWorld(),-23, -24, -65),BlockFace.WEST,true,false,this));
        doorIndex.put(40,new NarrowDoor(new Location(getWorld(),-43, -24, -79),BlockFace.NORTH,true,false,this));
        doorIndex.put(41,new NarrowDoor(new Location(getWorld(),-25, -24, -79),BlockFace.SOUTH,true,false,this));
        doorIndex.put(42,new NarrowDoor(new Location(getWorld(),-39, -24, -117),BlockFace.EAST,true,false,this));
        doorIndex.put(43,new NarrowDoor(new Location(getWorld(),-43, -24, -128),BlockFace.NORTH,true,true,this));
        doorIndex.put(44,new NarrowDoor(new Location(getWorld(),-36, -24, -144),BlockFace.NORTH,true,false,this));
        doorIndex.put(45,new NarrowDoor(new Location(getWorld(),-55, -24, -115),BlockFace.WEST,true,false,this));
        doorIndex.put(46,new NarrowDoor(new Location(getWorld(),-57, -24, -111),BlockFace.SOUTH,true,true,this));
        doorIndex.put(47,new NarrowDoor(new Location(getWorld(),-57, -24, -96),BlockFace.SOUTH,true,false,this));
        doorIndex.put(48,new NarrowDoor(new Location(getWorld(),-62, -24, -119),BlockFace.WEST,true,false,this));
        doorIndex.put(49,new NarrowDoor(new Location(getWorld(),-62, -24, -135),BlockFace.WEST,true,true,this));
        doorIndex.put(50,new NarrowDoor(new Location(getWorld(),-59, -24, -145),BlockFace.NORTH,true,false,this));
        doorIndex.put(51,new NarrowDoor(new Location(getWorld(),-55, -24, -155),BlockFace.WEST,true,false,this));
        doorIndex.put(52,new NarrowDoor(new Location(getWorld(),-53, -24, -165),BlockFace.EAST,true,true,this));
        doorIndex.put(53,new NarrowDoor(new Location(getWorld(),-38, -24, -163),BlockFace.WEST,true,true,this));
        doorIndex.put(54,new NarrowDoor(new Location(getWorld(),19, -24, -166),BlockFace.SOUTH,true,true,this));
        doorIndex.put(55,new NarrowDoor(new Location(getWorld(),25, -24, -161),BlockFace.EAST,true,true,this));
        doorIndex.put(56,new NarrowDoor(new Location(getWorld(),-1, -24, -161),BlockFace.SOUTH,true,true,this));
        doorIndex.put(57,new NarrowDoor(new Location(getWorld(),-17, -24, -161),BlockFace.SOUTH,true,false,this));
        doorIndex.put(58,new NarrowDoor(new Location(getWorld(),67, -24, -165),BlockFace.EAST,true,false,this));
        doorIndex.put(59,new NarrowDoor(new Location(getWorld(),81, -24, -168),BlockFace.NORTH,true,false,this));
        doorIndex.put(60,new NarrowDoor(new Location(getWorld(),87, -24, -165),BlockFace.EAST,true,false,this));
        doorIndex.put(61,new NarrowDoor(new Location(getWorld(),90, -24, -131),BlockFace.WEST,true,false,this));
        doorIndex.put(62,new NarrowDoor(new Location(getWorld(),84, -24, -136),BlockFace.NORTH,true,false,this));
        doorIndex.put(63,new NarrowDoor(new Location(getWorld(),86, -24, -128),BlockFace.SOUTH,true,true,this));
        doorIndex.put(64,new NarrowDoor(new Location(getWorld(),84, -24, -108),BlockFace.NORTH,true,false,this));
        doorIndex.put(65,new NarrowDoor(new Location(getWorld(),69, -24, -115),BlockFace.WEST,true,true,this));
        doorIndex.put(66,new NarrowDoor(new Location(getWorld(),69, -24, -131),BlockFace.WEST,true,true,this));
        doorIndex.put(67,new NarrowDoor(new Location(getWorld(),72, -24, -136),BlockFace.NORTH,true,false,this));
        doorIndex.put(68,new NarrowDoor(new Location(getWorld(),89, -24, -79),BlockFace.WEST,true,false,this));
        doorIndex.put(69,new NarrowDoor(new Location(getWorld(),62, -24, -84),BlockFace.NORTH,true,false,this));
        doorIndex.put(70,new NarrowDoor(new Location(getWorld(),58, -24, -81),BlockFace.EAST,true,false,this));
        doorIndex.put(71,new NarrowDoor(new Location(getWorld(),85, -24, -52),BlockFace.NORTH,true,false,this));openIndex.put(0,true);
        //Thin doors:
        doorIndex.put(72,new ThinDoor(new Location(getWorld(),68, -45, -61),BlockFace.NORTH,true,false,this));
        doorIndex.put(73,new ThinDoor(new Location(getWorld(),66, -45, -66),BlockFace.WEST,true,false,this));
        doorIndex.put(74,new ThinDoor(new Location(getWorld(),82, -45, -66),BlockFace.WEST,true,false,this));
        doorIndex.put(75,new ThinDoor(new Location(getWorld(),80, -45, -64),BlockFace.SOUTH,true,false,this));
        doorIndex.put(76,new ThinDoor(new Location(getWorld(),67, -24, -66),BlockFace.WEST,true,true,this));
        doorIndex.put(77,new ThinDoor(new Location(getWorld(),78, -24, -76),BlockFace.SOUTH,true,true,this));
        doorIndex.put(78,new ThinDoor(new Location(getWorld(),71, -24, -76),BlockFace.SOUTH,true,false,this));
        //basement door:
        doorIndex.put(79,new BasementDoor(new Location(getWorld(),-71, -52, -101),BlockFace.EAST,true,false,this));
        doorIndex.put(80,new BasementDoor(new Location(getWorld(),-71, -52, -109),BlockFace.EAST,true,false,this));
        doorIndex.put(81,new BasementDoor(new Location(getWorld(),-71, -52, -117),BlockFace.EAST,true,false,this));
        doorIndex.put(82,new BasementDoor(new Location(getWorld(),-71, -52, -125),BlockFace.EAST,true,false,this));
        doorIndex.put(83,new BasementDoor(new Location(getWorld(),-71, -52, -133),BlockFace.EAST,true,false,this));
        doorIndex.put(84,new BasementDoor(new Location(getWorld(),-55, -55, -151),BlockFace.EAST,true,false,this));
        doorIndex.put(85,new BasementDoor(new Location(getWorld(),-38, -55, -157),BlockFace.SOUTH,true,false,this));
        doorIndex.put(86,new BasementDoor(new Location(getWorld(),-55, -55, -162),BlockFace.EAST,true,false,this));
        doorIndex.put(87,new BasementDoor(new Location(getWorld(),-27, -55, -157),BlockFace.SOUTH,true,false,this));
        doorIndex.put(88,new BasementDoor(new Location(getWorld(),37, -55, -165),BlockFace.NORTH,true,false,this));
        doorIndex.put(89,new BasementDoor(new Location(getWorld(),39, -55, -157),BlockFace.SOUTH,true,false,this));
        doorIndex.put(90,new BasementDoor(new Location(getWorld(),25, -55, -157),BlockFace.SOUTH,true,false,this));
        doorIndex.put(91,new BasementDoor(new Location(getWorld(),71, -55, -108),BlockFace.NORTH,true,false,this));
        doorIndex.put(92,new BasementDoor(new Location(getWorld(),73, -55, -100),BlockFace.SOUTH,true,false,this));
        doorIndex.put(93,new BasementDoor(new Location(getWorld(),102, -55, -105),BlockFace.EAST,true,false,this));
        doorIndex.put(94,new BasementDoor(new Location(getWorld(),112, -55, -59),BlockFace.NORTH,true,false,this));
        doorIndex.put(95,new BasementDoor(new Location(getWorld(),99, -55, -100),BlockFace.SOUTH,true,false,this));
        doorIndex.put(96,new BasementDoor(new Location(getWorld(),88, -55, -69),BlockFace.NORTH,true,false,this));
        doorIndex.put(97,new BasementDoor(new Location(getWorld(),65, -55, -76),BlockFace.EAST,true,false,this));
        doorIndex.put(98,new BasementDoor(new Location(getWorld(),44, -55, -76),BlockFace.EAST,true,false,this));
        doorIndex.put(99,new BasementDoor(new Location(getWorld(),-1, -55, -62),BlockFace.SOUTH,true,false,this));
        doorIndex.put(100,new BasementDoor(new Location(getWorld(),-41, -55, -62),BlockFace.SOUTH,true,true,this));
        doorIndex.put(101,new BasementDoor(new Location(getWorld(),36, -55, -74),BlockFace.WEST,true,false,this));
        doorIndex.put(102,new BasementDoor(new Location(getWorld(),-1, -55, -71),BlockFace.SOUTH,true,false,this));
        doorIndex.put(103,new BasementDoor(new Location(getWorld(),45, -55, -162),BlockFace.EAST,true,false,this));

        //Ground floor
        openIndex.put(1,false);
        openIndex.put(2,true);
        openIndex.put(3,false);
        openIndex.put(4,true);
        openIndex.put(5,false);
        openIndex.put(6,true);
        openIndex.put(7,false);
        openIndex.put(8,true);
        openIndex.put(9,false);
        openIndex.put(10,true);
        openIndex.put(11,false);
        openIndex.put(12,true);
        openIndex.put(13,false);
        openIndex.put(14,true);
        openIndex.put(15,false);
        openIndex.put(16,true);
        openIndex.put(17,false);
        openIndex.put(18,true);
        openIndex.put(19,false);
        openIndex.put(20,true);
        openIndex.put(21,false);
        openIndex.put(22,true);
        openIndex.put(23,false);
        openIndex.put(24,true);
        openIndex.put(25,false);
        openIndex.put(26,true);
        openIndex.put(27,false);
        openIndex.put(28,true);
        openIndex.put(29,false);
        openIndex.put(30,true);
        openIndex.put(31,false);
        openIndex.put(32,true);
        openIndex.put(33,false);
        //1st floor
        openIndex.put(34,false);
        openIndex.put(35,false);
        openIndex.put(36,false);
        openIndex.put(37,false);
        openIndex.put(38,false);
        openIndex.put(39,false);
        openIndex.put(40,false);
        openIndex.put(41,false);
        openIndex.put(42,false);
        openIndex.put(43,false);
        openIndex.put(44,false);
        openIndex.put(45,false);
        openIndex.put(46,false);
        openIndex.put(47,false);
        openIndex.put(48,false);
        openIndex.put(49,false);
        openIndex.put(50,false);
        openIndex.put(51,false);
        openIndex.put(52,false);
        openIndex.put(53,false);
        openIndex.put(54,false);
        openIndex.put(55,false);
        openIndex.put(56,false);
        openIndex.put(57,false);
        openIndex.put(58,false);
        openIndex.put(59,false);
        openIndex.put(60,false);
        openIndex.put(61,false);
        openIndex.put(62,false);
        openIndex.put(63,false);
        openIndex.put(64,false);
        openIndex.put(65,false);
        openIndex.put(66,false);
        openIndex.put(67,false);
        openIndex.put(68,false);
        openIndex.put(69,false);
        openIndex.put(70,false);
        openIndex.put(71,false);
        //thin doors:
        openIndex.put(72,false);
        openIndex.put(73,false);
        openIndex.put(74,false);
        openIndex.put(75,false);
        openIndex.put(76,false);
        openIndex.put(77,false);
        openIndex.put(78,false);
        //Basement doors:
        openIndex.put(79,false);
        openIndex.put(80,false);
        openIndex.put(81,false);
        openIndex.put(82,false);
        openIndex.put(83,false);
        openIndex.put(84,false);
        openIndex.put(85,false);
        openIndex.put(86,false);
        openIndex.put(87,false);
        openIndex.put(88,false);
        openIndex.put(89,false);
        openIndex.put(90,false);
        openIndex.put(91,false);
        openIndex.put(92,false);
        openIndex.put(93,false);
        openIndex.put(94,false);
        openIndex.put(95,false);
        openIndex.put(96,false);
        openIndex.put(97,false);
        openIndex.put(98,false);
        openIndex.put(99,false);
        openIndex.put(100,false);
        openIndex.put(101,false);
        openIndex.put(102,false);
        openIndex.put(103,false);
    }

    public void loadDoorConfigFromFile(String fileName) {
        String totalFileName = "doorConfig_"+fileName+".json";


        Utility.sendMessageToAllAdmins("Loading: " + totalFileName);
        openIndex.clear();
        File dataFolder = getDataFolder();

        File jsonFile = new File(dataFolder, totalFileName);

        if (!jsonFile.exists()) {
            getLogger().warning(totalFileName+" - file not found!");
            return;
        }

        try (FileReader reader = new FileReader(jsonFile)) {
            Gson gson = new Gson();
            JsonArray jsonArray = gson.fromJson(reader, JsonArray.class);

            for (JsonElement element : jsonArray) {
                JsonObject jsonObject = element.getAsJsonObject();
                for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                    int index = Integer.parseInt(entry.getKey());
                    boolean value = entry.getValue().getAsBoolean();
                    openIndex.put(index, value);
                }
            }

            getLogger().info("Successfully loaded data from + " + totalFileName);
        } catch (IOException e) {
            getLogger().severe("Error reading " + totalFileName+": " + e.getMessage());
        }
    }
    


    public int getCurrentIRLDay() {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Objective objective = scoreboard.getObjective("config");
        int day = objective.getScore("day").getScore();
        return day;
    }

    public void setUpDay(int currentDay){
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Objective objective = scoreboard.getObjective("config");

        if(objective.getScore("night").getScore()==1){
            loadDoorConfigFromFile("nighttime");
            return;
        }

        if(objective.getScore("week1").getScore()==0){
            if(currentDay==0){
                loadDoorConfigFromFile("monday");
            }else if(currentDay==1){
                loadDoorConfigFromFile("tuesday");
            }else if(currentDay==2){
                loadDoorConfigFromFile("wednesday");
            }else if(currentDay==3){
                loadDoorConfigFromFile("thursday");
            }else if(currentDay==4){
                loadDoorConfigFromFile("friday");
            }else if(currentDay==5){
                loadDoorConfigFromFile("saturday");
            }else if(currentDay==6){
                loadDoorConfigFromFile("sunday");
            }
        }else{
            loadDoorConfigFromFile("week1");
        }
    }

    public void advanceDay(){
        int currentDay = getCurrentIRLDay();
        currentDay++;
        currentDay%=7;
        updateCurrentIRLDay(currentDay);
        setUpDay(currentDay);

        updateAllDoors();
    }

    public void updateCurrentIRLDay(int newDay) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Objective objective = scoreboard.getObjective("config");
        objective.getScore("day").setScore(newDay);
    }

    public Location getNarrowDoorTemplate() {
        return narrowDoorTemplate;
    }

    public Location getThinDoorTemplate() {
        return thinDoorTemplate;
    }

    public Location getWideDoorTemplate() {
        return wideDoorTemplate;
    }

    public Location getBasementDoorTemplate() {
        return basementDoorTemplate;
    }

    public void clearAllDoors(){
        for(Door door : doorIndex.values()){
            door.clear();
        }
    }


    public void createAllDoors(){
        init_doors();
        for(Door door : doorIndex.values()){
            door.createClosedDoor();
        }

        setUpDay(getCurrentIRLDay());
        updateAllDoors();
    }
    public void updateAllDoors(){
        for(Map.Entry<Integer,Boolean> entry : openIndex.entrySet()){
            if(entry.getValue()){
                doorIndex.get(entry.getKey()).openDoor();
            }else{
                doorIndex.get(entry.getKey()).closeDoor();
            }
        }
    }

    public void openAllDoors(){
        for(Door door : doorIndex.values()){
            door.openDoor();
        }
    }

    public void closeAllDoors(){
        for(Door door : doorIndex.values()){
            door.closeDoor();
        }
    }

    public void toggleAllDoors(){
        for(Door door : doorIndex.values()){
            if(door.isOpen()){
                door.closeDoor();
            }else{
                door.openDoor();
            }
        }
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event){
        if(lastMessageSent.containsKey(event.getPlayer().getName())){
            lastMessageSent.replace(event.getPlayer().getName(),event.getMessage());
        }else{
            lastMessageSent.put(event.getPlayer().getName(),event.getMessage());
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        String doorName="";
        if(event.getPlayer().getScoreboardTags().contains("NarrowDoorMode")){
            doorName="NarrowDoor";
        }else if(event.getPlayer().getScoreboardTags().contains("WideDoorMode")){
            doorName="WideDoor";
        }else if(event.getPlayer().getScoreboardTags().contains("ThinDoorMode")){
            doorName="ThinDoor";
        }else if(event.getPlayer().getScoreboardTags().contains("BasementDoorMode")){
            doorName="BasementDoor";
        }else if(event.getPlayer().getScoreboardTags().contains("LogLocationsMode") && event.getBlock().getType() == Material.DIAMOND_BLOCK && lastMessageSent.containsKey(event.getPlayer().getName())){
            int x = event.getBlock().getX();
            int y = event.getBlock().getY();
            int z = event.getBlock().getZ();

            String toWrite = lastMessageSent.get(event.getPlayer().getName())+" "+event.getPlayer().getFacing().toString()+":\n"+"new Location(getWorld(),"+x+", "+y+", "+z+");";

            try (PrintWriter out = new PrintWriter(new FileWriter("logged_locations.txt", true))) {
                out.println(toWrite);
                Utility.sendMessageToAllAdmins("Successfully logged location: " + toWrite);
            } catch (IOException e) {
                getLogger().warning("Failed to write to file: " + e.getMessage());
            }

            event.setCancelled(true);
            return;
        }

        if (!doorName.isEmpty() && event.getBlock().getType() == Material.OAK_DOOR) {
            int x = event.getBlock().getX();
            int y = event.getBlock().getY();
            int z = event.getBlock().getZ();

            BlockFace directionFacing = event.getPlayer().getFacing();


            boolean itemInRight = (event.getPlayer().getInventory().getItemInMainHand().getType()==Material.OAK_DOOR);

            String toWrite = "doors.add(new "+doorName+"(new Location(getWorld(),"+x+", "+y+", "+z+"),"+ "BlockFace."+directionFacing.toString()+","+!event.getPlayer().isSneaking()+","+itemInRight+",this));";

            try (PrintWriter out = new PrintWriter(new FileWriter(doorName+"_locations.txt", true))) {
                out.println(toWrite);
                Utility.sendMessageToAllAdmins("Successfully added the following door: " + toWrite);
            } catch (IOException e) {
                getLogger().warning("Failed to write to file: " + e.getMessage());
            }

            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        Location blockID = event.getPlayer().getLocation().clone();
        blockID.setY(-63);

        if(!ambientManager.getBasementPlayerMap().containsKey(event.getPlayer().getName())){
            ambientManager.getBasementPlayerMap().put(event.getPlayer().getName(),false);
        }

        //wither effect
        if(blockID.getBlock().getType()==Material.SOUL_SAND){
            addWitherEffect(event.getPlayer());
        }

        //Basement ambience
        if(blockID.getBlock().getType()==Material.COAL_BLOCK && event.getPlayer().getLocation().getY()<-49){
            ambientManager.checkBasementAmbience(event.getPlayer());
        }

        //Players basement ambience should be finished
        if(ambientManager.getBasementPlayerMap().get(event.getPlayer().getName())&&(blockID.getBlock().getType()!=Material.COAL_BLOCK || event.getPlayer().getLocation().getY()>-49)){
            event.getPlayer().stopSound(SoundCategory.AMBIENT);
            ambientManager.getBasementPlayerMap().put(event.getPlayer().getName(),false);
        }

    }

    private void addWitherEffect(Player player) {
        if(player.getPotionEffect(PotionEffectType.WITHER)==null){
            if(!player.getScoreboardTags().contains("WarnedPollution")){
                player.addScoreboardTag("WarnedPollution");
                player.sendMessage("The outside streets are far too polluted for a small rat like you, you'll suffocate out here!");
            }
            player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER,60,1));

        }
    }

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event){
        if (event.getRightClicked() instanceof Interaction) {
            Utility.sendMessageToAllAdmins("debug1");
            Tap tap = getTapFromInteraction((Interaction) event.getRightClicked());
            if(tap!=null){
                Utility.sendMessageToAllAdmins("debug2");
                tap.rotateTap();
            }else{
                Utility.sendMessageToAllAdmins("debug3");
            }
        }
    }

    private void burnPlayer(Player player) {
        if(player.getScoreboardTags().contains("BurnInvulnerability")){
            player.removeScoreboardTag("BurnInvulnerability");
        }else{
            Utility.sendMessageToAllAdmins("burning player");
            player.addScoreboardTag("BurnInvulnerability");
            Location abovePlayer = player.getLocation().clone();
            abovePlayer.add(0, 5, 0);

            Vector direction = abovePlayer.toVector().subtract(player.getLocation().toVector()).normalize();
            player.setVelocity(direction.multiply(0.8f));
            player.playSound(player,Sound.ENTITY_GENERIC_EXTINGUISH_FIRE,SoundCategory.BLOCKS,1,1);
            if(player.getHealth()>2){
                player.damage(2);
            }
            new BurntRunnable(this,player);
        }
    }

    public void resetAllTaps(){
        init_taps();
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        scoreboard.resetScores("taps");
    }

    public void tickRunnableTrigger() {
        for(Player player : Bukkit.getOnlinePlayers()){
            if(player.getGameMode()==GameMode.SURVIVAL){

                //Check stove:
                //If standing on detector rail
                if(player.getLocation().getBlock().getType()==Material.DETECTOR_RAIL){
                    Powerable rail = (Powerable)player.getLocation().getBlock().getBlockData();
                    if(rail.isPowered()){
                        //if not in the air
                        if(player.getLocation().getY()%1==0){
                            //if the block two blocks below the detector rail is a furnace
                            Location furnaceLocation = player.getLocation().getBlock().getLocation().add(0,-2,0);
                            if(furnaceLocation.getBlock().getType()==Material.BLAST_FURNACE){
                                burnPlayer(player);
                            }
                        }
                    }
                }

            }
        }

        for(Tap tap : taps){
            if(tap.isTurned()){
                tap.drip();
            }
        }
    }
}

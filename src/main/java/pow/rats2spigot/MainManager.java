package pow.rats2spigot;

import com.google.gson.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.Powerable;
import org.bukkit.block.data.Rail;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;
import pow.rats2spigot.build.BasementDoor;
import pow.rats2spigot.build.Door;
import pow.rats2spigot.build.NarrowDoor;
import pow.rats2spigot.build.ThinDoor;
import pow.rats2spigot.commands.*;
import pow.rats2spigot.interactables.Cheese;
import pow.rats2spigot.interactables.Radio;
import pow.rats2spigot.interactables.Tap;
import pow.rats2spigot.interactables.Trap;
import pow.rats2spigot.runnables.BurntRunnable;
import pow.rats2spigot.runnables.HalloweenRunnable;
import pow.rats2spigot.runnables.TickRunnable;
import pow.rats2spigot.util.LootChest;
import pow.rats2spigot.util.Utility;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;


import java.io.*;
import java.util.*;

public final class MainManager extends JavaPlugin implements Listener {

    private Location narrowDoorTemplate;
    private Location wideDoorTemplate;
    private Location thinDoorTemplate;
    private Location basementDoorTemplate;
    private StoveManager stoveManager;
    private ArrayList<Material> bedTypes = new ArrayList<>();
    private ArrayList<Tap> taps = new ArrayList<>();
    private TickRunnable tickRunnable;
    private HalloweenRunnable halloweenRunnable;
    private BukkitTask catDiscEvent = null;
    private ArrayList<Location> stoveTops = new ArrayList<>();
    private final HashMap<Integer,Door> doorIndex = new HashMap<>();
    private final HashMap<Integer,Boolean> openIndex = new HashMap<>();
    private AmbientManager ambientManager;
    private CitizensManipulate citizensManipulate;
    private CatcherManager catcherManager;
    private DayTimeRunnable dayTimeRunnable;
    private FreezerRunnable freezerRunnable;
    private ArrayList<Location> spiderLocations = new ArrayList<>();
    private Location lastSpiderLocation = null;
    private ChestRunnable chestRunnable;
    private ArrayList<String> returningRats = new ArrayList<>();
    private Team returningRatsTeam;
    private ArrayList<LootChest> lootChests = new ArrayList<>();
    private HashMap<String,Location> chestCopyLocations = new HashMap<>();
    private HashMap<Location,String> chestPasteLocations = new HashMap<>();
    private HashMap<String,String> lastMessageSent;
    private ArrayList<Location> cheeseLocations = new ArrayList<>();
    private ArrayList<Location> remainingCheeseLocations = new ArrayList<>();
    private ArrayList<Location> trapLocations = new ArrayList<>();
    private ArrayList<Location> remainingTrapLocations = new ArrayList<>();
    private ArrayList<Cheese> spawnedCheeses = new ArrayList<>();
    private ArrayList<Trap> spawnedTraps = new ArrayList<>();
    private HashMap<String,Boolean> playerIsTrapped = new HashMap<>();
    private Radio radio;
    private ArrayList<Material> cookableItems = new ArrayList<>();
    private Location teleportLocation;
    private Team mainTeam;


    @Override
    public void onDisable() {
        ambientManager.stopAllAmbientRunnables();
        removeAllTaps();
        citizensManipulate.packDownCitizensManipulate();
    }



    @Override
    public void onEnable() {
        lastMessageSent = new HashMap<>();
        loadChunks();

        mainTeam = Bukkit.getScoreboardManager().getMainScoreboard().getTeam("main");
        if(mainTeam==null){
            mainTeam = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam("main");
        }

        bedTypes.add(Material.BLACK_BED);
        bedTypes.add(Material.BLUE_BED);
        bedTypes.add(Material.BROWN_BED);
        bedTypes.add(Material.CYAN_BED);
        bedTypes.add(Material.GREEN_BED);
        bedTypes.add(Material.GRAY_BED);
        bedTypes.add(Material.LIGHT_BLUE_BED);
        bedTypes.add(Material.LIGHT_GRAY_BED);
        bedTypes.add(Material.LIME_BED);
        bedTypes.add(Material.MAGENTA_BED);
        bedTypes.add(Material.ORANGE_BED);
        bedTypes.add(Material.PINK_BED);
        bedTypes.add(Material.PURPLE_BED);
        bedTypes.add(Material.RED_BED);
        bedTypes.add(Material.WHITE_BED);
        bedTypes.add(Material.YELLOW_BED);

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
        new reset_all_taps(this);
        new begin_citizens(this);
        new clear_all_npcs(this);
        new spawn_citizens(this);
        new make_npc_walk(this);
        new pathing_tool_give(this);
        new clear_all_catchers(this);
        new create_cheese(this);
        new reset_all_cheeses(this);
        new update_lights(this);
        new create_trap(this);
        new reset_all_traps(this);
        new give_up(this);
        new fakename(this);
        new hard_clear_all_npcs(this);
        new little_tiles_chunk(this);
        new spook_effect_all(this);
        new spook_effect_near(this);
        new summon_tiny_spider(this);

        new halloween_dialogue_brawl(this);
        new halloween_dialogue_ending(this);
        new halloween_dialogue_graveyard(this);
        new halloween_dialogue_knowledge(this);
        new halloween_dialogue_shooting(this);
        new halloween_dialogue_starting(this);
        new cancel_cat_disc(this);
        new halloween_summon_brawl_1(this);
        new halloween_summon_brawl_2(this);
        new halloween_summon_brawl_3(this);
        new halloween_completed(this);
        new summon_tiny_spider_here(this);

        new inspector_crash(this);
        new inspector_doorbell(this);
        new inspector_dialogue_1(this);
        new inspector_dialogue_2(this);
        new inspector_dialogue_3(this);
        new inspector_dialogue_4(this);
        new declan_1(this);
        new declan_2(this);

        new give_sugar_cube(this);
        new give_funsnap(this);
        new give_lockpick(this);

        new reauthor_book(this);

        ambientManager = new AmbientManager(this);
        citizensManipulate = new CitizensManipulate(this);
        stoveManager = new StoveManager(this);
        catcherManager = new CatcherManager(this);
        dayTimeRunnable = new DayTimeRunnable(this);
        freezerRunnable = new FreezerRunnable(this);
        //chestRunnable = new ChestRunnable(this);

        narrowDoorTemplate= new Location(getWorld(),26, -48, 41);
        wideDoorTemplate = new Location(getWorld(),20, -48, 41);
        thinDoorTemplate = new Location(getWorld(),17, -48, 41);
        basementDoorTemplate = new Location(getWorld(),13, -48, 41);

        tickRunnable=new TickRunnable(this);
        halloweenRunnable = new HalloweenRunnable(this);

        setUpScoreboard();
        init_doors();
        init_chests();
        init_spider_locations();
        init_stove_tops();
        init_cookableItems();
        init_taps();
        BukkitTask bukkitTask = new BukkitRunnable() {
            @Override
            public void run() {
                init_cheeses();
                populateCheese(5);
                init_traps();
                populateTraps(3);
                createRadio();
                clearAllStoveFood();
                updateLights();
            }
        }.runTaskLater(this, 20); // 100 ticks = 5 seconds

        init_returning_rats();

        getWorld().setGameRule(GameRule.SHOW_DEATH_MESSAGES,false);
        //createAllDoors();
        //turnAllStoveTopsOn();
        teleportLocation = new Location(getWorld(),141.5, -2.5, -134.5);



        Utility.sendMessageToAllAdmins("Code initialised successfully");
    }

    private void init_returning_rats() {
        returningRats.clear();
        returningRatsTeam = Bukkit.getScoreboardManager().getMainScoreboard().getTeam("ReturningRats");
        if(returningRatsTeam==null){
            returningRatsTeam = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam("ReturningRats");
        }
        returningRatsTeam.setPrefix("§d");


        returningRats.add("soupforeloise");
        returningRats.add("InTheLittleWood");
        returningRats.add("bekyamon");
        returningRats.add("OrionSound");
        returningRats.add("willowmvp");
        returningRats.add("ShubbleYT");
        returningRats.add("Tubbo_");
    }

    private void init_cookableItems() {
        cookableItems.clear();
        cookableItems.add(Material.BEEF);
        cookableItems.add(Material.PORKCHOP);
        cookableItems.add(Material.CHICKEN);
        cookableItems.add(Material.RABBIT);
        cookableItems.add(Material.MUTTON);
        cookableItems.add(Material.COD);
        cookableItems.add(Material.SALMON);
        cookableItems.add(Material.POTATO);
        cookableItems.add(Material.KELP);
    }

    public ArrayList<String> getReturningRats() {
        return returningRats;
    }

    public Team getMainTeam() {
        return mainTeam;
    }

    public Team getReturningRatsTeam() {
        return returningRatsTeam;
    }

    private void createRadio() {
        clearAllRadios();
        radio = new Radio(new Location(getWorld(),-38, -53, -48),this);
    }

    public BukkitTask getCatDiscEvent() {
        return catDiscEvent;
    }

    public void populateCheese(int numberOfCheese){
        clearAllCheeses();
        for (int i = 0; i < numberOfCheese; i++) {
            if(!remainingCheeseLocations.isEmpty()){
                createCheese();
            }
        }
    }

    public void init_cheeses(){
        clearAllCheeses();


        cheeseLocations.clear();
        cheeseLocations.add(new Location(getWorld(),91, -43, -56));
        cheeseLocations.add(new Location(getWorld(),91, -43, -95));
        cheeseLocations.add(new Location(getWorld(),30, -45, -74));
        cheeseLocations.add(new Location(getWorld(),-13, -45, -34));
        cheeseLocations.add(new Location(getWorld(),65, -36, -66));
        cheeseLocations.add(new Location(getWorld(),80, -24, -86));
        cheeseLocations.add(new Location(getWorld(),-68, -24, -38));
        cheeseLocations.add(new Location(getWorld(),-33, -16, -103));
        cheeseLocations.add(new Location(getWorld(),-68, -22, -95));
        cheeseLocations.add(new Location(getWorld(),56, -22, -155));
        cheeseLocations.add(new Location(getWorld(),80, -16, -110));
        cheeseLocations.add(new Location(getWorld(),117, -55, -110));
        cheeseLocations.add(new Location(getWorld(),97, -55, -64));
        cheeseLocations.add(new Location(getWorld(),62, -55, -78));
        cheeseLocations.add(new Location(getWorld(),12, -55, -72));
        cheeseLocations.add(new Location(getWorld(),-25, -54, -69));
        cheeseLocations.add(new Location(getWorld(),-13, -53, -35));
        cheeseLocations.add(new Location(getWorld(),-96, -52, -83));
        cheeseLocations.add(new Location(getWorld(),-93, -48, -85));
        cheeseLocations.add(new Location(getWorld(),-73, -52, -87));

        remainingCheeseLocations.clear();
        remainingCheeseLocations.addAll(cheeseLocations);

    }

    private void clearAllCheeses() {
        for(Entity entity : getWorld().getEntities()){
            if(entity instanceof Interaction){
                if(entity.getScoreboardTags().contains("CheeseInteraction")){
                    entity.remove();
                }
            }

            if(entity instanceof ItemDisplay){
                if(entity.getScoreboardTags().contains("CheeseItemDisplay")){
                    entity.remove();
                }
            }
        }
    }

    private void clearAllStoveFood() {
        for(Entity entity : getWorld().getEntities()){
            if(entity instanceof ItemDisplay){
                if(entity.getScoreboardTags().contains("StoveItemDisplay")){
                    entity.remove();
                }
            }
        }
    }

    private void clearAllRadios() {
        for(Entity entity : getWorld().getEntities()){
            if(entity instanceof Interaction){
                if(entity.getScoreboardTags().contains("RadioInteraction")){
                    entity.remove();
                }
            }

            if(entity instanceof ItemDisplay){
                if(entity.getScoreboardTags().contains("RadioItemDisplay")){
                    entity.remove();
                }
            }
        }
    }

    public void createCheese(){
        Random random = new Random();
        if(!remainingCheeseLocations.isEmpty()){
            int randomIndex = random.nextInt(remainingCheeseLocations.size());
            Location locationToSpawnCheese = remainingCheeseLocations.get(randomIndex);
            remainingCheeseLocations.remove(locationToSpawnCheese);

            spawnedCheeses.add(new Cheese(locationToSpawnCheese,this));
        }

    }

    private void init_spider_locations(){
        spiderLocations.clear();
        spiderLocations.add(new Location(getWorld(),185, -55, -103));
        spiderLocations.add(new Location(getWorld(),311, -55, -104));
        spiderLocations.add(new Location(getWorld(),326, -55, -37));
        spiderLocations.add(new Location(getWorld(),304, -55, -104));
        spiderLocations.add(new Location(getWorld(),211, -55, -137));
        spiderLocations.add(new Location(getWorld(),197, -55, -137));
        spiderLocations.add(new Location(getWorld(),239, -55, -137));
        spiderLocations.add(new Location(getWorld(),247, -55, -151));
        spiderLocations.add(new Location(getWorld(),262, -55, -156));
        spiderLocations.add(new Location(getWorld(),311, -55, -191));
        spiderLocations.add(new Location(getWorld(),259, -55, -167));
        spiderLocations.add(new Location(getWorld(),183, -55, -152));
        spiderLocations.add(new Location(getWorld(),191, -55, -56));
        spiderLocations.add(new Location(getWorld(),186, -55, -42));
        spiderLocations.add(new Location(getWorld(),186, -55, -24));
        spiderLocations.add(new Location(getWorld(),357, -55, -51));
        spiderLocations.add(new Location(getWorld(),201, -55, -3));
        spiderLocations.add(new Location(getWorld(),231, -55, -25));
        spiderLocations.add(new Location(getWorld(),326, -55, 23));
        spiderLocations.add(new Location(getWorld(),297, -55, -11));
        spiderLocations.add(new Location(getWorld(),314, -55, 24));
        spiderLocations.add(new Location(getWorld(),221, -55, -57));
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
        chestCopyLocations.put("crypt",new Location(getWorld(),36, -63, 49));
        chestCopyLocations.put("crypt_rare",new Location(getWorld(),34, -63, 49));

        lootChests.clear();
        lootChests.add(new LootChest("Kitchen1","kitchen",new Location(getWorld(),96, -40, -87),BlockFace.WEST,this));
        lootChests.add(new LootChest("Kitchen2","kitchen",new Location(getWorld(),96, -40, -89),BlockFace.WEST,this));
        lootChests.add(new LootChest("Kitchen3","kitchen",new Location(getWorld(),84, -37, -61),BlockFace.EAST,this));
        lootChests.add(new LootChest("Kitchen4","kitchen",new Location(getWorld(),-45, -33, -174) ,BlockFace.WEST,this));
        lootChests.add(new LootChest("Fridge1","fridge",new Location(getWorld(),96, -37, -47),BlockFace.WEST,this));
        lootChests.add(new LootChest("Fridge2","fridge",new Location(getWorld(),87, -39, -46) ,BlockFace.EAST,this));
        lootChests.add(new LootChest("Fridge3","fridge",new Location(getWorld(),87, -45, -43) ,BlockFace.EAST,this));
        lootChests.add(new LootChest("Fridge4","fridge",new Location(getWorld(),87, -43, -40) ,BlockFace.EAST,this));
        lootChests.add(new LootChest("Fridge5","fridge",new Location(getWorld(),87, -39, -38) ,BlockFace.EAST,this));

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
        lootChests.add(new LootChest("Abandoned bathroom","abandoned_bathroom",new Location(getWorld(),-58, -23, -151) ,BlockFace.SOUTH,this));

        lootChests.add(new LootChest("Scientist Shelf","scientist_living_room",new Location(getWorld(),-69, -15, -166) ,BlockFace.EAST,this));

        //apos additions:
        lootChests.add(new LootChest("Kitchen4","kitchen",new Location(getWorld(),79,-43,-94) ,BlockFace.EAST,this));
        lootChests.add(new LootChest("Kitchen5","kitchen",new Location(getWorld(),96,-40,-57) ,BlockFace.WEST,this));
        lootChests.add(new LootChest("Pool Changing Room 2","pool_changing_rooms",new Location(getWorld(),-59,-45,-146) ,BlockFace.WEST,this));
        lootChests.add(new LootChest("Pool Changing Room 3","pool_changing_rooms",new Location(getWorld(),-69,-45,-124) ,BlockFace.EAST,this));
        lootChests.add(new LootChest("Main Lobby 5","main_lobby",new Location(getWorld(),-37,-45,-162) ,BlockFace.SOUTH,this));
        lootChests.add(new LootChest("Main Lobby 6","main_lobby",new Location(getWorld(),55,-40,-85) ,BlockFace.NORTH,this));

        //Crypt
        lootChests.add(new LootChest("Crypt1","crypt",new Location(getWorld(),135, -53, -98),BlockFace.NORTH,this));
        lootChests.add(new LootChest("Crypt2","crypt",new Location(getWorld(),210, -53, -91),BlockFace.EAST,this));
        lootChests.add(new LootChest("Crypt3","crypt",new Location(getWorld(),217, -53, -65),BlockFace.NORTH,this));
        lootChests.add(new LootChest("Crypt4","crypt",new Location(getWorld(),182, -50, -141),BlockFace.WEST,this));
        lootChests.add(new LootChest("Crypt5","crypt",new Location(getWorld(),260, -51, -112),BlockFace.EAST,this));
        lootChests.add(new LootChest("Crypt6","crypt",new Location(getWorld(),226, -55, -134),BlockFace.SOUTH,this));
        lootChests.add(new LootChest("Crypt7","crypt",new Location(getWorld(),317, -53, -154),BlockFace.WEST,this));
        lootChests.add(new LootChest("Crypt8","crypt",new Location(getWorld(),252, -51, -167),BlockFace.WEST,this));
        lootChests.add(new LootChest("Crypt9","crypt",new Location(getWorld(),280, -51, -185),BlockFace.EAST,this));
        lootChests.add(new LootChest("Crypt10","crypt",new Location(getWorld(),357, -53, -178),BlockFace.NORTH,this));
        lootChests.add(new LootChest("Crypt11","crypt",new Location(getWorld(),318, -55, -106),BlockFace.WEST,this));
        lootChests.add(new LootChest("Crypt12","crypt",new Location(getWorld(),319, -55, -183),BlockFace.EAST,this));
        lootChests.add(new LootChest("Crypt13","crypt",new Location(getWorld(),354, -55, -49),BlockFace.WEST,this));
        lootChests.add(new LootChest("Crypt14","crypt",new Location(getWorld(),329, -55, -98),BlockFace.SOUTH,this));
        lootChests.add(new LootChest("Crypt15","crypt",new Location(getWorld(),319, -55, -182),BlockFace.WEST,this));
        lootChests.add(new LootChest("Crypt16","crypt",new Location(getWorld(),198, -55, -73),BlockFace.WEST,this));
        lootChests.add(new LootChest("Crypt17","crypt",new Location(getWorld(),211, -50, -59),BlockFace.NORTH,this));
        lootChests.add(new LootChest("Crypt18","crypt",new Location(getWorld(),228, -55, -41),BlockFace.WEST,this));
        lootChests.add(new LootChest("Crypt19","crypt",new Location(getWorld(),235, -56, -25),BlockFace.WEST,this));
        lootChests.add(new LootChest("Crypt20","crypt",new Location(getWorld(),248, -55, -54),BlockFace.NORTH,this));
        lootChests.add(new LootChest("RareCrypt1","crypt_rare",new Location(getWorld(),352, -54, -216),BlockFace.NORTH,this));
        lootChests.add(new LootChest("RareCrypt2","crypt_rare",new Location(getWorld(),361, -63, -198),BlockFace.SOUTH,this));
        lootChests.add(new LootChest("RareCrypt3","crypt_rare",new Location(getWorld(),280, -63, -97),BlockFace.EAST,this));
        lootChests.add(new LootChest("RareCrypt4","crypt_rare",new Location(getWorld(),260, -55, -109),BlockFace.WEST,this));
        lootChests.add(new LootChest("RareCrypt5","crypt_rare",new Location(getWorld(),271, -55, 35),BlockFace.SOUTH,this));
        lootChests.add(new LootChest("RareCrypt6","crypt_rare",new Location(getWorld(),282, -63, 14),BlockFace.NORTH,this));
        lootChests.add(new LootChest("RareCrypt7","crypt_rare",new Location(getWorld(),282, -63, 14),BlockFace.NORTH,this));
        lootChests.add(new LootChest("RareCrypt8","crypt_rare",new Location(getWorld(),326, -55, -15),BlockFace.NORTH,this));
        lootChests.add(new LootChest("RareCrypt9","crypt_rare",new Location(getWorld(),326, -61, -15),BlockFace.SOUTH,this));
        lootChests.add(new LootChest("RareCrypt10","crypt_rare",new Location(getWorld(),211, -55, 2),BlockFace.NORTH,this));
        lootChests.add(new LootChest("RareCrypt11","crypt_rare",new Location(getWorld(),190, -55, 26),BlockFace.SOUTH,this));
        lootChests.add(new LootChest("RareCrypt12","crypt_rare",new Location(getWorld(),205, -55, -68),BlockFace.SOUTH,this));
        lootChests.add(new LootChest("RareCrypt13","crypt_rare",new Location(getWorld(),255, -55, -168),BlockFace.WEST,this));

        chestPasteLocations.clear();
    }

    public boolean isNight(){
        return (Bukkit.getScoreboardManager().getMainScoreboard().getObjective("config").getScore("night").getScore()==1);
    }


    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event){
        if (event.getInventory().getType() == InventoryType.CHEST && event.getInventory().isEmpty()) {
            if (event.getInventory().getLocation() != null) {
                for(LootChest lootChest : lootChests){
                    if(lootChest.getPasteLocation().getX()==event.getInventory().getLocation().getX()&&lootChest.getPasteLocation().getY()==event.getInventory().getLocation().getY()&&lootChest.getPasteLocation().getZ()==event.getInventory().getLocation().getZ()){
                        Utility.poofBlock(event.getInventory().getLocation());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onyPlayerSwapHandItems(PlayerSwapHandItemsEvent event){
        Player player = event.getPlayer();
        if(player.getScoreboardTags().contains("TilingChunk")){
            int teleportToX = player.getLocation().getBlockX();
            int teleportToY = player.getLocation().getBlockY();
            int teleportToZ = player.getLocation().getBlockZ();

            int chunkX = player.getLocation().getChunk().getX()*16;
            int chunkZ = player.getLocation().getChunk().getZ()*16;

            player.sendMessage("Your x is " + teleportToX);
            player.sendMessage("max x is");
            teleportToX+=1;
            if(teleportToX+1>chunkX+16){
                teleportToX=chunkX;
                teleportToZ+=1;
            }
            if(teleportToZ>chunkZ+16){
                teleportToZ=chunkZ;
                teleportToY+=1;
            }

            Location location = new Location(getWorld(),teleportToX,teleportToY,teleportToZ,player.getLocation().getYaw(),player.getLocation().getPitch());
            location.add(0.5,0.5,0.5);
            player.teleport(location);

            if(teleportToY>5){
                player.getScoreboardTags().remove("TilingChunk");
                player.sendMessage("You have successfully tiled this chunk. Thank you.");
                player.getLocation().getBlock().setType(Material.DIAMOND_BLOCK);
            }
            event.setCancelled(true);
        }
    }

    public ArrayList<Location> getRemainingCheeseLocations() {
        return remainingCheeseLocations;
    }

    public ArrayList<Location> getRemainingTrapLocations() {
        return remainingTrapLocations;
    }

    private void removeAllTaps() {
        for(Tap tap : taps){
            tap.getInteraction().remove();
            tap.getItemDisplay().remove();
        }
    }

    public HashMap<String, Location> getChestCopyLocations() {
        return chestCopyLocations;
    }

    public CitizensManipulate getCitizensManipulate() {
        return citizensManipulate;
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
                if(entity.getScoreboardTags().contains("TapInteraction")){
                    entity.remove();
                }
            }

            if(entity instanceof ItemDisplay){
                if(entity.getScoreboardTags().contains("TapItemDisplay")){
                    entity.remove();
                }
            }
        }


        //Green room taps
        taps.add(new Tap(new Location(getWorld(),71, -23, -87),BlockFace.WEST,new Location(getWorld(),72, -20, -87),this));
        taps.add(new Tap(new Location(getWorld(),71, -23, -90),BlockFace.WEST,new Location(getWorld(),72, -20, -90),this));
        taps.add(new Tap(new Location(getWorld(),71, -23, -93),BlockFace.WEST,new Location(getWorld(),72, -20, -93),this));
        taps.add(new Tap(new Location(getWorld(),77, -23, -93),BlockFace.EAST,new Location(getWorld(),76, -20, -93),this));
        taps.add(new Tap(new Location(getWorld(),77, -23, -90),BlockFace.EAST,new Location(getWorld(),76, -20, -90),this));
        taps.add(new Tap(new Location(getWorld(),77, -23, -87),BlockFace.EAST,new Location(getWorld(),76, -20, -87),this));




        taps.add(new Tap(new Location(getWorld(),-27, -23, -71),BlockFace.SOUTH, new Location(getWorld(),-27, -21, -72),this));
        taps.add(new Tap(new Location(getWorld(),-44, -23, -149),BlockFace.WEST,new Location(getWorld(),-42, -21, -149) ,this));
        taps.add(new Tap(new Location(getWorld(),-65, -23, -151),BlockFace.NORTH,new Location(getWorld(),-65, -19, -150) ,this));
        taps.add(new Tap(new Location(getWorld(),-67, -23, -57),BlockFace.SOUTH, new Location(getWorld(),-67, -21, -58),this));
        taps.add(new Tap(new Location(getWorld(),-65, -23, -154),BlockFace.SOUTH,new Location(getWorld(),-65, -20, -155) ,this));
        taps.add(new Tap(new Location(getWorld(),8, -23, -159),BlockFace.EAST, new Location(getWorld(),7, -20, -159),this));

        taps.add(new Tap(new Location(getWorld(),82, -23, -150),BlockFace.NORTH,new Location(getWorld(),82, -21, -149) ,this));

        taps.add(new Tap(new Location(getWorld(),74, -22, -145),BlockFace.SOUTH,new Location(getWorld(),74, -19, -146) ,this));


        taps.add(new Tap(new Location(getWorld(),-57, -54, -59),BlockFace.WEST,new Location(getWorld(),-56, -52, -59) ,this));
        taps.add(new Tap(new Location(getWorld(),-60, -54, -59),BlockFace.EAST, new Location(getWorld(),-61, -52, -59),this));
        taps.add(new Tap(new Location(getWorld(),-17, -54, -57),BlockFace.WEST, new Location(getWorld(),-16, -52, -57),this));
        taps.add(new Tap(new Location(getWorld(),-20, -54, -57),BlockFace.EAST,new Location(getWorld(),-21, -52, -57) ,this));

    }

    public void init_traps() {
        clearAllTraps();

        trapLocations.add(new Location(getWorld(),70, -10, -55));

        trapLocations.add(new Location(getWorld(),79, -17, -60));

        trapLocations.add(new Location(getWorld(),70, -31, -60));

        trapLocations.add(new Location(getWorld(),70, -45, -65));

        trapLocations.add(new Location(getWorld(),88, -45, -61));

        trapLocations.add(new Location(getWorld(),92, -45, -47));

        trapLocations.add(new Location(getWorld(),34, -45, -72));

        trapLocations.add(new Location(getWorld(),-3, -45, -70));

        trapLocations.add(new Location(getWorld(),-61, -45, -78));

        trapLocations.add(new Location(getWorld(),-61, -45, -40));

        trapLocations.add(new Location(getWorld(),18, -46, -112));

        trapLocations.add(new Location(getWorld(),10, -46, -138));

        trapLocations.add(new Location(getWorld(),16, -35, -97));

        trapLocations.add(new Location(getWorld(),57, -24, -43));

        trapLocations.add(new Location(getWorld(),74, -24, -43));

        trapLocations.add(new Location(getWorld(),75, -45, -169));

        trapLocations.add(new Location(getWorld(),87, -45, -165));

        trapLocations.add(new Location(getWorld(),-22, -45, -157));

        trapLocations.add(new Location(getWorld(),-18, -45, -161));

        trapLocations.add(new Location(getWorld(),-61, -24, -36));

        trapLocations.add(new Location(getWorld(),-43, -24, -43));

        trapLocations.add(new Location(getWorld(),-49, -24, -52));

        trapLocations.add(new Location(getWorld(),-66, -24, -80));

        remainingTrapLocations.clear();
        remainingTrapLocations.addAll(trapLocations);
    }

    public void populateTraps(int numberOfTraps){
        for (int i = 0; i < numberOfTraps; i++) {
            createTrap();
        }
    }

    public void createTrap(){
        Random random = new Random();
        if(!remainingTrapLocations.isEmpty()){
            int randomIndex = random.nextInt(remainingTrapLocations.size());
            Location locationToSpawnTrap = remainingTrapLocations.get(randomIndex);
            remainingTrapLocations.remove(locationToSpawnTrap);

            spawnedTraps.add(new Trap(locationToSpawnTrap,this));
        }
    }

    private void clearAllTraps(){
        for(Entity entity : getWorld().getEntities()){
            if(entity instanceof Interaction){
                if(entity.getScoreboardTags().contains("TrapInteraction")){
                    entity.remove();
                }
            }

            if(entity instanceof ItemDisplay){
                if(entity.getScoreboardTags().contains("TrapItemDisplay")){
                    entity.remove();
                }
            }
        }
    }

    public Tap getTapFromInteraction(Interaction interaction){
        for(Tap tap : taps){
            if(tap.getInteraction()==interaction){
                return tap;
            }
        }
        return null;
    }

    public Cheese getCheeseFromInteraction(Interaction interaction){
        for(Cheese cheese : spawnedCheeses){
            if(cheese.getInteraction()==interaction){
                return cheese;
            }
        }
        return null;
    }

    public Trap getTrapFromInteraction(Interaction interaction){
        for(Trap trap : spawnedTraps){
            if(trap.getInteraction()==interaction){
                return trap;
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
        stoveTops.add(new Location(getWorld(),91, -43, -87));
        stoveTops.add(new Location(getWorld(),91, -43, -88));
        stoveTops.add(new Location(getWorld(),92, -43, -87));
        stoveTops.add(new Location(getWorld(),92, -43, -88));
        stoveTops.add(new Location(getWorld(),88, -43, -87));
        stoveTops.add(new Location(getWorld(),88, -43, -88));
        stoveTops.add(new Location(getWorld(),89, -43, -87));
        stoveTops.add(new Location(getWorld(),89, -43, -88));
        stoveTops.add(new Location(getWorld(),91, -43, -82));
        stoveTops.add(new Location(getWorld(),91, -43, -83));
        stoveTops.add(new Location(getWorld(),92, -43, -82));
        stoveTops.add(new Location(getWorld(),92, -43, -83));
        stoveTops.add(new Location(getWorld(),88, -43, -82));
        stoveTops.add(new Location(getWorld(),88, -43, -83));
        stoveTops.add(new Location(getWorld(),89, -43, -82));
        stoveTops.add(new Location(getWorld(),89, -43, -83));
    }

    private void init_doors(){
        doorIndex.clear();
        doorIndex.put(0,new NarrowDoor(new Location(getWorld(),-22, -45, -41),BlockFace.WEST,true,false,this));
        doorIndex.put(1,new NarrowDoor(new Location(getWorld(),-19, -45, -57),BlockFace.WEST,true,true,this));
        doorIndex.put(2,new NarrowDoor(new Location(getWorld(),-19, -45, -73),BlockFace.WEST,true,false,this));
        doorIndex.put(3,new NarrowDoor(new Location(getWorld(),45, -45, -59),BlockFace.EAST,true,false,this));
        doorIndex.put(4,new NarrowDoor(new Location(getWorld(),95, -45, -97),BlockFace.SOUTH,true,false,this));
        doorIndex.put(5,new NarrowDoor(new Location(getWorld(),87, -45, -97),BlockFace.SOUTH,true,true,this));
        doorIndex.put(6,new NarrowDoor(new Location(getWorld(),91, -45, -144),BlockFace.NORTH,true,false,this));
        doorIndex.put(7,new NarrowDoor(new Location(getWorld(),49, -45, -91),BlockFace.EAST,true,false,this));
        doorIndex.put(8,new NarrowDoor(new Location(getWorld(),53, -45, -94),BlockFace.NORTH,true,false,this));
        doorIndex.put(9,new NarrowDoor(new Location(getWorld(),60, -45, -123),BlockFace.WEST,true,false,this));
        doorIndex.put(10,new NarrowDoor(new Location(getWorld(),61, -45, -152),BlockFace.NORTH,true,false,this));
        doorIndex.put(11,new NarrowDoor(new Location(getWorld(),53, -45, -152),BlockFace.NORTH,true,false,this));
        doorIndex.put(12,new NarrowDoor(new Location(getWorld(),-3, -45, -95),BlockFace.NORTH,true,false,this));
        doorIndex.put(13,new NarrowDoor(new Location(getWorld(),29, -45, -95),BlockFace.NORTH,true,false,this));
        doorIndex.put(14,new NarrowDoor(new Location(getWorld(),-42, -45, -91),BlockFace.WEST,true,false,this));
        doorIndex.put(15,new NarrowDoor(new Location(getWorld(),-42, -45, -99),BlockFace.WEST,true,false,this));
        doorIndex.put(16,new NarrowDoor(new Location(getWorld(),-42, -45, -147),BlockFace.WEST,true,false,this));
        doorIndex.put(17,new NarrowDoor(new Location(getWorld(),-48, -45, -144),BlockFace.NORTH,true,false,this));
        doorIndex.put(18,new NarrowDoor(new Location(getWorld(),-55, -45, -38),BlockFace.WEST,true,false,this));
        doorIndex.put(19,new NarrowDoor(new Location(getWorld(),-55, -45, -76),BlockFace.WEST,true,true,this));
        doorIndex.put(20,new NarrowDoor(new Location(getWorld(),-31, -45, -157),BlockFace.EAST,true,false,this));
        doorIndex.put(21,new NarrowDoor(new Location(getWorld(),-23, -45, -168),BlockFace.SOUTH,true,true,this));
        doorIndex.put(22,new NarrowDoor(new Location(getWorld(),-31, -45, -163),BlockFace.WEST,true,true,this));
        doorIndex.put(23,new NarrowDoor(new Location(getWorld(),-41, -24, -53),BlockFace.SOUTH,true,false,this));
        doorIndex.put(24,new NarrowDoor(new Location(getWorld(),-49, -24, -46),BlockFace.SOUTH,true,false,this));
        doorIndex.put(25,new NarrowDoor(new Location(getWorld(),-59, -24, -54),BlockFace.NORTH,true,false,this));
        doorIndex.put(26,new NarrowDoor(new Location(getWorld(),-23, -24, -65),BlockFace.WEST,true,false,this));
        doorIndex.put(27,new NarrowDoor(new Location(getWorld(),-43, -24, -79),BlockFace.NORTH,true,false,this));
        doorIndex.put(28,new NarrowDoor(new Location(getWorld(),-25, -24, -79),BlockFace.SOUTH,true,false,this));
        doorIndex.put(29,new NarrowDoor(new Location(getWorld(),-39, -24, -117),BlockFace.EAST,true,false,this));
        doorIndex.put(30,new NarrowDoor(new Location(getWorld(),-43, -24, -128),BlockFace.NORTH,true,true,this));
        doorIndex.put(31,new NarrowDoor(new Location(getWorld(),-36, -24, -144),BlockFace.NORTH,true,false,this));
        doorIndex.put(32,new NarrowDoor(new Location(getWorld(),-55, -24, -115),BlockFace.WEST,true,false,this));
        doorIndex.put(33,new NarrowDoor(new Location(getWorld(),-57, -24, -111),BlockFace.SOUTH,true,true,this));
        doorIndex.put(34,new NarrowDoor(new Location(getWorld(),-57, -24, -96),BlockFace.SOUTH,true,false,this));
        doorIndex.put(35,new NarrowDoor(new Location(getWorld(),-62, -24, -119),BlockFace.WEST,true,false,this));
        doorIndex.put(36,new NarrowDoor(new Location(getWorld(),-62, -24, -135),BlockFace.WEST,true,true,this));
        doorIndex.put(37,new NarrowDoor(new Location(getWorld(),-59, -24, -145),BlockFace.NORTH,true,false,this));
        doorIndex.put(38,new NarrowDoor(new Location(getWorld(),-55, -24, -155),BlockFace.WEST,true,false,this));
        doorIndex.put(39,new NarrowDoor(new Location(getWorld(),-53, -24, -165),BlockFace.EAST,true,true,this));
        doorIndex.put(40,new NarrowDoor(new Location(getWorld(),-38, -24, -163),BlockFace.WEST,true,true,this));
        doorIndex.put(41,new NarrowDoor(new Location(getWorld(),19, -24, -166),BlockFace.SOUTH,true,true,this));
        doorIndex.put(42,new NarrowDoor(new Location(getWorld(),25, -24, -161),BlockFace.EAST,true,true,this));
        doorIndex.put(43,new NarrowDoor(new Location(getWorld(),-1, -24, -161),BlockFace.SOUTH,true,true,this));
        doorIndex.put(44,new NarrowDoor(new Location(getWorld(),-17, -24, -161),BlockFace.SOUTH,true,false,this));
        doorIndex.put(45,new NarrowDoor(new Location(getWorld(),67, -24, -165),BlockFace.EAST,true,false,this));
        doorIndex.put(46,new NarrowDoor(new Location(getWorld(),81, -24, -168),BlockFace.NORTH,true,false,this));
        doorIndex.put(47,new NarrowDoor(new Location(getWorld(),87, -24, -165),BlockFace.EAST,true,false,this));
        doorIndex.put(48,new NarrowDoor(new Location(getWorld(),90, -24, -131),BlockFace.WEST,true,false,this));
        doorIndex.put(49,new NarrowDoor(new Location(getWorld(),84, -24, -136),BlockFace.NORTH,true,false,this));
        doorIndex.put(50,new NarrowDoor(new Location(getWorld(),86, -24, -128),BlockFace.SOUTH,true,true,this));
        doorIndex.put(51,new NarrowDoor(new Location(getWorld(),69, -24, -115),BlockFace.WEST,true,true,this));
        doorIndex.put(52,new NarrowDoor(new Location(getWorld(),69, -24, -131),BlockFace.WEST,true,true,this));
        doorIndex.put(53,new NarrowDoor(new Location(getWorld(),72, -24, -136),BlockFace.NORTH,true,false,this));
        doorIndex.put(54,new NarrowDoor(new Location(getWorld(),89, -24, -79),BlockFace.WEST,true,false,this));
        doorIndex.put(55,new NarrowDoor(new Location(getWorld(),62, -24, -84),BlockFace.NORTH,true,false,this));
        doorIndex.put(56,new NarrowDoor(new Location(getWorld(),58, -24, -81),BlockFace.EAST,true,false,this));
        doorIndex.put(57,new NarrowDoor(new Location(getWorld(),85, -24, -52),BlockFace.NORTH,true,false,this));
        doorIndex.put(58,new ThinDoor(new Location(getWorld(),66, -45, -66),BlockFace.WEST,true,false,this));
        doorIndex.put(59,new ThinDoor(new Location(getWorld(),82, -45, -66),BlockFace.WEST,true,false,this));
        doorIndex.put(60,new ThinDoor(new Location(getWorld(),78, -24, -76),BlockFace.SOUTH,true,true,this));
        doorIndex.put(61,new ThinDoor(new Location(getWorld(),71, -24, -76),BlockFace.SOUTH,true,false,this));
        doorIndex.put(62,new BasementDoor(new Location(getWorld(),-71, -52, -101),BlockFace.EAST,true,false,this));
        doorIndex.put(63,new BasementDoor(new Location(getWorld(),-71, -52, -109),BlockFace.EAST,true,false,this));
        doorIndex.put(64,new BasementDoor(new Location(getWorld(),-71, -52, -117),BlockFace.EAST,true,false,this));
        doorIndex.put(65,new BasementDoor(new Location(getWorld(),-71, -52, -125),BlockFace.EAST,true,false,this));
        doorIndex.put(66,new BasementDoor(new Location(getWorld(),-71, -52, -133),BlockFace.EAST,true,false,this));
        doorIndex.put(67,new BasementDoor(new Location(getWorld(),-55, -55, -151),BlockFace.EAST,true,false,this));
        doorIndex.put(68,new BasementDoor(new Location(getWorld(),-38, -55, -157),BlockFace.SOUTH,true,false,this));
        doorIndex.put(69,new BasementDoor(new Location(getWorld(),-55, -55, -162),BlockFace.EAST,true,false,this));
        doorIndex.put(70,new BasementDoor(new Location(getWorld(),-27, -55, -157),BlockFace.SOUTH,true,false,this));
        doorIndex.put(71,new BasementDoor(new Location(getWorld(),37, -55, -165),BlockFace.NORTH,true,false,this));
        doorIndex.put(72,new BasementDoor(new Location(getWorld(),39, -55, -157),BlockFace.SOUTH,true,false,this));
        doorIndex.put(73,new BasementDoor(new Location(getWorld(),25, -55, -157),BlockFace.SOUTH,true,false,this));
        doorIndex.put(74,new BasementDoor(new Location(getWorld(),71, -55, -108),BlockFace.NORTH,true,false,this));
        doorIndex.put(75,new BasementDoor(new Location(getWorld(),73, -55, -100),BlockFace.SOUTH,true,false,this));
        doorIndex.put(76,new BasementDoor(new Location(getWorld(),102, -55, -105),BlockFace.EAST,true,false,this));
        doorIndex.put(77,new BasementDoor(new Location(getWorld(),112, -55, -59),BlockFace.NORTH,true,false,this));
        doorIndex.put(78,new BasementDoor(new Location(getWorld(),99, -55, -100),BlockFace.SOUTH,true,false,this));
        doorIndex.put(79,new BasementDoor(new Location(getWorld(),88, -55, -69),BlockFace.NORTH,true,false,this));
        doorIndex.put(80,new BasementDoor(new Location(getWorld(),65, -55, -76),BlockFace.EAST,true,false,this));
        doorIndex.put(81,new BasementDoor(new Location(getWorld(),44, -55, -76),BlockFace.EAST,true,false,this));
        doorIndex.put(82,new BasementDoor(new Location(getWorld(),-1, -55, -62),BlockFace.SOUTH,true,false,this));
        doorIndex.put(83,new BasementDoor(new Location(getWorld(),-41, -55, -62),BlockFace.SOUTH,true,true,this));
        doorIndex.put(84,new BasementDoor(new Location(getWorld(),36, -55, -74),BlockFace.WEST,true,false,this));
        doorIndex.put(85,new BasementDoor(new Location(getWorld(),-1, -55, -71),BlockFace.SOUTH,true,false,this));
        doorIndex.put(86,new BasementDoor(new Location(getWorld(),45, -55, -162),BlockFace.EAST,true,false,this));

        openIndex.put(0,false);
        openIndex.put(1,false);
        openIndex.put(2,false);
        openIndex.put(3,false);
        openIndex.put(4,false);
        openIndex.put(5,false);
        openIndex.put(6,false);
        openIndex.put(7,false);
        openIndex.put(8,false);
        openIndex.put(9,false);
        openIndex.put(10,false);
        openIndex.put(11,false);
        openIndex.put(12,false);
        openIndex.put(13,false);
        openIndex.put(14,false);
        openIndex.put(15,false);
        openIndex.put(16,false);
        openIndex.put(17,false);
        openIndex.put(18,false);
        openIndex.put(19,false);
        openIndex.put(20,false);
        openIndex.put(21,false);
        openIndex.put(22,false);
        openIndex.put(23,false);
        openIndex.put(24,false);
        openIndex.put(25,false);
        openIndex.put(26,false);
        openIndex.put(27,false);
        openIndex.put(28,false);
        openIndex.put(29,false);
        openIndex.put(30,false);
        openIndex.put(31,false);
        openIndex.put(32,false);
        openIndex.put(33,false);
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
        openIndex.put(72,false);
        openIndex.put(73,false);
        openIndex.put(74,false);
        openIndex.put(75,false);
        openIndex.put(76,false);
        openIndex.put(77,false);
        openIndex.put(78,false);
        openIndex.put(79,false);
        openIndex.put(80,false);
        openIndex.put(81,false);
        openIndex.put(82,false);
        openIndex.put(83,false);
        openIndex.put(84,false);
        openIndex.put(85,false);
        openIndex.put(86,false);
    }

    public void loadDoorConfigFromFile(String fileName) {
        String totalFileName = "doorConfig_"+fileName+".json";

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

                // Ensure the JsonObject only has a single entry
                if (jsonObject.entrySet().size() == 1) {
                    Map.Entry<String, JsonElement> entry = jsonObject.entrySet().iterator().next();

                    int index = Integer.parseInt(entry.getKey()); // Extract the integer (key)
                    boolean value = entry.getValue().getAsBoolean(); // Extract the boolean (value)

                    openIndex.put(index, value); // Store in the HashMap
                } else {
                    Utility.sendMessageToAllAdmins("Unexpected object structure: " + jsonObject.toString());
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

    public CatcherManager getCatcherManager() {
        return catcherManager;
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

    public void setNightTimeLights(){
        getWorld().setGameRule(GameRule.COMMAND_MODIFICATION_BLOCK_LIMIT,999999999);
        int delay = 1;
        for(int y=15; y>-64; y--) {

            int finalY = y;
            BukkitTask bukkitTask = new BukkitRunnable(){
                @Override
                public void run(){
                    //getWorld().setGameRule(GameRule.SEND_COMMAND_FEEDBACK,false);
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"fill -110 "+ finalY +" -184 104 "+ finalY +" -26 light[level=8] replace light[level=12]");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"fill -110 "+ finalY +" -184 104 "+ finalY +" -26 light[level=5] replace light[level=13]");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"fill -110 "+ finalY +" -184 104 "+ finalY +" -26 light[level=0] replace light[level=15]");
                    //getWorld().setGameRule(GameRule.SEND_COMMAND_FEEDBACK,true);
                }
            }.runTaskLater(this,20+delay);

            delay+=10;
        }
    }

    public void setDayTimeLights(){
        getWorld().setGameRule(GameRule.COMMAND_MODIFICATION_BLOCK_LIMIT,999999999);
        int delay = 0;
        for(int y=15; y>-64; y--){

            int finalY = y;
            BukkitTask bukkitTask = new BukkitRunnable(){
                @Override
                public void run(){
                    //Utility.sendMessageToAllAdmins("y is now: " + finalY);
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"fill -110 "+ finalY +" -184 104 "+ finalY +" -26 light[level=12] replace light[level=8]");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"fill -110 "+ finalY +" -184 104 "+ finalY +" -26 light[level=13] replace light[level=5]");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"fill -110 "+ finalY +" -184 104 "+ finalY +" -26 light[level=15] replace light[level=0]");
                }
            }.runTaskLater(this,20+delay);

            delay+=10;
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
            if(doorIndex.get(entry.getKey())!=null){
                if(entry.getValue()){
                    doorIndex.get(entry.getKey()).openDoor();
                }else{

                    doorIndex.get(entry.getKey()).closeDoor();
                }
            }else{
                Utility.sendMessageToAllAdmins("ERROR: entry" + entry.getKey() + " is returning null for door index");
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
    public void onGrowEvent(BlockGrowEvent event){
        if(event.getBlock().getType()==Material.VINE){
            event.setCancelled(true);
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

            //String toWrite = lastMessageSent.get(event.getPlayer().getName())+" "+event.getPlayer().getFacing().toString()+":\n"+"new Location(getWorld(),"+x+", "+y+", "+z+");";
            String toWrite = "spiderLocations.add(new Location(mainManager.getWorld(),"+x+", "+y+", "+z+"));";

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
    public void onPlayerDeath(EntityDeathEvent event){
        if(event.getEntity() instanceof Player){
            Player playerWhoDied = (Player) event.getEntity();
            for(Player player : getWorld().getPlayers()){
                player.sendMessage(playerWhoDied.getPlayerListName() + " has died and lost a life!");
            }

            Objective objective = Bukkit.getScoreboardManager().getMainScoreboard().getObjective("deaths");
            if(objective==null){
                objective = Bukkit.getScoreboardManager().getMainScoreboard().registerNewObjective("deaths", Criteria.DUMMY,"deaths");
            }
            objective.getScore(playerWhoDied.getName()).setScore(objective.getScore(playerWhoDied.getName()).getScore()-1);

        }
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event){
        if(event.getItem()!=null && event.getItem().getType()==Material.DRIED_KELP){
            ItemStack item = event.getItem();
            if(item.getItemMeta().hasCustomModelData() && item.getItemMeta().getCustomModelData()==1){
                event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED,20*40,2,true,false));
                event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP,20*40,0,true,false));
            }
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event){
        if(event.getEntity() instanceof Player){
            Player player = (Player)event.getEntity();
            if(event.getCause()== EntityDamageEvent.DamageCause.FALL){
                double damage = event.getDamage();
                double health = player.getHealth();
                // Check if the player would die from this fall
                if (health - damage <= 0) {
                    // Cancel the event to prevent death
                    event.setCancelled(true);

                    // Optionally, set the player's health to 1 or another value to prevent death
                    player.setHealth(1.0);
                }
            }

            if(event.getCause()==EntityDamageEvent.DamageCause.SUFFOCATION){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event){
        Boolean value = playerIsTrapped.getOrDefault(event.getPlayer().getName(), false);
        if (value) {
            playerIsTrapped.replace(event.getPlayer().getName(),false);
        }

        BukkitTask bukkitTask = new BukkitRunnable() {
            @Override
            public void run() {
                String playerName = Utility.getNewNameFromFile(event.getPlayer().getUniqueId());
                if(playerName != null){
                    Utility.setPlayerName(event.getPlayer(),playerName, MainManager.this);
                }
            }
        }.runTaskLater(this, 20); // 100 ticks = 5 seconds

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        String playerName = Utility.getNewNameFromFile(event.getPlayer().getUniqueId());
        if(playerName == null){
            playerName=event.getPlayer().getName();
        }else{
            Utility.setPlayerName(event.getPlayer(),playerName, this);
        }

        event.setQuitMessage(ChatColor.GOLD + playerName + " has now taken a rat nap");

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        if(!event.getPlayer().getScoreboardTags().contains("rat")){
            event.setJoinMessage(".");
        }else{
            event.getPlayer().setInvulnerable(true);

            String playerName = Utility.getNewNameFromFile(event.getPlayer().getUniqueId());
            if(playerName == null){
                playerName=event.getPlayer().getName();
            }else{
                Utility.setPlayerName(event.getPlayer(),playerName,this);
            }

            event.setJoinMessage(ChatColor.GOLD + playerName + " woke up from their rat nap");

            //checkIfReturningRat(event.getPlayer());

            if(event.getPlayer().getScoreboardTags().contains("cat")){
                event.getPlayer().getScoreboardTags().remove("rat");
            }else{
                event.getPlayer().getScoreboardTags().add("rat");
            }

            Boolean value = playerIsTrapped.getOrDefault(event.getPlayer().getName(), false);
            if (value) {
                playerIsTrapped.replace(event.getPlayer().getName(),false);
            }

            if(!event.getPlayer().getScoreboardTags().contains("cat")){
                mainTeam.addEntry(event.getPlayer().getName());
            }

            BukkitTask bukkitTask = new BukkitRunnable(){
                @Override
                public void run(){
                    event.getPlayer().setInvulnerable(false);
                }
            }.runTaskLater(this,20*20);

        }
    }

    private void checkIfReturningRat(Player player) {
        if(returningRats.contains(player.getName())){
            returningRatsTeam.addEntry(player.getName());
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        if(event.getClickedBlock()!=null && event.getPlayer().getGameMode()==GameMode.SURVIVAL){
            if(bedTypes.contains(event.getClickedBlock().getType())){
                event.setCancelled(true);
                return;
            }else if(event.getClickedBlock().getType() == Material.DETECTOR_RAIL && event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getHand() == EquipmentSlot.HAND) { // This checks for the main hand
                Powerable rail = (Powerable)event.getClickedBlock().getLocation().getBlock().getBlockData();
                if(rail.isPowered()){
                    //if the block two blocks below the detector rail is a furnace
                    Location furnaceLocation = event.getClickedBlock().getLocation().getBlock().getLocation().add(0,-2,0);
                    if(furnaceLocation.getBlock().getType()==Material.BLAST_FURNACE){
                        if(cookableItems.contains(event.getPlayer().getInventory().getItemInMainHand().getType())){
                            boolean stoveOccupied = false;
                            for(Entity entity : getWorld().getEntities()){
                                if(event.getClickedBlock().getLocation().distance(entity.getLocation())<0.4){
                                    stoveOccupied=true;
                                    break;
                                }
                            }
                            if(!stoveOccupied){
                                stoveManager.rightClickedStove(event);
                            }
                        }
                    }
                }
            }
        }

        if(event.getAction().toString().contains("RIGHT_CLICK_BLOCK")){
            if(event.getClickedBlock().getType()==Material.JUKEBOX){
                ItemStack item = event.getItem();
                if(item!=null && item.getType()==Material.MUSIC_DISC_CAT){
                    Location jukeboxLocation = event.getClickedBlock().getLocation();
                    catDiscEvent = new BukkitRunnable() {
                        @Override
                        public void run() {
                            for(Player player : getWorld().getPlayers()){
                                if(player.getLocation().distance(jukeboxLocation)<20){
                                    player.setHealth(4);
                                    player.damage(1);
                                    Location behindPlayer = Utility.pointBehind(player.getLocation(),jukeboxLocation,2);
                                    behindPlayer.add(0,0.4f,0);
                                    Vector direction = behindPlayer.toVector().subtract(player.getLocation().toVector()).normalize();
                                    player.setVelocity(direction.multiply(1.5));
                                }
                            }
                        }
                    }.runTaskLater(this, 52*20);
                }
            }
        }



        if(event.getPlayer().isOp()) {
            //op's right clicking with sticks
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getPlayer().getInventory().getItemInMainHand().getType() == Material.STICK) {
                String itemName ="ratsmp2_forgenpc:"+ event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName()+"_block";
                Utility.sendMessageToAllAdmins("player right clicked block: " + event.getClickedBlock().getLocation().getBlockX() + " " + event.getClickedBlock().getLocation().getBlockY() + " " + event.getClickedBlock().getLocation().getBlockZ() + " with: " + itemName);
                Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "setblock " + event.getClickedBlock().getLocation().getBlockX() + " " + (event.getClickedBlock().getLocation().getBlockY() - 1) + " " + event.getClickedBlock().getLocation().getBlockZ() + " " + itemName);
                if (event.getClickedBlock().getLocation().clone().add(0, 1, 0).getBlock().getType() == Material.AIR) {
                    Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "setblock " + event.getClickedBlock().getLocation().getBlockX() + " " + (event.getClickedBlock().getLocation().getBlockY() + 1) + " " + event.getClickedBlock().getLocation().getBlockZ() + " minecraft:white_banner");
                }

                String toWrite = "\n"+event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName()+"\n " +"catcherLocations.add(new Location(mainManager.getWorld(),"+event.getClickedBlock().getLocation().getBlockX()+", "+event.getClickedBlock().getLocation().getBlockY()+", "+event.getClickedBlock().getLocation().getBlockZ()+"));";
                try (PrintWriter out = new PrintWriter(new FileWriter("catcher_locations.txt", true))) {
                    out.println(toWrite);
                    Utility.sendMessageToAllAdmins("Successfully logged location: " + toWrite);
                } catch (IOException e) {
                    getLogger().warning("Failed to write to file: " + e.getMessage());
                }


            }
        }

        if(event.getAction()==Action.RIGHT_CLICK_BLOCK){
            if(event.getItem()!=null && event.getItem().getType()==Material.PAPER && event.getItem().getItemMeta().hasCustomModelData()){
                if(event.getItem().getItemMeta().getCustomModelData()==2){
                    releaseFunSnap(event.getPlayer());
                }
            }
        }
    }

    private void releaseFunSnap(Player player) {
        player.damage(2);
        player.getWorld().playSound(player.getLocation(),Sound.ENTITY_GENERIC_EXPLODE,SoundCategory.PLAYERS,1,2);
        player.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL,player.getLocation(),0,0,0,1);
        Location behindPlayer = Utility.getPositionBehindHead(player);
        Vector direction = behindPlayer.toVector().subtract(player.getLocation().toVector()).normalize();
        Random random = new Random();
        float randomFloat = random.nextFloat(1.05f,1.3f);
        player.setVelocity(direction.multiply(randomFloat));
        if(player.getInventory().getItemInMainHand().getType()==Material.PAPER){
            player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount()-1);
        }else{
            if(player.getInventory().getItemInOffHand().getType()==Material.PAPER){
                player.getInventory().getItemInOffHand().setAmount(player.getInventory().getItemInOffHand().getAmount()-1);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        if(event.getPlayer().isOp()){
            if(event.getPlayer().getInventory().getItemInMainHand().getType()==Material.STICK){
                //String stringName ="ratsmp2_forgenpc:"+ event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName()+"_catcher";
                String stringName = event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName();

                String stringToSend = "catcherSpawnLocations.put(\"" + stringName +"\", \"" + event.getBlock().getLocation().getBlockX() + " " + (event.getBlock().getLocation().getBlockY()+1) + " " + event.getBlock().getLocation().getBlockZ() + "\");";

                TextComponent message = new TextComponent("Click to copy: \n"+ stringToSend);
                message.setBold(true);
                message.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, stringToSend));
                event.getPlayer().spigot().sendMessage(message);

                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        if(event.getPlayer().getLocation().distance(teleportLocation)<1){
            teleportPlayerToStartingLocation(event.getPlayer());
        }

        Location blockID = event.getPlayer().getLocation().clone();
        Location secondBlockID = event.getPlayer().getLocation().clone();
        blockID.setY(-63);
        secondBlockID.setY(-62);

        //check if player is in basement map, if they aren't, set their basement map to false
        if(!ambientManager.getBasementPlayerMap().containsKey(event.getPlayer().getName())){
            ambientManager.getBasementPlayerMap().put(event.getPlayer().getName(),false);
        }

        //wither effect
        if(event.getPlayer().getGameMode()==GameMode.SURVIVAL){
            if(blockID.getBlock().getType()==Material.SOUL_SAND){
                //TODO: put this back
                //addWitherEffect(event.getPlayer());
            }
        }


        //Basement ambience
        if((blockID.getBlock().getType()==Material.COAL_BLOCK||blockID.getBlock().getType()==Material.COAL_ORE) && event.getPlayer().getLocation().getY()<-49){
            ambientManager.checkBasementAmbience(event.getPlayer());
        }

        //boilerroom ambience
        if(secondBlockID.getBlock().getType()==Material.DEEPSLATE_COAL_ORE && event.getPlayer().getLocation().getY()<-49){
            ambientManager.checkBoilerroomAmbience(event.getPlayer());
        }

        //Players basement ambience should be finished
        if(ambientManager.getBasementPlayerMap().get(event.getPlayer().getName())&&(blockID.getBlock().getType()!=Material.COAL_BLOCK || event.getPlayer().getLocation().getY()>-49)){
            event.getPlayer().stopSound(SoundCategory.AMBIENT);
            ambientManager.getBasementPlayerMap().put(event.getPlayer().getName(),false);
        }



        if(!playerIsTrapped.containsKey(event.getPlayer().getName())){
            playerIsTrapped.put(event.getPlayer().getName(),false);
        }else{
            if(playerIsTrapped.get(event.getPlayer().getName())&&event.getPlayer().getGameMode()==GameMode.SURVIVAL){
                if(event.getFrom().getX()!=event.getTo().getX()||event.getFrom().getY()!=event.getTo().getY()||event.getFrom().getZ()!=event.getTo().getZ()){
                    event.setCancelled(true);
                }
            }
        }

        if(event.getPlayer().getGameMode()==GameMode.SURVIVAL){
            for(Trap trap : spawnedTraps){
                if(trap.getLocation().clone().add(0.5,0,0.6).distance(event.getPlayer().getLocation())<0.4f){
                    trap.setOffTrap(event.getPlayer());
                }
            }
        }
    }

    private void teleportPlayerToStartingLocation(Player player) {
        Location dumpster = new Location(getWorld(),-88, -48, -86);
        Location balcony_across_street = new Location(getWorld(),123, -18, -117);
        Location on_sign = new Location(getWorld(),14, -2, -29);
        Location car_trunk = new Location(getWorld(),-9, -47, -16);
        Location elevator_shaft = new Location(getWorld(),58, -45, -53);
        Location kitchen_window = new Location(getWorld(),100, -40, -52);
        Location towels_changing_room = new Location(getWorld(),-68, -40, -168);
        Location garden_tree = new Location(getWorld(),30, -22, -135);
        Location library = new Location(getWorld(),97, -31, -161);
        Location balcony_dumpster = new Location(getWorld(),-72, -24, -139);

        player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF,4));
        String name = player.getName();
        if(name.equals("OwengeJuice")){
            player.teleport(balcony_across_street);
        }else if(name.equals("soupforeloise")){
            player.teleport(towels_changing_room);
        }else if(name.equals("acho")){
            player.teleport(dumpster);
        }else if(name.equals("InTheLittleWood")){
            player.teleport(balcony_dumpster);
        }else if(name.equals("bekyamon")){
            player.teleport(kitchen_window);
        }else if(name.equals("OrionSound")){
            player.teleport(on_sign);
        }else if(name.equals("Smajor1995")){
            player.getInventory().addItem(new ItemStack(Material.WRITABLE_BOOK));
            // Create the potion item

            ItemStack potion1 = new ItemStack(Material.POTION);
            PotionMeta meta1 = (PotionMeta) potion1.getItemMeta();
            if (meta1 != null) {
                // Set the potion to be Instant Health
                meta1.setBasePotionData(new PotionData(PotionType.INSTANT_HEAL));
                potion1.setItemMeta(meta1);
            }
            // Give the potion to the player
            player.getInventory().addItem(potion1);

            ItemStack potion2 = new ItemStack(Material.POTION);
            PotionMeta meta2 = (PotionMeta) potion2.getItemMeta();
            if (meta2 != null) {
                // Set the potion to be Instant Health
                meta2.setBasePotionData(new PotionData(PotionType.SLOWNESS));
                potion2.setItemMeta(meta2);
            }
            // Give the potion to the player
            player.getInventory().addItem(potion2);

            player.teleport(library);
        }else if(name.equals("watermunch")){
            player.teleport(on_sign);
        }else if(name.equals("Renthedog")){
            player.teleport(balcony_dumpster);
        }else if(name.equals("willowmvp")){
            player.teleport(car_trunk);
        }else if(name.equals("MythicalSausage")){
            player.teleport(dumpster);
        }else if(name.equals("krowfang")){
            player.teleport(garden_tree);
        }else if(name.equals("KyleEff")){
            player.teleport(kitchen_window);
        }else if(name.equals("Mogswamp")){
            player.teleport(library);
        }else if(name.equals("Roscumber")){
            player.getInventory().addItem(new ItemStack(Material.WRITABLE_BOOK));
            player.getInventory().addItem(new ItemStack(Material.CORNFLOWER));
            player.teleport(car_trunk);
        }else if(name.equals("ShubbleYT")){
            player.teleport(towels_changing_room);
        }else if(name.equals("ImaShep")){
            player.teleport(balcony_across_street);
        }else if(name.equals("Snifferish")){
            player.teleport(library);
        }else if(name.equals("apokuna")){
            player.teleport(dumpster);
        }else if(name.equals("Tubbo_")){
            player.teleport(kitchen_window);
        }else{
            Utility.sendMessageToAllAdmins("Player Name not recognised");
        }
    }

    public HashMap<String, Boolean> getPlayerIsTrapped() {
        return playerIsTrapped;
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
            if(event.getRightClicked().getScoreboardTags().contains("TapInteraction")){
                Tap tap = getTapFromInteraction((Interaction) event.getRightClicked());
                if(tap!=null){
                    tap.rotateTap();
                }else{
                    Utility.sendMessageToAllAdmins("Interaction from tap not found");
                }
            }else if(event.getRightClicked().getScoreboardTags().contains("CheeseInteraction")){
                Cheese cheese = getCheeseFromInteraction((Interaction) event.getRightClicked());
                if(cheese!=null){
                    cheese.eatCheese(event.getPlayer());
                }else{
                    Utility.sendMessageToAllAdmins("Interaction from cheese not found");
                }
            }else if(event.getRightClicked().getScoreboardTags().contains("TrapInteraction")){
                Trap trap = getTrapFromInteraction((Interaction)event.getRightClicked());
                if(trap!=null){
                    trap.trapInteracted(event.getPlayer());
                }else{
                    Utility.sendMessageToAllAdmins("Interaction from trap not found");
                }
            }else if(event.getRightClicked().getScoreboardTags().contains("RadioInteraction")){
                radio.radioInteracted(event.getPlayer());
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
            player.setVelocity(direction.multiply(1.1f));
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

    public void loadChunks(){
        ArrayList<Location> locationsToLoad = new ArrayList<>();

        
        locationsToLoad.add(new Location(getWorld(),-106, -52, -22));
        locationsToLoad.add(new Location(getWorld(),-84, -51, -24));
        locationsToLoad.add(new Location(getWorld(),-68, -50, -25));
        locationsToLoad.add(new Location(getWorld(),-55, -50, -25));
        locationsToLoad.add(new Location(getWorld(),-41, -49, -24));
        locationsToLoad.add(new Location(getWorld(),-24, -49, -24));
        locationsToLoad.add(new Location(getWorld(),-8, -48, -23));
        locationsToLoad.add(new Location(getWorld(),7, -48, -23));
        locationsToLoad.add(new Location(getWorld(),23, -47, -24));
        locationsToLoad.add(new Location(getWorld(),39, -47, -24));
        locationsToLoad.add(new Location(getWorld(),56, -46, -25));
        locationsToLoad.add(new Location(getWorld(),72, -46, -24));
        locationsToLoad.add(new Location(getWorld(),88, -45, -24));
        locationsToLoad.add(new Location(getWorld(),103, -45, -25));
        locationsToLoad.add(new Location(getWorld(),105, -45, -40));
        locationsToLoad.add(new Location(getWorld(),106, -45, -56));
        locationsToLoad.add(new Location(getWorld(),105, -45, -72));
        locationsToLoad.add(new Location(getWorld(),103, -45, -88));
        locationsToLoad.add(new Location(getWorld(),104, -45, -102));
        locationsToLoad.add(new Location(getWorld(),104, -45, -119));
        locationsToLoad.add(new Location(getWorld(),104, -45, -136));
        locationsToLoad.add(new Location(getWorld(),105, -45, -150));
        locationsToLoad.add(new Location(getWorld(),104, -45, -168));
        locationsToLoad.add(new Location(getWorld(),104, -45, -182));
        locationsToLoad.add(new Location(getWorld(),104, -45, -198));
        locationsToLoad.add(new Location(getWorld(),103, -45, -215));
        locationsToLoad.add(new Location(getWorld(),103, -45, -232));
        locationsToLoad.add(new Location(getWorld(),103, -45, -246));
        locationsToLoad.add(new Location(getWorld(),103, -45, -263));
        locationsToLoad.add(new Location(getWorld(),103, -45, -280));
        locationsToLoad.add(new Location(getWorld(),103, -45, -295));
        locationsToLoad.add(new Location(getWorld(),103, -45, -311));
        locationsToLoad.add(new Location(getWorld(),103, -45, -327));
        locationsToLoad.add(new Location(getWorld(),103, -45, -343));
        locationsToLoad.add(new Location(getWorld(),102, -45, -357));
        locationsToLoad.add(new Location(getWorld(),88, -45, -356));
        locationsToLoad.add(new Location(getWorld(),74, -46, -356));
        locationsToLoad.add(new Location(getWorld(),57, -46, -356));
        locationsToLoad.add(new Location(getWorld(),42, -47, -356));
        locationsToLoad.add(new Location(getWorld(),26, -47, -356));
        locationsToLoad.add(new Location(getWorld(),10, -48, -356));
        locationsToLoad.add(new Location(getWorld(),-7, -48, -357));
        locationsToLoad.add(new Location(getWorld(),-22, -49, -357));
        locationsToLoad.add(new Location(getWorld(),-38, -49, -356));
        locationsToLoad.add(new Location(getWorld(),-54, -50, -356));
        locationsToLoad.add(new Location(getWorld(),-70, -50, -356));
        locationsToLoad.add(new Location(getWorld(),-86, -51, -356));
        locationsToLoad.add(new Location(getWorld(),-103, -51, -356));
        locationsToLoad.add(new Location(getWorld(),-110, -52, -344));
        locationsToLoad.add(new Location(getWorld(),-110, -52, -328));
        locationsToLoad.add(new Location(getWorld(),-110, -52, -313));
        locationsToLoad.add(new Location(getWorld(),-110, -52, -298));
        locationsToLoad.add(new Location(getWorld(),-110, -52, -281));
        locationsToLoad.add(new Location(getWorld(),-110, -52, -266));
        locationsToLoad.add(new Location(getWorld(),-110, -52, -250));
        locationsToLoad.add(new Location(getWorld(),-110, -52, -233));
        locationsToLoad.add(new Location(getWorld(),-110, -52, -219));
        locationsToLoad.add(new Location(getWorld(),-110, -52, -202));
        locationsToLoad.add(new Location(getWorld(),-110, -52, -184));
        locationsToLoad.add(new Location(getWorld(),-109, -52, -169));
        locationsToLoad.add(new Location(getWorld(),-109, -52, -154));
        locationsToLoad.add(new Location(getWorld(),-109, -52, -136));
        locationsToLoad.add(new Location(getWorld(),-108, -52, -120));
        locationsToLoad.add(new Location(getWorld(),-108, -52, -106));
        locationsToLoad.add(new Location(getWorld(),-108, -52, -90));
        locationsToLoad.add(new Location(getWorld(),-108, -52, -74));
        locationsToLoad.add(new Location(getWorld(),-108, -52, -57));
        locationsToLoad.add(new Location(getWorld(),-108, -52, -40));
        locationsToLoad.add(new Location(getWorld(),-107, -52, -8));
        locationsToLoad.add(new Location(getWorld(),-107, -52, 8));
        locationsToLoad.add(new Location(getWorld(),-105, -51, 18));
        locationsToLoad.add(new Location(getWorld(),-88, -51, 18));
        locationsToLoad.add(new Location(getWorld(),-72, -50, 18));
        locationsToLoad.add(new Location(getWorld(),-56, -50, 18));
        locationsToLoad.add(new Location(getWorld(),-37, -49, 18));
        locationsToLoad.add(new Location(getWorld(),-23, -49, 18));
        locationsToLoad.add(new Location(getWorld(),-8, -48, 18));
        locationsToLoad.add(new Location(getWorld(),8, -48, 18));
        locationsToLoad.add(new Location(getWorld(),23, -47, 18));
        locationsToLoad.add(new Location(getWorld(),41, -47, 18));
        locationsToLoad.add(new Location(getWorld(),55, -46, 17));
        locationsToLoad.add(new Location(getWorld(),71, -46, 17));
        locationsToLoad.add(new Location(getWorld(),87, -45, 18));
        locationsToLoad.add(new Location(getWorld(),104, -45, 18));
        locationsToLoad.add(new Location(getWorld(),119, -45, 18));
        locationsToLoad.add(new Location(getWorld(),135, -45, 18));
        locationsToLoad.add(new Location(getWorld(),151, -45, 18));
        locationsToLoad.add(new Location(getWorld(),167, -45, 18));
        locationsToLoad.add(new Location(getWorld(),184, -45, 18));
        locationsToLoad.add(new Location(getWorld(),198, -45, 17));
        locationsToLoad.add(new Location(getWorld(),198, -45, 6));
        locationsToLoad.add(new Location(getWorld(),198, -45, -8));
        locationsToLoad.add(new Location(getWorld(),198, -45, -24));
        locationsToLoad.add(new Location(getWorld(),185, -45, -24));
        locationsToLoad.add(new Location(getWorld(),169, -45, -24));
        locationsToLoad.add(new Location(getWorld(),153, -45, -24));
        locationsToLoad.add(new Location(getWorld(),137, -45, -24));
        locationsToLoad.add(new Location(getWorld(),120, -45, -24));
        locationsToLoad.add(new Location(getWorld(),119, -45, -39));
        locationsToLoad.add(new Location(getWorld(),118, -45, -58));
        locationsToLoad.add(new Location(getWorld(),118, -45, -73));
        locationsToLoad.add(new Location(getWorld(),117, -45, -87));
        locationsToLoad.add(new Location(getWorld(),117, -45, -104));
        locationsToLoad.add(new Location(getWorld(),119, -45, -121));
        locationsToLoad.add(new Location(getWorld(),117, -45, -136));
        locationsToLoad.add(new Location(getWorld(),118, -45, -150));
        locationsToLoad.add(new Location(getWorld(),118, -45, -168));
        locationsToLoad.add(new Location(getWorld(),118, -45, -185));
        locationsToLoad.add(new Location(getWorld(),118, -45, -202));
        locationsToLoad.add(new Location(getWorld(),118, -45, -218));
        locationsToLoad.add(new Location(getWorld(),118, -45, -232));
        locationsToLoad.add(new Location(getWorld(),118, -45, -247));
        locationsToLoad.add(new Location(getWorld(),117, -45, -264));
        locationsToLoad.add(new Location(getWorld(),117, -45, -278));
        locationsToLoad.add(new Location(getWorld(),117, -45, -295));
        locationsToLoad.add(new Location(getWorld(),118, -45, -310));
        locationsToLoad.add(new Location(getWorld(),118, -45, -327));
        locationsToLoad.add(new Location(getWorld(),118, -45, -343));
        locationsToLoad.add(new Location(getWorld(),118, -45, -356));
        locationsToLoad.add(new Location(getWorld(),118, -45, -376));
        locationsToLoad.add(new Location(getWorld(),105, -45, -378));
        locationsToLoad.add(new Location(getWorld(),89, -45, -378));
        locationsToLoad.add(new Location(getWorld(),74, -46, -378));
        locationsToLoad.add(new Location(getWorld(),59, -46, -378));
        locationsToLoad.add(new Location(getWorld(),42, -47, -379));
        locationsToLoad.add(new Location(getWorld(),26, -47, -379));
        locationsToLoad.add(new Location(getWorld(),10, -48, -379));
        locationsToLoad.add(new Location(getWorld(),-7, -48, -379));
        locationsToLoad.add(new Location(getWorld(),-23, -49, -379));
        locationsToLoad.add(new Location(getWorld(),-40, -49, -379));
        locationsToLoad.add(new Location(getWorld(),-55, -50, -379));
        locationsToLoad.add(new Location(getWorld(),-71, -50, -380));
        locationsToLoad.add(new Location(getWorld(),-88, -51, -380));
        locationsToLoad.add(new Location(getWorld(),-106, -52, -380));
        locationsToLoad.add(new Location(getWorld(),-124, -52, -378));
        locationsToLoad.add(new Location(getWorld(),-125, -52, -358));
        locationsToLoad.add(new Location(getWorld(),-125, -52, -342));
        locationsToLoad.add(new Location(getWorld(),-125, -52, -329));
        locationsToLoad.add(new Location(getWorld(),-125, -52, -314));
        locationsToLoad.add(new Location(getWorld(),-125, -52, -297));
        locationsToLoad.add(new Location(getWorld(),-125, -52, -281));
        locationsToLoad.add(new Location(getWorld(),-124, -52, -265));
        locationsToLoad.add(new Location(getWorld(),-125, -52, -251));
        locationsToLoad.add(new Location(getWorld(),-125, -52, -235));
        locationsToLoad.add(new Location(getWorld(),-124, -52, -218));
        locationsToLoad.add(new Location(getWorld(),-124, -52, -200));
        locationsToLoad.add(new Location(getWorld(),-124, -52, -185));
        locationsToLoad.add(new Location(getWorld(),-124, -52, -170));
        locationsToLoad.add(new Location(getWorld(),-124, -52, -153));
        locationsToLoad.add(new Location(getWorld(),-124, -52, -138));
        locationsToLoad.add(new Location(getWorld(),-124, -52, -122));
        locationsToLoad.add(new Location(getWorld(),-124, -52, -106));
        locationsToLoad.add(new Location(getWorld(),-124, -52, -90));
        locationsToLoad.add(new Location(getWorld(),-124, -52, -74));
        locationsToLoad.add(new Location(getWorld(),-123, -52, -59));
        locationsToLoad.add(new Location(getWorld(),-123, -52, -41));
        locationsToLoad.add(new Location(getWorld(),-123, -52, -25));

        //Whole hotel:
        locationsToLoad.add(new Location(getWorld(),80, 3, -48));
        locationsToLoad.add(new Location(getWorld(),64, 3, -48));
        locationsToLoad.add(new Location(getWorld(),48, 3, -48));
        locationsToLoad.add(new Location(getWorld(),32, 3, -48));
        locationsToLoad.add(new Location(getWorld(),16, 3, -48));
        locationsToLoad.add(new Location(getWorld(),0, 3, -48));
        locationsToLoad.add(new Location(getWorld(),-16, 3, -48));
        locationsToLoad.add(new Location(getWorld(),-32, 3, -48));
        locationsToLoad.add(new Location(getWorld(),-48, 3, -48));
        locationsToLoad.add(new Location(getWorld(),-64, 3, -48));
        locationsToLoad.add(new Location(getWorld(),-64, 3, -64));
        locationsToLoad.add(new Location(getWorld(),-48, 3, -64));
        locationsToLoad.add(new Location(getWorld(),-32, 3, -64));
        locationsToLoad.add(new Location(getWorld(),-16, 3, -64));
        locationsToLoad.add(new Location(getWorld(),0, 3, -64));
        locationsToLoad.add(new Location(getWorld(),16, 3, -64));
        locationsToLoad.add(new Location(getWorld(),32, 3, -64));
        locationsToLoad.add(new Location(getWorld(),48, 3, -64));
        locationsToLoad.add(new Location(getWorld(),64, 3, -64));
        locationsToLoad.add(new Location(getWorld(),80, 3, -64));
        locationsToLoad.add(new Location(getWorld(),80, 3, -80));
        locationsToLoad.add(new Location(getWorld(),64, 3, -80));
        locationsToLoad.add(new Location(getWorld(),48, 3, -80));
        locationsToLoad.add(new Location(getWorld(),32, 3, -80));
        locationsToLoad.add(new Location(getWorld(),16, 3, -80));
        locationsToLoad.add(new Location(getWorld(),0, 3, -80));
        locationsToLoad.add(new Location(getWorld(),-16, 3, -80));
        locationsToLoad.add(new Location(getWorld(),-32, 3, -80));
        locationsToLoad.add(new Location(getWorld(),-48, 3, -80));
        locationsToLoad.add(new Location(getWorld(),-64, 3, -80));
        locationsToLoad.add(new Location(getWorld(),-64, 3, -96));
        locationsToLoad.add(new Location(getWorld(),-48, 3, -96));
        locationsToLoad.add(new Location(getWorld(),-32, 2, -96));
        locationsToLoad.add(new Location(getWorld(),-16, -2, -96));
        locationsToLoad.add(new Location(getWorld(),0, -2, -96));
        locationsToLoad.add(new Location(getWorld(),16, -2, -96));
        locationsToLoad.add(new Location(getWorld(),32, -2, -96));
        locationsToLoad.add(new Location(getWorld(),48, -2, -96));
        locationsToLoad.add(new Location(getWorld(),64, 3, -96));
        locationsToLoad.add(new Location(getWorld(),80, 3, -96));
        locationsToLoad.add(new Location(getWorld(),80, 3, -112));
        locationsToLoad.add(new Location(getWorld(),64, 3, -112));
        locationsToLoad.add(new Location(getWorld(),48, -46, -112));
        locationsToLoad.add(new Location(getWorld(),32, -46, -112));
        locationsToLoad.add(new Location(getWorld(),16, -46, -112));
        locationsToLoad.add(new Location(getWorld(),0, -46, -112));
        locationsToLoad.add(new Location(getWorld(),-16, -46, -112));
        locationsToLoad.add(new Location(getWorld(),-32, 2, -112));
        locationsToLoad.add(new Location(getWorld(),-48, 3, -112));
        locationsToLoad.add(new Location(getWorld(),-64, 3, -112));
        locationsToLoad.add(new Location(getWorld(),-64, 3, -128));
        locationsToLoad.add(new Location(getWorld(),-48, 3, -128));
        locationsToLoad.add(new Location(getWorld(),-32, 2, -128));
        locationsToLoad.add(new Location(getWorld(),-16, -46, -128));
        locationsToLoad.add(new Location(getWorld(),0, -46, -128));
        locationsToLoad.add(new Location(getWorld(),16, -46, -128));
        locationsToLoad.add(new Location(getWorld(),32, -44, -128));
        locationsToLoad.add(new Location(getWorld(),48, -46, -128));
        locationsToLoad.add(new Location(getWorld(),64, 3, -128));
        locationsToLoad.add(new Location(getWorld(),80, 3, -128));
        locationsToLoad.add(new Location(getWorld(),80, 3, -144));
        locationsToLoad.add(new Location(getWorld(),64, 3, -144));
        locationsToLoad.add(new Location(getWorld(),48, -46, -144));
        locationsToLoad.add(new Location(getWorld(),32, -46, -144));
        locationsToLoad.add(new Location(getWorld(),16, -46, -144));
        locationsToLoad.add(new Location(getWorld(),0, -46, -144));
        locationsToLoad.add(new Location(getWorld(),-16, -46, -144));
        locationsToLoad.add(new Location(getWorld(),-32, 2, -144));
        locationsToLoad.add(new Location(getWorld(),-48, 3, -144));
        locationsToLoad.add(new Location(getWorld(),-64, 3, -144));
        locationsToLoad.add(new Location(getWorld(),-64, 3, -160));
        locationsToLoad.add(new Location(getWorld(),-48, 3, -160));
        locationsToLoad.add(new Location(getWorld(),-32, 3, -160));
        locationsToLoad.add(new Location(getWorld(),-16, 3, -160));
        locationsToLoad.add(new Location(getWorld(),0, 3, -160));
        locationsToLoad.add(new Location(getWorld(),16, 3, -160));
        locationsToLoad.add(new Location(getWorld(),32, 3, -160));
        locationsToLoad.add(new Location(getWorld(),48, 3, -160));
        locationsToLoad.add(new Location(getWorld(),64, 3, -160));
        locationsToLoad.add(new Location(getWorld(),80, 3, -160));
        locationsToLoad.add(new Location(getWorld(),80, -2, -176));
        locationsToLoad.add(new Location(getWorld(),64, -2, -176));
        locationsToLoad.add(new Location(getWorld(),48, -2, -176));
        locationsToLoad.add(new Location(getWorld(),32, -2, -176));
        locationsToLoad.add(new Location(getWorld(),16, -2, -176));
        locationsToLoad.add(new Location(getWorld(),0, -2, -176));
        locationsToLoad.add(new Location(getWorld(),-16, -2, -176));
        locationsToLoad.add(new Location(getWorld(),-32, -2, -176));
        locationsToLoad.add(new Location(getWorld(),-48, -2, -176));
        locationsToLoad.add(new Location(getWorld(),-64, -2, -176));

        //Safety inspector location
        locationsToLoad.add(new Location(getWorld(),14, -47, -349));

        for(Location location : locationsToLoad){
            location.getChunk().setForceLoaded(true);
            location.getChunk().load();
        }
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

    public void updateLights() {
        if(isNight()){
            setNightTimeLights();
        }else{
            setDayTimeLights();
        }
    }

    public void summonTinySpider(Location location,int numberOfSpiders) {

        Random random = new Random();
        int randomindex;
        Location locationToSpawn;

        for (int i = 0; i < numberOfSpiders; i++) {
            randomindex = random.nextInt(spiderLocations.size());
            locationToSpawn = spiderLocations.get(randomindex);
            if(locationToSpawn==lastSpiderLocation){
                randomindex = random.nextInt(spiderLocations.size());
                locationToSpawn = spiderLocations.get(randomindex);
            }

            Entity entity = location.getWorld().spawnEntity(locationToSpawn, EntityType.SPIDER);
            entity.setPersistent(true);
            ((LivingEntity)entity).addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,20,1,false,false));
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"execute as "+ entity.getUniqueId() +" run scale set pehkui:base 0.2 @s");
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"execute as "+ entity.getUniqueId() +" run scale set pehkui:hitbox_width 2 @s");
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"execute as "+ entity.getUniqueId() +" run scale set pehkui:hitbox_height 2 @s");
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"execute as "+ entity.getUniqueId() +" run scale set pehkui:motion 0.8 @s");
        }
    }

    public void summonTinySpiderAtLoc(Location location,int numberOfSpiders) {
        for (int i = 0; i < numberOfSpiders; i++) {
            Entity entity = location.getWorld().spawnEntity(location, EntityType.SPIDER);
            entity.setPersistent(true);
            ((LivingEntity)entity).addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,20,1,false,false));
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"execute as "+ entity.getUniqueId() +" run scale set pehkui:base 0.2 @s");
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"execute as "+ entity.getUniqueId() +" run scale set pehkui:hitbox_width 2 @s");
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"execute as "+ entity.getUniqueId() +" run scale set pehkui:hitbox_height 2 @s");
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"execute as "+ entity.getUniqueId() +" run scale set pehkui:motion 0.8 @s");
        }
    }
}

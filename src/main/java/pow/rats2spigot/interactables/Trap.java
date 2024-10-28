package pow.rats2spigot.interactables;

import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.Objective;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import pow.rats2spigot.MainManager;
import pow.rats2spigot.util.Utility;

import java.util.Objects;
import java.util.Random;

public class Trap {
    Location location;
    ItemDisplay baseItemDisplay;
    ItemDisplay barItemDisplay;
    ItemDisplay cheeseItemDisplay;
    Interaction interaction;
    Random random = new Random();
    MainManager mainManager;
    boolean set = true;
    boolean hasCheese = true;

    public Trap(Location location, MainManager mainManager) {
        this.location = location;
        this.mainManager = mainManager;
        createItemDisplay();
        createInteraction();
    }

    public Location getLocation() {
        return location;
    }

    public Interaction getInteraction() {
        return interaction;
    }

    public ItemDisplay getItemDisplay() {
        return baseItemDisplay;
    }

    private void createInteraction() {
        Location interactionLocation = location.clone();
        interactionLocation.add(0.5,0,0.5);
        interaction = (Interaction) interactionLocation.getWorld().spawnEntity(interactionLocation,EntityType.INTERACTION);
        interaction.getScoreboardTags().add("TrapInteraction");
        interaction.setInteractionHeight(0.5f);
        interaction.setInteractionWidth(0.5f);
        interaction.setResponsive(true);
    }
    private void createItemDisplay() {
        // Set offset coordinates and rotation angle
        Location displayLocation = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
        // Create a ItemDisplay entity
        baseItemDisplay = (ItemDisplay) displayLocation.getWorld().spawnEntity(displayLocation, EntityType.ITEM_DISPLAY);
        baseItemDisplay.getScoreboardTags().add("TrapItemDisplay");

        // Set the block data to display
        ItemStack trapModel = new ItemStack(Material.CARVED_PUMPKIN);
        ItemMeta meta = trapModel.getItemMeta();
        meta.setCustomModelData(20002);
        trapModel.setItemMeta(meta);
        //itemDisplay.setItemStack(new ItemStack(Material.IRON_BLOCK));
        baseItemDisplay.setItemStack(trapModel);

        Transformation transform = new Transformation(
                new Vector3f(0.5f,0.035f,0.5f),                          // translation
                new Quaternionf(),               // left rotation
                new Vector3f(1.5f, 1.5f, 1.5f),  // scale
                new Quaternionf()                // right rotation
        );
        baseItemDisplay.setTransformation(transform);

        //-----------------

        // Create a ItemDisplay entity
        barItemDisplay = (ItemDisplay) displayLocation.getWorld().spawnEntity(displayLocation, EntityType.ITEM_DISPLAY);
        barItemDisplay.getScoreboardTags().add("TrapItemDisplay");

        // Set the block data to display
        ItemStack barModel = new ItemStack(Material.CARVED_PUMPKIN);
        ItemMeta barMeta = barModel.getItemMeta();
        barMeta.setCustomModelData(20003);
        barModel.setItemMeta(barMeta);
        //itemDisplay.setItemStack(new ItemStack(Material.IRON_BLOCK));
        barItemDisplay.setItemStack(barModel);

        barItemDisplay.setTransformation(transform);

        //----------

        // Create a ItemDisplay entity
        cheeseItemDisplay = (ItemDisplay) displayLocation.getWorld().spawnEntity(displayLocation, EntityType.ITEM_DISPLAY);
        cheeseItemDisplay.getScoreboardTags().add("TrapItemDisplay");

        // Set the block data to display
        ItemStack cheeseModel = new ItemStack(Material.CARVED_PUMPKIN);
        ItemMeta cheeseMeta = cheeseModel.getItemMeta();
        cheeseMeta.setCustomModelData(20001);
        cheeseModel.setItemMeta(cheeseMeta);
        cheeseItemDisplay.setItemStack(cheeseModel);

        cheeseItemDisplay.setTransformation(transform);
    }

    public void trapInteracted(Player player) {

        if(set){
            if(hasCheese){
                if(random.nextInt(100)>50){
                    setOffTrap(player);
                }else{
                    Objective objective = Bukkit.getScoreboardManager().getMainScoreboard().getObjective("traps_avoided");
                    if(objective==null){
                        objective = Bukkit.getScoreboardManager().getMainScoreboard().registerNewObjective("traps_avoided", Criteria.DUMMY,"traps_avoided");
                    }
                    objective.getScore(player.getName()).setScore(objective.getScore(player.getName()).getScore()+1);

                    givePlayerCheese(player);
                }
            }else{
                disarmTrap();
            }
        }else{
            //Trap is already set off
            if(hasCheese){
                givePlayerCheese(player);
            }else{
                //remove trap
                cleanUpTrap();
            }
        }
    }



    private void cleanUpTrap() {
        baseItemDisplay.remove();
        barItemDisplay.remove();
        cheeseItemDisplay.remove();
        interaction.remove();
        mainManager.getRemainingTrapLocations().add(location);
    }

    private void givePlayerCheese(Player player){
        hasCheese=false;
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"give " + player.getName() + " ratsmp2_forgenpc:cheese");
        location.getWorld().playSound(location,Sound.ENTITY_FISHING_BOBBER_RETRIEVE,SoundCategory.BLOCKS,1,1);
        //player.getInventory().addItem(new ItemStack(Material.BREAD,1));
        cheeseItemDisplay.remove();
    }

    public void trapPlayer(Player player){
        Objective objective = Bukkit.getScoreboardManager().getMainScoreboard().getObjective("traps_caught");
        if(objective==null){
            objective = Bukkit.getScoreboardManager().getMainScoreboard().registerNewObjective("traps_caught", Criteria.DUMMY,"traps_avoided");
        }
        objective.getScore(player.getName()).setScore(objective.getScore(player.getName()).getScore()+1);

        player.damage(10);
        Location trapLocation = location.clone().add(0.5,0,0.5);
        trapLocation.setYaw(0);
        trapLocation.setPitch(90);
        player.teleport(trapLocation);
        mainManager.getPlayerIsTrapped().replace(player.getName(),true);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,400,1,false,false));
        BukkitTask bukkitTask = new BukkitRunnable() {
            @Override
            public void run() {
                mainManager.getPlayerIsTrapped().replace(player.getName(),false);
            }
        }.runTaskLater(mainManager, 200);
    }

    private void disarmTrap() {
        if(set){
            set=false;
            // Set the block data to display
            location.getWorld().playSound(location,Sound.ITEM_CROSSBOW_SHOOT,SoundCategory.BLOCKS,1,1);

            Transformation transform = barItemDisplay.getTransformation();

            Quaternionf rotation = null;
            rotation = new Quaternionf().rotationX((float) Math.toRadians(170));

            // Apply rotation
            transform.getLeftRotation().mul(rotation);

            // Set new transformation
            barItemDisplay.setTransformation(transform);
            barItemDisplay.setInterpolationDelay(1);
            barItemDisplay.setInterpolationDuration(1);
        }
    }

    public void setOffTrap(Player player) {

        if(set){

            trapPlayer(player);

            set=false;
            // Set the block data to display
            location.getWorld().playSound(location,Sound.ITEM_CROSSBOW_SHOOT,SoundCategory.BLOCKS,1,1);

            Transformation transform = barItemDisplay.getTransformation();

            Quaternionf rotation = null;
            rotation = new Quaternionf().rotationX((float) Math.toRadians(170));

            // Apply rotation
            transform.getLeftRotation().mul(rotation);

            // Set new transformation
            barItemDisplay.setTransformation(transform);
            barItemDisplay.setInterpolationDelay(1);
            barItemDisplay.setInterpolationDuration(1);
        }


    }
}

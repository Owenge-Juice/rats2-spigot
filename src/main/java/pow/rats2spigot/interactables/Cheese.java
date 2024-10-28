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
import org.bukkit.scoreboard.Score;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import pow.rats2spigot.CatcherRunnable;
import pow.rats2spigot.MainManager;

import java.util.Random;

public class Cheese {
    Location location;
    ItemDisplay itemDisplay;
    Interaction interaction;
    Random random = new Random();
    MainManager mainManager;
    int chunksRemaining = 3;

    public Cheese(Location location, MainManager mainManager) {
        this.location = location;
        this.mainManager = mainManager;
        createItemDisplay();
        createInteraction();
    }

    public Interaction getInteraction() {
        return interaction;
    }

    public ItemDisplay getItemDisplay() {
        return itemDisplay;
    }

    private void createInteraction() {
        Location interactionLocation = location.clone();
        interactionLocation.add(0.5,0,0.5);
        interaction = (Interaction) interactionLocation.getWorld().spawnEntity(interactionLocation,EntityType.INTERACTION);
        interaction.getScoreboardTags().add("CheeseInteraction");
        interaction.setInteractionHeight(0.5f);
        interaction.setInteractionWidth(0.5f);
        interaction.setResponsive(true);
    }
    private void createItemDisplay() {
        // Set offset coordinates and rotation angle
        Location displayLocation = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
        // Create a ItemDisplay entity
        itemDisplay = (ItemDisplay) displayLocation.getWorld().spawnEntity(displayLocation, EntityType.ITEM_DISPLAY);
        itemDisplay.getScoreboardTags().add("CheeseItemDisplay");

        // Set the block data to display
        ItemStack cheeseModel = new ItemStack(Material.CARVED_PUMPKIN);
        ItemMeta meta = cheeseModel.getItemMeta();
        meta.setCustomModelData(10002);
        cheeseModel.setItemMeta(meta);
        //itemDisplay.setItemStack(new ItemStack(Material.IRON_BLOCK));
        itemDisplay.setItemStack(cheeseModel);

        Transformation transform = new Transformation(
                new Vector3f(0.5f,0.6f,0.5f),                          // translation
                new Quaternionf(),               // left rotation
                new Vector3f(1.2f, 1.2f, 1.2f),  // scale
                new Quaternionf()                // right rotation
        );
        itemDisplay.setTransformation(transform);

    }


    public void eatCheese(Player player){
        Objective objective = Bukkit.getScoreboardManager().getMainScoreboard().getObjective("cheese_eaten");
        if(objective==null){
            objective = Bukkit.getScoreboardManager().getMainScoreboard().registerNewObjective("cheese_eaten", Criteria.DUMMY,"cheese_eaten");
        }
        objective.getScore(player.getName()).setScore(objective.getScore(player.getName()).getScore()+1);



        chunksRemaining-=1;
        player.playSound(player,Sound.ENTITY_GENERIC_EAT,SoundCategory.PLAYERS,0.5f,1);
        player.setFoodLevel(player.getFoodLevel()+7);
        //player.setSaturation(player.getSaturation()+4);
        random.nextInt(100);
        if(random.nextInt(100)>80){

            BukkitTask bukkitTask = new BukkitRunnable() {
                @Override
                public void run() {
                    poisonPlayer(player);
                }
            }.runTaskLater(mainManager, 40); // 2 seconds

        }

        if(chunksRemaining<=0){
            itemDisplay.remove();
            interaction.remove();
            mainManager.getRemainingCheeseLocations().add(location);
        }else{
            // Set the block data to display
            ItemStack cheeseModel = new ItemStack(Material.CARVED_PUMPKIN);
            ItemMeta meta = cheeseModel.getItemMeta();

            meta.setCustomModelData(itemDisplay.getItemStack().getItemMeta().getCustomModelData()+1);
            cheeseModel.setItemMeta(meta);
            //itemDisplay.setItemStack(new ItemStack(Material.IRON_BLOCK));
            itemDisplay.setItemStack(cheeseModel);
        }

    }

    private void poisonPlayer(Player player) {
        Objective objective = Bukkit.getScoreboardManager().getMainScoreboard().getObjective("cheese_poisoned");
        if(objective==null){
            objective = Bukkit.getScoreboardManager().getMainScoreboard().registerNewObjective("cheese_poisoned", Criteria.DUMMY,"cheese_poisoned");
        }
        objective.getScore(player.getName()).setScore(objective.getScore(player.getName()).getScore()+1);

        player.addPotionEffect(new PotionEffect(PotionEffectType.POISON,220,1,true,true));
    }


}

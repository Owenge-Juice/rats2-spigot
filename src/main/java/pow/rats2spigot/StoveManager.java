package pow.rats2spigot;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SoundCategory;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import pow.rats2spigot.runnables.AmbientRunnable;
import pow.rats2spigot.util.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class StoveManager {

    MainManager mainManager;
    private StoveRunnable stoveRunnable;
    private HashMap<UUID,Integer> cookedFoods = new HashMap<>();
    private HashMap<Material, Material> cookingConversion = new HashMap<>();

    public StoveManager(MainManager mainManager) {
        this.mainManager = mainManager;
        init_cookingConversion();
        stoveRunnable = new StoveRunnable(this, mainManager);

    }

    private void init_cookingConversion() {
        cookingConversion.put(Material.BEEF,Material.COOKED_BEEF);
        cookingConversion.put(Material.CHICKEN,Material.COOKED_CHICKEN);
        cookingConversion.put(Material.PORKCHOP,Material.COOKED_PORKCHOP);
        cookingConversion.put(Material.RABBIT,Material.COOKED_RABBIT);
        cookingConversion.put(Material.MUTTON,Material.COOKED_MUTTON);
        cookingConversion.put(Material.COD,Material.COOKED_COD);
        cookingConversion.put(Material.SALMON,Material.COOKED_SALMON);
        cookingConversion.put(Material.POTATO,Material.BAKED_POTATO);
        cookingConversion.put(Material.KELP,Material.DRIED_KELP);
    }

    public HashMap<UUID, Integer> getCookedFoods() {
        return cookedFoods;
    }

    public HashMap<Material, Material> getCookingConversion() {
        return cookingConversion;
    }

    public void rightClickedStove(PlayerInteractEvent event){
        Utility.sendMessageToAllAdmins("right clicked stove");
        ItemStack itemInHand = event.getPlayer().getInventory().getItemInMainHand();
        Location location = event.getClickedBlock().getLocation();
        // Set offset coordinates and rotation angle
        Location displayLocation = new Location(event.getClickedBlock().getWorld(), location.getX(), location.getY(), location.getZ());
        // Create a ItemDisplay entity
        ItemDisplay itemDisplay = (ItemDisplay) displayLocation.getWorld().spawnEntity(displayLocation, EntityType.ITEM_DISPLAY);
        itemDisplay.getScoreboardTags().add("StoveItemDisplay");

        Quaternionf rotation = new Quaternionf().rotationX((float) Math.toRadians(90));
        Transformation transform = new Transformation(
                new Vector3f(0.5f,0.1f,0.5f),                          // translation
                new Quaternionf().mul(rotation),               // left rotation
                new Vector3f(1f, 1f, 1f),  // scale
                new Quaternionf()                // right rotation
        );
        itemDisplay.setTransformation(transform);

        // Set the block data to display
        itemDisplay.setItemStack(new ItemStack(itemInHand.getType()));

        cookedFoods.put(itemDisplay.getUniqueId(),0);
        event.getClickedBlock().getWorld().playSound(event.getClickedBlock().getLocation(),"farmersdelight:block.skillet.sizzle",SoundCategory.BLOCKS,1,1);


        if(itemInHand.getAmount() > 1) {
            // Reduce the item stack size by 1
            itemInHand.setAmount(itemInHand.getAmount() - 1);
        } else {
            // Remove the item completely if there's only 1 left
            event.getPlayer().getInventory().setItemInMainHand(null);
        }

    }

}

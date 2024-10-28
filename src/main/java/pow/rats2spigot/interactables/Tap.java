package pow.rats2spigot.interactables;

import jdk.tools.jmod.Main;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Score;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import pow.rats2spigot.MainManager;

import java.util.Random;

public class Tap {
    Location location;
    Location showerHeadLocation;
    ItemDisplay itemDisplay;
    Interaction interaction;
    BlockFace blockFace;
    boolean turned;
    Random random = new Random();
    Score tapRunning;
    MainManager mainManager;

    public Tap(Location location, BlockFace blockFace, Location showerHeadLocation, MainManager mainManager) {
        this.location = location;
        this.blockFace = blockFace;
        this.showerHeadLocation = showerHeadLocation.add(0.5,0.8f,0.5);
        turned=false;
        this.mainManager = mainManager;
        createItemDisplay();
        createInteraction();

        tapRunning= Bukkit.getScoreboardManager().getMainScoreboard().getObjective("taps").getScore(showerHeadLocation.toString());
        if(tapRunning.getScore()<1){
            tapRunning.setScore(1);
        }
    }

    public Interaction getInteraction() {
        return interaction;
    }

    public ItemDisplay getItemDisplay() {
        return itemDisplay;
    }

    public boolean isTurned() {
        return turned;
    }

    public void drip(){
        Location dropletLocation = showerHeadLocation.clone();
        dropletLocation.add(random.nextFloat(-0.05f,0.05f),0,random.nextFloat(-0.05f,0.05f));
        if(random.nextBoolean()){
            dropletLocation.getWorld().spawnParticle(Particle.DRIP_WATER,dropletLocation,1);
        }

        dropletLocation.add(random.nextFloat(-0.1f,0.1f),0,random.nextFloat(-0.1f,0.1f));

        dropletLocation.getWorld().spawnParticle(Particle.FALLING_WATER,dropletLocation,1);
        dropletLocation.add(random.nextFloat(-0.1f,0.1f),0,random.nextFloat(-0.1f,0.1f));
        dropletLocation.getWorld().spawnParticle(Particle.FALLING_WATER,dropletLocation,1);

        tapRunning.setScore(tapRunning.getScore()+1);
    }

    private void createInteraction() {
        Location interactionLocation = location.clone();
        applyOffsetToLocation(interactionLocation,blockFace);
        interaction = (Interaction) interactionLocation.getWorld().spawnEntity(interactionLocation,EntityType.INTERACTION);
        interaction.getScoreboardTags().add("TapInteraction");
        interaction.setInteractionHeight(0.5f);
        interaction.setInteractionWidth(0.5f);
        interaction.setResponsive(true);
    }

    private void createItemDisplay() {
        // Set offset coordinates and rotation angle
        Location displayLocation = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
        // Create a ItemDisplay entity
        itemDisplay = (ItemDisplay) displayLocation.getWorld().spawnEntity(displayLocation, EntityType.ITEM_DISPLAY);


        itemDisplay.getScoreboardTags().add("TapItemDisplay");
        // Set the block data to display
        ItemStack tapModel = new ItemStack(Material.CARVED_PUMPKIN);
        ItemMeta meta = tapModel.getItemMeta();
        meta.setCustomModelData(10001);
        tapModel.setItemMeta(meta);
        //itemDisplay.setItemStack(new ItemStack(Material.IRON_BLOCK));
        itemDisplay.setItemStack(tapModel);



        Transformation transform = new Transformation(
                new Vector3f(0.5f,0.5f,0.5f),                        // translation
                new Quaternionf(),               // left rotation
                new Vector3f(1f, 1f, 1f),  // scale
                new Quaternionf()                // right rotation
        );
        itemDisplay.setTransformation(transform);


        transform = itemDisplay.getTransformation();
        Quaternionf rotation = null;
        switch (blockFace) {
            case NORTH: rotation = new Quaternionf().rotationY((float) Math.toRadians(0*1));
                break;
            case SOUTH: rotation = new Quaternionf().rotationY((float) Math.toRadians(180*1));
                break;
            case EAST:  rotation = new Quaternionf().rotationY((float) Math.toRadians(270*1));
                break;
            case WEST:  rotation = new Quaternionf().rotationY((float) Math.toRadians(90*1));
                break;
            default:    rotation = null;
        }

        // Apply rotation
        transform.getLeftRotation().mul(rotation);
        // Set new transformation
        itemDisplay.setTransformation(transform);


    }

    private Vector3f calculateOffset(BlockFace face) {

        switch (face) {
            case NORTH: return new Vector3f(0.5f,0.5f,0.5f);
            case SOUTH: return new Vector3f(0.5f, 0.5f, 0.5f);
            case EAST:  return new Vector3f(0.5f, 0.5f, 0.5f);
            case WEST:  return new Vector3f(0.5f, 0.5f, 0.5f);
            default:    return new Vector3f(0f,0f,0f);
        }
    }

    public void rotateTap(){

        int rotationOpposite;
        int randomInt = random.nextInt(3);
        if(turned){
            rotationOpposite=-1;
            location.getWorld().playSound(location,"rats2:powevents.sound.faucet_close_"+randomInt, SoundCategory.MASTER,1,1);
            for(Player player : Bukkit.getOnlinePlayers()){
                if(player.getLocation().distance(location)<16){
                    player.stopSound(SoundCategory.BLOCKS);
                }
            }
        }else{
            rotationOpposite=1;
            location.getWorld().playSound(location,"rats2:powevents.sound.faucet_open_"+randomInt,SoundCategory.MASTER,1,1);
            playRunningShowerSound();

        }


        Transformation transform = itemDisplay.getTransformation();

        Quaternionf rotation = null;
        if(blockFace ==BlockFace.NORTH){
            rotation = new Quaternionf().rotationZ((float) Math.toRadians(-150*rotationOpposite));
        }else if(blockFace ==BlockFace.SOUTH){
            rotation = new Quaternionf().rotationZ((float) Math.toRadians(-150*rotationOpposite));
        }else if(blockFace ==BlockFace.EAST){
            rotation = new Quaternionf().rotationZ((float) Math.toRadians(-150*rotationOpposite));
        }else if(blockFace ==BlockFace.WEST){
            rotation = new Quaternionf().rotationZ((float) Math.toRadians(-150*rotationOpposite));
        }

        // Apply rotation
        transform.getLeftRotation().mul(rotation);

        // Set new transformation
        itemDisplay.setTransformation(transform);
        itemDisplay.setInterpolationDelay(1);
        itemDisplay.setInterpolationDuration(5);

        turned=!turned;

    }

    private void playRunningShowerSound() {
        showerHeadLocation.getWorld().playSound(showerHeadLocation,"rats2:powevents.sound.running_shower",SoundCategory.BLOCKS,1,1);
        BukkitTask bukkitTask = new BukkitRunnable() {
            @Override
            public void run() {
                if(turned){
                    playRunningShowerSound();
                }
            }
        }.runTaskLater(mainManager, 15*20); // 100 ticks = 5 seconds
    }



    private Location applyOffsetToLocation(Location location, BlockFace face){
        switch (face) {
            case NORTH: return location.add(0.5f,0.25f,0.25);
            case SOUTH: return location.add(0.5f, 0.25f, 0.75f);
            case EAST:  return location.add(0.75f, 0.25f, 0.5f);
            case WEST:  return location.add(0.25f, 0.25f, 0.5f);
            default:    return location.add(0f,0f,0);
        }
    }


}

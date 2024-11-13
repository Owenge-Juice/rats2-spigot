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
import pow.rats2spigot.RadioRunnable;
import pow.rats2spigot.util.Utility;

import java.util.Random;

public class Radio {
    Location location;
    ItemDisplay itemDisplay;
    ItemDisplay radioDialItemDisplay;
    Interaction interaction;
    Random random = new Random();
    MainManager mainManager;
    boolean playingSong = false;
    RadioRunnable radioRunnable;
    BukkitTask bukkitTask;
    int lastPlayedSong=-1;

    public Radio(Location location, MainManager mainManager) {
        this.location = location;
        this.mainManager = mainManager;
        createItemDisplay();
        createInteraction();
        radioRunnable = new RadioRunnable(this, mainManager);
    }

    public Interaction getInteraction() {
        return interaction;
    }

    public ItemDisplay getItemDisplay() {
        return itemDisplay;
    }

    public Location getLocation() {
        return location;
    }

    public boolean isPlayingSong() {
        return playingSong;
    }

    public void radioInteracted(Player playerWhoInteracted){
        Transformation transform = radioDialItemDisplay.getTransformation();

        Quaternionf rotation;
        if(playingSong){
            rotation = new Quaternionf().rotationZ((float) Math.toRadians(75));
        }else{
            rotation = new Quaternionf().rotationZ((float) Math.toRadians(-75));
        }

        // Apply rotation
        transform.getLeftRotation().mul(rotation);

        // Set new transformation
        radioDialItemDisplay.setTransformation(transform);
        radioDialItemDisplay.setInterpolationDelay(1);
        radioDialItemDisplay.setInterpolationDuration(4);

        location.getWorld().playSound(location,"rats2:powevents.sound.radioclick",SoundCategory.BLOCKS,1f,1f);
        if(playingSong){
            for(Player player : mainManager.getWorld().getPlayers()){
                player.stopSound(SoundCategory.RECORDS);
            }
            playingSong=false;
        }else{
            playSong();
            playingSong=true;
        }
    }

    private void playSong() {
        if(bukkitTask!=null){
            bukkitTask.cancel();
        }

        int randomInt = random.nextInt(90);
        Utility.sendMessageToAllAdmins(randomInt+"");
        //0-19
        if(randomInt<=16){
            if(lastPlayedSong==1){
                playSong();
            }else{
                lastPlayedSong=1;
                location.getWorld().playSound(location,"rats2:powevents.sound.radiosong1",SoundCategory.RECORDS,0.5f,1f);
                cueStop((95)*20);
            }
        }else if(randomInt<=32){
            if(lastPlayedSong==2){
                playSong();
            }else{
                lastPlayedSong=2;
                location.getWorld().playSound(location,"rats2:powevents.sound.radiosong2",SoundCategory.RECORDS,0.5f,1f);
                cueStop((113)*20);
            }

        }else if(randomInt<=48){
            if(lastPlayedSong==3){
                playSong();
            }else{
                lastPlayedSong=3;
                location.getWorld().playSound(location,"rats2:powevents.sound.radiosong3",SoundCategory.RECORDS,0.5f,1f);
                cueStop(((5*60)+(1))*20);
            }

            //Unlock below oe closer to christmas
        }else if(randomInt<=64){
            if(lastPlayedSong==4){
                playSong();
            }else{
                lastPlayedSong=4;

                location.getWorld().playSound(location,"rats2:powevents.sound.radiosong4",SoundCategory.RECORDS,0.5f,1f);
                cueStop((143)*20);
            }

        }else if(randomInt<=80){
            if(lastPlayedSong==7){
                playSong();
            }else{
                lastPlayedSong=7;
                location.getWorld().playSound(location,"rats2:powevents.sound.radiosong6",SoundCategory.RECORDS,0.5f,1f);
                cueStop((116)*20);
            }
        }else{
            if(lastPlayedSong==4){
                playSong();
            }else{
                lastPlayedSong=4;
                location.getWorld().playSound(location,"rats2:powevents.sound.radiosong7",SoundCategory.RECORDS,0.5f,1f);
                cueStop((39)*20);
            }
        }
    }

    private void cueStop(int ticks) {
        if(bukkitTask!=null){
            bukkitTask.cancel();
        }
        bukkitTask = new BukkitRunnable(){
            @Override
            public void run(){
                if(playingSong){
                    Transformation transform = radioDialItemDisplay.getTransformation();

                    Quaternionf rotation = new Quaternionf().rotationZ((float) Math.toRadians(75));

                    // Apply rotation
                    transform.getLeftRotation().mul(rotation);

                    // Set new transformation
                    radioDialItemDisplay.setTransformation(transform);
                    radioDialItemDisplay.setInterpolationDelay(1);
                    radioDialItemDisplay.setInterpolationDuration(4);

                    playingSong=false;
                }
            }
        }.runTaskLater(mainManager,ticks);
    }


    private void createInteraction() {
        Location interactionLocation = location.clone();
        interactionLocation.add(0.3f,0.02f,0.35f);
        interaction = (Interaction) interactionLocation.getWorld().spawnEntity(interactionLocation,EntityType.INTERACTION);
        interaction.getScoreboardTags().add("RadioInteraction");
        interaction.setInteractionHeight(0.6f);
        interaction.setInteractionWidth(0.5f);
        interaction.setResponsive(true);
    }
    private void createItemDisplay() {
        // Set offset coordinates and rotation angle
        Location displayLocation = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
        // Create a ItemDisplay entity
        itemDisplay = (ItemDisplay) displayLocation.getWorld().spawnEntity(displayLocation, EntityType.ITEM_DISPLAY);
        itemDisplay.getScoreboardTags().add("RadioItemDisplay");

        // Set the block data to display
        ItemStack radioModel = new ItemStack(Material.CARVED_PUMPKIN);
        ItemMeta meta = radioModel.getItemMeta();
        meta.setCustomModelData(30001);
        radioModel.setItemMeta(meta);
        //itemDisplay.setItemStack(new ItemStack(Material.IRON_BLOCK));
        itemDisplay.setItemStack(radioModel);

        Transformation transform = new Transformation(
                new Vector3f(0.6f,0.28f,0.7f),                          // translation
                new Quaternionf(),               // left rotation
                new Vector3f(3f, 3f, 3f),  // scale
                new Quaternionf()                // right rotation
        );
        itemDisplay.setTransformation(transform);

        radioDialItemDisplay = (ItemDisplay) displayLocation.getWorld().spawnEntity(displayLocation, EntityType.ITEM_DISPLAY);
        radioDialItemDisplay.getScoreboardTags().add("RadioItemDisplay");
        ItemStack radioDialModel = new ItemStack(Material.CARVED_PUMPKIN);
        ItemMeta metaDial = radioDialModel.getItemMeta();
        metaDial.setCustomModelData(30002);
        radioDialModel.setItemMeta(metaDial);
        radioDialItemDisplay.setItemStack(radioDialModel);
        radioDialItemDisplay.setTransformation(transform);


    }


}

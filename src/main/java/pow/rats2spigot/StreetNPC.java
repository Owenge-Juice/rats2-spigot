package pow.rats2spigot;

import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import java.util.Random;


public class StreetNPC {
    String name;
    String textureURL;
    String textureValue;
    String textureSignature;
    boolean clockwise;
    boolean walking;
    private StreetNPCRunnable streetNPCRunnable;
    NPC npc;
    MainManager mainManager;
    Location destinationStreetCorner = null;

    public StreetNPC(String name, String textureURL, String textureValue, String textureSignature, MainManager mainManager) {
        walking=false;
        Random random = new Random();
        clockwise = random.nextBoolean();
        this.name = name;
        this.textureURL = textureURL;
        this.textureValue=textureValue;
        this.textureSignature=textureSignature;
        this.mainManager = mainManager;
        this.streetNPCRunnable = new StreetNPCRunnable(this,mainManager);
        streetNPCRunnable.startTaskTimer();
        spawnNPC();
    }

    public NPC getNpc() {
        return npc;
    }

    public boolean isWalking() {
        return walking;
    }

    public boolean isClockwise() {
        return clockwise;
    }

    public void setClockwise(boolean clockwise) {
        this.clockwise = clockwise;
    }

    public void setWalking(boolean walking) {
        this.walking = walking;
    }

    public Location getDestinationStreetCorner() {
        return destinationStreetCorner;
    }

    public void setDestinationStreetCorner(Location destinationStreetCorner) {
        this.destinationStreetCorner = destinationStreetCorner;
    }

    public void spawnNPC(){
        // Create the NPC (using a player as an example, you can change this)
        npc = mainManager.getCitizensManipulate().getNpcRegistry().createNPC(EntityType.PLAYER, name);


        // Spawn the NPC at the specified location

        SkinTrait skinTrait = npc.getOrAddTrait(SkinTrait.class);
        skinTrait.setSkinPersistent(textureURL,textureSignature,textureValue);

        npc.spawn(mainManager.getCitizensManipulate().getCitizensSpawnLocation());
        //mainManager.getWorld().setGameRule(GameRule.SEND_COMMAND_FEEDBACK,false);
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"scale set pehkui:height 2 "+npc.getEntity().getUniqueId());
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"scale set pehkui:width 2 "+npc.getEntity().getUniqueId());
        //mainManager.getWorld().setGameRule(GameRule.SEND_COMMAND_FEEDBACK,true);
        mainManager.getCitizensManipulate().getNpcTeam().addEntry(npc.getEntity().getName());
    }
}

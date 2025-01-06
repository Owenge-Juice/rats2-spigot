package pow.rats2spigot;

import org.bukkit.Location;
import org.bukkit.entity.*;
import pow.rats2spigot.util.Utility;



public class RideableArmourStand {
    Rabbit entityToTrack;
    ArmorStand armorStand;
    Player player;

    public RideableArmourStand(Rabbit entityToTrack, Player player) {
        this.entityToTrack = entityToTrack;
        this.player = player;
        armorStand = (ArmorStand) player.getWorld().spawnEntity(entityToTrack.getLocation(), EntityType.ARMOR_STAND);
        armorStand.setInvulnerable(true);

        armorStand.addPassenger(player);
    }

    public void move(){
        armorStand.teleport(entityToTrack.getLocation());
        Utility.sendMessageToAllAdmins("Move2!");
    }

    public void cleanUp(){
        armorStand.remove();

    }

    public boolean checkCleanUp(){
        if(armorStand.getPassengers().isEmpty()){
            cleanUp();
            return true;
        }else{
            return false;
        }
    }
}

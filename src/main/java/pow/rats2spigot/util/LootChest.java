package pow.rats2spigot.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import pow.rats2spigot.MainManager;

import java.util.Map;

public class LootChest {
    private String name;
    private Location copyLocation = null;
    private Location pasteLocation;
    private BlockFace facing;
    MainManager mainManager;

    public LootChest(String name, String copyLocationName, Location pasteLocation, BlockFace facing, MainManager mainManager) {
        this.name = name;
        copyLocation = mainManager.getChestCopyLocations().get(copyLocationName);
        this.pasteLocation = pasteLocation;
        this.facing = facing;
        this.mainManager = mainManager;
    }

    public void populateChest(){
        if(copyLocation !=null){
            pasteLocation.getBlock().setType(Material.CHEST);

            Directional directional = (Directional) pasteLocation.getBlock().getBlockData();
            directional.setFacing(facing);
            pasteLocation.getBlock().setBlockData(directional);

            ((Chest) pasteLocation.getBlock().getState()).getInventory().clear();
            ((Chest) pasteLocation.getBlock().getState()).getInventory().setContents(mainManager.getItemStackFromStack(copyLocation));
        }else{
            Utility.sendMessageToAllAdmins("ERROR: CopyLocation Name could not be found: " + name);
        }


    }
}

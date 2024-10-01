package pow.rats2spigot.build;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import pow.rats2spigot.MainManager;

public class BasementDoor extends Door{

    public BasementDoor(Location rootLocation, BlockFace blockFace, boolean opensOutwards, boolean doorKnobRight, MainManager mainManager) {
        super(rootLocation, mainManager.getBasementDoorTemplate(),blockFace, 3,opensOutwards,doorKnobRight,mainManager);
    }
}

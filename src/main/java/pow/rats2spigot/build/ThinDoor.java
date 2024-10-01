package pow.rats2spigot.build;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import pow.rats2spigot.MainManager;

public class ThinDoor extends Door{

    public ThinDoor(Location rootLocation, BlockFace blockFace, boolean opensOutwards, boolean doorKnobRight, MainManager mainManager) {
        super(rootLocation, mainManager.getThinDoorTemplate(),blockFace, 2,opensOutwards,doorKnobRight,mainManager);
    }
}

package pow.rats2spigot.build;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import pow.rats2spigot.MainManager;

public class NarrowDoor extends Door{

    public NarrowDoor(Location rootLocation, BlockFace blockFace,boolean opensOutwards,boolean doorKnobRight, MainManager mainManager) {
        super(rootLocation, mainManager.getNarrowDoorTemplate(),blockFace, 3,opensOutwards,doorKnobRight,mainManager);
    }
}

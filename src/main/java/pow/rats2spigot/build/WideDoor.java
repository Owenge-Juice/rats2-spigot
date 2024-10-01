package pow.rats2spigot.build;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import pow.rats2spigot.MainManager;

public class WideDoor extends Door{

    public WideDoor(Location rootLocation, BlockFace blockFace, boolean opensOutwards, boolean doorKnobRight, MainManager mainManager) {
        super(rootLocation, mainManager.getWideDoorTemplate(),blockFace, 4,opensOutwards,doorKnobRight,mainManager);
    }
}

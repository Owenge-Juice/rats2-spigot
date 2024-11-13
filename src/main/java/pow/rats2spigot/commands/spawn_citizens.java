package pow.rats2spigot.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pow.rats2spigot.MainManager;
import pow.rats2spigot.util.Utility;

import java.util.Objects;


public class spawn_citizens implements CommandExecutor {
    private final MainManager mainManager;

    public spawn_citizens(MainManager mainManager) {
        this.mainManager = mainManager;
        Objects.requireNonNull(mainManager.getCommand("spawn_citizens")).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        Utility.sendMessageToAllAdmins("Spawning street npcs");
        mainManager.getCitizensManipulate().spawnCitizens();
        mainManager.getCitizensManipulate().startCitizensWalking(50);

        return true;
    }
}


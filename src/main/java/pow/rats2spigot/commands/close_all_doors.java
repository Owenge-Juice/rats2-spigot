package pow.rats2spigot.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pow.rats2spigot.MainManager;
import java.util.Objects;


public class close_all_doors implements CommandExecutor {
    private final MainManager mainManager;

    public close_all_doors(MainManager mainManager) {
        this.mainManager = mainManager;
        Objects.requireNonNull(mainManager.getCommand("close_all_doors")).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {


        mainManager.closeAllDoors();

        return true;
    }
}

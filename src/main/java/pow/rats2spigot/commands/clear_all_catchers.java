package pow.rats2spigot.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pow.rats2spigot.MainManager;
import pow.rats2spigot.util.Utility;

import java.util.Objects;


public class clear_all_catchers implements CommandExecutor {
    private final MainManager mainManager;

    public clear_all_catchers(MainManager mainManager) {
        this.mainManager = mainManager;
        Objects.requireNonNull(mainManager.getCommand("clear_all_catchers")).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        //mainManager.getWorld().setGameRule(GameRule.SEND_COMMAND_FEEDBACK,false);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"kill @e[type=ratsmp2_forgenpc:wife_catcher]");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"kill @e[type=ratsmp2_forgenpc:youngest_child_catcher]");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"kill @e[type=ratsmp2_forgenpc:widow_oldest_child_catcher]");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"kill @e[type=ratsmp2_forgenpc:widow_catcher]");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"kill @e[type=ratsmp2_forgenpc:scientist_catcher]");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"kill @e[type=ratsmp2_forgenpc:safety_inspector_catcher]");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"kill @e[type=ratsmp2_forgenpc:owners_son_catcher]");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"kill @e[type=ratsmp2_forgenpc:owner_catcher]");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"kill @e[type=ratsmp2_forgenpc:husband_catcher]");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"kill @e[type=ratsmp2_forgenpc:gom_catcher]");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"kill @e[type=ratsmp2_forgenpc:evil_sister_catcher]");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"kill @e[type=ratsmp2_forgenpc:evil_brother_catcher]");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"kill @e[type=ratsmp2_forgenpc:bach_catcher]");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"kill @e[type=ratsmp2_forgenpc:boilerman_catcher]");
        //mainManager.getWorld().setGameRule(GameRule.SEND_COMMAND_FEEDBACK,true);
        Utility.sendMessageToAllAdmins("clearing all catchers");
        return true;
    }
}


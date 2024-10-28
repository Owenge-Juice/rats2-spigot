package pow.rats2spigot.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pow.rats2spigot.MainManager;
import pow.rats2spigot.util.Utility;

import java.util.Objects;


public class pathing_tool_give implements CommandExecutor {
    private final MainManager mainManager;

    public pathing_tool_give(MainManager mainManager) {
        this.mainManager = mainManager;
        Objects.requireNonNull(mainManager.getCommand("pathing_tool_give")).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(strings.length!=1){
            Utility.sendMessageToAllAdmins("ERROR: 1 argument required (Tool name)");
            return false;
        }

        if(!(commandSender instanceof Player)){
            Utility.sendMessageToAllAdmins("ERROR: Can only be run by players");
            return false;
        }

        Player player = (Player)commandSender;

        ItemStack itemStack = new ItemStack(Material.STICK);

        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(strings[0]);

        itemMeta.addEnchant(Enchantment.MENDING,1,true);
        itemStack.setItemMeta(itemMeta);

        player.getInventory().addItem(itemStack);

        return true;
    }
}


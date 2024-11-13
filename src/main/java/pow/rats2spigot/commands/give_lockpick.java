package pow.rats2spigot.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pow.rats2spigot.MainManager;

import java.util.Collections;
import java.util.Objects;


public class give_lockpick implements CommandExecutor {
    private final MainManager mainManager;

    public give_lockpick(MainManager mainManager) {
        this.mainManager = mainManager;
        Objects.requireNonNull(mainManager.getCommand("give_lockpick")).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player)commandSender;
        player.sendMessage("Did you mean to run this?\nThis was a test command left in by Owen, if you're looking for how to get an actual lockpick, you need to give yourself the 'Paperclip' item with the give command.");
        /*
        ItemStack lockPick = new ItemStack(Material.IRON_NUGGET);
        ItemMeta meta = lockPick.getItemMeta();
        meta.setCustomModelData(1);
        meta.setDisplayName("Bent Safety Clip");
        meta.setLore(Collections.singletonList("A safety clip bent to help in a pinch. Right click when trapped to free yourself from a cage!"));
        lockPick.setItemMeta(meta);

        player.getInventory().addItem(lockPick);
*/
        return true;
    }


}


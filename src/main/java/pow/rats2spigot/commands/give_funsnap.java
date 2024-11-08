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


public class give_funsnap implements CommandExecutor {
    private final MainManager mainManager;

    public give_funsnap(MainManager mainManager) {
        this.mainManager = mainManager;
        Objects.requireNonNull(mainManager.getCommand("give_funsnap")).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player)commandSender;

        ItemStack funSnap = new ItemStack(Material.PAPER);
        ItemMeta meta = funSnap.getItemMeta();
        meta.setCustomModelData(2);
        meta.setDisplayName("Fun Snap");
        meta.setLore(Collections.singletonList("A papery wrapper covers rocks and... gunpowder?"));
        funSnap.setItemMeta(meta);

        player.getInventory().addItem(funSnap);

        return true;
    }


}


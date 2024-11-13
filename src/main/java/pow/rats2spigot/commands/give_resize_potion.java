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


public class give_resize_potion implements CommandExecutor {
    private final MainManager mainManager;

    public give_resize_potion(MainManager mainManager) {
        this.mainManager = mainManager;
        Objects.requireNonNull(mainManager.getCommand("give_resize_potion")).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player)commandSender;

        ItemStack potion = new ItemStack(Material.POTION);
        ItemMeta meta = potion.getItemMeta();
        if(Objects.equals(strings[0], "small")){
            meta.setCustomModelData(1);
        }else if(Objects.equals(strings[0], "big")){
            meta.setCustomModelData(2);
        }

        meta.setDisplayName("Strange Potion");
        meta.setLore(Collections.singletonList("Purple wispy liquid rests inside..."));
        potion.setItemMeta(meta);

        player.getInventory().addItem(potion);

        return true;
    }


}


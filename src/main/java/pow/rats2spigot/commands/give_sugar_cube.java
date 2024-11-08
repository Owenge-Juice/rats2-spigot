package pow.rats2spigot.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import pow.rats2spigot.MainManager;

import java.util.Collections;
import java.util.Objects;


public class give_sugar_cube implements CommandExecutor {
    private final MainManager mainManager;

    public give_sugar_cube(MainManager mainManager) {
        this.mainManager = mainManager;
        Objects.requireNonNull(mainManager.getCommand("give_sugar_cube")).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player)commandSender;

        ItemStack sugarCube = new ItemStack(Material.DRIED_KELP);
        ItemMeta meta = sugarCube.getItemMeta();
        meta.setCustomModelData(1);
        meta.setDisplayName("Sugar Cube");
        meta.setLore(Collections.singletonList("Careful not to sugar crash..."));
        sugarCube.setItemMeta(meta);

        player.getInventory().addItem(sugarCube);

        return true;
    }


}


package pow.rats2spigot.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import pow.rats2spigot.MainManager;

import java.util.Objects;


public class reauthor_book implements CommandExecutor {
    private final MainManager mainManager;

    public reauthor_book(MainManager mainManager) {
        this.mainManager = mainManager;
        Objects.requireNonNull(mainManager.getCommand("reauthor_book")).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        // Ensure that the sender is a player
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("This command can only be executed by a player.");
            return false;
        }

        Player player = (Player) commandSender;

        // Get the item in hand
        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        // Check if the item is a written book
        if (itemInHand.getType() != org.bukkit.Material.WRITTEN_BOOK) {
            player.sendMessage("You must be holding a written book to rename the author.");
            return false;
        }

        // Check if there is a valid author name passed in the command
        if (strings.length == 0) {
            player.sendMessage("Please provide a new author name.");
            return false;
        }

        // Get the new author name
        String newAuthorName = String.join(" ", strings);

        // Cast the ItemStack to BookMeta
        BookMeta bookMeta = (BookMeta) itemInHand.getItemMeta();
        if (bookMeta != null) {
            // Set the new author
            bookMeta.setAuthor(newAuthorName);

            // Update the book's metadata
            itemInHand.setItemMeta(bookMeta);

            player.sendMessage("The book author has been renamed to " + newAuthorName + ".");
        } else {
            player.sendMessage("This item does not have book metadata.");
        }

        return true;
    }


}


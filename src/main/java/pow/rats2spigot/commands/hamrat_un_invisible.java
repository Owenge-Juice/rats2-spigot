package pow.rats2spigot.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import pow.rats2spigot.MainManager;

import java.util.Objects;


public class hamrat_un_invisible implements CommandExecutor {
    private final MainManager mainManager;

    public hamrat_un_invisible(MainManager mainManager) {
        this.mainManager = mainManager;
        Objects.requireNonNull(mainManager.getCommand("hamrat_un_invisible")).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player)commandSender;
        player.removePotionEffect(PotionEffectType.INVISIBILITY);

        return true;
    }


}


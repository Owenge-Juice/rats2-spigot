package pow.rats2spigot.commands;

import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pow.rats2spigot.MainManager;

import java.util.Objects;


public class halloween_dialogue_brawl implements CommandExecutor {
    private final MainManager mainManager;

    public halloween_dialogue_brawl(MainManager mainManager) {
        this.mainManager = mainManager;
        Objects.requireNonNull(mainManager.getCommand("halloween_dialogue_brawl")).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player)commandSender;

        for(Player playerScanned : mainManager.getWorld().getPlayers()){
            if(playerScanned.getLocation().distance(player.getLocation())<30){
                playerScanned.playSound(playerScanned,"rats2:powevents.sound.halloween_brawl", SoundCategory.VOICE,1,1);
                playerScanned.sendMessage("<Ghostly Voice> Welcome to my brawl arena rodents! Survive the enemies and take your prize.");
            }
        }

        return true;
    }


}


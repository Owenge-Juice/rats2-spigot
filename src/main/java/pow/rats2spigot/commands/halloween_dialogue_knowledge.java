package pow.rats2spigot.commands;

import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pow.rats2spigot.MainManager;

import java.util.Objects;


public class halloween_dialogue_knowledge implements CommandExecutor {
    private final MainManager mainManager;

    public halloween_dialogue_knowledge(MainManager mainManager) {
        this.mainManager = mainManager;
        Objects.requireNonNull(mainManager.getCommand("halloween_dialogue_knowledge")).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player)commandSender;

        for(Player playerScanned : mainManager.getWorld().getPlayers()){
            if(playerScanned.getLocation().distance(player.getLocation())<30){
                playerScanned.playSound(playerScanned,"rats2:powevents.sound.halloween_knowledge", SoundCategory.VOICE,1,1);
                playerScanned.sendMessage("<Ghostly Voice> Welcome to my realm of knowledge rodents! Complete my 3 riddles, answer each correctly to claim your prize.");

            }
        }

        return true;
    }


}


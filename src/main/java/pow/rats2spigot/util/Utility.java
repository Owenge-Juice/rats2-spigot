package pow.rats2spigot.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Utility {
    public static void sendMessageToAllAdmins(String message){
        for(Player player : Bukkit.getOnlinePlayers()){
            if(player.isOp()){
                player.sendMessage(message);
            }
        }
    }

}

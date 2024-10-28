package pow.rats2spigot.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.JSONObject;
import pow.rats2spigot.MainManager;
import pow.rats2spigot.util.Utility;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;


public class fakename implements CommandExecutor {
    private final MainManager mainManager;


    public fakename(MainManager mainManager) {
        this.mainManager = mainManager;
        Objects.requireNonNull(mainManager.getCommand("fakename")).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)){
            commandSender.sendMessage("ERROR: This command can only be run by players in game");
            return false;
        }

        Player player = (Player)commandSender;

        if(strings.length == 1){
            if(strings[0].length()>15){
                player.sendMessage("ERROR: Name cannot be longer than 15 characters long");
                return false;
            }

            if(strings[0].equals("reset")){
                Utility.setPlayerName(player, player.getName(),mainManager);
                player.sendMessage("Successfully reset player " + player.getName() + " name.");
                return true;
            }else{
                Utility.setPlayerName(player, strings[0],mainManager);
                player.sendMessage("Successfully changed player " + player.getName() + " name to " + strings[0]);
                return true;
            }
        }else if(strings.length==2){
            if(strings[1].length()>15){
                player.sendMessage("ERROR: Name cannot be longer than 15 characters long");
                return false;
            }

            if(player.isOp()){
                Player targetPlayer = Bukkit.getPlayer(strings[0]);
                if(targetPlayer!=null){
                    if(strings[1].equals("reset")){
                        Utility.setPlayerName(targetPlayer,targetPlayer.getName(),mainManager);
                        player.sendMessage("Successfully reset player " + targetPlayer.getName() + " name.");
                    }else{
                        Utility.setPlayerName(targetPlayer,strings[1],mainManager);
                        player.sendMessage("Successfully changed player " + targetPlayer.getName() + " name to " + targetPlayer.getName());
                    }
                    return true;
                }else{
                    player.sendMessage("ERROR: Cannot find player " + strings[0] + ", are they online?");
                    return false;
                }
            }else{
                player.sendMessage("ERROR: Only admins can change the name of other players");
                return false;
            }
        }else{
            player.sendMessage("ERROR: Requires either 1 argument /fakename <name> or 2 arguments /fakename <player> <name>");
            return false;
        }

    }




}


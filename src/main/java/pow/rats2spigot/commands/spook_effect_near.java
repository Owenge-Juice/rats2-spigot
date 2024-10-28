package pow.rats2spigot.commands;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import pow.rats2spigot.MainManager;

import java.util.Objects;
import java.util.Random;


public class spook_effect_near implements CommandExecutor {
    private final MainManager mainManager;

    public spook_effect_near(MainManager mainManager) {
        this.mainManager = mainManager;
        Objects.requireNonNull(mainManager.getCommand("spook_effect_near")).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player)commandSender;
        Location runLocation = player.getLocation();
        Random random = new Random();
        int spookedPlayerCount=0;
        for(Player playerScan : mainManager.getWorld().getPlayers()){
            int randomInt = random.nextInt(11);
            if(playerScan.getLocation().distance(runLocation)<10 || player.getGameMode()== GameMode.SURVIVAL){
                if(randomInt<=3){
                    playerScan.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,200,1,true,false));
                }else if(randomInt<=6){
                    playerScan.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS,200,1,true,false));
                }else if(randomInt<=9){
                    playerScan.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,200,1,true,false));
                }else{
                    playerScan.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION,30,1,true,false));
                }
            }
            spookedPlayerCount+=1;
            player.playSound(player, Sound.ENTITY_CAT_HISS, SoundCategory.HOSTILE,1,1);
        }
        commandSender.sendMessage(spookedPlayerCount + " players in surival have been spooked");
        return true;
    }


}


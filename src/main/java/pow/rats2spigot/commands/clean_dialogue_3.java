package pow.rats2spigot.commands;

import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import pow.rats2spigot.MainManager;

import java.util.Objects;


public class clean_dialogue_3 implements CommandExecutor {
    private final MainManager mainManager;

    public clean_dialogue_3(MainManager mainManager) {
        this.mainManager = mainManager;
        Objects.requireNonNull(mainManager.getCommand("clean_dialogue_3")).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player)commandSender;

        for(Player playerScanned : mainManager.getWorld().getPlayers()){
            if(playerScanned.getLocation().distance(player.getLocation())<100){
                playerScanned.playSound(playerScanned,"rats2:powevents.sound.clean_3", SoundCategory.VOICE,1,1);
                cueSubtitles(playerScanned);
            }
        }

        return true;
    }

    private void cueSubtitles(Player playerScanned) {
        cueSubtitle(playerScanned,"<Quincy> Baudet! It's time to leave! Get your bags-",1);
        cueSubtitle(playerScanned,"<Quincy> How'd you clean all this up?!?!",4*20);
        cueSubtitle(playerScanned,"<Quincy> Whatever. You got lucky.",(6*20)+10);
        cueSubtitle(playerScanned,"<Quincy> One more screw up and you're out. Got it?",9*20);
        cueSubtitle(playerScanned,"<Nadine> There won't be another screw up.",12*20);
        cueSubtitle(playerScanned,"<Quincy> *Under his breath* I'll make sure there is...",15*20);
    }

    private void cueSubtitle(Player playerScanned, String s, long i) {
        BukkitTask bukkitTask = new BukkitRunnable() {
            @Override
            public void run() {
                playerScanned.sendMessage(s);
            }
        }.runTaskLater(mainManager, i);
    }


}


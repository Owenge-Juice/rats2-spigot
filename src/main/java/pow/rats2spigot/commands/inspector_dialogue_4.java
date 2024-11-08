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


public class inspector_dialogue_4 implements CommandExecutor {
    private final MainManager mainManager;

    public inspector_dialogue_4(MainManager mainManager) {
        this.mainManager = mainManager;
        Objects.requireNonNull(mainManager.getCommand("inspector_dialogue_4")).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player)commandSender;

        for(Player playerScanned : mainManager.getWorld().getPlayers()){
            if(playerScanned.getLocation().distance(player.getLocation())<100){
                playerScanned.playSound(playerScanned,"rats2:powevents.sound.inspector_4", SoundCategory.VOICE,1,1);
                cueSubtitles(playerScanned);
            }
        }

        return true;
    }

    private void cueSubtitles(Player playerScanned) {
        cueSubtitle(playerScanned,"<Quincy> Why a few months! Why can't this stupid place shut down now!",1);
        cueSubtitle(playerScanned,"<Joëlle> *From over the phone* Look Quincy, unless you want the government breathing down your back for tenant contract violations, you wait the months out.",5*20);
        cueSubtitle(playerScanned,"<Quincy> You sure this'll work?",12*20);
        cueSubtitle(playerScanned,"<Joëlle> People don't call me \"The Homewrecker\" for nothing.",14*20);
        cueSubtitle(playerScanned,"<Joëlle> Just keep this place a mess, and I'll keep logging the violations.",17*20);
        cueSubtitle(playerScanned,"<Joëlle> Then in a few months, sell the land to the highest bidder.",20*20);
        cueSubtitle(playerScanned,"<Quincy> Got it. Payday is coming soon Joëlle, I'll keep you updated..",23*20);
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


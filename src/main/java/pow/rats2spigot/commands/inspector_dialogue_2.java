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


public class inspector_dialogue_2 implements CommandExecutor {
    private final MainManager mainManager;

    public inspector_dialogue_2(MainManager mainManager) {
        this.mainManager = mainManager;
        Objects.requireNonNull(mainManager.getCommand("inspector_dialogue_2")).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player)commandSender;

        for(Player playerScanned : mainManager.getWorld().getPlayers()){
            if(playerScanned.getLocation().distance(player.getLocation())<100){
                playerScanned.playSound(playerScanned,"rats2:powevents.sound.inspector_2", SoundCategory.VOICE,1,1);
                cueSubtitles(playerScanned);
            }
        }

        return true;
    }

    private void cueSubtitles(Player playerScanned) {
        cueSubtitle(playerScanned,"<Unknown Woman> Yes, hello, what exactly did you need me for?",1);
        cueSubtitle(playerScanned,"<Quincy> I'm afraid to say it, but I'm worried about the safety of this building!",4*20);
        cueSubtitle(playerScanned,"<Quincy> A pillar has just collapsed in the lobby. I think it needs a proper inspection.",9*20);
        cueSubtitle(playerScanned,"<Unknown Woman> Very well, most of the time this sort of thing is the result of....",13*20);
        cueSubtitle(playerScanned,"<Unknown Woman> Rats...",18*20);
        cueSubtitle(playerScanned,"<Unknown Woman> I will have my cats and colleague take a loot at it.",19*20);
        cueSubtitle(playerScanned,"<Unknown Woman> But if it doesn't look good. It may be the end for this establishment.",21*20);
        cueSubtitle(playerScanned,"<Quincy> Are you saying there's a chance...",26*20);
        cueSubtitle(playerScanned,"<Quincy> this humble little aparthotel... ",28*20);
        cueSubtitle(playerScanned,"<Quincy> may CLOSE down?!?!?",(31*20)-10);
        cueSubtitle(playerScanned,"<Unknown Woman> We will get to the bottom of it either way.",32*20);
        cueSubtitle(playerScanned,"<Unknown Woman> Quian, take a look around.",34*20);
        cueSubtitle(playerScanned,"<Quian> Don't worry Joelle, if the rats are here, I will get em!",36*20);
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


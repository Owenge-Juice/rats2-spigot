package pow.rats2spigot.commands;

import org.bukkit.Bukkit;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import pow.rats2spigot.MainManager;

import java.util.ArrayList;
import java.util.Objects;


public class halloween_dialogue_starting implements CommandExecutor {
    private final MainManager mainManager;

    public halloween_dialogue_starting(MainManager mainManager) {
        this.mainManager = mainManager;
        Objects.requireNonNull(mainManager.getCommand("halloween_dialogue_starting")).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player)commandSender;

        for(Player playerScanned : mainManager.getWorld().getPlayers()){
            if(playerScanned.getLocation().distance(player.getLocation())<100){
                playerScanned.playSound(playerScanned,"rats2:powevents.sound.halloween_starting", SoundCategory.VOICE,1,1);
                cueSubtitles(playerScanned);
            }
        }

        return true;
    }

    private void cueSubtitles(Player playerScanned) {
        cueSubtitle(playerScanned,"<Ghostly Voice> Rats! How dare you disturb my eternal slumber!",1);
        cueSubtitle(playerScanned,"<Ghostly Voice> I should kill you all for your transgression. But I like playing with my catches first.",180);
        cueSubtitle(playerScanned,"<Ghostly Voice> Lets play a little game, scattered around my CAT-acombs are my four soul fragments.",19*20);
        cueSubtitle(playerScanned,"<Ghostly Voice> Split into groups and conquer each challenge, only then may you escape unharmed.",31*20);
        cueSubtitle(playerScanned,"<Ghostly Voice> Let's motivate you pesky rodents... Fail... and you ALL turn into cats forever!",40*20);

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


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


public class clean_dialogue_1 implements CommandExecutor {
    private final MainManager mainManager;

    public clean_dialogue_1(MainManager mainManager) {
        this.mainManager = mainManager;
        Objects.requireNonNull(mainManager.getCommand("clean_dialogue_1")).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player)commandSender;

        for(Player playerScanned : mainManager.getWorld().getPlayers()){
            if(playerScanned.getLocation().distance(player.getLocation())<100){
                playerScanned.playSound(playerScanned,"rats2:powevents.sound.clean_1", SoundCategory.VOICE,1,1);
                cueSubtitles(playerScanned);
            }
        }

        return true;
    }

    private void cueSubtitles(Player playerScanned) {
        cueSubtitle(playerScanned,"<Nadine> Attention residents of The Terrière! With the risk of closure looming upon us I ask for us to come together!",1);
        cueSubtitle(playerScanned,"<Nadine> Or not! That's fine!",(20*10)+10);
        cueSubtitle(playerScanned,"<Nadine> Listen! Just leave whatever trash you wanna get rid of at your front doors.",14*20);
        cueSubtitle(playerScanned,"<Nadine> Because in an effort to keep this place from shutting down, I'll be doing ALL the autumn cleaning!",17*20);
        cueSubtitle(playerScanned,"<Nadine> All by myself",23*20);
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


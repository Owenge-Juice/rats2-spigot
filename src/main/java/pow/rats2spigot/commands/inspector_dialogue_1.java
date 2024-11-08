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


public class inspector_dialogue_1 implements CommandExecutor {
    private final MainManager mainManager;

    public inspector_dialogue_1(MainManager mainManager) {
        this.mainManager = mainManager;
        Objects.requireNonNull(mainManager.getCommand("inspector_dialogue_1")).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player)commandSender;

        for(Player playerScanned : mainManager.getWorld().getPlayers()){
            if(playerScanned.getLocation().distance(player.getLocation())<100){
                playerScanned.playSound(playerScanned,"rats2:powevents.sound.inspector_1", SoundCategory.VOICE,1,1);
                cueSubtitles(playerScanned);
            }
        }

        return true;
    }

    private void cueSubtitles(Player playerScanned) {
        cueSubtitle(playerScanned,"<Quincy> Oh my God!",1);
        cueSubtitle(playerScanned,"<Quincy> That's it, this place is falling apart, I don't care what Dad says. I'm calling someone about this.",50);
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


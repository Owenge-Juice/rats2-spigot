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


public class halloween_dialogue_ending implements CommandExecutor {
    private final MainManager mainManager;

    public halloween_dialogue_ending(MainManager mainManager) {
        this.mainManager = mainManager;
        Objects.requireNonNull(mainManager.getCommand("halloween_dialogue_ending")).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player)commandSender;

        for(Player playerScanned : mainManager.getWorld().getPlayers()){
            if(playerScanned.getLocation().distance(player.getLocation())<100){
                playerScanned.playSound(playerScanned,"rats2:powevents.sound.halloween_ending", SoundCategory.VOICE,1,1);
                playerScanned.sendMessage("<Ghostly Voice> I see you've all found my fragments! But there's one left...");
                cueSubtitle(playerScanned,"<Ghostly Voice>RETURN THE DISC YOU STOLE FROM MY TOMB RATS! OR SUFFER!",8*20);
            }

        }

        return true;
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


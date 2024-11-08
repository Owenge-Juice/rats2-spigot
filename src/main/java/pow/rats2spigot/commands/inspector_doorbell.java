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


public class inspector_doorbell implements CommandExecutor {
    private final MainManager mainManager;

    public inspector_doorbell(MainManager mainManager) {
        this.mainManager = mainManager;
        Objects.requireNonNull(mainManager.getCommand("inspector_doorbell")).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player)commandSender;

        for(Player playerScanned : mainManager.getWorld().getPlayers()){
            playerScanned.playSound(playerScanned,"rats2:powevents.sound.inspector_doorbell", SoundCategory.VOICE,1,1);
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


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


public class inspector_dialogue_3 implements CommandExecutor {
    private final MainManager mainManager;

    public inspector_dialogue_3(MainManager mainManager) {
        this.mainManager = mainManager;
        Objects.requireNonNull(mainManager.getCommand("inspector_dialogue_3")).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player)commandSender;

        for(Player playerScanned : mainManager.getWorld().getPlayers()){
            if(playerScanned.getLocation().distance(player.getLocation())<100){
                playerScanned.playSound(playerScanned,"rats2:powevents.sound.inspector_3", SoundCategory.VOICE,1,1);
                cueSubtitles(playerScanned);
            }
        }

        return true;
    }

    private void cueSubtitles(Player playerScanned) {
        cueSubtitle(playerScanned,"<Joëlle> This establishment is just a DISGRACE to safety.",1);
        cueSubtitle(playerScanned,"<Quincy> What?!?!",(4*20)+10);
        cueSubtitle(playerScanned,"<Joëlle> I've never been so embarassed to step foot in a building!",(5*20)+10);
        cueSubtitle(playerScanned,"<Joëlle> There are RATS. EVERYWHERE.",9*20);
        cueSubtitle(playerScanned,"<Joëlle> It MUST be shut down at once!",12*20);
        cueSubtitle(playerScanned,"<Quincy> Oh no! Surely you can give us another chance?! Think of the children!",14*20);
        cueSubtitle(playerScanned,"<Joëlle> Well... perhaps...",19*20);
        cueSubtitle(playerScanned,"<Joëlle> I'll give you a few months to prepare your tenants, and I'll leave my cats here to take care of those... rats.",22*20);
        cueSubtitle(playerScanned,"<Quincy> How will I tell my poor father!... It was his... dream!...",29*20);
        cueSubtitle(playerScanned,"<Joëlle> I'm just a Safety Inspector, not a family therapist. Good day sir.",34*20);
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


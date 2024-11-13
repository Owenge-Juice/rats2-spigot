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


public class newrats_dialogue_1 implements CommandExecutor {
    private final MainManager mainManager;

    public newrats_dialogue_1(MainManager mainManager) {
        this.mainManager = mainManager;
        Objects.requireNonNull(mainManager.getCommand("newrats_dialogue_1")).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player)commandSender;

        for(Player playerScanned : mainManager.getWorld().getPlayers()){
            if(playerScanned.getLocation().distance(player.getLocation())<20){
                playerScanned.playSound(playerScanned,"rats2:powevents.sound.newrats_dialogue_1", SoundCategory.VOICE,1,1);
                cueSubtitles(playerScanned);
            }
        }

        return true;
    }

    private void cueSubtitles(Player playerScanned) {
        cueSubtitle(playerScanned,"<Quincy> Youuuuu.... STUPID RATS!",1);
        cueSubtitle(playerScanned,"<Quincy> I've caught you!",2*20);
        cueSubtitle(playerScanned,"<Quincy> I KNOW YOUR SECRETS! I KNOW YOU CAN UNDERSTAND ME!",5*20);
        cueSubtitle(playerScanned,"<Quincy> LOOK AT ME! LOOK AT ME!",9*20);
        cueSubtitle(playerScanned,"<Quincy> There's other rats in here... Like you all.",11*20);
        cueSubtitle(playerScanned,"<Quincy> THEY'RE RUINING MY LIFE! STEALING ALL THE STUFF I STOLE, BREAKING MY WINDOWS. I'VE HAD ENOUGH OF IT",14*20);
        cueSubtitle(playerScanned,"<Quincy> If you want to keep your little rat lives... Help me.",23*20);
        cueSubtitle(playerScanned,"<Quincy> Infiltrate their lair and report back to me... Now go! GO!",27*20);

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


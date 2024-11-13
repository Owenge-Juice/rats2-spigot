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


public class clean_dialogue_2 implements CommandExecutor {
    private final MainManager mainManager;

    public clean_dialogue_2(MainManager mainManager) {
        this.mainManager = mainManager;
        Objects.requireNonNull(mainManager.getCommand("clean_dialogue_2")).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player)commandSender;

        for(Player playerScanned : mainManager.getWorld().getPlayers()){
            if(playerScanned.getLocation().distance(player.getLocation())<100){
                playerScanned.playSound(playerScanned,"rats2:powevents.sound.clean_2", SoundCategory.VOICE,1,1);
                cueSubtitles(playerScanned);
            }
        }

        return true;
    }

    private void cueSubtitles(Player playerScanned) {
        cueSubtitle(playerScanned,"<Quincy> Baudet! You've ruined the entire upper floor with your stupid trash!",1);
        cueSubtitle(playerScanned,"<Quincy> DID YOU HEAR ME BAUDET?!",6*20);
        cueSubtitle(playerScanned,"<Nadine> It's Mrs. Baudet. And I haven't ruined anything.",8*20);
        cueSubtitle(playerScanned,"<Quincy> Yeah, sure, MRS. Baudet.",13*20);
        cueSubtitle(playerScanned,"<Quincy> Anyways, look at what you've done! There's sopping wet trash on the floor!",16*20);
        cueSubtitle(playerScanned,"<Quincy> The stench is horrid!",(20*20)-5);
        cueSubtitle(playerScanned,"<Quincy> And even WORSE! IT'S ATTRACTING THE RATS!",(22*20)-10);
        cueSubtitle(playerScanned,"<Nadine> I'm trying to help you Quincy! I don't want this place to shutdown either!",25*20);
        cueSubtitle(playerScanned,"<Quincy> Sure doesn't look like it, I slipped on 5 different banana peels walking down here!",31*20);
        cueSubtitle(playerScanned,"<Nadine> I'm cleaning it up! By myself I might add! Where are all the workers???",35*20);
        cueSubtitle(playerScanned,"<Quincy> The Terrier doesn't need your charity work!",40*20);
        cueSubtitle(playerScanned,"<Nadine> Are you dense?",43*20);
        cueSubtitle(playerScanned,"<Quincy> ENOUGH! IF YOU DON'T CLEAN ALL THIS STINKY STUPID SMELLY GARBAGE UP WITHIN THE NEXT HOUR YOU'RE OUT.",44*20);
        cueSubtitle(playerScanned,"<Quincy> DO YOU WANNA EXPLAIN TO YOUR KIDS WHY THEY'RE SLEEPING UNDER A BRIDGE TONIGHT?",50*20);
        cueSubtitle(playerScanned,"<Nadine> YOU CAN'T JUST EVICT ME???",55*20);
        cueSubtitle(playerScanned,"<Quincy> I CAN AND I WILL! NOW CLEAN IT.",56*20);
        cueSubtitle(playerScanned,"<Nadine> I- I can't get it done that fast...",60*20);
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


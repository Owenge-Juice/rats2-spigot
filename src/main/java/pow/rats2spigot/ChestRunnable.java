package pow.rats2spigot;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import pow.rats2spigot.util.Utility;

public class ChestRunnable extends BukkitRunnable {

    MainManager mainManager;
    private BukkitTask bukkitTask;

    public ChestRunnable(MainManager mainManager) {
        this.mainManager = mainManager;
        startTaskTimer();
    }

    public BukkitTask getBukkitTask() {
        return bukkitTask;
    }

    //ran every 5 seconds
    @Override
    public void run() {
        Objective objective = Bukkit.getServer().getScoreboardManager().getMainScoreboard().getObjective("config");
        objective.getScore("timeTillNextChest").setScore(objective.getScore("timeTillNextChest").getScore()+1);

        if(objective.getScore("timeTillNextChest").getScore()>=(60*5)){        //60 minutes times 5 hours
            Utility.sendMessageToAllAdmins("replenishing chests");
            mainManager.populateAllChests();
            objective.getScore("timeTillNextChest").setScore(0);
        }
    }

    public BukkitTask startTaskTimer() {
        //updates once every minute
        bukkitTask = runTaskTimer(mainManager, 0, 60*20);
        return bukkitTask;
    }
}

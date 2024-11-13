package pow.rats2spigot;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.ScoreboardManager;
import pow.rats2spigot.util.Utility;

import java.util.ArrayList;
import java.util.Objects;

public class CatcherRunnable extends BukkitRunnable {

    MainManager mainManager;
    private BukkitTask bukkitTask;

    public CatcherRunnable(MainManager mainManager) {
        this.mainManager = mainManager;


        if(Bukkit.getScoreboardManager().getMainScoreboard().getObjective("catchers")==null){
            Bukkit.getScoreboardManager().getMainScoreboard().registerNewObjective("catchers", Criteria.DUMMY,"catchers");
        }
        startTaskTimer();
    }

    public BukkitTask getBukkitTask() {
        return bukkitTask;
    }

    //ran every 5 seconds
    @Override
    public void run() {
        checkCatchers(0);
    }

    public void checkCatchers(int attempts){
        if(attempts<3){
            Objective objective = Bukkit.getScoreboardManager().getMainScoreboard().getObjective("catchers");

            ArrayList<String> names = new ArrayList<>();
            names.add("boilerman");
            names.add("owner");
            names.add("scientist");
            names.add("bach");
            //names.add("widow");
            names.add("evil_brother");
            names.add("evil_sister");
            names.add("gom");
            names.add("husband");
            //names.add("owners_son");
            names.add("safety_inspector");
            names.add("widow_oldest_child");
            names.add("youngest_child");
            names.add("wife");

            for(String name : names){
                objective.getScore(name).setScore(0);
                //mainManager.getWorld().setGameRule(GameRule.SEND_COMMAND_FEEDBACK,false);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"execute as @e[type=ratsmp2_forgenpc:"+name+"_catcher] run scoreboard players add "+name+" catchers 1");
                //mainManager.getWorld().setGameRule(GameRule.SEND_COMMAND_FEEDBACK,true);
                if(objective.getScore(name).getScore()==0){
                    Utility.sendMessageToAllAdmins("no " + name + ": summoning new one");
                    mainManager.getCatcherManager().summonCatcher(name);
                }
            }
        }

    }

    public BukkitTask startTaskTimer() {
        bukkitTask = runTaskTimer(mainManager, 0, 60*5);
        return bukkitTask;
    }
}

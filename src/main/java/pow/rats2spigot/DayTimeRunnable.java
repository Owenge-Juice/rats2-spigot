package pow.rats2spigot;

import org.bukkit.Bukkit;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import pow.rats2spigot.util.Utility;

public class DayTimeRunnable extends BukkitRunnable {

    MainManager mainManager;
    private BukkitTask bukkitTask;

    public DayTimeRunnable(MainManager mainManager) {
        this.mainManager = mainManager;
        startTaskTimer();
    }

    public BukkitTask getBukkitTask() {
        return bukkitTask;
    }

    //ran every 5 seconds
    @Override
    public void run() {
        if(mainManager.getWorld().getTime()==1000 || mainManager.getWorld().getTime()==13000){
            mainManager.getWorld().setTime(mainManager.getWorld().getTime()+1);
            for(Player player : mainManager.getWorld().getPlayers()){
                player.playSound(player,"rats2:powevents.sound.clock",SoundCategory.AMBIENT,0.6f,1);
            }
        }
        if(mainManager.getWorld().getTime()==1400){
            mainManager.getWorld().setTime(mainManager.getWorld().getTime()+1);
            dayBreak();
        }else if(mainManager.getWorld().getTime()==13400){
            mainManager.getWorld().setTime(mainManager.getWorld().getTime()+1);
            sunset();
        }
    }

    public void dayBreak(){
        Utility.sendMessageToAllAdmins("starting dayBreak");
        mainManager.randomiseTradeNumber();


        mainManager.turnAllStoveTopsOn();
        mainManager.populateCheese(5);
        Bukkit.getServer().getScoreboardManager().getMainScoreboard().getObjective("config").getScore("night").setScore(0);


        BukkitTask bukkitTask = new BukkitRunnable() {
            @Override
            public void run() {
                mainManager.setDayTimeLights();
                mainManager.getCatcherManager().summonAllCatchers();
                mainManager.setUpDay(mainManager.getCurrentIRLDay());
                mainManager.updateAllDoors();
            }
        }.runTaskLater(mainManager, 30*20); // 100 ticks = 5 seconds
    }

    public void sunset(){
        Utility.sendMessageToAllAdmins("starting sunset");

        mainManager.turnAllStoveTopsOff();
        Bukkit.getServer().getScoreboardManager().getMainScoreboard().getObjective("config").getScore("night").setScore(1);

        BukkitTask bukkitTask = new BukkitRunnable() {
            @Override
            public void run() {
                mainManager.setNightTimeLights();
                mainManager.getCatcherManager().summonAllCatchers();
                mainManager.setUpDay(mainManager.getCurrentIRLDay());
                mainManager.updateAllDoors();
            }
        }.runTaskLater(mainManager, 30*20); // 100 ticks = 5 seconds
    }

    public BukkitTask startTaskTimer() {
        bukkitTask = runTaskTimer(mainManager, 0, 1);
        return bukkitTask;
    }
}

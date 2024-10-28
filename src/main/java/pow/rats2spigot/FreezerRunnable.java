package pow.rats2spigot;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import pow.rats2spigot.util.Utility;

import java.util.HashMap;
import java.util.Map;

public class FreezerRunnable extends BukkitRunnable {

    MainManager mainManager;
    private BukkitTask bukkitTask;
    private HashMap<String, Integer> freezeMap;
    private final int startingValue = -50;

    public FreezerRunnable(MainManager mainManager) {
        this.mainManager = mainManager;
        freezeMap = new HashMap<>();
        startTaskTimer();
    }

    public BukkitTask getBukkitTask() {
        return bukkitTask;
    }

    //ran every 5 ticks
    @Override
    public void run() {
        for(Player player : mainManager.getWorld().getPlayers()){
            if(player.getLocation().getY()<-37 && player.getGameMode()== GameMode.SURVIVAL){
                Location location = player.getLocation().clone();
                location.setY(-61);
                Material blockID = location.getBlock().getType();
                if(blockID == Material.SNOW_BLOCK){
                    if(freezeMap.containsKey(player.getName())){
                        freezeMap.replace(player.getName(),freezeMap.get(player.getName())+7);
                    }else{
                        freezeMap.put(player.getName(),startingValue+11);
                    }
                    player.setFreezeTicks(Math.max(freezeMap.get(player.getName()),0));
                    //Utility.sendMessageToAllAdmins("after addition for " + player.getName() + ": "+freezeMap.get(player.getName()));
                }else{
                    if(freezeMap.containsKey(player.getName())){
                        freezeMap.replace(player.getName(),10);
                    }else{
                        freezeMap.put(player.getName(),startingValue);
                    }
                }
            }

        }
        for(Map.Entry<String, Integer> entry : freezeMap.entrySet()){
            if(entry.getValue()>-35){
                freezeMap.replace(entry.getKey(),entry.getValue()-5);
                //Utility.sendMessageToAllAdmins("After subtraction for " + player.getName() + ": "+freezeMap.get(player.getName()));
            }
        }
    }

    public BukkitTask startTaskTimer() {
        bukkitTask = runTaskTimer(mainManager, 0, 5);
        return bukkitTask;
    }
}

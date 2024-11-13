package pow.rats2spigot.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.json.JSONObject;
import pow.rats2spigot.MainManager;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class Utility {
    public static void sendMessageToAllAdmins(String message){
        for(Player player : Bukkit.getOnlinePlayers()){
            if(player.isOp()&&player.getScoreboardTags().contains("TellMe")){
                player.sendMessage(message);
            }
        }
        System.out.println(message);
    }
    // Method to retrieve the word by name from the file
    public static String getNewNameFromFile(UUID playerUuid) {
        File dataFile = new File("plugins/fakenames.json");
        try {
            if (!dataFile.exists()) {
                return null; // If the file doesn't exist, return null
            }

            // Read data from the file
            try (FileReader reader = new FileReader(dataFile)) {
                char[] buffer = new char[(int) dataFile.length()];
                reader.read(buffer);
                JSONObject jsonObject = new JSONObject(new String(buffer));

                // Return the word associated with the name, or null if not found
                return jsonObject.optString(playerUuid.toString(), null);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void cueSubtitlesForPlayers(ArrayList<Player> players,String s, long i,String sound,MainManager mainManager){
        for(Player player : players){
            cueSubtitle(player, s, i, sound, mainManager);
        }
    }

    public static void cueSubtitle(Player playerScanned, String s, long i,String sound,MainManager mainManager) {
        BukkitTask bukkitTask = new BukkitRunnable() {
            @Override
            public void run() {
                playerScanned.sendMessage(s);
                playerScanned.playSound(playerScanned,sound,1,1);
            }
        }.runTaskLater(mainManager, i);
    }

    public static void poofBlock(Location givenLocation){
        Location location = givenLocation.clone();
        location.getBlock().setType(Material.AIR);
        location.add(0.5, 0.5, 0.5);
        location.getWorld().spawnParticle(Particle.CLOUD, location, 0);
    }

    public static void writeNewNameToFile(UUID playerUuid, String newName) {
        File dataFile = new File("plugins/fakenames.json");
        try {
            // Ensure the file and its directories exist
            if (!dataFile.exists()) {
                dataFile.getParentFile().mkdirs();
                dataFile.createNewFile();
            }

            // Read existing data
            JSONObject jsonObject;
            if (dataFile.length() > 0) {
                try (FileReader reader = new FileReader(dataFile)) {
                    char[] buffer = new char[(int) dataFile.length()];
                    reader.read(buffer);
                    jsonObject = new JSONObject(new String(buffer));
                }
            } else {
                jsonObject = new JSONObject();
            }

            // Update or add the new name-word pair
            jsonObject.put(playerUuid.toString(), newName);

            // Write back to the file
            try (FileWriter writer = new FileWriter(dataFile)) {
                writer.write(jsonObject.toString(4)); // Pretty print with indentation
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setPlayerName(Player player, String newName, MainManager mainManager){
        player.setDisplayName(newName);
        player.setCustomName(newName);
        player.setCustomNameVisible(true);
        Utility.writeNewNameToFile(player.getUniqueId(),newName);
        if(mainManager.getReturningRats().contains(player.getName())){
            newName=mainManager.getReturningRatsTeam().getPrefix()+newName;
        }
        player.setPlayerListName(newName);

    }


    public static Location pointBehind(Location target, Location source, double distance) {
        // Calculate the direction vector from the target to this point (source)
        double dx = source.getX() - target.getX();
        double dy = source.getY() - target.getY();
        double dz = source.getZ() - target.getZ();

        // Calculate the magnitude (length) of the direction vector
        double magnitude = Math.sqrt(dx * dx + dy * dy + dz * dz);

        // Normalize the direction vector and scale by the distance
        double unitX = dx / magnitude * distance;
        double unitY = dy / magnitude * distance;
        double unitZ = dz / magnitude * distance;

        // Calculate the new point 1 unit behind the target
        return new Location(target.getWorld(),target.getX() - unitX, target.getY() - unitY, target.getZ() - unitZ);
    }

    public static Location getPositionBehindHead(Player player) {
        // Get the player's location
        Location playerLocation = player.getLocation();

        // Dynamically calculate head height based on player height
        double headHeightOffset = player.getHeight() * 0.85;  // Approximate head height at 85% of full height
        playerLocation.add(0, headHeightOffset, 0);

        // Get the direction the player is looking and reverse it for the position behind them
        Vector direction = playerLocation.getDirection().normalize();
        Vector reverseDirection = direction.multiply(-0.5);  // Adjust this multiplier for distance behind the head

        // Add the reversed direction to the location to get the position behind
        Location positionBehindHead = playerLocation.add(reverseDirection);

        return positionBehindHead;
    }
}

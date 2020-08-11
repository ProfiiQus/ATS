package eu.profiiqus.ats.managers;

import eu.profiiqus.ats.ATS;
import eu.profiiqus.ats.mysql.SQLManager;
import eu.profiiqus.ats.object.LocalPlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Class for handling all player data and sql manager connection
 *
 * @author Prof
 */
public class DataManager {

    private static DataManager instance;
    private ConfigManager config;
    private ATS plugin;

    private HashMap<UUID, LocalPlayer> playerData;
    private SQLManager sqlManager;

    /**
     * Class constructor, loads all values
     */
    private DataManager() {
        this.plugin = ATS.getPlugin();
        this.config = ConfigManager.getInstance();
        this.sqlManager = SQLManager.getInstance();
        this.playerData = this.sqlManager.getPlayerData();
    }

    /**
     * Gets static instance of Data Manager singleton
     * @return Instance of Data manager singleton
     */
    public static DataManager getInstance() {
        if(DataManager.instance == null) DataManager.instance = new DataManager();
        return DataManager.instance;
    }

    /**
     * Gets map of player data
     * @return Player data
     */
    public HashMap<UUID, LocalPlayer> getPlayerData() {
        return this.playerData;
    }

    /**
     * Schedules automatic data saves and time updates
      */
    public void scheduleAutoSaves() {
        this.plugin.getProxy().getScheduler().schedule(this.plugin, new Runnable() {
            @Override
            public void run() {
                updateTimes();
                sqlManager.savePlayerData(playerData);
            }
        }, this.config.delay, this.config.delay, TimeUnit.MINUTES);
    }

    /**
     * Updates times of all online players
     */
    private void updateTimes() {
        for(Map.Entry<UUID, LocalPlayer> entry: this.playerData.entrySet()) {
            LocalPlayer lp = entry.getValue();
            if(plugin.getProxy().getPlayer(entry.getKey()) != null) {
                lp.raiseByMillis(System.currentTimeMillis() - lp.getLastUpdate());
            }
        }
    }

    /**
     * Gets data object of provided player
     * @param player The player to get data
     * @return The player's data object
     */
    public LocalPlayer getPlayer(ProxiedPlayer player) {
        return this.playerData.get(player.getUniqueId());
    }

    /**
     * Checks whether player data constains player's UUID key
     * @param player Player to check for
     * @return Whether this player is tracked or not
     */
    public boolean isTracked(ProxiedPlayer player) {
        return this.playerData.containsKey(player.getUniqueId());
    }

    /**
     * Adds player to collection of tracked players
     * @param player The player to add
     */
    public void startTracking(ProxiedPlayer player) {
        UUID uniqueId = player.getUniqueId();
        this.playerData.put(uniqueId, new LocalPlayer(player.getName(), 0, 1));
        this.sqlManager.createPlayerData(uniqueId);
    }

    /**
     * Drops player from collection of tracked players
     * @param player The player to stop tracking
     */
    public void stopTracking(ProxiedPlayer player) {
        UUID uniqueId = player.getUniqueId();
        this.playerData.remove(uniqueId);
        this.sqlManager.dropPlayerData(uniqueId);
    }

    /**
     * Resets all player's data
     */
    public void resetStats() {
        HashMap<UUID, LocalPlayer> newData = new HashMap<>();
        for(Map.Entry<UUID, LocalPlayer> entry: this.playerData.entrySet()) {
            newData.put(entry.getKey(), new LocalPlayer(entry.getValue().getPlayerName(), 0, 1));
        }
        this.playerData = newData;
    }
}

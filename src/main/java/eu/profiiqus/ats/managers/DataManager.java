package eu.profiiqus.ats.managers;

import eu.profiiqus.ats.ATS;
import eu.profiiqus.ats.mysql.SQLManager;
import eu.profiiqus.ats.object.LocalPlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class DataManager {

    private static DataManager instance;
    private ConfigManager config;
    private ATS plugin;

    private HashMap<UUID, LocalPlayer> playerData;
    private SQLManager sqlManager;

    private DataManager() {
        this.plugin = ATS.getPlugin();
        this.config = ConfigManager.getInstance();
        this.sqlManager = SQLManager.getInstance();
        this.playerData = this.sqlManager.getPlayerData();
    }

    public static DataManager getInstance() {
        if(DataManager.instance == null) DataManager.instance = new DataManager();
        return DataManager.instance;
    }

    public HashMap<UUID, LocalPlayer> getPlayerData() {
        return this.playerData;
    }

    public void scheduleAutoSaves() {
        this.plugin.getProxy().getScheduler().schedule(this.plugin, new Runnable() {
            @Override
            public void run() {
                updateTimes();
                sqlManager.savePlayerData(playerData);
            }
        }, this.config.delay, this.config.delay, TimeUnit.MINUTES);
    }

    private void updateTimes() {
        for(Map.Entry<UUID, LocalPlayer> entry: this.playerData.entrySet()) {
            LocalPlayer lp = entry.getValue();
            if(plugin.getProxy().getPlayer(entry.getKey()) != null) {
                lp.raiseByMillis(System.currentTimeMillis() - lp.getLastUpdate());
            }
        }
    }

    public LocalPlayer getPlayer(ProxiedPlayer player) {
        return this.playerData.get(player.getUniqueId());
    }

    public boolean isTracked(ProxiedPlayer player) {
        return this.playerData.containsKey(player.getUniqueId());
    }

    public void startTracking(ProxiedPlayer player) {
        UUID uniqueId = player.getUniqueId();
        this.playerData.put(uniqueId, new LocalPlayer(player.getName(), 0, 1));
        this.sqlManager.createPlayerData(uniqueId);
    }

    public void stopTracking(ProxiedPlayer player) {
        UUID uniqueId = player.getUniqueId();
        this.playerData.remove(uniqueId);
        this.sqlManager.dropPlayerData(uniqueId);
    }

    public void resetStats() {
        HashMap<UUID, LocalPlayer> newData = new HashMap<>();
        for(Map.Entry<UUID, LocalPlayer> entry: this.playerData.entrySet()) {
            newData.put(entry.getKey(), new LocalPlayer(entry.getValue().getPlayerName(), 0, 1));
        }
        this.playerData = newData;
    }
}

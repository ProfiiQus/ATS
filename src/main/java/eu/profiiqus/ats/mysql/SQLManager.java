package eu.profiiqus.ats.mysql;

import eu.profiiqus.ats.ATS;
import eu.profiiqus.ats.StaticUtils;
import eu.profiiqus.ats.managers.ConfigManager;
import eu.profiiqus.ats.object.LocalPlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * SQL Manager class establishes and performs all database communication.
 * It loads, saves, drops and creates data and other stuff.
 *
 * @author Prof
 */
public class SQLManager {

    private ConnectionPoolManager pool;
    private ConfigManager config;
    private ATS plugin;
    private static SQLManager instance;

    /**
     * SQL Manager private constructor
     */
    private SQLManager() {
        this.plugin = ATS.getPlugin();
        this.pool = new ConnectionPoolManager(ATS.getPlugin());
    }

    /**
     * Get Instance returns singleton instance of SQL Manager
     * @return Singleton instance of SQL Manager
     */
    public static SQLManager getInstance() {
        if(SQLManager.instance == null) SQLManager.instance = new SQLManager();
        return SQLManager.instance;
    }

    /**
     * Test Connection method tests connection to the database and
     * returns boolean on whether the test was successful.
     * @return Whether connection to database was established
     */
    public boolean testConnection() {
        Connection conn = null;

        try {
            conn = this.pool.getConnection();
            conn.close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Create tables method creates plugin's data table if it
     * does not exist already in the DB.
     */
    public void createTables() {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS ats_data (Id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, PlayerUUID NVARCHAR(128), PlayerName NVARCHAR(32), TimePlayed BIGINT(19), JoinCounter INT)");
            ps.execute();
            ps.close();
            conn.close();
        } catch (SQLException e) {
            StaticUtils.logMessage("Critical error occurred while creating ats_data table: ");
            e.printStackTrace();
        }
    }

    /**
     * Get Player data loads all player data from the database
     * @return All player data from the database
     */
    public HashMap<UUID, LocalPlayer> getPlayerData() {
        Connection conn = null;
        PreparedStatement ps = null;
        HashMap<UUID, LocalPlayer> resultData = new HashMap<>();

        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT * FROM ats_data");
            ResultSet resultSet = ps.executeQuery();
            while(resultSet.next()) {
                resultData.put(UUID.fromString(resultSet.getString("PlayerUUID")), new LocalPlayer(resultSet.getString("PlayerName"), resultSet.getLong("TimePlayed"), resultSet.getInt("JoinCounter")));
            }
            ps.closeOnCompletion();
        } catch (SQLException e) {
            StaticUtils.logMessage("Critical error occurred while loading data from database: ");
            e.printStackTrace();
        }
        return resultData;
    }

    /**
     * Drop Player data drops player's data from the database. All is again made async.
     * @param uniqueId Player's UUID to drop data
     */
    public void dropPlayerData(UUID uniqueId) {
        plugin.getProxy().getScheduler().runAsync(plugin, new Runnable() {
            @Override
            public void run() {
                Connection conn = null;
                PreparedStatement ps = null;

                try {
                    conn = pool.getConnection();
                    ps = conn.prepareStatement("DELETE FROM ats_data WHERE PlayerUUID = '" + uniqueId.toString() + "'");
                    ps.executeUpdate();
                    ps.closeOnCompletion();
                    conn.close();
                } catch (SQLException e) {
                    StaticUtils.logMessage("Critical error occurred while trying to drop player data: ");
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Save Player data method saves provided player data into the database.
     * All is made async as it's possible to be done so.
     * @param playerData Player data to be saved
     */
    public void savePlayerData(HashMap<UUID, LocalPlayer> playerData) {
        plugin.getProxy().getScheduler().runAsync(plugin, new Runnable() {
            @Override
            public void run() {
                Connection conn = null;
                PreparedStatement ps = null;

                try {
                    conn = pool.getConnection();
                    LocalPlayer lp;
                    for(Map.Entry<UUID, LocalPlayer> entry: playerData.entrySet()) {
                        lp = entry.getValue();
                        ps = conn.prepareStatement("UPDATE ats_data SET TimePlayed = ?, JoinCounter = ?, PlayerName = ? WHERE PlayerUUID = '" + entry.getKey().toString() + "'");
                        ps.setLong(1, lp.getPlayed());
                        ps.setInt(2, lp.getTimesJoined());
                        ps.setString(3, lp.getPlayerName());
                        ps.executeUpdate();
                        ps.closeOnCompletion();
                        conn.close();
                    }
                } catch (SQLException e) {
                    StaticUtils.logMessage("Critical error occurred while creating default player data: ");
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create Player data method opens connection to the database and creates default player data.
     * All is async as there is no bungee/bukkit api communication at all.
     * @param uniqueId Player's UUID to create data for
     */
    public void createPlayerData(UUID uniqueId) {
        plugin.getProxy().getScheduler().runAsync(plugin, new Runnable() {
            @Override
            public void run() {
                Connection conn = null;
                PreparedStatement ps = null;

                try {
                    conn = pool.getConnection();
                    ps = conn.prepareStatement("INSERT INTO ats_data (PlayerUUID, TimePlayed, JoinCounter) VALUES (?, ?, ?)");
                    ps.setString(1, uniqueId.toString());
                    ps.setLong(2, 0);
                    ps.setInt(3, 1);
                    ps.execute();
                    ps.closeOnCompletion();
                    conn.close();
                } catch (SQLException e) {
                    StaticUtils.logMessage("Critical error occurred while creating default player data: ");
                    e.printStackTrace();
                }
            }
        });
    }
}

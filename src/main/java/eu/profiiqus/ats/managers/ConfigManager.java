package eu.profiiqus.ats.managers;

import eu.profiiqus.ats.ATS;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;

/**
 * Class for handling and managing all plugin's configuration
 *
 * @author Prof
 */
public class ConfigManager {

    private File configFile;
    private Configuration config;
    private final ATS plugin;

    public String hostname, database, username, password, notEnoughPermissions, addPlayer, showPlayer, dropPlayer, playerNotTracked, playerAlreadyTracked, playerNotFound, commandUsage, listHeader, listFooter, listFormat, dataReset;
    public int delay, port, timeout, minimumConnections, maximumConnections;
    public ArrayList<String> helpMenu;

    private static ConfigManager instance;

    /**
     * Config manager constructor
     */
    private ConfigManager() {
        this.plugin = ATS.getPlugin();
    }

    /**
     * Gets singleton instance of Config manager
     * @return Singleton instance of Config manager
     */
    public static ConfigManager getInstance() {
        if(instance == null) instance = new ConfigManager();
        return instance;
    }

    /**
     * Sets up all variables and loads all configuration from files
     */
    public void setupFiles() {

        if(!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        configFile = new File(plugin.getDataFolder(), "config.yml");

        if(!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            try (InputStream in = this.plugin.getResourceAsStream("config.yml")) {
                Files.copy(in, this.configFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Loads all values from file into configuration fields
     */
    public void loadConfig() {
        try {
            this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(this.plugin.getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.hostname = this.config.getString("mysql.host");
        this.port = this.config.getInt("mysql.port");
        this.database = this.config.getString("mysql.database");
        this.username = this.config.getString("mysql.user");
        this.password = this.config.getString("mysql.password");
        this.timeout = this.config.getInt("mysql.timeout");
        this.minimumConnections = this.config.getInt("mysql.min-connections");
        this.maximumConnections = this.config.getInt("mysql.max-connections");
        this.delay = this.config.getInt("delay");
        this.notEnoughPermissions = this.config.getString("messages.not-enough-permissions");
        this.addPlayer = this.config.getString("messages.player-added");
        this.dropPlayer = this.config.getString("messages.player-removed");
        this.showPlayer = this.config.getString("messages.player-show");
        this.playerNotFound = this.config.getString("messages.player-not-found");
        this.playerNotTracked = this.config.getString("messages.player-not-tracked");
        this.playerAlreadyTracked = this.config.getString("messages.player-already-tracked");
        this.commandUsage = this.config.getString("messages.command-usage");
        this.helpMenu = (ArrayList<String>) this.config.getList("messages.help-menu");
        this.listHeader = this.config.getString("messages.player-list.header");
        this.listFormat = this.config.getString("messages.player-list.format");
        this.listFooter = this.config.getString("messages.player-list.footer");
        this.dataReset = this.config.getString("messages.stats-reset");
    }
}

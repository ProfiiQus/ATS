package eu.profiiqus.ats;

import eu.profiiqus.ats.commands.PluginCommand;
import eu.profiiqus.ats.handlers.ConnectHandler;
import eu.profiiqus.ats.handlers.DisconnectHandler;
import eu.profiiqus.ats.managers.ConfigManager;
import eu.profiiqus.ats.managers.DataManager;
import eu.profiiqus.ats.mysql.SQLManager;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

/**
 * ATS class is the plugin's main class
 * It contains all the main initiation methods and holds its static instnace
 *
 * @author Prof
 */
public final class ATS extends Plugin {

    private static ATS plugin;

    /**
     * On enable method contains the plugin's on-load functionality.
     * If SQL Manager loads correctly, all other managers, events and commands are also loaded.
     * If it's not loaded, the plugin is not loaded.
     */
    @Override
    public void onEnable() {
        ATS.plugin = this;
        this.setupConfig();
        if(this.setupSQLManager()) {
            StaticUtils.logMessage("Database connection established successfully");
            this.setupDataManager();
            this.registerCommands();
            this.registerEvents();
            StaticUtils.logMessage("Plugin initialization finished");
        } else {
            StaticUtils.logMessage("Failed to establish database connection. Please re-check your settings");
        }
    }

    /**
     * On disable method contains the plugin's on disable functionality.
     * It saves online player's data when it disables.
     */
    @Override
    public void onDisable() {
        SQLManager.getInstance().savePlayerData(DataManager.getInstance().getPlayerData());
    }

    /**
     * Get Plugin method returns static instance of ATS plugin
     * @return Static instance of ATS plugin
     */
    public static ATS getPlugin() {
        return ATS.plugin;
    }

    /**
     * Setup Config initiates plugin's configuration manager
     */
    private void setupConfig() {
        ConfigManager config = ConfigManager.getInstance();
        config.setupFiles();
        config.loadConfig();
    }

    /**
     * Setup Data Manager initiates data manager's works
     */
    private void setupDataManager() {
        DataManager dataManager = DataManager.getInstance();
        SQLManager.getInstance().createTables();
        dataManager.scheduleAutoSaves();
    }

    /**
     * Setup SQL Manager method sets up SQL Manager and returns
     * boolean on whether test connection was successful or not
     * @return Boolean whether test connection was successful
     */
    private boolean setupSQLManager() {
        SQLManager sqlManager = SQLManager.getInstance();
        return sqlManager.testConnection();
    }

    /**
     * Register commands method registers all ATS plugin's commands
     */
    private void registerCommands() {
        PluginManager manager = getProxy().getPluginManager();
        manager.registerCommand(this, new PluginCommand());
    }

    /**
     * Register events method registers all ATS plugin's events
     */
    private void registerEvents() {
        PluginManager manager = getProxy().getPluginManager();
        manager.registerListener(this, new ConnectHandler());
        manager.registerListener(this, new DisconnectHandler());
    }
}

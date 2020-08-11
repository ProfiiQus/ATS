package eu.profiiqus.ats.commands.subcommands;

import eu.profiiqus.ats.StaticUtils;
import eu.profiiqus.ats.managers.ConfigManager;
import eu.profiiqus.ats.managers.DataManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * Wrapper class for reset players sub command
 *
 * @author Prof
 */
public class ResetPlayers implements SubCommand {

    private ConfigManager config;
    private DataManager dataManager;

    /**
     * Reset Players constructor
     */
    public ResetPlayers() {
        this.config = ConfigManager.getInstance();
        this.dataManager = DataManager.getInstance();
    }

    /**
     * Resets all player's data
     * @param sender Sender of the command
     * @param args Command's arguments
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        dataManager.resetStats();
        sender.sendMessage(new TextComponent(StaticUtils.alterColors(this.config.dataReset)));
    }
}

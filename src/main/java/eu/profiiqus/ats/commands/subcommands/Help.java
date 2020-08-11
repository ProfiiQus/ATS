package eu.profiiqus.ats.commands.subcommands;

import eu.profiiqus.ats.StaticUtils;
import eu.profiiqus.ats.managers.ConfigManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * Wrapper class for help menu sub command
 *
 * @author Prof
 */
public class Help implements SubCommand {

    private ConfigManager config;

    /**
     * Help constructor
     */
    public Help() {
        this.config = ConfigManager.getInstance();
    }

    /**
     * Shows a help menu to sender
     * @param sender Sender of the command
     * @param args Command's arguments
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        for(String msg: this.config.helpMenu) {
            sender.sendMessage(new TextComponent(StaticUtils.alterColors(msg)));
        }
    }
}

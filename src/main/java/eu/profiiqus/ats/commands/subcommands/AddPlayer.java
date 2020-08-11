package eu.profiiqus.ats.commands.subcommands;

import eu.profiiqus.ats.ATS;
import eu.profiiqus.ats.StaticUtils;
import eu.profiiqus.ats.managers.ConfigManager;
import eu.profiiqus.ats.managers.DataManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * Add Player class is a wrapper class for add player sub command.
 * This sub command should add the player to the list of tracked players.
 *
 * @author Prof
 */
public class AddPlayer implements SubCommand {

    private DataManager dataManager;
    private ConfigManager config;
    private ATS plugin;

    /**
     * Add Player constructor
     */
    public AddPlayer() {
        this.plugin = ATS.getPlugin();
        this.config = ConfigManager.getInstance();
        this.dataManager = DataManager.getInstance();
    }

    /**
     * Adds player to tracked player's collection
     * @param sender Sender of the command
     * @param args Command's arguments
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 1) {
            sender.sendMessage(new TextComponent(StaticUtils.alterColors(this.config.commandUsage
                    .replace("{ACTION}", args[0]))));
            return;
        }

        String playerName = args[1];
        ProxiedPlayer player = plugin.getProxy().getPlayer(playerName);

        if(player == null) {
            sender.sendMessage(new TextComponent(StaticUtils.alterColors(this.config.playerNotFound
                .replace("{PLAYER}", playerName))));
            return;
        }

        if(dataManager.isTracked(player)) {
            sender.sendMessage(new TextComponent(StaticUtils.alterColors(this.config.playerAlreadyTracked
                    .replace("{PLAYER}", playerName))));
            return;
        }

        dataManager.startTracking(player);
        sender.sendMessage(new TextComponent(StaticUtils.alterColors(this.config.addPlayer
                .replace("{PLAYER}", playerName))));
    }
}

package eu.profiiqus.ats.commands.subcommands;

import eu.profiiqus.ats.ATS;
import eu.profiiqus.ats.StaticUtils;
import eu.profiiqus.ats.managers.ConfigManager;
import eu.profiiqus.ats.managers.DataManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class DropPlayer implements SubCommand {

    private DataManager dataManager;
    private ConfigManager config;
    private ATS plugin;

    public DropPlayer() {
        this.plugin = ATS.getPlugin();
        this.config = ConfigManager.getInstance();
        this.dataManager = DataManager.getInstance();
    }

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

        if(!this.dataManager.isTracked(player)) {
            sender.sendMessage(new TextComponent(StaticUtils.alterColors(this.config.playerNotTracked
                .replace("{PLAYER}", playerName))));
            return;
        }

        this.dataManager.stopTracking(player);
        sender.sendMessage(new TextComponent(StaticUtils.alterColors(this.config.dropPlayer
            .replace("{PLAYER}", playerName))));
    }
}

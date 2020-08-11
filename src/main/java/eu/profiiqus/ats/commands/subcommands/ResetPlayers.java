package eu.profiiqus.ats.commands.subcommands;

import eu.profiiqus.ats.StaticUtils;
import eu.profiiqus.ats.managers.ConfigManager;
import eu.profiiqus.ats.managers.DataManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

public class ResetPlayers implements SubCommand {

    private ConfigManager config;
    private DataManager dataManager;

    public ResetPlayers() {
        this.config = ConfigManager.getInstance();
        this.dataManager = DataManager.getInstance();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        dataManager.resetStats();
        sender.sendMessage(new TextComponent(StaticUtils.alterColors(this.config.dataReset)));
    }
}

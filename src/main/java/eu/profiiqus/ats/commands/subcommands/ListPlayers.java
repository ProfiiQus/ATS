package eu.profiiqus.ats.commands.subcommands;

import eu.profiiqus.ats.StaticUtils;
import eu.profiiqus.ats.managers.ConfigManager;
import eu.profiiqus.ats.managers.DataManager;
import eu.profiiqus.ats.object.LocalPlayer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.Map;
import java.util.UUID;

public class ListPlayers implements SubCommand {

    private DataManager dataManager;
    private ConfigManager config;

    public ListPlayers() {
        this.config = ConfigManager.getInstance();
        this.dataManager = DataManager.getInstance();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage(new TextComponent(StaticUtils.alterColors(this.config.listHeader)));
        LocalPlayer lp;
        for(Map.Entry<UUID, LocalPlayer> entry: dataManager.getPlayerData().entrySet()) {
            lp = entry.getValue();
            sender.sendMessage(new TextComponent(StaticUtils.alterColors(this.config.listFormat
                .replace("{PLAYER}", lp.getPlayerName())
                .replace("{TIME}", lp.getFormattedTime())
                .replace("{TIMES_JOINED}", Integer.toString(lp.getTimesJoined())))));
        }
        sender.sendMessage(new TextComponent(StaticUtils.alterColors(this.config.listFooter)));
    }
}

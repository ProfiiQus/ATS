package eu.profiiqus.ats.commands.subcommands;

import eu.profiiqus.ats.StaticUtils;
import eu.profiiqus.ats.managers.ConfigManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

public class Help implements SubCommand {

    private ConfigManager config;

    public Help() {
        this.config = ConfigManager.getInstance();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        for(String msg: this.config.helpMenu) {
            sender.sendMessage(new TextComponent(StaticUtils.alterColors(msg)));
        }
    }
}

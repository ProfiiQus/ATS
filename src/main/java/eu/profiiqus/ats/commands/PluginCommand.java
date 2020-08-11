package eu.profiiqus.ats.commands;

import eu.profiiqus.ats.commands.subcommands.*;
import eu.profiiqus.ats.managers.ConfigManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.HashMap;

public class PluginCommand extends Command {

    private HashMap<String, SubCommand> availableSubCommands;
    private ConfigManager config;

    public PluginCommand() {
        super("ats");
        this.config = ConfigManager.getInstance();
        this.availableSubCommands = new HashMap<String, SubCommand>() {
            {
                put("list", new ListPlayers());
                put("show", new ShowPlayer());
                put("track", new AddPlayer());
                put("drop", new DropPlayer());
                put("help", new Help());
                put("reset", new ResetPlayers());
            }
        };
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer) {
            if(!sender.getPermissions().contains("ats.admin")) {
                sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', this.config.notEnoughPermissions)));
                return;
            }
        }

        if(args.length == 0) {
            this.availableSubCommands.get("help").execute(sender, args);
            return;
        }

        if(this.availableSubCommands.containsKey(args[0])) {
            this.availableSubCommands.get(args[0]).execute(sender, args);
        } else {
            this.availableSubCommands.get("help").execute(sender, args);
        }
    }
}

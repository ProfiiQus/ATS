package eu.profiiqus.ats.commands.subcommands;

import net.md_5.bungee.api.CommandSender;

public interface SubCommand {

    void execute(CommandSender sender, String[] args);
}

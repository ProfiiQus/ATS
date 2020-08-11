package eu.profiiqus.ats.commands.subcommands;

import net.md_5.bungee.api.CommandSender;

/**
 * Interface for command pattern sub commands
 *
 * @author Prof
 */
public interface SubCommand {

    /**
     * Sub commands functionality method
     * @param sender Sender of the command
     * @param args Command's arguments
     */
    void execute(CommandSender sender, String[] args);
}

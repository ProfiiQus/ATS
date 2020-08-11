package eu.profiiqus.ats;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * Static Utils is a static class for holding utility methods and other static variables
 *
 * @author Prof
 */
public class StaticUtils {

    /**
     * Log Message method logs colored message into the console
     * @param message Message to display
     */
    public static void logMessage(String message) {
        ATS.getPlugin().getProxy().getConsole().sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&3[&bATS&3]&f " + message)));
    }

    /**
     * Alter colors method alters color codes in message and returns a colored string
     * @param message Message to alter color codes in
     * @return Message with altered color codes (colored message)
     */
    public static String alterColors(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}

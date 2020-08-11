package eu.profiiqus.ats.handlers;

import eu.profiiqus.ats.managers.DataManager;
import eu.profiiqus.ats.object.LocalPlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * Disconnect Handler class is a listener wrapper for player quit server event.
 * If the player disconnects, his data are updated.
 *
 * @author Prof
 */
public class DisconnectHandler implements Listener {

    private DataManager dataManager;

    /**
     * Constructor loads all required field values
     */
    public DisconnectHandler() {
        this.dataManager = DataManager.getInstance();
    }

    /**
     * The event listening method, when a player disconnects, it updates
     * it's player data and saves it into memory.
     * @param disconnectEvent The player disconnect event
     */
    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent disconnectEvent) {
        ProxiedPlayer pp = disconnectEvent.getPlayer();
        if(this.dataManager.isTracked(pp)) {
            LocalPlayer lp = this.dataManager.getPlayer(pp);
            lp.raiseByMillis(System.currentTimeMillis() - lp.getLastUpdate());
        }
    }
}

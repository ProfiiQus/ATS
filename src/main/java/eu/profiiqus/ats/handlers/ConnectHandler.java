package eu.profiiqus.ats.handlers;

import eu.profiiqus.ats.managers.DataManager;
import eu.profiiqus.ats.object.LocalPlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * Wrapper class for player joining events
 *
 * @author Prof
 */
public class ConnectHandler implements Listener {

    private DataManager dataManager;

    /**
     * Connect Handler constructor
     */
    public ConnectHandler() {
        this.dataManager = DataManager.getInstance();
    }

    /**
     * Listener for the player post login event, updates the last update time and raises joins
     * @param loginEvent The player join event
     */
    @EventHandler
    public void onPlayerJoin(PostLoginEvent loginEvent) {
        ProxiedPlayer pp = loginEvent.getPlayer();
        if(this.dataManager.isTracked(pp)) {
            LocalPlayer lp = this.dataManager.getPlayer(pp);
            lp.setLastUpdate(System.currentTimeMillis());
            lp.raiseJoins();
        }
    }
}

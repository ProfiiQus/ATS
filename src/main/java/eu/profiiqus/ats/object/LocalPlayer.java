package eu.profiiqus.ats.object;

import java.util.concurrent.TimeUnit;

/**
 * Local Player object is the plugin's main object for storing player data.
 *
 * @author Prof
 */
public class LocalPlayer {

    private long played, lastUpdate;
    private String playerName;
    private int timesJoined;

    /**
     * Local player object constructor
     * @param playerName The player's name
     * @param played Amount of milliseconds the player has played
     * @param timesJoined Amount of times the player has joined
     */
    public LocalPlayer(String playerName, long played, int timesJoined) {
        this.playerName = playerName;
        this.played = played;
        this.timesJoined = timesJoined;
        this.lastUpdate = System.currentTimeMillis();
    }

    /**
     * Gets the player's playtime
     * @return The player's playtime
     */
    public long getPlayed() {
        return this.played;
    }

    /**
     * Gets the amount of time the player has joined the server
     * @return Amount of times the player has joined
     */
    public int getTimesJoined() {
        return this.timesJoined;
    }

    /**
     * Gets the milliseconds of the last update
     * @return Timestamp of the last update
     */
    public long getLastUpdate() {
        return this.lastUpdate;
    }

    /**
     * Sets the timestamp of last update
     * @param millis New timestamp of last update
     */
    public void setLastUpdate(long millis) {
        this.lastUpdate = millis;
    }

    /**
     * Raises the amount the player has joined the server
     */
    public void raiseJoins() {
        this.timesJoined++;
    }

    /**
     * Raises the player's playtime by milliseconds
     * @param millis The amount of milliseconds to raise the playtime by
     */
    public void raiseByMillis(long millis) {
        this.played += millis;
        this.lastUpdate = System.currentTimeMillis();
    }

    /**
     * Gets the player's name
     * @return The player's name
     */
    public String getPlayerName() {
        return this.playerName;
    }

    /**
     * Gets formatted string of player's playtime
     * @return Formatted string of player's playtime
     */
    public String getFormattedTime() {
        if(this.played > 86400000) {
            return TimeUnit.MILLISECONDS.toDays(this.played) + "d "
                    + (TimeUnit.MILLISECONDS.toHours(this.played) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(this.played))) + "h "
                    + (TimeUnit.MILLISECONDS.toMinutes(this.played) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(this.played))) + "m "
                    + (TimeUnit.MILLISECONDS.toSeconds(this.played) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(this.played))) + "s";
        }

        if(this.played > 3600000) {
            return TimeUnit.MILLISECONDS.toHours(this.played) + "h "
                    + (TimeUnit.MILLISECONDS.toMinutes(this.played) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(this.played))) + "m "
                    + (TimeUnit.MILLISECONDS.toSeconds(this.played) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(this.played))) + "s";
        }

        if(this.played > 60000) {
            return TimeUnit.MILLISECONDS.toMinutes(this.played) + "m "
                    + (TimeUnit.MILLISECONDS.toSeconds(this.played) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(this.played))) + "s";
        }

        return TimeUnit.MILLISECONDS.toSeconds(this.played) + "s";
    }
}

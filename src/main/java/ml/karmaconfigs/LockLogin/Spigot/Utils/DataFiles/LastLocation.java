package ml.karmaconfigs.LockLogin.Spigot.Utils.DataFiles;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

/*
GNU LESSER GENERAL PUBLIC LICENSE
                       Version 2.1, February 1999

 Copyright (C) 1991, 1999 Free Software Foundation, Inc.
 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 Everyone is permitted to copy and distribute verbatim copies
 of this license document, but changing it is not allowed.

[This is the first released version of the Lesser GPL.  It also counts
 as the successor of the GNU Library Public License, version 2, hence
 the version number 2.1.]
 */

public final class LastLocation {

    private final LocationDatabase data;

    public LastLocation(Player player) {
        this.data = new LocationDatabase(player);
    }

    public LastLocation(String player) {
        this.data = new LocationDatabase(player);
    }

    /**
     * Check if the player has last location
     *
     * @return a boolean
     */
    public final boolean hasLastLocation() {
        return data.hasLastLoc();
    }

    /**
     * Get the player last location
     * world
     *
     * @return a World
     */
    public final World getWorld() {
        return data.getWorld();
    }

    /**
     * Get the player last location X
     *
     * @return a double
     */
    public final double getX() {
        return data.getX();
    }

    /**
     * Get the player last location Y
     *
     * @return a double
     */
    public final double getY() {
        return data.getY();
    }

    /**
     * Get the player last location Z
     *
     * @return a double
     */
    public final double getZ() {
        return data.getZ();
    }

    /**
     * Get the player last location Pitch
     *
     * @return a double
     */
    public final double getPitch() {
        return data.getPitch();
    }

    /**
     * Get the player last location Yaw
     *
     * @return a double
     */
    public final double getYaw() {
        return data.getYaw();
    }

    /**
     * Get the player last location location
     *
     * @return a location
     */
    public final Location getLastLocation() {
        return data.getLastLoc();
    }

    /**
     * Set the player last location location
     */
    public final void saveLocation() {
        data.saveLocation();
    }

    /**
     * Remove the player last location location
     */
    public final void removeLocation() {
        data.removeLocation();
    }
}

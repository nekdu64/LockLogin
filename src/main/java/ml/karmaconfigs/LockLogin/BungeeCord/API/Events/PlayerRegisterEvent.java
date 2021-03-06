package ml.karmaconfigs.LockLogin.BungeeCord.API.Events;

import ml.karmaconfigs.LockLogin.BungeeCord.API.PlayerAPI;
import ml.karmaconfigs.LockLogin.BungeeCord.Utils.Files.BungeeFiles;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Event;

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

public class PlayerRegisterEvent extends Event implements BungeeFiles {

    private static String registerMessage;
    private final ProxiedPlayer player;

    /**
     * Initialize the player verify event
     *
     * @param player   the player
     */
    public PlayerRegisterEvent(ProxiedPlayer player) {
        this.player = player;
        registerMessage = messages.Registered();
    }

    /**
     * Get the event player
     *
     * @return a Proxied player
     */
    public final ProxiedPlayer getPlayer() {
        return player;
    }

    /**
     * Get the plugin register message
     *
     * @return a String
     */
    public final String getRegisterMessage() {
        return registerMessage;
    }

    /**
     * Set the login message
     *
     * @param message the message
     */
    public final void setRegisterMessage(String message) {
        if (!message.isEmpty()) {
            registerMessage = message.replace("{player}", player.getName());
        }
    }

    /**
     * Get the player API
     *
     * @return a PlayerAPI
     */
    public final PlayerAPI getAPI() {
        return new PlayerAPI(player);
    }
}

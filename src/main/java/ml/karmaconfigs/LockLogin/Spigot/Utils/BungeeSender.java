package ml.karmaconfigs.LockLogin.Spigot.Utils;

import ml.karmaconfigs.LockLogin.Logs.Logger;
import ml.karmaconfigs.LockLogin.Platform;
import ml.karmaconfigs.LockLogin.Spigot.LockLoginSpigot;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

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

public final class BungeeSender implements LockLoginSpigot {

    private final Player player;

    /**
     * Initialize the bungee sender class
     *
     * @param player the player
     */
    public BungeeSender(Player player) {
        this.player = player;
    }

    /**
     * Send the pin input to BungeeCord
     *
     * @param input the pin input
     */
    public final void sendPinInput(int input) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("PinInput_" + player.getUniqueId().toString() + "_" + input);
        } catch (Throwable e) {
            Logger.log(Platform.SPIGOT, "ERROR WHILE SENDING PIN INPUT TO BUNGEECORD", e);
        }
        player.sendPluginMessage(plugin, "ll:info", b.toByteArray());
    }
}

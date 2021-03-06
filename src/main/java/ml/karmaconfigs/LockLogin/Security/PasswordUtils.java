package ml.karmaconfigs.LockLogin.Security;

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

import ml.karmaconfigs.LockLogin.Security.Codifications.Codification;
import ml.karmaconfigs.LockLogin.Security.Plugins.AuthMe.AuthMeAuth;
import ml.karmaconfigs.LockLogin.Security.Plugins.AuthMe.AuthMeBcrypt;
import ml.karmaconfigs.LockLogin.Security.Plugins.LoginSecurity.LoginSecurityAuth;
import org.apache.commons.codec.binary.Base64;

public final class PasswordUtils {

    private final String password;
    private String token;
    private byte[] encoded;

    /**
     * Start the password utils
     *
     * @param password the password
     */
    public PasswordUtils(String password) {
        this.password = password;
    }

    /**
     * Start the password utils
     *
     * @param password the password
     * @param token    the token
     */
    public PasswordUtils(String password, String token) {
        this.password = password;
        this.token = token;
    }

    /**
     * Get the encrypted password
     *
     * @return a String
     */
    public final String Encrypted() {
        return new Codification().hash(password);
    }

    /**
     * Get a hashed string of
     * password
     *
     * @return a String
     */
    public final String Hash() {
        encoded = Base64.encodeBase64(Encrypted().getBytes());
        return new String(encoded);
    }

    /**
     * Get a hashed string of
     * password string
     *
     * @return a String
     */
    public final String HashString() {
        encoded = Base64.encodeBase64(password.getBytes());
        return new String(encoded);
    }

    /**
     * Unhash the password
     *
     * @return a String
     */
    public final String UnHash() {
        byte[] decoded = Base64.decodeBase64(password);
        return new String(decoded);
    }

    /**
     * Check if the password is equals
     * to the decoded password
     *
     * @return a boolean
     */
    public final boolean PasswordIsOk() {
        try {
            byte[] decode = Base64.decodeBase64(token);

            if (new Codification().auth(password, new String(decode))) {
                return true;
            } else {
                if (new AuthMeAuth().check(password, token)) {
                    return true;
                } else {
                    return new LoginSecurityAuth().check(password, token);
                }
            }
        } catch (Throwable e) {
            if (new AuthMeAuth().check(password, token)) {
                return true;
            } else {
                if (new AuthMeBcrypt().check(password, token)) {
                    return true;
                } else {
                    return new LoginSecurityAuth().check(password, token);
                }
            }
        }
    }
}

package ml.karmaconfigs.LockLogin.Spigot.Commands;

import ml.karmaconfigs.LockLogin.FileMigration.FileInserter;
import ml.karmaconfigs.LockLogin.InsertInfo;
import ml.karmaconfigs.LockLogin.MySQL.AccountMigrate;
import ml.karmaconfigs.LockLogin.MySQL.Bucket;
import ml.karmaconfigs.LockLogin.MySQL.Insert.BucketInserter;
import ml.karmaconfigs.LockLogin.MySQL.Migrate;
import ml.karmaconfigs.LockLogin.MySQL.SQLite.SQLiteReader;
import ml.karmaconfigs.LockLogin.MySQL.Utils;
import ml.karmaconfigs.LockLogin.Platform;
import ml.karmaconfigs.LockLogin.Spigot.LockLoginSpigot;
import ml.karmaconfigs.LockLogin.Spigot.Utils.DataFiles.MySQLData;
import ml.karmaconfigs.LockLogin.Spigot.Utils.Files.SpigotFiles;
import ml.karmaconfigs.LockLogin.Spigot.Utils.LockLoginSpigotManager;
import ml.karmaconfigs.LockLogin.Spigot.Utils.User.User;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.io.File;
import java.sql.Connection;

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

public final class LockLoginCommand implements CommandExecutor, LockLoginSpigot, SpigotFiles {

    private final Permission migratePermission = new Permission("locklogin.migrate", PermissionDefault.FALSE);
    private final Permission applyUpdatePermission = new Permission("locklogin.update", PermissionDefault.FALSE);

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            User user = new User(player);

            if (args.length == 0) {
                user.Message(messages.Prefix() + "&cSpecify an action &7( &e/locklogin migrate &7|| &e/locklogin applyUpdates &7)");
            } else {
                if (args[0] != null) {
                    if (args[0].equals("migrate")) {
                        if (player.hasPermission(migratePermission)) {
                            if (!config.isBungeeCord()) {
                                if (args.length == 1) {
                                    user.Message(messages.Prefix() + "&cPlease specify the migration: &7/locklogin migrate <MySQL, AuthMe>");
                                } else {
                                    if (args.length == 2) {
                                        String method = args[1];
                                        switch (method) {
                                            case "MySQL":
                                                migrateMySQL(player);
                                                break;
                                            case "AuthMe":
                                                user.Message(messages.Prefix() + "&cPlease, specify database name and table name (must exist in plugins/AuthMe folder)");
                                                break;
                                            default:
                                                user.Message(messages.Prefix() + "&cPlease specify the migration: &7/locklogin migrate <MySQL, AuthMe>");
                                                break;
                                        }
                                    } else {
                                        if (args.length == 3) {
                                            String method = args[1];
                                            switch (method) {
                                                case "MySQL":
                                                    user.Message(messages.Prefix() + "&cToo many args, please, use /locklogin migrate MySQL");
                                                    break;
                                                case "AuthMe":
                                                    user.Message(messages.Prefix() + "&cPlease, specify table name");
                                                    break;
                                                default:
                                                    user.Message(messages.Prefix() + "&cPlease specify the migration: &7/locklogin migrate <MySQL, AuthMe>");
                                                    break;
                                            }
                                        } else {
                                            if (args.length == 4) {
                                                String method = args[1];
                                                switch (method) {
                                                    case "MySQL":
                                                        user.Message(messages.Prefix() + "&cToo many args, please, use /locklogin migrate MySQL");
                                                        break;
                                                    case "AuthMe":
                                                        user.Message(messages.Prefix() + "&cPlease specify the 'realname' column");
                                                        break;
                                                    default:
                                                        user.Message(messages.Prefix() + "&cPlease specify the migration: &7/locklogin migrate <MySQL, AuthMe>");
                                                        break;
                                                }
                                            } else {
                                                if (args.length == 5) {
                                                    String method = args[1];
                                                    switch (method) {
                                                        case "MySQL":
                                                            user.Message(messages.Prefix() + "&cToo many args, please, use /locklogin migrate MySQL");
                                                            break;
                                                        case "AuthMe":
                                                            user.Message(messages.Prefix() + "&cPlease specify the 'password' column");
                                                            break;
                                                        default:
                                                            user.Message(messages.Prefix() + "&cPlease specify the migration: &7/locklogin migrate <MySQL, AuthMe>");
                                                            break;
                                                    }
                                                } else {
                                                    if (args.length == 6) {
                                                        String method = args[1];
                                                        switch (method) {
                                                            case "MySQL":
                                                                user.Message(messages.Prefix() + "&cToo many args, please, use /locklogin migrate MySQL");
                                                                break;
                                                            case "AuthMe":
                                                                user.Message(messages.Prefix() + "&aMigrating from AuthMe sqlite");
                                                                if (migrateAuthMe(args[2], args[3], args[4], args[5])) {
                                                                    user.Message(messages.Prefix() + "&aMigration successfully");
                                                                } else {
                                                                    user.Message(messages.Prefix() + "&cSome error occurred while migrating");
                                                                }
                                                                break;
                                                            default:
                                                                user.Message(messages.Prefix() + "&cPlease specify the migration: &7/locklogin migrate <MySQL, AuthMe>");
                                                                break;
                                                        }
                                                    } else {
                                                        user.Message(messages.Prefix() + "&cPlease specify the migration: &7/locklogin migrate <MySQL, AuthMe>");
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                user.Message(messages.Prefix() + "&cNot allowed in BungeeCord mode!");
                            }
                        }
                    } else {
                        if (args[0].equals("applyUpdates")) {
                            if (player.hasPermission(applyUpdatePermission)) {
                                new LockLoginSpigotManager().applyUpdate();
                                user.Message(messages.Prefix() + "&aLockLogin have been reloaded and its updates have been applied");
                            } else {
                                user.Message(messages.Prefix() + messages.PermissionError(applyUpdatePermission.getName()));
                            }
                        }
                    }
                }
            }
        } else {
            if (args.length == 0) {
                out.Message(messages.Prefix() + "&cSpecify an action &7( &e/locklogin migrate &7|| &e/locklogin applyUpdates &7)");
            } else {
                if (args[0].equals("migrate")) {
                    if (!config.isBungeeCord()) {
                        if (args.length == 1) {
                            out.Message(messages.Prefix() + "&cPlease specify the migration: &7/locklogin migrate <MySQL, AuthMe>");
                        } else {
                            if (args.length == 2) {
                                String method = args[1];
                                switch (method) {
                                    case "MySQL":
                                        migrateMySQL();
                                        break;
                                    case "AuthMe":
                                        out.Message(messages.Prefix() + "&cPlease, specify database name, table name, realname and password column name (must exist in plugins/AuthMe folder)");
                                        break;
                                    default:
                                        out.Message(messages.Prefix() + "&cPlease specify the migration: &7/locklogin migrate <MySQL, AuthMe>");
                                        break;
                                }
                            } else {
                                if (args.length == 3) {
                                    String method = args[1];
                                    switch (method) {
                                        case "MySQL":
                                            out.Message(messages.Prefix() + "&cToo many args, please, use /locklogin migrate MySQL");
                                            break;
                                        case "AuthMe":
                                            out.Message(messages.Prefix() + "&cPlease, specify table name");
                                            break;
                                        default:
                                            out.Message(messages.Prefix() + "&cPlease specify the migration: &7/locklogin migrate <MySQL, AuthMe>");
                                            break;
                                    }
                                } else {
                                    if (args.length == 4) {
                                        String method = args[1];
                                        switch (method) {
                                            case "MySQL":
                                                out.Message(messages.Prefix() + "&cToo many args, please, use /locklogin migrate MySQL");
                                                break;
                                            case "AuthMe":
                                                out.Message(messages.Prefix() + "&cPlease specify the 'realname' column");
                                                break;
                                            default:
                                                out.Message(messages.Prefix() + "&cPlease specify the migration: &7/locklogin migrate <MySQL, AuthMe>");
                                                break;
                                        }
                                    } else {
                                        if (args.length == 5) {
                                            String method = args[1];
                                            switch (method) {
                                                case "MySQL":
                                                    out.Message(messages.Prefix() + "&cToo many args, please, use /locklogin migrate MySQL");
                                                    break;
                                                case "AuthMe":
                                                    out.Message(messages.Prefix() + "&cPlease specify the 'password' column");
                                                    break;
                                                default:
                                                    out.Message(messages.Prefix() + "&cPlease specify the migration: &7/locklogin migrate <MySQL, AuthMe>");
                                                    break;
                                            }
                                        } else {
                                            if (args.length == 6) {
                                                String method = args[1];
                                                switch (method) {
                                                    case "MySQL":
                                                        out.Message(messages.Prefix() + "&cToo many args, please, use /locklogin migrate MySQL");
                                                        break;
                                                    case "AuthMe":
                                                        out.Message(messages.Prefix() + "&aMigrating from AuthMe sqlite");
                                                        if (migrateAuthMe(args[2], args[3], args[4], args[5])) {
                                                            out.Message(messages.Prefix() + "&aMigration successfully");
                                                        } else {
                                                            out.Message(messages.Prefix() + "&cSome error occurred while migrating");
                                                        }
                                                        break;
                                                    default:
                                                        out.Message(messages.Prefix() + "&cPlease specify the migration: &7/locklogin migrate <MySQL, AuthMe>");
                                                        break;
                                                }
                                            } else {
                                                out.Message(messages.Prefix() + "&cPlease specify the migration: &7/locklogin migrate <MySQL, AuthMe>");
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        out.Message(messages.Prefix() + "&cNot allowed in BungeeCord mode!");
                    }
                } else {
                    if (args[0].equals("applyUpdates")) {
                        new LockLoginSpigotManager().applyUpdate();
                    }
                }
            }
        }
        return false;
    }

    /**
     * Do a mysql migration
     *
     * @param player the executor
     */
    private void migrateMySQL(Player player) {
        User user = new User(player);

        if (config.isMySQL()) {
            Utils sql = new Utils();

            user.Message(messages.Prefix() + messages.MigratingAll());

            for (String id : sql.getUUIDs()) {
                Utils sqlUUID = new Utils(id);

                new AccountMigrate(sqlUUID, Migrate.YAML, Platform.SPIGOT);

                user.Message(messages.Prefix() + messages.MigratingYaml(id));
            }
            user.Message(messages.Prefix() + messages.Migrated());
        } else {
            user.Message(messages.Prefix() + "&bTrying to establish a connection with MySQL");
            MySQLData SQLData = new MySQLData();

            Bucket bucket = new Bucket(
                    SQLData.getHost(),
                    SQLData.getDatabase(),
                    SQLData.getTable(),
                    SQLData.getUser(),
                    SQLData.getPassword(),
                    SQLData.getPort(),
                    SQLData.useSSL());

            bucket.setOptions(SQLData.getMaxConnections(), SQLData.getMinConnections(), SQLData.getTimeOut(), SQLData.getLifeTime());

            Connection connection = null;
            try {
                connection = Bucket.getBucket().getConnection();
            } catch (Throwable ignore) {}

            if (connection != null) {
                user.Message(messages.Prefix() + messages.MigratingAll());
                bucket.prepareTables();

                Utils sql = new Utils();

                for (String id : sql.getUUIDs()) {
                    Utils sqlUUID = new Utils(id);

                    new AccountMigrate(sqlUUID, Migrate.YAML, Platform.SPIGOT);

                    user.Message(messages.Prefix() + messages.MigratingYaml(id));
                }
                user.Message(messages.Prefix() + messages.Migrated());
            } else {
                user.Message(messages.Prefix() + messages.MigrationConnectionError());
            }
        }
    }

    /**
     * Do a mysql migration
     */
    private void migrateMySQL() {
        if (config.isMySQL()) {
            Utils sql = new Utils();

            out.Message(messages.Prefix() + messages.MigratingAll());

            sql.checkTables();

            for (String id : sql.getUUIDs()) {
                Utils sqlUUID = new Utils(id);

                new AccountMigrate(sqlUUID, Migrate.YAML, Platform.SPIGOT);

                out.Message(messages.Prefix() + messages.MigratingYaml(id));
            }
            out.Message(messages.Prefix() + messages.Migrated());
        } else {
            out.Message(messages.Prefix() + "&bTrying to establish a connection with MySQL");
            MySQLData SQLData = new MySQLData();

            Bucket bucket = new Bucket(
                    SQLData.getHost(),
                    SQLData.getDatabase(),
                    SQLData.getTable(),
                    SQLData.getUser(),
                    SQLData.getPassword(),
                    SQLData.getPort(),
                    SQLData.useSSL());

            bucket.setOptions(SQLData.getMaxConnections(), SQLData.getMinConnections(), SQLData.getTimeOut(), SQLData.getLifeTime());

            Connection connection = null;
            try {
                connection = Bucket.getBucket().getConnection();
            } catch (Exception | Error ignore) {}

            if (connection != null) {
                out.Message(messages.Prefix() + messages.MigratingAll());
                bucket.prepareTables();

                Utils sql = new Utils();

                sql.checkTables();

                for (String id : sql.getUUIDs()) {
                    Utils sqlUUID = new Utils(id);

                    new AccountMigrate(sqlUUID, Migrate.YAML, Platform.SPIGOT);

                    out.Message(messages.Prefix() + messages.MigratingYaml(id));
                }
                out.Message(messages.Prefix() + messages.Migrated());
            } else {
                out.Message(messages.Prefix() + messages.MigrationConnectionError());
            }
        }
    }

    /**
     * Do an AuthMe sqlite migration
     *
     * @param database the database name
     * @param table the database table where the info is
     */
    private boolean migrateAuthMe(String database, String table, String realnameColumn, String passwordColumn) {
        File authMe = new File(plugin.getServer().getWorldContainer() + "/plugins/AuthMe/");
        if (authMe.exists()) {
            File data = new File(authMe, database + ".db");

            if (data.exists()) {
                SQLiteReader reader = new SQLiteReader(data, table, realnameColumn, passwordColumn);
                if (config.isMySQL()) {
                    if (reader.tryConnection()) {
                        for (String name : reader.getPlayers()) {
                            String password = reader.getPassword(name);

                            if (password != null && !password.isEmpty()) {
                                try {
                                    InsertInfo insert = new InsertInfo(name);
                                    insert.setPassword(password);
                                    insert.setFly(false);
                                    insert.setGAuthStatus(false);
                                    insert.setGauthToken("");
                                    insert.setPin("");

                                    BucketInserter inserter = new BucketInserter(insert);
                                    inserter.insert();
                                } catch (Throwable e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        return true;
                    }
                } else {
                    for (String name : reader.getPlayers()) {
                        String password = reader.getPassword(name);

                        if (password != null && !password.isEmpty()) {
                            try {
                                InsertInfo insert = new InsertInfo(name);
                                insert.setPassword(password);
                                insert.setFly(false);
                                insert.setGAuthStatus(false);
                                insert.setGauthToken("");
                                insert.setPin("");

                                FileInserter inserter = new FileInserter(insert);
                                inserter.insert();
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    return true;
                }
            }
        }

        return false;
    }
}

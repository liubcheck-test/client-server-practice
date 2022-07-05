package stepanenko.practice4.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBUtil {
    private static final String URL = "jdbc:sqlite:store.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Can't find the JDBC driver", e);
        }
    }

    public static void initDatabase() {
        try (Connection connection =
                     DriverManager.getConnection(URL);
             PreparedStatement initGroupsStatement =
                     connection.prepareStatement("CREATE TABLE IF NOT EXISTS 'groups'"
                             + " (`id` INTEGER PRIMARY KEY AUTOINCREMENT, "
                             + "`name` VARCHAR(225) NOT NULL, "
                             + "`is_deleted` INTEGER NOT NULL DEFAULT 0);");
             PreparedStatement initProductsStatement =
                     connection.prepareStatement("CREATE TABLE IF NOT EXISTS 'products'"
                             + " (`id` INTEGER PRIMARY KEY AUTOINCREMENT, "
                             + "`name` VARCHAR(225) NOT NULL, "
                             + "`amount` INTEGER NOT NULL, "
                             + "`price` INTEGER NOT NULL, "
                             + "`group_id` INTEGER NOT NULL, "
                             + "`is_deleted` INTEGER NOT NULL DEFAULT 0);")) {
            initGroupsStatement.executeUpdate();
            initProductsStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Can't initialize the database", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}

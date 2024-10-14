package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String DATABASE_URL = "jdbc:sqlite:readingroom.db";
    private Connection connection;

    public DatabaseConnection() {
        try {
            connection = DriverManager.getConnection(DATABASE_URL);
            System.out.println("Connection to database established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connection closed.");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}

package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Database connection class that encapsulates the logic for establishing and managing a connection to a SQLite database.
// (developer-xdeveloper-x, Separate JDBC connection and Resultset Code 1961)
public class DatabaseConnection {
	
	// constant String variable DATABASE_URL to declare the database file location 
    private static final String DATABASE_URL = "jdbc:sqlite:readingroom.db";
    private Connection connection;

    // database connection constructor method
    public DatabaseConnection() {
        try {
        	// establishing a database
            connection = DriverManager.getConnection(DATABASE_URL);
            System.out.println("The connection to the reading room database is successfully established");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // getter method to provide the database connection
    public Connection getConnection() {
        return connection;
    }

    // method to close the database connection
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connection of the database reading room is successfully closed.");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}

// References 

// developer-xdeveloper-x  8311 gold badge22 silver badges99 bronze badges and CodeJockNYCCodeJockNYC 7455 bronze badges (1961) Separate JDBC connection and Resultset Code, Stack Overflow. Available at: https://stackoverflow.com/questions/38316942/separate-jdbc-connection-and-resultset-code (Accessed: 10 October 2024). 
package application;

// importing all the necessary required libraries
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import Model.BookRegistration;

public class CreateDatabase {
	
	// creating a constant variable for the file location of the reading room database
    private static final String DATABASE_URL = "jdbc:sqlite:readingroom.db";

    // this method is responsible for creating a database, creating tables and adding the books to the database
    public static void main(String[] args) {
        setupDatabase();
        BookRegistration bookRegistration = new BookRegistration();
        bookRegistration.addBooks();
    }

    // method to set up the database
    public static void setupDatabase() {
        createDatabase();
        createTables();
    }

    // method to create a database
    private static void createDatabase() {
        try (Connection conn = DriverManager.getConnection(DATABASE_URL)) {
            if (conn != null) {
                System.out.println("A new database has been set up for the reading room application.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // method to create the users, books, orders, order_details and cart_items tables in the reading room database
    private static void createTables() {
    	
    	String deletingUsersTable = "DROP TABLE IF EXISTS users;";
    	String deletingBooksTable = "DROP TABLE IF EXISTS books;";
    	String deletingOrdersTable = "DROP TABLE IF EXISTS orders;";
    	String deletingOrderDetailsTable = "DROP TABLE IF EXISTS order_details;";
    	String deletingCartItemsTable = "DROP TABLE IF EXISTS cart_items;";
    	
    	// SQL statements to create users table
        String creatingUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT NOT NULL UNIQUE," +
                "password TEXT NOT NULL," +
                "first_name TEXT NOT NULL," +
                "last_name TEXT NOT NULL" +
                ");";
        
        // SQL statements to create books table
        String creatingBooksTable = "CREATE TABLE IF NOT EXISTS books (" +
                "book_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT NOT NULL UNIQUE," +
                "author TEXT NOT NULL," +
                "price INTEGER NOT NULL," +
                "quantity INTEGER NOT NULL," +
                "sold INTEGER NOT NULL" +
                ");";

        // SQL statements to create orders table
        String creatingOrdersTable = "CREATE TABLE IF NOT EXISTS orders (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER," +
                "order_date TEXT NOT NULL," +
                "total_price REAL NOT NULL," +
                "FOREIGN KEY (user_id) REFERENCES users (id)" +
                ");";

        // SQL statements to create order_details table
        String creatingOrderDetailsTable = "CREATE TABLE IF NOT EXISTS order_details (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "order_id INTEGER," +
                "book_id INTEGER," +
                "quantity INTEGER NOT NULL," +
                "price REAL NOT NULL," +
                "FOREIGN KEY (order_id) REFERENCES orders (id)," +
                "FOREIGN KEY (book_id) REFERENCES books (book_id)" +
                ");";
        
        // SQL statements to create cart items table
        String creatingCartItemsTable = "CREATE TABLE IF NOT EXISTS cart_items (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER NOT NULL," +
                "book_id INTEGER NOT NULL," +
                "quantity INTEGER NOT NULL," +
                "price REAL NOT NULL," +
                "FOREIGN KEY (user_id) REFERENCES users (id)," +
                "FOREIGN KEY (book_id) REFERENCES books (book_id)" +
                ");";

        // establishing a database connection
        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             Statement stmt = conn.createStatement()) {

        	// executing the statements
        	stmt.execute(deletingUsersTable);
        	stmt.execute(deletingBooksTable);
        	stmt.execute(deletingOrdersTable);
        	stmt.execute(deletingOrderDetailsTable);
        	stmt.execute(deletingCartItemsTable);
            stmt.execute(creatingUsersTable);
            stmt.execute(creatingBooksTable); 
            stmt.execute(creatingOrdersTable);
            stmt.execute(creatingOrderDetailsTable);
            stmt.execute(creatingCartItemsTable);

            System.out.println("Created tables in the database, Success!!.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

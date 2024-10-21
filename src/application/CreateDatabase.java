package application;

// importing all the necessary required libraries
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import Model.BookRegistration;

public class CreateDatabase {
	
	// creating a constant variable for the url of the reading room database
    private static final String DATABASE_URL = "jdbc:sqlite:readingroom.db";

    // this method is responsible for creating a database, creating tables and registering the books
    public static void main(String[] args) {
        setupDatabase();
        
        // creating an object of BookRegistration class to register all the specified books in the database
        BookRegistration bookRegistration = new BookRegistration();
        bookRegistration.registerBooks();
    }

    public static void setupDatabase() {
        createDatabase();
        createTables();
    }

    // method to create a database
    private static void createDatabase() {
        try (Connection conn = DriverManager.getConnection(DATABASE_URL)) {
            if (conn != null) {
                System.out.println("A new database has been created.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // method to create the users, books, orders and order_details tables in the reading room database
    private static void createTables() {
    	
    	String dropUsersTable = "DROP TABLE IF EXISTS users;";
    	String dropBooksTable = "DROP TABLE IF EXISTS books;";
    	String dropOrdersTable = "DROP TABLE IF EXISTS orders;";
    	String dropOrderDetailsTable = "DROP TABLE IF EXISTS order_details;";
    	
    	// SQL statements to create users table
        String usersTable = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT NOT NULL UNIQUE," +
                "password TEXT NOT NULL," +
                "first_name TEXT NOT NULL," +
                "last_name TEXT NOT NULL" +
                ");";
        
        // SQL statements to create books table
        String booksTable = "CREATE TABLE IF NOT EXISTS books (" +
                "book_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT NOT NULL UNIQUE," +
                "author TEXT NOT NULL," +
                "price INTEGER NOT NULL," +
                "quantity INTEGER NOT NULL," +
                "sold INTEGER NOT NULL" +
                ");";

        // SQL statements to create orders table
        String ordersTable = "CREATE TABLE IF NOT EXISTS orders (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER," +
                "order_date TEXT NOT NULL," +
                "total_price REAL NOT NULL," +
                "FOREIGN KEY (user_id) REFERENCES users (id)" +
                ");";

        // SQL statements to create order_details table
        String orderDetailsTable = "CREATE TABLE IF NOT EXISTS order_details (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "order_id INTEGER," +
                "book_id INTEGER," +
                "quantity INTEGER NOT NULL," +
                "price REAL NOT NULL," +
                "FOREIGN KEY (order_id) REFERENCES orders (id)," +
                "FOREIGN KEY (book_id) REFERENCES books (book_id)" +
                ");";

        // establishing a database connection
        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             Statement stmt = conn.createStatement()) {

        	// executing the statements
        	stmt.execute(dropUsersTable);
        	stmt.execute(dropBooksTable);
        	stmt.execute(dropOrdersTable);
        	stmt.execute(dropOrderDetailsTable);
            stmt.execute(usersTable);
            stmt.execute(booksTable); 
            stmt.execute(ordersTable);
            stmt.execute(orderDetailsTable);

            System.out.println("Created tables in the database, Success!!.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

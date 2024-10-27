package Model;

// importing all the necessary required libraries
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// book registration class to register the books in the reading room system database
public class BookRegistration {
	
	// constant String variable DATABASE_URL to declare the database file location 
    private static final String DATABASE_URL = "jdbc:sqlite:readingroom.db";

    // method to register the books in the database
    public void addBooks() {
    	
    	// defining the SQL statement to add the books in the database
        String addBookSQLStatement = "INSERT INTO books (title, author, price, quantity, sold) VALUES (?, ?, ?, ?, ?)";
        
        // establishing a database connection to insert data in the database
        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(addBookSQLStatement)) {

            // Using a 2d String array to define the books
            String[][] bookList = {
                {"Absolute Java", "Savitch", "50", "10", "142"},
                {"JAVA: Ho to Program", "Deitel and Deitel", "70", "100", "475"},
                {"Computing Concepts with JAVA 8 Essentials", "Horstman", "89", "500", "60"},
                {"Java Software Solutions", "Lewis and Loftus", "99", "500", "12"},
                {"Java Program Design", "Cohoon and Davidson", "29", "2", "86"},
                {"Clean Code", "Robert Martin", "45", "100", "300"},
                {"Gray Hat C#", "Brandon Perry", "68", "300", "178"},
                {"Python Basics", "David Amos", "49", "1000", "79"},
                {"Bayesian Statistics The Fun Way", "Will Kurt", "42", "600", "155"}
            };

            // using a for loop to iterate over each book and execute the book add statements
            for (String[] book : bookList) {
                pstmt.setString(1, book[0]); 
                pstmt.setString(2, book[1]); 
                pstmt.setInt(3, Integer.parseInt(book[2])); 
                pstmt.setInt(4, Integer.parseInt(book[3])); 
                pstmt.setInt(5, Integer.parseInt(book[4])); 
                pstmt.executeUpdate();
            }
            System.out.println("Books are added successfully to the database.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

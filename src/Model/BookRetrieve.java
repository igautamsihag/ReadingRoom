package Model;

// importing all the required necessary libraries for the Book Retrieve class
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookRetrieve {
	
	// constant String variable DATABASE_URL to declare the database file location 
    private static final String DATABASE_URL = "jdbc:sqlite:readingroom.db";

    // method to access the books from the database
    public List<Book> getBooks() {
    	
    	// storing the books in a arrayList
        List<Book> books = new ArrayList<>();
        
        // defining the SQL statement to get the books from the database
        String bookAccessSQLStatement = "SELECT title, author, price, quantity, sold FROM books";

        // establishing a database connection to insert data in the database
        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
             Statement smt = connection.createStatement();
             ResultSet rs = smt.executeQuery(bookAccessSQLStatement)) {

        	// using a while loop to access each feature of all book records and adding it to an arrayList
            while (rs.next()) {
                String title = rs.getString("title");
                String author = rs.getString("author");
                int price = rs.getInt("price");
                int quantity = rs.getInt("quantity");
                int sold = rs.getInt("sold");

                // Book object is created and added to the books arrayList.
                books.add(new Book(title, author, price, quantity, sold));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return books;
    }
    
    // defining a method to update the quantity of a particular book
    public boolean updateBookQuantity(Book book) {
    	
    	// defining the SQL statement to update the book quantity in the database
        String updateBookSQLStatement = "UPDATE books SET quantity = ? WHERE title = ?";
        
        // establishing a database connection to insert data in the database
        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = connection.prepareStatement(updateBookSQLStatement)) {
            
        	// executing the book update statements
            pstmt.setInt(1, book.getQuantity());
            pstmt.setString(2, book.getTitle());     
            int updatedBookRows = pstmt.executeUpdate();
            return updatedBookRows > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}

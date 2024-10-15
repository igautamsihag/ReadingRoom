package Model;

// importing all the required necessary libraries for the Book Retrieve class
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookRetrieve {
    private static final String DATABASE_URL = "jdbc:sqlite:readingroom.db";

    // method to retrieve all the books
    public List<Book> getBooks() {
    	
    	// storing the books in a arrayList
        List<Book> books = new ArrayList<>();
        String bookQuery = "SELECT title, author, price, quantity, sold FROM books";

        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
             Statement smt = connection.createStatement();
             ResultSet rs = smt.executeQuery(bookQuery)) {

        	// retrieving each info of a particular book and adding it to an arrayList
            while (rs.next()) {
                String title = rs.getString("title");
                String author = rs.getString("author");
                int price = rs.getInt("price");
                int quantity = rs.getInt("quantity");
                int sold = rs.getInt("sold");

                books.add(new Book(title, author, price, quantity, sold));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return books;
    }
    
    public boolean updateBookQuantity(Book book) {
        String updateQuery = "UPDATE books SET quantity = ? WHERE title = ?";
        
        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = connection.prepareStatement(updateQuery)) {
            
            pstmt.setInt(1, book.getQuantity());
            pstmt.setString(2, book.getTitle());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}

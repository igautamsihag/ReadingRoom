package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BookRegistration {
    private static final String DATABASE_URL = "jdbc:sqlite:readingroom.db";

    public void registerBooks() {
        String insertBookSQL = "INSERT INTO books (title, author, price, quantity, sold) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(insertBookSQL)) {

            // Example book data
            String[][] books = {
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

            for (String[] book : books) {
                pstmt.setString(1, book[0]); // Title
                pstmt.setString(2, book[1]); // Author
                pstmt.setInt(3, Integer.parseInt(book[2])); // Price
                pstmt.setInt(4, Integer.parseInt(book[3])); // Quantity
                pstmt.setInt(5, Integer.parseInt(book[4])); // Sold
                pstmt.executeUpdate();
            }
            System.out.println("Books inserted successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

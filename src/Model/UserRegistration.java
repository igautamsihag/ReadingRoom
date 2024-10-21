package Model;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserRegistration {
    private DatabaseConnection dbConnection;

    public UserRegistration(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public boolean registerUser(User user) {
        String sql = "INSERT INTO users (username, password, first_name, last_name) VALUES (?, ?, ?, ?)";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getFirstName());
            pstmt.setString(4, user.getLastName());
            pstmt.executeUpdate();
            return true; // Registration successful
        } catch (SQLException e) {
            System.out.println("Registration failed: " + e.getMessage());
            return false; // Registration failed
        }
    }
    
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET first_name = ?, last_name = ?, password = ? WHERE username = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getFirstName());
            pstmt.setString(2, user.getLastName());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getUsername());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
            return false;
        }
    }

}

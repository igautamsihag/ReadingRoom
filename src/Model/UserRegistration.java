package Model;

//importing all the necessary required libraries
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// user registration class to register the user in the reading room system database
public class UserRegistration {
    private DatabaseConnection databaseConnection;

    // user registration constructor method
    public UserRegistration(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    // method to add the user in the database
    public boolean addUser(User user) {
    	
    	// defining the SQL statement to add the user in the database
        String addUserSQLStatement = "INSERT INTO users (username, password, first_name, last_name) VALUES (?, ?, ?, ?)";

        // establishing a database connection to add the user in the database
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(addUserSQLStatement)) {
        	
        	// executing the statements 
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getFirstName());
            pstmt.setString(4, user.getLastName());
            pstmt.executeUpdate();
            return true; 
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    // method to update the user info in the database
    public boolean updateUser(User user) {
    	
    	// defining the SQL statement to update the user info in the database
        String updateUserSQLStatement = "UPDATE users SET first_name = ?, last_name = ?, password = ? WHERE username = ?";
        
        // establishing a database connection to update the user info in the database
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateUserSQLStatement)) {
        	
        	// executing the statements
            pstmt.setString(1, user.getFirstName());
            pstmt.setString(2, user.getLastName());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getUsername());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

}

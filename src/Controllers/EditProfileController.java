package Controllers;

//importing all the necessary required libraries for this dash board controller
import java.sql.Connection;
import java.sql.PreparedStatement;

import Model.CurrentSession;
import Model.DatabaseConnection;
import Model.User;
import Model.UserRegistration;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class EditProfileController {

	// FXML statement to link the UI components
	// button for log out
    @FXML
    private Button btnlogout;
    
    // button for dash board
    @FXML
    private Button btndashboard;
    
    // text field for first name
    @FXML
    private TextField firstnameinput;

    // text field for last name
    @FXML
    private TextField lastnameinput;

    // text field for password
    @FXML
    private TextField passwordinput;
    
    // button to update first name
    @FXML
    private Button updateFirstname;
    
    // button to update last name
    @FXML
    private Button updateLastname;
    
    // button to update password
    @FXML
    private Button updatePassword;
    

    // method to access the current user details from the current session and filling the text fields with existing details
 
    @FXML
    public void initialize() {
        User currentUser = CurrentSession.getInstance().getCurrentUser();
        if (currentUser != null) {
            firstnameinput.setText(currentUser.getFirstName());
            lastnameinput.setText(currentUser.getLastName());
            passwordinput.setText(currentUser.getPassword());
        } else {
            System.err.println("Error. Try again");
        }
        firstnameinput.clear();
        lastnameinput.clear();
        passwordinput.clear();
    }

    // method to update the first name of the user
    @FXML
    public void updateFirstname() {
        User currentUser = CurrentSession.getInstance().getCurrentUser();
        if (currentUser != null) {
        	// getting the input from the text field and storing it in the variable and updating it in the database and current user session 
            String newFirstName = firstnameinput.getText();
            updateUserTable(currentUser.getUsername(), newFirstName, null, null);
            currentUser.setFirstName(newFirstName); // Update in session
        } else {
            System.err.println("Error. Try updating first name again.");
        }
    }

    // method to update the last name of the user
    @FXML
    public void updateLastname() {
        User currentUser = CurrentSession.getInstance().getCurrentUser();
        if (currentUser != null) {
        	// getting the input from the text field and storing it in the variable and updating it in the database and current user session 
            String newLastName = lastnameinput.getText();
            updateUserTable(currentUser.getUsername(), null, newLastName, null);
            currentUser.setLastName(newLastName); // Update in session
        } else {
            System.err.println("Error. Try updating last name again.");
        }
    }

    // method to update the password of the user
    @FXML
    public void updatePassword() {
    	User currentUser = CurrentSession.getInstance().getCurrentUser();
        if (currentUser != null) {
        	// getting the input from the text field and storing it in the variable and updating it in the database and current user session 
            String newPassword = passwordinput.getText();
            updateUserTable(currentUser.getUsername(), null, null, newPassword);
            currentUser.setPassword(newPassword); 
        } else {
            System.err.println("Error. Try updating password again.");
        }
    }
    
    // method to the update the user info in the database
    private void updateUserTable(String username, String firstName, String lastName, String password) {
    	
    	// String variable DATABASE_URL to declare the database file location
        String DATABASE_URL = "jdbc:sqlite:readingroom.db"; 
        
        // defining the SQL statement to update the user details in the database 
        String updatedetailSQLStatement = "UPDATE users SET first_name = COALESCE(?, first_name), last_name = COALESCE(?, last_name), password = COALESCE(?, password) WHERE username = ?";

        // establishing the database connection and executing the query
        try (Connection conn = new DatabaseConnection().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updatedetailSQLStatement)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, password);
            pstmt.setString(4, username);
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
    //  method to navigate the user to the log in page when logout button is clicked
    public void goToLogOut() {
        try {
            Parent loginPage = FXMLLoader.load(getClass().getResource("/Views/Login.fxml"));
            Stage stage = (Stage) btnlogout.getScene().getWindow();
            stage.setScene(new Scene(loginPage));
            stage.show();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    //  method to navigate the user to the dash board page when dash board button is clicked
    public void goToDashboard() {
        try {
            Parent dashboardPage = FXMLLoader.load(getClass().getResource("/Views/DashboardView.fxml"));
            Stage stage = (Stage) btndashboard.getScene().getWindow();
            stage.setScene(new Scene(dashboardPage));
            stage.show();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
package Controllers;

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

    @FXML
    private Button btnlogout;
    
    @FXML
    private Button btndashboard;
    
    @FXML
    private TextField firstnameinput;

    @FXML
    private TextField lastnameinput;

    @FXML
    private TextField passwordinput;
    
    @FXML
    private Button updateFirstname;
    
    @FXML
    private Button updateLastname;
    
    @FXML
    private Button updatePassword;
    

 
    @FXML
    public void initialize() {
        DatabaseConnection dbConnection = new DatabaseConnection();
        User currentUser = CurrentSession.getInstance().getCurrentUser();
        if (currentUser != null) {
            firstnameinput.setText(currentUser.getFirstName());
            lastnameinput.setText(currentUser.getLastName());
            passwordinput.setText(currentUser.getPassword());
        } else {
            System.err.println("No user is currently logged in.");
        }
        firstnameinput.clear();
        lastnameinput.clear();
        passwordinput.clear();
    }

    
    @FXML
    public void updateFirstname() {
        User currentUser = CurrentSession.getInstance().getCurrentUser();
        if (currentUser != null) {
            String newFirstName = firstnameinput.getText();
            updateUserInDatabase(currentUser.getUsername(), newFirstName, null, null);
            currentUser.setFirstName(newFirstName); // Update in session
        } else {
            System.err.println("No user is currently logged in. Cannot update first name.");
        }
    }

    @FXML
    public void updateLastname() {
        User currentUser = CurrentSession.getInstance().getCurrentUser();
        if (currentUser != null) {
            String newLastName = lastnameinput.getText();
            updateUserInDatabase(currentUser.getUsername(), null, newLastName, null);
            currentUser.setLastName(newLastName); // Update in session
        } else {
            System.err.println("No user is currently logged in. Cannot update last name.");
        }
    }

    @FXML
    public void updatePassword() {
    	User currentUser = CurrentSession.getInstance().getCurrentUser();
        if (currentUser != null) {
            String newPassword = passwordinput.getText();
            updateUserInDatabase(currentUser.getUsername(), null, null, newPassword);
            currentUser.setPassword(newPassword); // Update in session
        } else {
            System.err.println("No user is currently logged in. Cannot update password.");
        }
    }
    
    private void updateUserInDatabase(String username, String firstName, String lastName, String password) {
        String url = "jdbc:sqlite:readingroom.db"; // Update if needed
        String sql = "UPDATE users SET first_name = COALESCE(?, first_name), last_name = COALESCE(?, last_name), password = COALESCE(?, password) WHERE username = ?";

        try (Connection conn = new DatabaseConnection().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, password);
            pstmt.setString(4, username);
            pstmt.executeUpdate();
            System.out.println("User profile updated successfully!");
        } catch (Exception e) {
            System.err.println("Failed to update user profile: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void goToLogOut() {
        System.out.println("Logout button clicked!");
        try {
            Parent loginPage = FXMLLoader.load(getClass().getResource("/Views/Login.fxml"));
            Stage stage = (Stage) btnlogout.getScene().getWindow();
            stage.setScene(new Scene(loginPage));
            stage.show();
        } catch (Exception e) {
            System.err.println("Failed to load Login page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void goToDashboard() {
        System.out.println("Export button clicked!");
        try {
            Parent exportPage = FXMLLoader.load(getClass().getResource("/Views/DashboardView.fxml"));
            Stage stage = (Stage) btndashboard.getScene().getWindow();
            stage.setScene(new Scene(exportPage));
            stage.show();
        } catch (Exception e) {
            System.err.println("Failed to load export page: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
package Controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import Model.CurrentSession;
import Model.User;
//import Model.CurrentSession;
//import Model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class LoginController {

	@FXML
    private Button btnsignup;
	
	@FXML
	private Button btnlogin;
	
	@FXML
    private TextField usernamelogin;

    @FXML
    private PasswordField passwordlogin;
    
    @FXML
    public void initialize() {
        btnsignup.setOnAction(event -> goToSignUp());
        btnlogin.setOnAction(event -> loginManager());
    }
    
    @FXML
    public void loginManager() {
        String username = usernamelogin.getText();
        String password = passwordlogin.getText();
        User currentUser = userAuthentication(username, password);

        if(adminAuthentication(username, password)) {
        	try {
                Parent nextPage = FXMLLoader.load(getClass().getResource("/Views/AdminDashboardView.fxml"));
                Stage stage = (Stage) btnlogin.getScene().getWindow();
                stage.setScene(new Scene(nextPage));
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        if (currentUser != null) {
            // Set the current user in UserSession
            CurrentSession.getInstance().setCurrentUser(currentUser);

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/DashboardView.fxml"));
                Parent nextPage = loader.load();
                DashboardController controller = loader.getController();
                controller.setUsername(currentUser.getUsername());// Set username if needed
                Stage stage = (Stage) btnlogin.getScene().getWindow();
                stage.setScene(new Scene(nextPage));
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Authentication failed.");
            // Optionally show an error message to the user
        }
    }
      
    
	public void goToSignUp() {
        try {
            Parent signupPage = FXMLLoader.load(getClass().getResource("/Views/Signup.fxml"));
            Stage stage = (Stage) btnsignup.getScene().getWindow();
            stage.setScene(new Scene(signupPage));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	private boolean adminAuthentication(String username, String password) {
		return username.equals("admin") && password.equals("reading_admin");
	}

	private User userAuthentication(String username, String password) {
        String url = "jdbc:sqlite:readingroom.db"; // Update if needed
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Retrieve user details from the ResultSet
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                int id = rs.getInt("id");
                User user = new User(username, password, firstName, lastName);
                user.setID(id); // Set the ID after creating the user
                return user;
             // Create User object
            } // Returns true if a record was found
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Return false if there's an error
    }
}


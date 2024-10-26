package Controllers;

import java.io.IOException;

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

public class SignupController {

	@FXML
    private Button btnlogin;
	
	@FXML
    private TextField usernamesignup;
    
    @FXML
    private TextField passwordsignup;
    
    @FXML
    private TextField firstNamesignup;
    
    @FXML
    private TextField lastNamesignup;
    
    @FXML
    private Button btnsignup;
    
	@FXML
    public void initialize() {
        btnlogin.setOnAction(event -> goToLogIn());
        btnsignup.setOnAction(event -> handleRegister());
    }
	
	@FXML
	public void goToLogIn() {
        try {
            Parent signupPage = FXMLLoader.load(getClass().getResource("/Views/Login.fxml"));
            Stage stage = (Stage) btnlogin.getScene().getWindow();
            stage.setScene(new Scene(signupPage));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	 @FXML
	 public void handleRegister() {
	        String username = usernamesignup.getText();
	        String password = passwordsignup.getText();
	        String firstName = firstNamesignup.getText();
	        String lastName = lastNamesignup.getText();

	        if (containsNumbers(firstName)) {
	            System.out.println("First name must not contain numeric characters.");
	            return; // Exit if validation fails
	        }
	        
	        if (containsNumbers(lastName)) {
	            System.out.println("Last name must not contain numeric characters.");
	            return; // Exit if validation fails
	        }
	        // Create a new User object
	        User newUser = new User(username, password, firstName, lastName);

	        // Register the user
	        DatabaseConnection dbConnection = new DatabaseConnection();
	        UserRegistration userRegistration = new UserRegistration(dbConnection);
	       // userRegistration.registerUser(newUser);
	        
	        // Optionally, clear the fields after registration
	        if (userRegistration.registerUser(newUser)) {
	            loadDashboard();
	        } else {
	            System.out.println("Registration failed.");
	        }

	        dbConnection.closeConnection();
	        
	    }
	 public boolean containsNumbers(String str) {
		    return str.matches(".*\\d.*"); // Checks if the string contains any digit
		}
	 
	 private void loadDashboard() {
	        try {
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/DashboardView.fxml"));
	            Parent dashboardRoot = loader.load();
	            Scene dashboardScene = new Scene(dashboardRoot);

	            Stage currentStage = (Stage) btnsignup.getScene().getWindow();
	            currentStage.setScene(dashboardScene);
	            currentStage.setTitle("Dashboard");
	            currentStage.show();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    

	    public void clearFields() {
	        usernamesignup.clear();
	        passwordsignup.clear();
	        firstNamesignup.clear();
	        lastNamesignup.clear();
	    }
	}
	

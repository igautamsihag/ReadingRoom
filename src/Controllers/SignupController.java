package Controllers;

// importing all the necessary required libraries for sign up controller
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

	// FXML statement to link the UI components
	// button for log in
	@FXML
    private Button btnlogin;
	
	// text field for user name input
	@FXML
    private TextField usernamesignup;
    
	// text field for password input
    @FXML
    private TextField passwordsignup;
    
    // text field for first name input
    @FXML
    private TextField firstNamesignup;
    
    // text field for last name input
    @FXML
    private TextField lastNamesignup;
    
    // button for signing up
    @FXML
    private Button btnsignup;
    
    // initialize method to set up the event handlers for the login and sign up button
	@FXML
    public void initialize() {
        btnlogin.setOnAction(event -> goToLogIn());
        btnsignup.setOnAction(event -> handleRegister());
    }
	
	// method to navigate the user to the log in page when login button is clicked
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
	
	// method to handle the registration process
	 @FXML
	 public void handleRegister() {
		 
		 	// retrieving the user input from the text fields to the string variables
	        String username = usernamesignup.getText();
	        String password = passwordsignup.getText();
	        String firstName = firstNamesignup.getText();
	        String lastName = lastNamesignup.getText();

	        // validating the first name and last name
	        if (containsNumbers(firstName)) {
	            System.out.println("First name is invalid. Please do not enter numbers.");
	            return; 
	        }
	        
	        if (containsNumbers(lastName)) {
	            System.out.println("Last name is invalid. Please do not enter numbers.");
	            return; 
	        }
	        
	        // Creating a new User object
	        User newUser = new User(username, password, firstName, lastName);

	        // Establishing a database connection and adding the user to the database using UserRegistration class instance
	        DatabaseConnection databaseConnection = new DatabaseConnection();
	        UserRegistration userRegistration = new UserRegistration(databaseConnection);

	        // if the user is successfully registered then the dash board is loaded
	        if (userRegistration.addUser(newUser)) {
	            loadDashboard();
	        } else {
	            System.out.println("Registration failed for the user.");
	        }

	        // closing the database
	        databaseConnection.closeConnection();
	    }
	 
	 // method to validate the names by checking if they contain number or not
	 public boolean containsNumbers(String str) {
		    return str.matches(".*\\d.*"); 
		}
	 
	// method to navigate the user to the dash board page when they are successfully registered
	 private void loadDashboard() {
	        try {
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/DashboardView.fxml"));
	            Parent dashboardView = loader.load();
	            Scene dashboardScene = new Scene(dashboardView);
	            Stage currentStage = (Stage) btnsignup.getScene().getWindow();
	            currentStage.setScene(dashboardScene);
	            currentStage.setTitle("Dashboard");
	            currentStage.show();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    
	 
	 	// method to clear all the text fields 
	    public void clearFields() {
	        usernamesignup.clear();
	        passwordsignup.clear();
	        firstNamesignup.clear();
	        lastNamesignup.clear();
	    }
	}
	

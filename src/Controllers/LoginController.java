package Controllers;

// importing all the necessary required libraries for the login controller class
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import Model.CurrentSession;
import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class LoginController {
	
	// FXML statement to link the UI components
    @FXML 
    // text field for user name input
    private TextField usernamelogin;
    @FXML 
    // text field for password input
    private PasswordField passwordlogin;
    @FXML 
	// button for log in
    private Button btnlogin;
    @FXML 
    // button for sign up
    private Button btnsignup;
    @FXML 
    // label for displaying error
    private Label labelerror;

    // initialize method to set up the event handlers for the login and sign up button
    @FXML
    public void initialize() {
        
        // Set up button handlers
        btnsignup.setOnAction(event -> goToSignUp());
        btnlogin.setOnAction(event -> loginProcess());
    }

    // method to handle the login process
    @FXML
    public void loginProcess() {
    	
    	// retrieving the user input from the text fields to the string variables
        String username = usernamelogin.getText();
        String password = passwordlogin.getText();

        // user name and password empty validation
        if (username.isEmpty() || password.isEmpty()) {
            labelerror.setText("Please enter both username and password");
            return;
        }

        // Checking if it is admin or not by calling admin authentication method
        // if it is admin then the admin dashboard page is loaded
        if (adminAuthentication(username, password)) {
            try {
                Parent adminDashboardPage = FXMLLoader.load(getClass().getResource("/Views/AdminDashboardView.fxml"));
                Stage stage = (Stage) btnlogin.getScene().getWindow();
                stage.setScene(new Scene(adminDashboardPage));
                stage.show();
                return;
            } catch (Exception e) {
                e.printStackTrace();
                labelerror.setText("Error encountered while logging");
                return;
            }
        }

        // if it is not admin then it validates user name and passowrd by calling userAuthentication method
        User currentUser = userAuthentication(username, password);
        
        if (currentUser != null) {
            // Sets the current user in Current Session so that other components of application can access it
            CurrentSession.getInstance().setCurrentUser(currentUser);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/DashboardView.fxml"));
                Parent dashboardPage = loader.load();
                
                // passing the user details to the dashboard controller
                DashboardController dashboardController = loader.getController();
                dashboardController.setCurrentUserId(currentUser.getID());
                dashboardController.loadUserCart();
                dashboardController.setFirstName(currentUser.getFirstName());
 
                Stage stage = (Stage) btnlogin.getScene().getWindow();
                stage.setScene(new Scene(dashboardPage));
                stage.show();
            } catch (Exception e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
                labelerror.setText("Error encountered while logging");
            }
        } else {
            labelerror.setText("Try Again! Invalid username or password");
        }
    }

    // method to navigate the user to the sign up page when sign up button is clicked
    public void goToSignUp() {
        try {
            Parent signupPage = FXMLLoader.load(getClass().getResource("/Views/Signup.fxml"));
            Stage stage = (Stage) btnsignup.getScene().getWindow();
            stage.setScene(new Scene(signupPage));
            stage.show();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            labelerror.setText("Error encountered while loading signup page");
        }
    }

    // method to check if admin is logging or user
    public boolean adminAuthentication(String username, String password) {
        return username.equals("admin") && password.equals("reading_admin");
    }

    // method to validate user details for logging
    private User userAuthentication(String username, String password) {
    	// String variable DATABASE_URL to declare the database file location
        String DATABASE_URL = "jdbc:sqlite:readingroom.db";
        
        // defining the SQL statement to check if user exists or not in the database
        String checkUserSQLStatement = "SELECT * FROM users WHERE username = ? AND password = ?";

        // establishing the database connection
        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(checkUserSQLStatement)) {
            
        	// executing the SQL Statement
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            // if the user details are found then it is retrieved in the variables
            if (rs.next()) {
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                int id = rs.getInt("id");
                User user = new User(username, password, firstName, lastName);
                user.setID(id);
                return user;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
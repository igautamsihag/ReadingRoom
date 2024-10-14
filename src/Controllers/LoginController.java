package Controllers;

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
        
        if(userAuthentication(username, password)) {
        	try {
                Parent nextPage = FXMLLoader.load(getClass().getResource("/Views/Dashboard.fxml"));
                Stage stage = (Stage) btnlogin.getScene().getWindow();
                stage.setScene(new Scene(nextPage));
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
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

	private boolean userAuthentication(String username, String password) {
		return username.equals("user") && password.equals("reading");
	}
}


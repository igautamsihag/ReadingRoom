package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class LoginController {

	@FXML
    private Button btnsignup;
	
	@FXML
    public void initialize() {
        btnsignup.setOnAction(event -> goToSignUp());
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
	
}

package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class SignupController {

	@FXML
    private Button btnlogin;
	
	@FXML
    public void initialize() {
        btnlogin.setOnAction(event -> goToLogIn());
    }
	

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
	
}

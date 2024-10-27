package Controllers;
//Importing necessary libraries for checkout confirm controller
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class CheckoutConfirmController {

	// FXML statement to link the UI components
	// button for log out
    @FXML
    private Button btnlogout;
    
    // button for dash board
    @FXML
    private Button btndashboard;
    
 
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
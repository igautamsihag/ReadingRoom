package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class CheckoutConfirmController {

    @FXML
    private Button btnlogout;
    
    @FXML
    private Button btndashboard;
    
//    @FXML
//    private Button btncart;

    @FXML
    public void initialize() {
        System.out.println("Initializing DashboardController");
        
        // Check if buttons are properly initialized
        System.out.println("btnlogout: " + (btnlogout == null ? "null" : "initialized"));
        System.out.println("btnexport: " + (btndashboard == null ? "null" : "initialized"));
        //System.out.println("btncart: " + (btncart == null ? "null" : "initialized"));
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
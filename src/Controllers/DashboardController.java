package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class DashboardController {

    @FXML
    private Button btnlogout;
    
    @FXML
    private Button btnexport;
    
    @FXML
    private Button btncart;
    
    @FXML
    private Button btncheck;
    

    @FXML
    public void initialize() {
        System.out.println("Initializing DashboardController");
        
        // Check if buttons are properly initialized
        System.out.println("btnlogout: " + (btnlogout == null ? "null" : "initialized"));
        System.out.println("btnexport: " + (btnexport == null ? "null" : "initialized"));
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

    public void goToExport() {
        System.out.println("Export button clicked!");
        try {
            Parent exportPage = FXMLLoader.load(getClass().getResource("/Views/Export.fxml"));
            Stage stage = (Stage) btnexport.getScene().getWindow();
            stage.setScene(new Scene(exportPage));
            stage.show();
        } catch (Exception e) {
            System.err.println("Failed to load export page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void goToCart() {
        System.out.println("Cart button clicked!");
        try {
            Parent cartPage = FXMLLoader.load(getClass().getResource("/Views/Cart.fxml"));
            Stage stage = (Stage) btncart.getScene().getWindow();
            stage.setScene(new Scene(cartPage));
            stage.show();
        } catch (Exception e) {
            System.err.println("Failed to load cart page: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void goToCheckout() {
        System.out.println("Cart button clicked!");
        try {
            Parent cartPage = FXMLLoader.load(getClass().getResource("/Views/Checkout.fxml"));
            Stage stage = (Stage) btncheck.getScene().getWindow();
            stage.setScene(new Scene(cartPage));
            stage.show();
        } catch (Exception e) {
            System.err.println("Failed to load cart page: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

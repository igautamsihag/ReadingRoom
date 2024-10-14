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
	
//	@FXML
//    public void initialize() {
//		System.out.println("Initializing DashboardController");
//        btnlogout.setOnAction(event -> goToLogOut());
//        btnexport.setOnAction(event -> goToExport());
//        btncart.setOnAction(event -> goToCart());
//    }
	

//	public void goToLogOut() {
//		System.out.println("Logout button clicked!");
//        try {
//            Parent dashboardPage = FXMLLoader.load(getClass().getResource("/Views/Login.fxml"));
//            Stage stage = (Stage) btnlogout.getScene().getWindow();
//            stage.setScene(new Scene(dashboardPage));
//            stage.show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//	
//	public void goToExport() {
//        try {
//            Parent dashboardPage = FXMLLoader.load(getClass().getResource("/Views/Export.fxml"));
//            Stage stage = (Stage) btnexport.getScene().getWindow();
//            stage.setScene(new Scene(dashboardPage));
//            stage.show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//	
//	public void goToCart() {
//        try {
//            Parent dashboardPage = FXMLLoader.load(getClass().getResource("/Views/Cart.fxml"));
//            Stage stage = (Stage) btncart.getScene().getWindow();
//            stage.setScene(new Scene(dashboardPage));
//            stage.show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
	
	@FXML
    public void initialize() {
        System.out.println("Initializing DashboardController");
        
        if (btnlogout == null) System.out.println("btnlogout is null");
        if (btnexport == null) System.out.println("btnexport is null");
        if (btncart == null) System.out.println("btncart is null");

        // Use FXML-defined onAction methods instead of programmatic setting
        // This assumes you've set up onAction="#methodName" in your FXML
        btnlogout.setOnAction(event -> {
            System.out.println("Logout button clicked!");
            goToLogOut();
        });
        btnexport.setOnAction(event -> {
            System.out.println("Export button clicked!");
            goToExport();
        });
        btncart.setOnAction(event -> {
            System.out.println("Cart button clicked!");
            goToCart();
        });
    }



	 public void goToLogOut() {
	        System.out.println("Attempting to go to Login page");
	        try {
	            Parent loginPage = FXMLLoader.load(getClass().getResource("/Views/Login.fxml"));
	            Stage stage = (Stage) btnlogout.getScene().getWindow();
	            stage.setScene(new Scene(loginPage));
	            stage.show();
	            System.out.println("Successfully switched to Login page");
	        } catch (Exception e) {
	            System.err.println("Failed to load Login page: " + e.getMessage());
	            e.printStackTrace();
	        }
	    }

    public void goToExport() {
        // Similar implementation as goToLogOut, with appropriate logging
    	System.out.println("Attempting to go to export page");
        try {
            Parent loginPage = FXMLLoader.load(getClass().getResource("/Views/Export.fxml"));
            Stage stage = (Stage) btnlogout.getScene().getWindow();
            stage.setScene(new Scene(loginPage));
            stage.show();
            System.out.println("Successfully switched to export page");
        } catch (Exception e) {
            System.err.println("Failed to load export page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void goToCart() {
        // Similar implementation as goToLogOut, with appropriate logging
    	System.out.println("Attempting to go to cart page");
        try {
            Parent loginPage = FXMLLoader.load(getClass().getResource("/Views/Cart.fxml"));
            Stage stage = (Stage) btnlogout.getScene().getWindow();
            stage.setScene(new Scene(loginPage));
            stage.show();
            System.out.println("Successfully switched to cart page");
        } catch (Exception e) {
            System.err.println("Failed to load cart page: " + e.getMessage());
            e.printStackTrace();
        }
    }
}



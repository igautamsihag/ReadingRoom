package Controllers;

import Model.CartItem;
import Model.ShoppingCart;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class CartController {

    @FXML
    private Button btnlogout;
    
    @FXML
    private Button btndashboard;
    
    @FXML
    private Button btncheck;

    @FXML
    private VBox bookList;

    private ShoppingCart shoppingCart;

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
        displayCartItems();
    }

    private void displayCartItems() {
        bookList.getChildren().clear();
        
        for (CartItem item : shoppingCart.getItems()) {
            HBox itemRow = new HBox(10);
            Label bookLabel = new Label(item.getTitle() + " (Price: " + item.getPrice() + ")");
            TextField quantityField = new TextField(String.valueOf(item.getQuantity()));
            Button removeButton = new Button("Remove");

            removeButton.setOnAction(event -> {
                int quantityToRemove = Integer.parseInt(quantityField.getText().isEmpty() ? "0" : quantityField.getText());
                int currentQuantity = item.getQuantity();
                if (currentQuantity > quantityToRemove) {
                    // Decrease the quantity
                    item.setQuantity(currentQuantity - quantityToRemove);
                } else {
                    // Remove item from cart if quantity becomes zero or less
                    shoppingCart.removeItem(item);
                }
                displayCartItems(); // Refresh the cart display
            });

            quantityField.textProperty().addListener((obs, oldText, newText) -> {
                try {
                    int quantity = Integer.parseInt(newText.isEmpty() ? "0" : newText);
                    item.setQuantity(quantity); // Update the quantity in the CartItem
                } catch (NumberFormatException e) {
                    quantityField.setText(oldText); // Revert to old text on invalid input
                }
            });

            itemRow.getChildren().addAll(bookLabel, quantityField, removeButton);
            bookList.getChildren().add(itemRow);
        }
    }
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
    
    public void goToCheckout() {
        System.out.println("Export button clicked!");
        try {
            Parent exportPage = FXMLLoader.load(getClass().getResource("/Views/Checkout.fxml"));
            Stage stage = (Stage) btncheck.getScene().getWindow();
            stage.setScene(new Scene(exportPage));
            stage.show();
        } catch (Exception e) {
            System.err.println("Failed to load export page: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
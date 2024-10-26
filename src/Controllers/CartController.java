package Controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

    @FXML
    private Label errorMessage; // Add this for error messages

    private ShoppingCart shoppingCart;
    private int currentUserId; // Add this field

    public void setCurrentUserId(int userId) {
        this.currentUserId = userId;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
        displayCartItems();
    }

    private void displayCartItems() {
        bookList.getChildren().clear();
        errorMessage.setText(""); // Clear error message initially
        
        for (CartItem item : shoppingCart.getItems()) {
            HBox itemRow = new HBox(10);
            Label bookLabel = new Label(item.getTitle() + " (Price: " + item.getPrice() + ", Available: " + item.getAvailableQuantity() + ")");
            TextField quantityField = new TextField(String.valueOf(item.getQuantity()));
            quantityField.setPrefWidth(50);
            
            // Increase and Decrease buttons
            Button increaseButton = new Button("+");
            Button decreaseButton = new Button("-");
            Button removeButton = new Button("Remove");

            increaseButton.setOnAction(event -> {
                if (item.getQuantity() < item.getAvailableQuantity()) {
                    item.setQuantity(item.getQuantity() + 1); // Increase quantity
                } else {
                    errorMessage.setText("Only " + item.getAvailableQuantity() + " available for " + item.getTitle());
                }
                displayCartItems(); // Refresh the cart display
            });

            decreaseButton.setOnAction(event -> {
                int currentQuantity = item.getQuantity();
                if (currentQuantity > 1) {
                    item.setQuantity(currentQuantity - 1); // Decrease quantity
                } else {
                    shoppingCart.removeItem(item); // Remove item if quantity is 1
                }
                displayCartItems(); // Refresh the cart display
            });

            removeButton.setOnAction(event -> {
                shoppingCart.removeItem(item);
                removeCartItemFromDb(item);// Remove item from cart
                displayCartItems(); // Refresh the cart display
            });

            // Update the quantity field when the item quantity changes
            quantityField.textProperty().addListener((obs, oldText, newText) -> {
                try {
                    int quantity = Integer.parseInt(newText.isEmpty() ? "0" : newText);
                    if (quantity <= item.getAvailableQuantity()) {
                        item.setQuantity(quantity);
                        updateCartItemInDb(item);// Update the quantity in the CartItem
                        errorMessage.setText(""); // Clear any previous error
                    } else {
                        errorMessage.setText("Only " + item.getAvailableQuantity() + " available for " + item.getTitle());
                        quantityField.setText(oldText); // Revert to old text on invalid input
                    }
                } catch (NumberFormatException e) {
                    quantityField.setText(oldText); // Revert to old text on invalid input
                }
            });

            itemRow.getChildren().addAll(bookLabel, decreaseButton, quantityField, increaseButton, removeButton);
            bookList.getChildren().add(itemRow);
        }
    }

    @FXML
    public void initialize() {
        System.out.println("Initializing CartController");
        System.out.println("btnlogout: " + (btnlogout == null ? "null" : "initialized"));
        System.out.println("btndashboard: " + (btndashboard == null ? "null" : "initialized"));
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
        System.out.println("Dashboard button clicked!");
        try {
            Parent exportPage = FXMLLoader.load(getClass().getResource("/Views/DashboardView.fxml"));
            Stage stage = (Stage) btndashboard.getScene().getWindow();
            stage.setScene(new Scene(exportPage));
            stage.show();
        } catch (Exception e) {
            System.err.println("Failed to load dashboard page: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void goToCheckout() {
        System.out.println("Going to checkout from cart!");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Checkout.fxml"));
            Parent checkoutPage = loader.load();
            CheckoutController checkoutController = loader.getController();
            checkoutController.setShoppingCart(shoppingCart); // Pass the shopping cart
            Stage stage = (Stage) btncheck.getScene().getWindow();
            stage.setScene(new Scene(checkoutPage));
            stage.show();
        } catch (Exception e) {
            System.err.println("Failed to load checkout page: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private void updateCartItemInDb(CartItem item) {
        String url = "jdbc:sqlite:readingroom.db";
        String sql = "UPDATE cart_items SET quantity = ? WHERE user_id = ? AND book_title = ?";
        
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, item.getQuantity());
            pstmt.setInt(2, currentUserId);
            pstmt.setString(3, item.getTitle());
            pstmt.setInt(3, getBookId(item.getTitle()));
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void removeCartItemFromDb(CartItem item) {
        String url = "jdbc:sqlite:readingroom.db";
        String sql = "DELETE FROM cart_items WHERE user_id = ? AND book_title = ?";
        
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, currentUserId);
            pstmt.setString(2, item.getTitle());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private int getBookId(String title) {
        String url = "jdbc:sqlite:readingroom.db";
        String sql = "SELECT book_id FROM books WHERE title = ?";
        
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, title);
            var rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("book_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}


//package Controllers;
//
//import Model.CartItem;
//import Model.ShoppingCart;
//import javafx.fxml.FXML;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.control.TextField;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.VBox;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//import javafx.fxml.FXMLLoader;
//
//public class CartController {
//
//    @FXML
//    private Button btnlogout;
//    
//    @FXML
//    private Button btndashboard;
//    
//    @FXML
//    private Button btncheck;
//
//    @FXML
//    private VBox bookList;
//
//    private ShoppingCart shoppingCart;
//
//    public void setShoppingCart(ShoppingCart shoppingCart) {
//        this.shoppingCart = shoppingCart;
//        displayCartItems();
//    }
//
//    private void displayCartItems() {
//        bookList.getChildren().clear();
//        
//        for (CartItem item : shoppingCart.getItems()) {
//            HBox itemRow = new HBox(10);
//            Label bookLabel = new Label(item.getTitle() + " (Price: " + item.getPrice() + ")");
//            TextField quantityField = new TextField(String.valueOf(item.getQuantity()));
//            quantityField.setPrefWidth(50); // Set preferred width for the quantity field
//            
//            // Increase and Decrease buttons
//            Button increaseButton = new Button("+");
//            Button decreaseButton = new Button("-");
//            Button removeButton = new Button("Remove");
//
//            increaseButton.setOnAction(event -> {
//                item.setQuantity(item.getQuantity() + 1); // Increase quantity
//                displayCartItems(); // Refresh the cart display
//            });
//
//            decreaseButton.setOnAction(event -> {
//                int currentQuantity = item.getQuantity();
//                if (currentQuantity > 1) {
//                    item.setQuantity(currentQuantity - 1); // Decrease quantity
//                } else {
//                    shoppingCart.removeItem(item); // Remove item if quantity is 1
//                }
//                displayCartItems(); // Refresh the cart display
//            });
//
//            removeButton.setOnAction(event -> {
//                shoppingCart.removeItem(item); // Remove item from cart
//                displayCartItems(); // Refresh the cart display
//            });
//
//            // Update the quantity field when the item quantity changes
//            quantityField.textProperty().addListener((obs, oldText, newText) -> {
//                try {
//                    int quantity = Integer.parseInt(newText.isEmpty() ? "0" : newText);
//                    item.setQuantity(quantity); // Update the quantity in the CartItem
//                } catch (NumberFormatException e) {
//                    quantityField.setText(oldText); // Revert to old text on invalid input
//                }
//            });
//
//            itemRow.getChildren().addAll(bookLabel, decreaseButton, quantityField, increaseButton, removeButton);
//            bookList.getChildren().add(itemRow);
//        }
//    }
//
//    @FXML
//    public void initialize() {
//        System.out.println("Initializing CartController");
//        System.out.println("btnlogout: " + (btnlogout == null ? "null" : "initialized"));
//        System.out.println("btndashboard: " + (btndashboard == null ? "null" : "initialized"));
//    }
// 
//    public void goToLogOut() {
//        System.out.println("Logout button clicked!");
//        try {
//            Parent loginPage = FXMLLoader.load(getClass().getResource("/Views/Login.fxml"));
//            Stage stage = (Stage) btnlogout.getScene().getWindow();
//            stage.setScene(new Scene(loginPage));
//            stage.show();
//        } catch (Exception e) {
//            System.err.println("Failed to load Login page: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    public void goToDashboard() {
//        System.out.println("Dashboard button clicked!");
//        try {
//            Parent exportPage = FXMLLoader.load(getClass().getResource("/Views/DashboardView.fxml"));
//            Stage stage = (Stage) btndashboard.getScene().getWindow();
//            stage.setScene(new Scene(exportPage));
//            stage.show();
//        } catch (Exception e) {
//            System.err.println("Failed to load dashboard page: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//    
//    public void goToCheckout() {
//        System.out.println("Going to checkout from cart!");
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Checkout.fxml"));
//            Parent checkoutPage = loader.load();
//            CheckoutController checkoutController = loader.getController();
//            checkoutController.setShoppingCart(shoppingCart); // Pass the shopping cart
//            Stage stage = (Stage) btncheck.getScene().getWindow();
//            stage.setScene(new Scene(checkoutPage));
//            stage.show();
//        } catch (Exception e) {
//            System.err.println("Failed to load checkout page: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//}
//
//

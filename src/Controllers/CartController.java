package Controllers;
//Importing necessary libraries for cart controller
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

	// FXML statement to link the UI components
	// button for log out
    @FXML
    private Button btnlogout;
    
    // button for dash board
    @FXML
    private Button btndashboard;
    
    // button for checkout
    @FXML
    private Button btncheck;

    // v box to display cart items
    @FXML
    private VBox bookList;

    // label for displaying error
    @FXML
    private Label errorlabels; 

    private ShoppingCart shoppingCart;
    private int currentUserId; 

    public void setCurrentUserId(int userId) {
        this.currentUserId = userId;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
        displayCartItems();
    }

    // method to display cart items in a v box
    private void displayCartItems() {
    	
    	// Clearing all the book list and error messages
        bookList.getChildren().clear();
        errorlabels.setText(""); 
        
        // using a for loop to iterate over the cart items for displaying 
        for (CartItem item : shoppingCart.getItems()) {
            HBox itemRow = new HBox(10);
            Label bookLabel = new Label(item.getTitle() + " (Price: " + item.getPrice() + ", Available: " + item.getAvailableQuantity() + ")");
            TextField quantityField = new TextField(String.valueOf(item.getQuantity()));
            quantityField.setPrefWidth(50);
            
            // buttons for increasing and decreasing book quantity
            Button plusButton = new Button("+");
            Button minusButton = new Button("-");
            Button removeButton = new Button("Remove Item");

            // if the user clicks on plus button, then get available quantity function is called and if it is greater then book quantity is incremented in the cart
            plusButton.setOnAction(event -> {
                if (item.getQuantity() < item.getAvailableQuantity()) {
                    item.setQuantity(item.getQuantity() + 1); 
                } else {
                    errorlabels.setText("There is only " + item.getAvailableQuantity() + " stock left fot " + item.getTitle());
                }
                displayCartItems(); 
            });

         // if the user clicks on minus button, then get quantity function is called and if it is greater than 1 then book quantity is decremented in the cart
            minusButton.setOnAction(event -> {
                int currentQuantity = item.getQuantity();
                if (currentQuantity > 1) {
                    item.setQuantity(currentQuantity - 1); 
                } else {
                    shoppingCart.removeItem(item); 
                }
                displayCartItems(); 
            });

            // if the user clicks on remove button then the item is removed from the cart
            removeButton.setOnAction(event -> {
                shoppingCart.removeItem(item);
                deleteCartItem(item);
                displayCartItems(); 
            });


            // (supernovsupernov, JavaFX Textfield Listener is never called 1967)
            // listener checking the changes in the quantity input
            quantityField.textProperty().addListener((obs, oldText, newText) -> {
                try {
                	// if the new value is valid then it updates the quantity
                    int quantity = Integer.parseInt(newText.isEmpty() ? "0" : newText);
                    if (quantity <= item.getAvailableQuantity()) {
                        item.setQuantity(quantity);
                        updateCartItem(item);
                        errorlabels.setText(""); 
                        // else it displays an error message
                    } else {
                        errorlabels.setText("There is only " + item.getAvailableQuantity() + " stock left for " + item.getTitle());
                        quantityField.setText(oldText); // Revert to old text on invalid input
                    }
                } catch (NumberFormatException e) {
                    quantityField.setText(oldText); // Revert to old text on invalid input
                }
            });

            itemRow.getChildren().addAll(bookLabel, minusButton, quantityField, plusButton, removeButton);
            bookList.getChildren().add(itemRow);
        }
    }

 
    // method to navigate the user to the logout page
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

    // method to navigate the user to the dash board page
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
    
    // method to navigate the user to the checkout page
    public void goToCheckout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Checkout.fxml"));
            Parent checkoutPage = loader.load();
            CheckoutController checkoutController = loader.getController();
            // Pass the shopping cart details to the checkout controller
            checkoutController.setShoppingCart(shoppingCart);
            Stage stage = (Stage) btncheck.getScene().getWindow();
            stage.setScene(new Scene(checkoutPage));
            stage.show();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
    // method to update cart item from the database
    private void updateCartItem(CartItem item) {
    	
    	// String variable DATABASE_URL to declare the database file location
        String DATABASE_URL = "jdbc:sqlite:readingroom.db";
        
        // defining the SQL statement to update cart items from the table
        String updateCartSQLStatement = "UPDATE cart_items SET quantity = ? WHERE user_id = ? AND book_title = ?";
        
        // establishing the database connection and executing the query
        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(updateCartSQLStatement)) {         
            pstmt.setInt(1, item.getQuantity());
            pstmt.setInt(2, currentUserId);
            pstmt.setString(3, item.getTitle());
            pstmt.setInt(3, getBookId(item.getTitle()));           
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // method to delete a cart item from the table
    private void deleteCartItem(CartItem item) {
    	
    	// String variable DATABASE_URL to declare the database file location
        String DATABASE_URL = "jdbc:sqlite:readingroom.db";
        
        // defining the SQL statement to delete cart items from the table
        String deleteCartSQLStatement = "DELETE FROM cart_items WHERE user_id = ? AND book_title = ?";
        
        // establishing the database connection and executing the query
        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(deleteCartSQLStatement)) {
            pstmt.setInt(1, currentUserId);
            pstmt.setString(2, item.getTitle());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // method to get the book id through title
    private int getBookId(String title) {
    	
    	// String variable DATABASE_URL to declare the database file location
        String DATABASE_URL = "jdbc:sqlite:readingroom.db";
        
        // defining the SQL statement to get the book id from the table
        String bookIDSQLStatement = "SELECT book_id FROM books WHERE title = ?";
        
        // establishing the database connection and executing the query
        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(bookIDSQLStatement)) {       
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

// References

// supernovsupernov 111 silver badge55 bronze badges and James_DJames_D 208k1616 gold badges307307 silver badges340340 bronze badges (1967) JavaFX Textfield Listener is never called, Stack Overflow. Available at: https://stackoverflow.com/questions/72533183/javafx-textfield-listener-is-never-called (Accessed: 20 October 2024). 
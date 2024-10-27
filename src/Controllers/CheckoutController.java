package Controllers;

//importing all the necessary required libraries for this dash board controller
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import Model.CartItem;
import Model.CurrentSession;
import Model.ShoppingCart;
import Model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class CheckoutController {

	// FXML statement to link the UI components
	// button for log out
    @FXML
    private Button btnlogout;

    // button for dash board page
    @FXML
    private Button btndashboard;

    // button for confirming payment
    @FXML
    private Button btnconfirm;

    // text field for credit card number
    @FXML
	public TextField txtCreditCardNumber;

    // text field for cvv
    @FXML
    private TextField txtCVV;

    // text field for Date
    @FXML
    private TextField txtDate; 

    // text flow for credit card error
    @FXML
    private TextFlow errcreditcard;

    // text flow for cvv error
    @FXML
    private TextFlow errcvv;

    // text flow for date error
    @FXML
    private TextFlow errdate;

    // text flow for stock 
    @FXML
    private TextFlow errStock; 

    // label for displaying total price
    @FXML
    private Label pricelabel;

    private ShoppingCart shoppingCart;
    

    // method to get the shopping cart 
    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
        updateTotalPrice();
    }

    // method to calculate the total price of the shopping cart
    private void updateTotalPrice() {
        double totalPrice = shoppingCart.getTotalPrice();
        pricelabel.setText("$" + totalPrice);
    }
    
    // method to navigate user to the log in page
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

    // method to navigate user to the dash board page
    public void goToDashboard() {
        try {
            Parent exportPage = FXMLLoader.load(getClass().getResource("/Views/DashboardView.fxml"));
            Stage stage = (Stage) btndashboard.getScene().getWindow();
            stage.setScene(new Scene(exportPage));
            stage.show();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    // method to confirm the payment
    public void goToConfirm() {

        removeErrorLabels();

        boolean canProceed = true; 

        // checking if credit card number is valid or not
        if (!validateCreditCard(txtCreditCardNumber.getText())) {
            showError(errcreditcard, "Invalid credit card number.");
            canProceed = false;
        }

     // checking if cvv number is valid or not
        if (!validateCVV(txtCVV.getText())) {
            showError(errcvv, "Invalid CVV.");
            canProceed = false;
        }

        // checking if expiry date is valid or not
        if (!validateExpiryDate(txtDate.getText().trim())) {
            showError(errdate, "Invalid expiry date.");
            canProceed = false;
        }

        // using a for loop to iterate over each item in the cart to check their stock
        for (CartItem item : shoppingCart.getItems()) {
            int availableQuantity = getAvailableQuantity(item.getTitle());

            if (availableQuantity < item.getQuantity()) {
                showError(errStock, item.getTitle() + " is out of stock. Only " + availableQuantity + " available.");
                canProceed = false; 
                continue; 
            }
        }

        // if all the above conditions are met then the order is processed
        if (canProceed) {
            createOrder(); 

            // Navigate to confirmation page if order is successfully processed
            try {
                Parent confirmPage = FXMLLoader.load(getClass().getResource("/Views/CheckoutConfirm.fxml"));
                Stage stage = (Stage) btnconfirm.getScene().getWindow();
                stage.setScene(new Scene(confirmPage));
                stage.show();
            } catch (Exception e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Error. Try again");
        }
    }

    // method to create an order
    private void createOrder() {
    	
    	// String variable DATABASE_URL to declare the database file location
        String DATABASE_URL = "jdbc:sqlite:readingroom.db"; 
        
        // defining the SQL statement to insert order details to the database
        String addOrderSQLStatement = "INSERT INTO orders (user_id, order_date, total_price) VALUES (?, ?, ?)";
        String addOrderDetailsSQLStatement = "INSERT INTO order_details (order_id, book_id, quantity, price) VALUES (?, ?, ?, ?)";
        String updateStockSQLStatement = "UPDATE books SET quantity = quantity - ? WHERE book_id = ?";

        // establishing a database connection and executing sql statement
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DATABASE_URL);
            conn.setAutoCommit(false);

            //		(Autogenerated keys, 2024)
            PreparedStatement orderStmt = conn.prepareStatement(addOrderSQLStatement, PreparedStatement.RETURN_GENERATED_KEYS);
            orderStmt.setInt(1, getUserId());
            orderStmt.setString(2, getCurrentDate());
            orderStmt.setDouble(3, shoppingCart.getTotalPrice());
            orderStmt.executeUpdate();

            ResultSet generatedKeys = orderStmt.getGeneratedKeys();			//		(Autogenerated keys, 2024)
            int orderId = -1;	
            if (generatedKeys.next()) {
                orderId = generatedKeys.getInt(1);
            }

            for (CartItem item : shoppingCart.getItems()) {
                int availableQuantity = getAvailableQuantity(item.getTitle());
                if (availableQuantity >= item.getQuantity()) {
                    PreparedStatement detailStmt = conn.prepareStatement(addOrderDetailsSQLStatement);
                    detailStmt.setInt(1, orderId);
                    detailStmt.setInt(2, getBookId(item.getTitle()));
                    detailStmt.setInt(3, item.getQuantity());
                    detailStmt.setDouble(4, item.getPrice());
                    detailStmt.executeUpdate();

                    PreparedStatement updateStmt = conn.prepareStatement(updateStockSQLStatement);
                    updateStmt.setInt(1, item.getQuantity());
                    updateStmt.setInt(2, getBookId(item.getTitle()));
                    updateStmt.executeUpdate();
                }
            }

            // once an order is processed the cart items table for that user is cleared
            conn.commit();
            deleteCartItems();
            System.out.println("Order is processed successfully!");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            try {
                if (conn != null) {
                    conn.rollback();		// 		 (Feek, 2013)
                }
            } catch (SQLException rollbackEx) {
                System.err.println(rollbackEx.getMessage());
            }
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    // method to get the user id from the current session
    private int getUserId() {
        User currentUser = CurrentSession.getInstance().getCurrentUser();
        return currentUser != null ? currentUser.getID() : -1;
    }

    // method to get the current date
    private String getCurrentDate() {
        return java.time.LocalDate.now().toString();
    }

    public int getAvailableQuantity(String title) {
    	
    	// String variable DATABASE_URL to declare the database file location
        String DATABASE_URL = "jdbc:sqlite:readingroom.db";
        
        // defining the SQL statement to get available quantity from the database
        String bookStockSQLStatement = "SELECT quantity FROM books WHERE title = ?";

        // establishing a database connection and executing sql statement
        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(bookStockSQLStatement)) {
            pstmt.setString(1, title);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("quantity");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return 0;
    }

    // method to clear all the cart items from the database
    private void deleteCartItems() {
    	
    	// String variable DATABASE_URL to declare the database file location
        String DATABASE_URL = "jdbc:sqlite:readingroom.db";
        
        // defining the SQL statement to delete the items from the database
        String clearCartSQLStatement = "DELETE FROM cart_items WHERE user_id = ?";

     // establishing a database connection and executing sql statement
        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(clearCartSQLStatement)) {            
            pstmt.setInt(1, getUserId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public int getBookId(String title) {
    	
    	// String variable DATABASE_URL to declare the database file location
        String DATABASE_URL = "jdbc:sqlite:readingroom.db";
        
        // defining the SQL statement to get the book id through title
        String bookIDSQLStatement = "SELECT book_id FROM books WHERE title = ?";

        // establishing a database connection and executing sql statement
        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(bookIDSQLStatement)) {
            pstmt.setString(1, title);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("book_id");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return -1;
    }

    // method to clear all the error labels
    private void removeErrorLabels() {
        errcreditcard.getChildren().clear();
        errcvv.getChildren().clear();
        errdate.getChildren().clear();
        errStock.getChildren().clear();
    }

    // method to show error for input fields and item stock			(GeeksforGeeks, 2018)
    private void showError(TextFlow errorFlow, String message) {
        errorFlow.getChildren().clear();
        Text errorText = new Text(message);
        errorText.setFill(Color.RED);
        errorFlow.getChildren().add(errorText);
        errorFlow.setVisible(true); 
    }

    // method to check if credit card number is numeric and length is 16 or not
    public boolean validateCreditCard(String creditCardNumber) {
        return creditCardNumber.length() == 16 && creditCardNumber.matches("\\d+");
    }

    // method to check if cvv number length is 3 or not
    public boolean validateCVV(String cvv) {
        return cvv.length() == 3 && cvv.matches("\\d+");
    }

    // method to check if the expiry date is of future or not
    public boolean validateExpiryDate(String expiryDate) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
            YearMonth expiryYearMonth = YearMonth.parse(expiryDate, formatter);
            YearMonth currentYearMonth = YearMonth.now();
            return !expiryYearMonth.isBefore(currentYearMonth);
        } catch (DateTimeParseException e) {
            return false;
        }
    }
    

}


// REFERNCES

// GeeksforGeeks (2018) JavaFX | TextFlow Class, GeeksforGeeks. Available at: https://www.geeksforgeeks.org/javafx-textflow-class/ (Accessed: 17 October 2024).
// Autogenerated keys (2024) Oracle.com. Available at: https://docs.oracle.com/javadb/10.10.1.2/ref/crefjavstateautogen.html (Accessed: 20 October 2024).
// Feek, S. (2013) JDBC: Does call to rollback() method have effect only if call to commit() method does not succeed?, Stack Overflow. Available at: https://stackoverflow.com/questions/15031866/jdbc-does-call-to-rollback-method-have-effect-only-if-call-to-commit-method (Accessed: 21 October 2024).

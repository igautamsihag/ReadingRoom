package Controllers;

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

    @FXML
    private Button btnlogout;

    @FXML
    private Button btndashboard;

    @FXML
    private Button btnconfirm;

    @FXML
	public TextField txtCreditCardNumber;

    @FXML
    private TextField txtCVV;

    @FXML
    private TextField txtDate; // Assuming this is formatted as MM/YYYY

    @FXML
    private TextFlow errcreditcard;

    @FXML
    private TextFlow errcvv;

    @FXML
    private TextFlow errdate;

    @FXML
    private TextFlow errStock; // New TextFlow for stock errors

    @FXML
    private Label totalPriceLabel;

    private ShoppingCart shoppingCart;
    

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
        updateTotalPrice();
    }

    private void updateTotalPrice() {
        double totalPrice = shoppingCart.getTotalPrice();
        totalPriceLabel.setText("$" + totalPrice);
    }

    @FXML
    public void initialize() {
        System.out.println("Initializing CheckoutController");
    }

    public void goToLogOut() {
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

    public void goToConfirm() {
        System.out.println("Confirm button clicked!");

        clearErrorMessages();

        boolean canProceed = true; // Flag to determine if we can proceed

        if (!isValidCreditCard(txtCreditCardNumber.getText())) {
            showError(errcreditcard, "Invalid credit card number.");
            canProceed = false;
        }

        if (!isValidCVV(txtCVV.getText())) {
            showError(errcvv, "Invalid CVV.");
            canProceed = false;
        }

        if (!isValidExpiryDate(txtDate.getText().trim())) {
            showError(errdate, "Invalid expiry date.");
            canProceed = false;
        }

        for (CartItem item : shoppingCart.getItems()) {
            int availableQuantity = getAvailableQuantity(item.getTitle());

            if (availableQuantity < item.getQuantity()) {
                showError(errStock, item.getTitle() + " is out of stock. Only " + availableQuantity + " available.");
                canProceed = false; // Set flag to false
                continue; // Skip to the next item
            }
        }

        // Proceed only if all validations are passed
        if (canProceed) {
            createOrder(); // Your order creation logic

            // Navigate to confirmation page
            try {
                Parent exportPage = FXMLLoader.load(getClass().getResource("/Views/CheckoutConfirm.fxml"));
                Stage stage = (Stage) btnconfirm.getScene().getWindow();
                stage.setScene(new Scene(exportPage));
                stage.show();
            } catch (Exception e) {
                System.err.println("Failed to load confirmation page: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Cannot proceed to confirmation due to errors.");
        }
    }

    private void createOrder() {
        String url = "jdbc:sqlite:readingroom.db"; // Your database URL
        String insertOrderSQL = "INSERT INTO orders (user_id, order_date, total_price) VALUES (?, ?, ?)";
        String insertOrderDetailsSQL = "INSERT INTO order_details (order_id, book_id, quantity, price) VALUES (?, ?, ?, ?)";
        String updateBookQuantitySQL = "UPDATE books SET quantity = quantity - ? WHERE book_id = ?";

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            conn.setAutoCommit(false);

            PreparedStatement orderStmt = conn.prepareStatement(insertOrderSQL, PreparedStatement.RETURN_GENERATED_KEYS);
            orderStmt.setInt(1, getCurrentUserId());
            orderStmt.setString(2, getCurrentDate());
            orderStmt.setDouble(3, shoppingCart.getTotalPrice());
            orderStmt.executeUpdate();

            ResultSet generatedKeys = orderStmt.getGeneratedKeys();
            int orderId = -1;
            if (generatedKeys.next()) {
                orderId = generatedKeys.getInt(1);
            }

            for (CartItem item : shoppingCart.getItems()) {
                int availableQuantity = getAvailableQuantity(item.getTitle());
                if (availableQuantity >= item.getQuantity()) {
                    PreparedStatement detailStmt = conn.prepareStatement(insertOrderDetailsSQL);
                    detailStmt.setInt(1, orderId);
                    detailStmt.setInt(2, getBookId(item.getTitle()));
                    detailStmt.setInt(3, item.getQuantity());
                    detailStmt.setDouble(4, item.getPrice());
                    detailStmt.executeUpdate();

                    PreparedStatement updateStmt = conn.prepareStatement(updateBookQuantitySQL);
                    updateStmt.setInt(1, item.getQuantity());
                    updateStmt.setInt(2, getBookId(item.getTitle()));
                    updateStmt.executeUpdate();
                }
            }

            conn.commit();
            clearCartItemsFromDb();
            System.out.println("Order created successfully!");
        } catch (SQLException e) {
            System.err.println("Failed to create order: " + e.getMessage());
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException rollbackEx) {
                System.err.println("Failed to rollback transaction: " + rollbackEx.getMessage());
            }
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Failed to close connection: " + e.getMessage());
            }
        }
    }

    private int getCurrentUserId() {
        User currentUser = CurrentSession.getInstance().getCurrentUser();
        return currentUser != null ? currentUser.getID() : -1;
    }

    private String getCurrentDate() {
        return java.time.LocalDate.now().toString();
    }

    public int getAvailableQuantity(String title) {
        String url = "jdbc:sqlite:readingroom.db";
        String query = "SELECT quantity FROM books WHERE title = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, title);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("quantity");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching available quantity: " + e.getMessage());
        }

        return 0;
    }

    private void clearCartItemsFromDb() {
        String url = "jdbc:sqlite:readingroom.db";
        String sql = "DELETE FROM cart_items WHERE user_id = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, getCurrentUserId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error clearing cart items: " + e.getMessage());
        }
    }

    public int getBookId(String title) {
        String url = "jdbc:sqlite:readingroom.db";
        String query = "SELECT book_id FROM books WHERE title = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, title);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("book_id");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching book ID: " + e.getMessage());
        }

        return -1;
    }

    private void clearErrorMessages() {
        errcreditcard.getChildren().clear();
        errcvv.getChildren().clear();
        errdate.getChildren().clear();
        errStock.getChildren().clear(); // Clear stock error messages
    }

    private void showError(TextFlow errorFlow, String message) {
        errorFlow.getChildren().clear();
        Text errorText = new Text(message);
        errorText.setFill(Color.RED);
        errorFlow.getChildren().add(errorText);
        errorFlow.setVisible(true); // Ensure the TextFlow is visible
    }

    public boolean isValidCreditCard(String creditCardNumber) {
        return creditCardNumber.length() == 16 && creditCardNumber.matches("\\d+");
    }

    public boolean isValidCVV(String cvv) {
        return cvv.length() == 3 && cvv.matches("\\d+");
    }

    public boolean isValidExpiryDate(String expiryDate) {
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

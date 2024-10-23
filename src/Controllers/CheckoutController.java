package Controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
// importing all the necessary required libraries
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
	private TextField txtCreditCardNumber;

	@FXML
	private TextField txtCVV;

	@FXML
	private TextField txtDate;

	@FXML
	private TextFlow errcreditcard;

	@FXML
	private TextFlow errcvv;

	@FXML
	private TextFlow errdate;
	
	@FXML
    private Label totalPriceLabel; // Add this label in your FXML

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
		System.out.println("Initializing DashboardController");

		// Check if buttons are properly initialized
		System.out.println("btnlogout: " + (btnlogout == null ? "null" : "initialized"));
		System.out.println("btnexport: " + (btndashboard == null ? "null" : "initialized"));
		// System.out.println("btncart: " + (btncart == null ? "null" : "initialized"));
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

	public void goToConfirm() {
		System.out.println("Export button clicked!");
		System.out.println("Date entered: " + txtDate.getText());

		clearErrorMessages();

		boolean isValid = true;

		if (!isValidCreditCard(txtCreditCardNumber.getText())) {
			showError(errcreditcard, "Invalid credit card number.");
			isValid = false;
		}
		if (!isValidCVV(txtCVV.getText())) {
			showError(errcvv, "Invalid CVV.");
			isValid = false;
		}
		if (!isValidExpiryDate(txtDate.getText().trim())) {
			showError(errdate, "Invalid expiry date.");
			isValid = false;
		}

		if (isValid) {
			createOrder();
			try {

				Parent exportPage = FXMLLoader.load(getClass().getResource("/Views/CheckoutConfirm.fxml"));
				Stage stage = (Stage) btnconfirm.getScene().getWindow();
				stage.setScene(new Scene(exportPage));
				stage.show();
			} catch (Exception e) {
				System.err.println("Failed to load export page: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	private void createOrder() {
        String url = "jdbc:sqlite:readingroom.db"; // Your database URL
        String insertOrderSQL = "INSERT INTO orders (user_id, order_date, total_price) VALUES (?, ?, ?)";
        String insertOrderDetailsSQL = "INSERT INTO order_details (order_id, book_id, quantity, price) VALUES (?, ?, ?, ?)";

        Connection conn = null;
        try {
            // Start a transaction
        	conn = DriverManager.getConnection(url);
            conn.setAutoCommit(false);

            // Insert the order
            PreparedStatement orderStmt = conn.prepareStatement(insertOrderSQL, PreparedStatement.RETURN_GENERATED_KEYS);
            // Assuming you have a method to get the current user ID
            orderStmt.setInt(1, getCurrentUserId()); // Get current user ID
            orderStmt.setString(2, getCurrentDate()); // Current date
            orderStmt.setDouble(3, shoppingCart.getTotalPrice());
            orderStmt.executeUpdate();

            // Get the generated order ID
            ResultSet generatedKeys = orderStmt.getGeneratedKeys();
            int orderId = -1;
            if (generatedKeys.next()) {
                orderId = generatedKeys.getInt(1);
            }

            // Insert order details
            for (CartItem item : shoppingCart.getItems()) {
                PreparedStatement detailStmt = conn.prepareStatement(insertOrderDetailsSQL);
                detailStmt.setInt(1, orderId);
                detailStmt.setInt(2, getBookId(item.getTitle())); // You need to implement this method
                detailStmt.setInt(3, item.getQuantity());
                detailStmt.setDouble(4, item.getPrice());
                detailStmt.executeUpdate();
            }

            // Commit the transaction
            conn.commit();
            System.out.println("Order created successfully!");
        } catch (SQLException e) {
            System.err.println("Failed to create order: " + e.getMessage());
            // Rollback in case of error
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException rollbackEx) {
                System.err.println("Failed to rollback transaction: " + rollbackEx.getMessage());
            }
        }
    }

    private int getCurrentUserId() {
    	User currentUser = CurrentSession.getInstance().getCurrentUser();
        return currentUser != null ? currentUser.getID() : -1;
    }

    private String getCurrentDate() {
        // Logic to get the current date as a string
        return java.time.LocalDate.now().toString();
    }

    private int getBookId(String title) {
    	String url = "jdbc:sqlite:readingroom.db"; // Your database URL
        String query = "SELECT book_id FROM books WHERE title = ?"; // Update this with your actual table name

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, title);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("book_id"); // Return the book ID
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
	}

	private void showError(TextFlow errorFlow, String message) {
		Text errorText = new Text(message);
		errorText.setFill(Color.RED);
		errorFlow.getChildren().add(errorText);
	}

	public boolean isValidCreditCard(String creditCardNumber) {
		return creditCardNumber.length() == 16 && creditCardNumber.matches("\\d+");
	}

	private boolean isValidCVV(String cvv) {
		return cvv.length() == 3 && cvv.matches("\\d+");
	}

	private boolean isValidExpiryDate(String expiryDate) {
		System.out.println("Validating date: " + expiryDate);
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
			YearMonth expiryYearMonth = YearMonth.parse(expiryDate, formatter);
			YearMonth currentYearMonth = YearMonth.now();

			System.out.println("Parsed expiry year/month: " + expiryYearMonth);
			System.out.println("Current year/month: " + currentYearMonth);
			System.out.println("Is expiry after or equal to current: " + !expiryYearMonth.isBefore(currentYearMonth));

			return !expiryYearMonth.isBefore(currentYearMonth);
		} catch (DateTimeParseException e) {
			System.out.println("Date parsing failed: " + e.getMessage());
			return false;
		}
	}

}
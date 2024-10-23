package Controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.CartItem;
import Model.ShoppingCart;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
//import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class DashboardController {
	
	private ShoppingCart shoppingCart; // Add this field

    public DashboardController() {
        shoppingCart = new ShoppingCart(); // Initialize the shopping cart
    }

	@FXML
	private Button btnlogout;

	@FXML
	private Button btncart;

	@FXML
	private Button btncheck;

	@FXML
	private Button btnedit;

	@FXML
	private Button btnsearch;

	@FXML
	private Label welcomeLabel;

	@FXML
	private TextField inputsearch;

	@FXML
	private VBox popularBooksList;

	@FXML
	private Button btnlist;
	
	@FXML
	private Button btndetails;
	
	@FXML
	private Label messageLabel;

	@FXML
	public void initialize() {
		System.out.println("Initializing DashboardController");

		// Check if buttons are properly initialized
		System.out.println("btnlogout: " + (btnlogout == null ? "null" : "initialized"));
		//System.out.println("btnexport: " + (btnexport == null ? "null" : "initialized"));
		// System.out.println("btncart: " + (btncart == null ? "null" : "initialized"));

		// Load the top 5 popular books
		List<String> popularBooks = getTop5PopularBooks();
		// popularBooksList.getItems().addAll(popularBooks);
		populateBooks(popularBooks);
	}

	private void populateBooks(List<String> books) {
		popularBooksList.getChildren().clear();
		messageLabel.setText("");// Clear previous entries
		for (String title : books) {
			// Create a horizontal layout for each book
			HBox bookItem = new HBox(10); // spacing of 10
			Label bookLabel = new Label(title);
			TextField quantityField = new TextField(); // Default quantity
			Button buyButton = new Button("Buy");

			int availableQuantity = getAvailableQuantity(title);
			buyButton.setOnAction(event -> {
				String quantityText = quantityField.getText();
				// Add your buy logic here
				int bookquantity = Integer.parseInt(quantityText.isEmpty() ? "0" : quantityText);
				if (bookquantity > availableQuantity) {
					messageLabel.setText("Only " + availableQuantity + " copies of " + title + " are available.");
	                System.out.println("Only " + availableQuantity + " copies of " + title + " are available.");
	                // Display an error message to the user
	                // You might want to use a Label or Dialog for this purpose
	                return; // Stop further execution
	            }
                double price = getBookPrice(title); // Assume a method to get price
                shoppingCart.addItem(new CartItem(title, bookquantity, price, availableQuantity));
				System.out.println("Buying " + quantityText + " copies of " + title);
			});

			bookItem.getChildren().addAll(bookLabel, quantityField, buyButton);
			popularBooksList.getChildren().add(bookItem);
		}
	}
	
	private int getAvailableQuantity(String title) {
	    String url = "jdbc:sqlite:readingroom.db"; // Update with your DB path
	    String sql = "SELECT quantity FROM books WHERE title = ?"; // Adjust this query based on your database schema

	    try (Connection conn = DriverManager.getConnection(url);
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

	        pstmt.setString(1, title);
	        ResultSet rs = pstmt.executeQuery();

	        if (rs.next()) {
	            return rs.getInt("quantity"); // Ensure this matches your column name for available quantity
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return 0; // Default if not found
	}
	
	private double getBookPrice(String title) {
		String url = "jdbc:sqlite:readingroom.db"; // Update with your DB path
	    String sql = "SELECT price FROM books WHERE title = ?";

	    try (Connection conn = DriverManager.getConnection(url);
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

	        pstmt.setString(1, title);
	        ResultSet rs = pstmt.executeQuery();

	        if (rs.next()) {
	            return rs.getDouble("price");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return 0.0;
    }

	public void setUsername(String username) {
		welcomeLabel.setText("Welcome, " + username + "!");
	}

	private List<String> getTop5PopularBooks() {
		List<String> books = new ArrayList<>();
		String url = "jdbc:sqlite:readingroom.db"; // Update with your DB path

		String sql = "SELECT title FROM books ORDER BY sold DESC LIMIT 5";

		try (Connection conn = DriverManager.getConnection(url);
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {

			while (rs.next()) {
				books.add(rs.getString("title"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return books;
	}

	@FXML
	public void listAllBooks(ActionEvent event) {
		List<String> books = new ArrayList<>();
		String url = "jdbc:sqlite:readingroom.db"; // Update with your DB path

		String sql = "SELECT title FROM books";

		try (Connection conn = DriverManager.getConnection(url);
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {

			while (rs.next()) {
				books.add(rs.getString("title"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		populateBooks(books);
		// popularBooksList.getItems().clear();
		// popularBooksList.getItems().addAll(books);
	}

	@FXML
	public void searchBooks(ActionEvent event) {
		String query = inputsearch.getText();
		if (!query.isEmpty()) {
			List<String> searchResults = searchBooksInDatabase(query);
			//popularBooksList.getItems().clear(); // Clear previous results
			//popularBooksList.getItems().addAll(searchResults);
			populateBooks(searchResults);
		}
	}

	private List<String> searchBooksInDatabase(String query) {
		List<String> books = new ArrayList<>();
		String url = "jdbc:sqlite:readingroom.db"; // Update with your DB path

		String sql = "SELECT title FROM books WHERE title LIKE ?";

		try (Connection conn = DriverManager.getConnection(url); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, "%" + query + "%");
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				books.add(rs.getString("title"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return books;
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

//	public void goToExport() {
//		System.out.println("Export button clicked!");
//		try {
//			Parent exportPage = FXMLLoader.load(getClass().getResource("/Views/Export.fxml"));
//			Stage stage = (Stage) btnexport.getScene().getWindow();
//			stage.setScene(new Scene(exportPage));
//			stage.show();
//		} catch (Exception e) {
//			System.err.println("Failed to load export page: " + e.getMessage());
//			e.printStackTrace();
//		}
//	}
	
//	public void goToOrders() {
//	    System.out.println("Viewing all orders.");
//	    try {
//	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ViewOrder.fxml"));
//	        Parent orderDetailsPage = loader.load();
//	        Stage stage = (Stage) btnview.getScene().getWindow();
//	        stage.setScene(new Scene(orderDetailsPage));
//	        stage.show();
//	    } catch (Exception e) {
//	        System.err.println("Failed to load order details page: " + e.getMessage());
//	        e.printStackTrace();
//	    }
//	}

	public void goToCart() {
		System.out.println("Cart button clicked!");
		try {
			//Parent cartPage = FXMLLoader.load(getClass().getResource("/Views/Cart.fxml"));
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Cart.fxml"));
	        Parent cartPage = loader.load();
	        CartController cartController = loader.getController();
	        cartController.setShoppingCart(shoppingCart);
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
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Checkout.fxml")); // Create a new FXMLLoader instance
	        Parent checkoutPage = loader.load();
			//Parent checkoutPage = FXMLLoader.load(getClass().getResource("/Views/Checkout.fxml"));
			CheckoutController checkoutController = loader.getController();
            checkoutController.setShoppingCart(shoppingCart);
			Stage stage = (Stage) btncheck.getScene().getWindow();
			stage.setScene(new Scene(checkoutPage));
			stage.show();
		} catch (Exception e) {
			System.err.println("Failed to load cart page: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void goToEdit() {
		System.out.println("Cart button clicked!");
		try {
			Parent cartPage = FXMLLoader.load(getClass().getResource("/Views/EditProfile.fxml"));
			Stage stage = (Stage) btnedit.getScene().getWindow();
			stage.setScene(new Scene(cartPage));
			stage.show();
		} catch (Exception e) {
			System.err.println("Failed to load cart page: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void goToOrderDetails() {
		System.out.println("Cart button clicked!");
		try {
			Parent cartPage = FXMLLoader.load(getClass().getResource("/Views/OrderView.fxml"));
			Stage stage = (Stage) btndetails.getScene().getWindow();
			stage.setScene(new Scene(cartPage));
			stage.show();
		} catch (Exception e) {
			System.err.println("Failed to load cart page: " + e.getMessage());
			e.printStackTrace();
		}
	}
}

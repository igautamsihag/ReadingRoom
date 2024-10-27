package Controllers;

// importing all the necessary required libraries for this dash board controller
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
	// shopping cart instance to hold books in the cart
	private ShoppingCart shoppingCart; 

	// initializing the shopping cart object using Dash board controller constructor method
    public DashboardController() {
        shoppingCart = new ShoppingCart(); 
    }

    // FXML statement to link the UI components
 	// button for log out
	@FXML
	private Button btnlogout;

	// button for cart page
	@FXML
	private Button btncart;

	// button for checkout page
	@FXML
	private Button btncheck;

	// button for edit profile page
	@FXML
	private Button btnedit;

	// button for implementing search books
	@FXML
	private Button btnsearch;

	// label for displaying welcome message
	@FXML
	private Label welcomeLabel;

	// text field for book title input
	@FXML
	private TextField inputsearch;

	// v box for displaying popular books
	@FXML
	private VBox popularBooksList;

	// button for listing all books
	@FXML
	private Button btnlist;
	
	// button for order details page
	@FXML
	private Button btndetails;
	
	// label for displaying error messages
	@FXML
	private Label errorlabel;
	
	// variable for setting the current user id
	private int currentUserId;

	// method to display top 5 famous books in the dash board page
	@FXML
	public void initialize() {
		List<String> popularBooks = getFamousBooks();
		displayBooks(popularBooks);
	}

	// method to load items from cart items table based on the user id 
	public void setCurrentUserId(int userId) {
	    this.currentUserId = userId;
	    loadUserCart(); 
	}
	
	// method to load any items available in the cart for the user
	public void loadUserCart() {
		
		// String variable DATABASE_URL to declare the database file location
	    String DATABASE_URL = "jdbc:sqlite:readingroom.db";
	    
	    // defining the SQL statement to fetch the book items in the database
	    String getItemSQLStatement = "SELECT book_id, quantity, price FROM cart_items WHERE user_id = ?";
	    
	    // establishing a database connection to get items
	    try (Connection conn = DriverManager.getConnection(DATABASE_URL);
	         PreparedStatement pstmt = conn.prepareStatement(getItemSQLStatement)) {
	        
	    	// executing SQL statements
	        pstmt.setInt(1, currentUserId);
	        ResultSet rs = pstmt.executeQuery();
	        
	        // using  a while loop to get the fetch the  book items
	        while (rs.next()) {
	        	int bookId = rs.getInt("book_id");
	        	String title = getBookTitle(bookId);
	            int quantity = rs.getInt("quantity");
	            double price = rs.getDouble("price");
	            int availableStock = getAvailableStock(title);
	            
	            // Adding the item to the shopping cart
	            shoppingCart.addItem(new CartItem(title, quantity, price, availableStock));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	// method to get the book title based on the book id
	private String getBookTitle(int bookId) {
		
		// String variable DATABASE_URL to declare the database file location
	    String DATABASE_URL = "jdbc:sqlite:readingroom.db";
	    
	    // defining the SQL statement to get the book title from the database
	    String bookTitleSQLStatement = "SELECT title FROM books WHERE book_id = ?";
	    
	    // establishing a database connection to fetch book title
	    try (Connection conn = DriverManager.getConnection(DATABASE_URL);
	         PreparedStatement pstmt = conn.prepareStatement(bookTitleSQLStatement )) {
	    	
	    	// executing the statement
	        pstmt.setInt(1, bookId);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            return rs.getString("title");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null; 
	}


	// method to save a new item in the cart items database 
	private void addCartItem(CartItem item) {
		
		// String variable DATABASE_URL to declare the database file location
	    String DATABASE_URL = "jdbc:sqlite:readingroom.db";
	    
	    // defining the SQL statement to save the book items in the cart items table
	    String addItemSQLStatement = "INSERT INTO cart_items (user_id, book_id, quantity, price) VALUES (?, ?, ?, ?)";
	    
	    // establishing a database connection to add book item in the cart item table
	    try (Connection conn = DriverManager.getConnection(DATABASE_URL);
	         PreparedStatement pstmt = conn.prepareStatement(addItemSQLStatement)) {
	        
	    	// executing the SQL statement
	        pstmt.setInt(1, currentUserId);
	        pstmt.setInt(2, getBookId(item.getTitle()));
	        pstmt.setInt(3, item.getQuantity());
	        pstmt.setDouble(4, item.getPrice());	        
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	// method to get the book id from the table based on the book title
	private int getBookId(String title) {
		
		// String variable DATABASE_URL to declare the database file location
	    String DATABASE_URL = "jdbc:sqlite:readingroom.db";
	    
	    // defining the SQL statement to save the book items in the cart items table
	    String bookIdSQLStatement = "SELECT book_id FROM books WHERE title = ?";
	    
	    // establishing a database connection to get book id from the table
	    try (Connection conn = DriverManager.getConnection(DATABASE_URL);
	         PreparedStatement pstmt = conn.prepareStatement(bookIdSQLStatement)) {
	    	
	    	// executing the SQL statement
	        pstmt.setString(1, title);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            return rs.getInt("book_id");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return 0; 
	}

	// method to display a list of books in HBOX
	private void displayBooks(List<String> books) {
		
		// Clearing all the previous books in the list and error messages
		popularBooksList.getChildren().clear();
		errorlabel.setText("");
		
		// using a for loop to iterate over each book in the array list to display them in the H box
		for (String title : books) {
			// creating a H box to display a book row consisting its details
			HBox bookRow = new HBox(10); 
			// creating a label for displaying book title
			Label bookTitle = new Label(title);
			// creating a text field for the user to enter the quantity of the book
			TextField quantityField = new TextField();
			// creating a buy button
			Button buyButton = new Button("Buy");
			
			// calling the method to get available quantity of the book
			int availableStock = getAvailableStock(title);
			buyButton.setOnAction(event -> {
				String quantityText = quantityField.getText();
			
				int bookquantity = Integer.parseInt(quantityText.isEmpty() ? "0" : quantityText);
				if (bookquantity > availableStock) {
					errorlabel.setText("There is only " + availableStock + " stock left for " + title + " !!");
	                return; 
	            }
				// adding the items to the cart item
                double price = getBookPrice(title); 
                CartItem item = new CartItem(title, bookquantity, price, availableStock);
                shoppingCart.addItem(item);
                addCartItem(item);
			});

			bookRow.getChildren().addAll(bookTitle, quantityField, buyButton);
			popularBooksList.getChildren().add(bookRow);
		}
	}
	
	private int getAvailableStock(String title) {
		
		// String variable DATABASE_URL to declare the database file location
	    String DATABASE_URL = "jdbc:sqlite:readingroom.db"; 
	    
	    // defining the SQL statement to get the book stock from the table
	    String getStockSQLStatement = "SELECT quantity FROM books WHERE title = ?"; 

	    // establishing the database connection and executing the sql statement
	    try (Connection conn = DriverManager.getConnection(DATABASE_URL);
	         PreparedStatement pstmt = conn.prepareStatement(getStockSQLStatement)) {

	        pstmt.setString(1, title);
	        ResultSet rs = pstmt.executeQuery();

	        if (rs.next()) {
	            return rs.getInt("quantity"); 
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return 0;
	}
	
	private double getBookPrice(String title) {
		
		// String variable DATABASE_URL to declare the database file location
		String DATABASE_URL = "jdbc:sqlite:readingroom.db"; 
		
		// defining the SQL statement to get the book price from the table
	    String bookPriceSQLStatement = "SELECT price FROM books WHERE title = ?";

	    // establishing the database connection and executing the sql statement
	    try (Connection conn = DriverManager.getConnection(DATABASE_URL);
	         PreparedStatement pstmt = conn.prepareStatement(bookPriceSQLStatement)) {

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

	// method to display the first name in the welcome label
	public void setFirstName(String firstName) {
	    welcomeLabel.setText("Welcome, " + firstName + "!");
	}


	// method to retrieve top 5 popular books based on sold copies
	private List<String> getFamousBooks() {
		
		// creating a new arrayList to store top 5 popular books
		List<String> books = new ArrayList<>();
		
		// String variable DATABASE_URL to declare the database file location
		String DATABASE_URL = "jdbc:sqlite:readingroom.db"; 

		// defining the SQL statement to get top 5 the books from the table
		String famousBookSQLStatement = "SELECT title FROM books ORDER BY sold DESC LIMIT 5";

		// establishing the database connection and executing the sql statement
		try (Connection conn = DriverManager.getConnection(DATABASE_URL);
				PreparedStatement pstmt = conn.prepareStatement(famousBookSQLStatement);
				ResultSet rs = pstmt.executeQuery()) {

			// using a while loop to get famous book and adding them to the arrayList
			while (rs.next()) {
				books.add(rs.getString("title"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return books;
	}

	// method to retrieve all books
	@FXML
	public void listAllBooks(ActionEvent event) {
		// creating a new arrayList to store all books
		List<String> books = new ArrayList<>();
		
		// String variable DATABASE_URL to declare the database file location
		String DATABASE_URL = "jdbc:sqlite:readingroom.db"; 

		// defining the SQL statement to list all the books from the table
		String getBookSQLStatement = "SELECT title FROM books";

		// establishing the database connection and executing the query
		try (Connection conn = DriverManager.getConnection(DATABASE_URL);
				PreparedStatement pstmt = conn.prepareStatement(getBookSQLStatement);
				ResultSet rs = pstmt.executeQuery()) {

			// using a while loop to get books and adding them to the arrayList
			while (rs.next()) {
				books.add(rs.getString("title"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// calling the displayBooks method to display all the books
		displayBooks(books);
	}

	// method to access books from the user search input
	@FXML
	public void searchBooks(ActionEvent event) {
		// storing the search input entered by the user in a string variable
		String searchEnter = inputsearch.getText();
		if (!searchEnter.isEmpty()) {
			List<String> searchResults = searchBooksFromTable(searchEnter);
			// calling the displayBooks method to display searched books
			displayBooks(searchResults);
		}
	}

	// method to search book from the database
	private List<String> searchBooksFromTable(String searchEnter) {
		
		// creating a new arrayList to store all searched books
		List<String> books = new ArrayList<>();
		
		// String variable DATABASE_URL to declare the database file location
		String DATABASE_URL = "jdbc:sqlite:readingroom.db"; 

		// defining the SQL statement to search the books from the table
		String searchBookSQLStatement = "SELECT title FROM books WHERE title LIKE ?";

		// establishing the database connection and executing the query
		try (Connection conn = DriverManager.getConnection(DATABASE_URL);
				PreparedStatement pstmt = conn.prepareStatement(searchBookSQLStatement)) {

			pstmt.setString(1, "%" + searchEnter + "%");
			ResultSet rs = pstmt.executeQuery();

			// using a while loop to add matching title books in the arrayList
			while (rs.next()) {
				books.add(rs.getString("title"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return books;
	}

	//  method to navigate the user to the log in page when logout button is clicked
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

	//  method to navigate the user to the cart page when cart button is clicked
	public void goToCart() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Cart.fxml"));
	        Parent cartPage = loader.load();
	        CartController cartController = loader.getController();
	        // passing the shopping cart object to the cart controller for accessing details
	        cartController.setShoppingCart(shoppingCart);
			Stage stage = (Stage) btncart.getScene().getWindow();
			stage.setScene(new Scene(cartPage));
			stage.show();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	//  method to navigate the user to the checkout page when checkout button is clicked
	public void goToCheckout() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Checkout.fxml")); 
	        Parent checkoutPage = loader.load();
			CheckoutController checkoutController = loader.getController();
			// passing the shopping cart object to the checkout controller for accessing details
            checkoutController.setShoppingCart(shoppingCart);
			Stage stage = (Stage) btncheck.getScene().getWindow();
			stage.setScene(new Scene(checkoutPage));
			stage.show();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	//  method to navigate the user to the edit profile page when edit button is clicked
	public void goToEdit() {
		try {
			Parent editPage = FXMLLoader.load(getClass().getResource("/Views/EditProfile.fxml"));
			Stage stage = (Stage) btnedit.getScene().getWindow();
			stage.setScene(new Scene(editPage));
			stage.show();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	//  method to navigate the user to the order history page when view order details button is clicked
	public void goToOrderDetails() {
		try {
			Parent orderDetailsPage = FXMLLoader.load(getClass().getResource("/Views/OrderView.fxml"));
			Stage stage = (Stage) btndetails.getScene().getWindow();
			stage.setScene(new Scene(orderDetailsPage));
			stage.show();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}
}

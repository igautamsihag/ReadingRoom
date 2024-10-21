package Controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
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
    private Button btnedit;
    
    @FXML
    private Button btnsearch;
    
    @FXML
    private Label welcomeLabel;
    
    @FXML
    private TextField inputsearch;
    
    @FXML
    private ListView<String> popularBooksList;
    
    @FXML
    private Button btnlist;
    
    @FXML
    public void initialize() {
        System.out.println("Initializing DashboardController");
        
        // Check if buttons are properly initialized
        System.out.println("btnlogout: " + (btnlogout == null ? "null" : "initialized"));
        System.out.println("btnexport: " + (btnexport == null ? "null" : "initialized"));
        //System.out.println("btncart: " + (btncart == null ? "null" : "initialized"));

        // Load the top 5 popular books
        List<String> popularBooks = getTop5PopularBooks();
        popularBooksList.getItems().addAll(popularBooks);
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

        popularBooksList.getItems().clear();
        popularBooksList.getItems().addAll(books);
    }
    @FXML
    public void searchBooks(ActionEvent event) {
        String query = inputsearch.getText();
        if (!query.isEmpty()) {
            List<String> searchResults = searchBooksInDatabase(query);
            popularBooksList.getItems().clear(); // Clear previous results
            popularBooksList.getItems().addAll(searchResults);
        }
    } 
    
    private List<String> searchBooksInDatabase(String query) {
        List<String> books = new ArrayList<>();
        String url = "jdbc:sqlite:readingroom.db"; // Update with your DB path

        String sql = "SELECT title FROM books WHERE title LIKE ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
        	
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
}

package Controllers;

// Importing necessary libraries for admin dashboard controller
import Model.Book;
import Model.BookRetrieve;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.util.List;

public class AdminDashboardController {

	@FXML
	private Button btnlogout;
    @FXML
    private TableView<Book> booksTable;
    @FXML
    private TableColumn<Book, String> titleColumn;
    @FXML
    private TableColumn<Book, String> authorColumn;
    @FXML
    private TableColumn<Book, Integer> priceColumn;
    @FXML
    private TableColumn<Book, Integer> quantityColumn;
    @FXML
    private TableColumn<Book, Integer> soldColumn;

    private ObservableList<Book> bookList;
    private BookRetrieve bookDAO;

    @FXML
    public void initialize() {
    	bookDAO = new BookRetrieve();
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        soldColumn.setCellValueFactory(new PropertyValueFactory<>("sold"));
        
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        soldColumn.setCellValueFactory(new PropertyValueFactory<>("sold"));

        // Make the quantity column editable
        quantityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        quantityColumn.setOnEditCommit(event -> {
            Book book = event.getRowValue();
            int newQuantity = event.getNewValue();
            book.setQuantity(newQuantity);
            updateBookQuantity(book);
        });

        booksTable.setEditable(true);
        loadBooks();
    }

    private void loadBooks() {
        BookRetrieve bookDAO = new BookRetrieve();
        List<Book> books = bookDAO.getBooks();
        bookList = FXCollections.observableArrayList(books);
        booksTable.setItems(bookList);
    }
    
    private void updateBookQuantity(Book book) {
        boolean success = bookDAO.updateBookQuantity(book);
        if (success) {
            System.out.println("Book quantity updated successfully");
        } else {
            System.out.println("Failed to update book quantity");
            // Optionally, you can show an alert to the user
            showAlert("Update Failed", "Failed to update book quantity. Please try again.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
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
}

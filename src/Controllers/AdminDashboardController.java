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
	
	// FXML statement to link the UI components
	// button for log out
	@FXML
	private Button btnlogout;
	// creating a table to display every book and corresponding details in a table
    @FXML
    private TableView<Book> booksTable;
    
    // columns for individual book details such as title, author, price, quantity and sold copies
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

    
    // (Matiullah KarimiMatiullah Karimi 1 et al., What is the difference between arraylist and observablelist? 1962)
    // creating an instance of Book Retrieve to communicate with the database and an observable list to hold the book data
    private ObservableList<Book> bookList;
    private BookRetrieve bookDAO;

    // (Property Value Factory 2019)
    // using a set cell value factory because it will connect each cell by each book detail and display it in the table cell
    @FXML
    public void initialize() {
    	// initializing a book retrieve instance and loading up the book details in corresponding table columns
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

        // Making the quantity column editable upon mouse click 
        // (sifsif 208k1616 gold badges307307 silver badges340340 bronze badges, Make individual cell editable in javafx tableview 1960)
        quantityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        quantityColumn.setOnEditCommit(event -> {
            Book book = event.getRowValue();
            int newQuantity = event.getNewValue();
            book.setQuantity(newQuantity);
            // calling the updateBookQuantity method to update the book quantity
            updateBookQuantity(book);
        });

        booksTable.setEditable(true);
        loadBooks();
    }

    // method to load the books using book retrieve instance and display them in a table
    private void loadBooks() {
        BookRetrieve bookDAO = new BookRetrieve();
        // Retrieving the books and loading them in the table
        List<Book> books = bookDAO.getBooks();
        bookList = FXCollections.observableArrayList(books);
        booksTable.setItems(bookList);
    }
    
    // method to update the quantity of the book by the admin
    private void updateBookQuantity(Book book) {
        boolean bookUpdated = bookDAO.updateBookQuantity(book);
        if (bookUpdated) {
            System.out.println("Book quantity in the database is updated successfully");
        } else {
            showAlert("Update Failed", "Please try again. Cannot update book quantity");
        }
    }

    // show alert method that creates alert dialog and displays an error message
    private void showAlert(String title, String content) {		// 	(Alert JavaFX 2015)
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    // method to navigate the administrator back to the log in page
    public void goToLogOut() {
        System.out.println("Logout button clicked by the admin");
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
}

// References

// Alert JavaFX (2015) Alert (javafx 8). Available at: https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/Alert.html (Accessed: 18 October 2024). 
// Matiullah KarimiMatiullah Karimi 1 et al. (1962) What is the difference between arraylist and observablelist?, Stack Overflow. Available at: https://stackoverflow.com/questions/41920217/what-is-the-difference-between-arraylist-and-observablelist (Accessed: 18 October 2024). 
// Property Value Factory (2019) Propertyvaluefactory (javafx 12). Available at: https://openjfx.io/javadoc/12/javafx.controls/javafx/scene/control/cell/PropertyValueFactory.html (Accessed: 14 October 2024). 
// sifsif 4911 gold badge11 silver badge22 bronze badges and James_DJames_D 208k1616 gold badges307307 silver badges340340 bronze badges (1960) Make individual cell editable in javafx tableview, Stack Overflow. Available at: https://stackoverflow.com/questions/28414825/make-individual-cell-editable-in-javafx-tableview (Accessed: 17 October 2024). 
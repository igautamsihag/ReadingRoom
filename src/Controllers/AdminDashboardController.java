package Controllers;

// Importing necessary libraries for admin dashboard controller
import Model.Book;
import Model.BookRetrieve;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class AdminDashboardController {

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

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        soldColumn.setCellValueFactory(new PropertyValueFactory<>("sold"));
        loadBooks();
    }

    private void loadBooks() {
        BookRetrieve bookDAO = new BookRetrieve();
        List<Book> books = bookDAO.getBooks();
        bookList = FXCollections.observableArrayList(books);
        booksTable.setItems(bookList);
    }
}

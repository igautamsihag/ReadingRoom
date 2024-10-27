package Controllers;

// importing all the necessary required libraries for this class
import Model.OrderDetail;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class OrderDetailController {

	// FXML statement to link the UI components
	// table to display the list of order details
    @FXML
    private TableView<OrderDetail> orderDetails;

    // column to display the book id
    @FXML
    private TableColumn<OrderDetail, Integer> bookId;

    // column to display the book quantity
    @FXML
    private TableColumn<OrderDetail, Integer> quantity;

    // column to display the price of book
    @FXML
    private TableColumn<OrderDetail, Double> price;

    // (Property Value Factory 2019)
    // using a set cell value factory because it will connect each cell by each book detail and display it in the table cell
    public void initialize() {
        bookId.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    // method to load the order details to the table 
    public void loadOrderDetails(List<OrderDetail> details) {
        orderDetails.getItems().addAll(details);
    }
}

// REFERENCES

// Property Value Factory (2019) Propertyvaluefactory (javafx 12). Available at: https://openjfx.io/javadoc/12/javafx.controls/javafx/scene/control/cell/PropertyValueFactory.html (Accessed: 14 October 2024). 
package Controllers;

import Model.OrderDetail;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class OrderDetailController {

    @FXML
    private TableView<OrderDetail> orderDetailTable;

    @FXML
    private TableColumn<OrderDetail, Integer> bookIdColumn;

    @FXML
    private TableColumn<OrderDetail, Integer> quantityColumn;

    @FXML
    private TableColumn<OrderDetail, Double> priceColumn;

    public void initialize() {
        bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    public void loadOrderDetails(List<OrderDetail> details) {
        orderDetailTable.getItems().addAll(details);
    }
}

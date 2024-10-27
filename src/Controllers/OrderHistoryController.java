package Controllers;
//importing all the necessary required libraries for this order history controller
import Model.Order;
import Model.OrderDetail;
import Model.CurrentSession;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.stage.FileChooser;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class OrderHistoryController {

	// FXML statement to link the UI components
	// table to display the list of orders
    @FXML
    private TableView<Order> orderHistory;

    // column to display the order id
    @FXML
    private TableColumn<Order, Integer> orderId;

    // column to display the order date
    @FXML
    private TableColumn<Order, String> orderDate;

    // column to display the order price
    @FXML
    private TableColumn<Order, Double> totalPrice;

    // column to display the order details
    @FXML
    private TableColumn<Order, Void> view; 

    // column to export order 
    @FXML
    private TableColumn<Order, Void> export; 

    // button for log out
    @FXML
    private Button btnLogout;

    // button for dash board
    @FXML
    private Button btnDashboard;

    // (Property Value Factory 2019)
    // using a set cell value factory because it will connect each cell by each book detail and display it in the table cell
    @FXML
    public void initialize() {
        orderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        orderDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        totalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        // creating the view and export column as click able		// (Hope, 2018)
        view.setCellFactory(col -> new TableCell<Order, Void>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setText("View");
                    setOnMouseClicked(e -> {
                        Order order = getTableView().getItems().get(getIndex());
                        viewOrderDetails(order);
                    });
                }
            }
        });

        // creating button in each row for export column		// (Hope, 2018)
        export.setCellFactory(col -> new TableCell<Order, Void>() {
            private final Button exportButton = new Button("Export");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(exportButton);
                    Order order = getTableView().getItems().get(getIndex());
                    exportButton.setOnAction(e -> downloadOrder(order));
                }
            }
        });

        accessOrders();
    }

    // method to retrieve all the orders of the user using getOrders method and storing them in a list
    private void accessOrders() {
        List<Order> orders = getOrders();
        orderHistory.getItems().addAll(orders);
    }

    // method to get orders from the database 
    private List<Order> getOrders() {
    	
    	// defining a list to store orders
        List<Order> orders = new ArrayList<>();
        
        // String variable DATABASE_URL to declare the database file location
        String DATABASE_URL = "jdbc:sqlite:readingroom.db"; 
        
        // defining the SQL statement to retrieve the orders from the database
        String getOrderSQLStatement = "SELECT id, order_date, total_price FROM orders WHERE user_id = ?";

        // establishing the database connection and executing the query
        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(getOrderSQLStatement)) {
            pstmt.setInt(1, getUserId());
            ResultSet rs = pstmt.executeQuery();
            // using a while loop to iterate over each order feature and adding them in a list
            while (rs.next()) {
                int orderId = rs.getInt("id");
                String orderDate = rs.getString("order_date");
                double totalPrice = rs.getDouble("total_price");
                orders.add(new Order(orderId, orderDate, totalPrice));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return orders;
    }

    // method to get the user id
    private int getUserId() {
        return CurrentSession.getInstance().getCurrentUser().getID();
    }

    // method to get order details from the database 
    private List<OrderDetail> getOrderDetails(int orderId) {
    	// defining a list to store order details
        List<OrderDetail> orderDetails = new ArrayList<>();
        
        // String variable DATABASE_URL to declare the database file location
        String DATABASE_URL = "jdbc:sqlite:readingroom.db";
        
        // defining the SQL statement to retrieve the order details from the database
        String orderDetailSQLStatement = "SELECT book_id, quantity, price FROM order_details WHERE order_id = ?";

        // establishing the database connection and executing the query
        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(orderDetailSQLStatement)) {
            pstmt.setInt(1, orderId);
            ResultSet rs = pstmt.executeQuery();
            // using a while loop to iterate over each order feature and adding them in a list
            while (rs.next()) {
                int bookId = rs.getInt("book_id");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");
                orderDetails.add(new OrderDetail(orderId, bookId, quantity, price));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching order details: " + e.getMessage());
        }

        return orderDetails;
    }

    private void viewOrderDetails(Order order) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/OrderDetailView.fxml"));
            Parent root = loader.load();
            OrderDetailController controller = loader.getController();
            List<OrderDetail> orderDetails = getOrderDetails(order.getOrderId());
            controller.loadOrderDetails(orderDetails);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    // method to download the order details in csv format
    // (JavaFX | FileChooser Class, 2018)
    private void downloadOrder(Order order) {
        // creating a file chooser object for asking user to specify location
        FileChooser fc = new FileChooser();
        fc.setTitle("Download order");
        // defining the file order as csv
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fc.showSaveDialog(btnLogout.getScene().getWindow());

        if (file != null) {
        	// using a file writer to write the order info in the csv file
            try (FileWriter writer = new FileWriter(file)) {
                writer.append("Order ID,Order Date,Total Price,Book ID,Quantity,Price\n");
                List<OrderDetail> orderDetails = getOrderDetails(order.getOrderId());
                for (OrderDetail detail : orderDetails) {
                    writer.append(order.getOrderId() + ",");
                    writer.append(order.getOrderDate() + ",");
                    writer.append(order.getTotalPrice() + ",");
                    writer.append(detail.getBookId() + ",");
                    writer.append(detail.getQuantity() + ",");
                    writer.append(detail.getPrice() + "\n");
                }
                System.out.println("File downloaded to " + file.getAbsolutePath());
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        } else {
            System.out.println("Error. try again.");
        }
    }

    // method to navigate user to the log in page
    public void goToLogout() {
        try {
            Parent loginPage = FXMLLoader.load(getClass().getResource("/Views/Login.fxml"));
            Stage stage = (Stage) btnLogout.getScene().getWindow();
            stage.setScene(new Scene(loginPage));
            stage.show();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    // method to navigate user to the dash board page
    public void goToDashboard() {
        try {
            Parent dashboardPage = FXMLLoader.load(getClass().getResource("/Views/DashboardView.fxml"));
            Stage stage = (Stage) btnDashboard.getScene().getWindow();
            stage.setScene(new Scene(dashboardPage));
            stage.show();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}

// REFERENCES

// Property Value Factory (2019) Propertyvaluefactory (javafx 12). Available at: https://openjfx.io/javadoc/12/javafx.controls/javafx/scene/control/cell/PropertyValueFactory.html (Accessed: 14 October 2024). 
// Hope, D. (2018) JavaFX - Put a control (like a button) in a TableCell?, Stack Overflow. Available at: https://stackoverflow.com/questions/48714816/javafx-put-a-control-like-a-button-in-a-tablecell (Accessed: 23 October 2024).
// JavaFX | FileChooser Class (2018) GeeksforGeeks. Available at: https://www.geeksforgeeks.org/javafx-filechooser-class/.
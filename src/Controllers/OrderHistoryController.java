package Controllers;

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

    @FXML
    private TableView<Order> orderTable;

    @FXML
    private TableColumn<Order, Integer> orderIdColumn;

    @FXML
    private TableColumn<Order, String> orderDateColumn;

    @FXML
    private TableColumn<Order, Double> totalPriceColumn;

    @FXML
    private TableColumn<Order, Void> viewColumn; // Change to Void

    @FXML
    private TableColumn<Order, Void> exportColumn; // Change to Void

    @FXML
    private Button btnLogout;

    @FXML
    private Button btnDashboard;

    @FXML
    public void initialize() {
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        orderDateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        // Create clickable view column
        viewColumn.setCellFactory(col -> new TableCell<Order, Void>() {
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

        // Create export button in each row
        exportColumn.setCellFactory(col -> new TableCell<Order, Void>() {
            private final Button exportButton = new Button("Export");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(exportButton);
                    Order order = getTableView().getItems().get(getIndex());
                    exportButton.setOnAction(e -> exportOrder(order));
                }
            }
        });

        loadOrders();
    }

    private void loadOrders() {
        List<Order> orders = getOrdersForCurrentUser();
        orderTable.getItems().addAll(orders);
    }

    private List<Order> getOrdersForCurrentUser() {
        List<Order> orders = new ArrayList<>();
        String url = "jdbc:sqlite:readingroom.db"; // Your database URL
        String query = "SELECT id, order_date, total_price FROM orders WHERE user_id = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, getCurrentUserId());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int orderId = rs.getInt("id");
                String orderDate = rs.getString("order_date");
                double totalPrice = rs.getDouble("total_price");
                orders.add(new Order(orderId, orderDate, totalPrice));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching orders: " + e.getMessage());
        }

        return orders;
    }

    private int getCurrentUserId() {
        // Get the current user ID
        return CurrentSession.getInstance().getCurrentUser().getID();
    }

    private List<OrderDetail> getOrderDetails(int orderId) {
        List<OrderDetail> details = new ArrayList<>();
        String url = "jdbc:sqlite:readingroom.db";
        String query = "SELECT book_id, quantity, price FROM order_details WHERE order_id = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, orderId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int bookId = rs.getInt("book_id");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");
                details.add(new OrderDetail(orderId, bookId, quantity, price));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching order details: " + e.getMessage());
        }

        return details;
    }

    private void viewOrderDetails(Order order) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/OrderDetailView.fxml"));
            Parent root = loader.load();

            OrderDetailController controller = loader.getController();
            List<OrderDetail> details = getOrderDetails(order.getOrderId());
            controller.loadOrderDetails(details);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            System.err.println("Failed to load order detail view: " + e.getMessage());
        }
    }

    private void exportOrder(Order order) {
        // Prompt user for file location
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Order as CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(btnLogout.getScene().getWindow());

        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                // Write header
                writer.append("Order ID,Order Date,Total Price,Book ID,Quantity,Price\n");
                List<OrderDetail> details = getOrderDetails(order.getOrderId());
                for (OrderDetail detail : details) {
                    writer.append(order.getOrderId() + ",");
                    writer.append(order.getOrderDate() + ",");
                    writer.append(order.getTotalPrice() + ",");
                    writer.append(detail.getBookId() + ",");
                    writer.append(detail.getQuantity() + ",");
                    writer.append(detail.getPrice() + "\n");
                }
                System.out.println("Export successful to " + file.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Error writing to file: " + e.getMessage());
            }
        } else {
            System.out.println("Export cancelled.");
        }
    }

    public void goToLogout() {
        System.out.println("Logout button clicked!");
        try {
            Parent loginPage = FXMLLoader.load(getClass().getResource("/Views/Login.fxml"));
            Stage stage = (Stage) btnLogout.getScene().getWindow();
            stage.setScene(new Scene(loginPage));
            stage.show();
        } catch (Exception e) {
            System.err.println("Failed to load Login page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void goToDashboard() {
        System.out.println("Dashboard button clicked!");
        try {
            Parent exportPage = FXMLLoader.load(getClass().getResource("/Views/DashboardView.fxml"));
            Stage stage = (Stage) btnDashboard.getScene().getWindow();
            stage.setScene(new Scene(exportPage));
            stage.show();
        } catch (Exception e) {
            System.err.println("Failed to load dashboard page: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

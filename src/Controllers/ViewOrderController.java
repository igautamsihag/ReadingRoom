package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ViewOrderController {

    @FXML
    private ListView<String> ordersListView;
    @FXML
    private Label orderDetailsLabel;
    @FXML
    private Button btnlogout;
    @FXML
    private Button btndashboard;

    public void initialize() {
        loadAllOrders();
    }

    private void loadAllOrders() {
        String url = "jdbc:sqlite:readingroom.db"; // Your database URL
        String sql = "SELECT id, order_date, total_price FROM orders ORDER BY order_date DESC";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String orderDetail = String.format("Order ID: %d - Date: %s - Total: $%.2f",
                        rs.getInt("id"),
                        rs.getString("order_date"),
                        rs.getDouble("total_price"));
                ordersListView.getItems().add(orderDetail);
            }

            // Load details for the first order by default (if there are any orders)
            if (!ordersListView.getItems().isEmpty()) {
                int firstOrderId = Integer.parseInt(ordersListView.getItems().get(0).split(" - ")[0].split(": ")[1]);
                showOrderItems(firstOrderId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showOrderItems(int orderId) {
        String url = "jdbc:sqlite:readingroom.db"; // Your database URL
        String sqlDetails = "SELECT books.title AS book_title, order_details.quantity AS book_quantity, order_details.price AS book_price " +
                            "FROM order_details " +
                            "INNER JOIN books ON order_details.book_id = books.book_id " +
                            "WHERE order_details.order_id = ?";

        StringBuilder details = new StringBuilder();
        details.append("Details for Order ID: ").append(orderId).append("\n");

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmtDetails = conn.prepareStatement(sqlDetails)) {

            pstmtDetails.setInt(1, orderId);
            ResultSet rsDetails = pstmtDetails.executeQuery();

            while (rsDetails.next()) {
                details.append(String.format("%s - Quantity: %d - Price: $%.2f\n",
                        rsDetails.getString("book_title"),
                        rsDetails.getInt("book_quantity"),
                        rsDetails.getDouble("book_price")));
            }

            orderDetailsLabel.setText(details.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onOrderSelected() {
        String selectedOrder = ordersListView.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            int orderId = Integer.parseInt(selectedOrder.split(" - ")[0].split(": ")[1]);
            showOrderItems(orderId);
        }
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

	public void goToDashboard() {
		System.out.println("Export button clicked!");
		try {
			Parent exportPage = FXMLLoader.load(getClass().getResource("/Views/DashboardView.fxml"));
			Stage stage = (Stage) btndashboard.getScene().getWindow();
			stage.setScene(new Scene(exportPage));
			stage.show();
		} catch (Exception e) {
			System.err.println("Failed to load export page: " + e.getMessage());
			e.printStackTrace();
		}
	}
}

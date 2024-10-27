package Model;

// Order class that is used as a data structure to manage the user order 
public class Order {
	
	// Order class attribute declaration
	private int orderId;
	private String orderDate;
	private double totalPrice;

	// Order class constructor method
	public Order(int orderId, String orderDate, double totalPrice) {
		this.orderId = orderId;
		this.orderDate = orderDate;
		this.totalPrice = totalPrice;
	}

	// Getter methods of Order class
	
	public int getOrderId() {
		return orderId;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public double getTotalPrice() {
		return totalPrice;
	}
	
	// setter methods of Order class
	
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
}

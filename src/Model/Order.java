package Model;

public class Order {
	private int orderId;
	private String orderDate;
	private double totalPrice;

	// Constructor, getters, and setters
	public Order(int orderId, String orderDate, double totalPrice) {
		this.orderId = orderId;
		this.orderDate = orderDate;
		this.totalPrice = totalPrice;
	}

	public int getOrderId() {
		return orderId;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public double getTotalPrice() {
		return totalPrice;
	}
	
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

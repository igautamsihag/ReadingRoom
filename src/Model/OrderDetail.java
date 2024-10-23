package Model;

public class OrderDetail {
	private int orderId;
	private int bookId;
	private int quantity;
	private double price;

	// Constructor, getters, and setters
	public OrderDetail(int orderId, int bookId, int quantity, double price) {
		this.orderId = orderId;
		this.bookId = bookId;
		this.quantity = quantity;
		this.price = price;
	}

	public int getOrderId() {
		return orderId;
	}

	public int getBookId() {
		return bookId;
	}

	public int getQuantity() {
		return quantity;
	}

	public double getPrice() {
		return price;
	}
}

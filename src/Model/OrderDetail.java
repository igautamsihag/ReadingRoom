package Model;

// Order detail class that is used as a data structure to manage the user each order details
public class OrderDetail {
	
	// Order detail class attribute declaration
	private int orderId;
	private int bookId;
	private int quantity;
	private double price;

	// Order Detail constructor method
	
	public OrderDetail(int orderId, int bookId, int quantity, double price) {
		this.orderId = orderId;
		this.bookId = bookId;
		this.quantity = quantity;
		this.price = price;
	}

	// Order detail getter methods
	
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

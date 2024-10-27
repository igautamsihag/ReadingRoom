package Model;

// Cart item class that is representing a single item of the shopping cart
public class CartItem {
	
	// cart item class attribute declaration
    private String title;
    private int quantity;
    private double price;
    private int availableQuantity; 

    // cart item class constructor method
    public CartItem(String title, int quantity, double price, int availableQuantity) {
        this.title = title;
        this.quantity = quantity;
        this.price = price;
        this.availableQuantity = availableQuantity; 
    }

    // cart item class getter methods
    public String getTitle() {
        return title;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public double getTotalPrice() {
        return price * quantity;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // method to set the available quantity of the book in case book quantity gets updated from the shopping cart page
    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }
}

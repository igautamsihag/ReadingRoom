package Model;

public class CartItem {
    private String title;
    private int quantity;
    private double price;
    private int availableQuantity; // Add this field

    public CartItem(String title, int quantity, double price, int availableQuantity) {
        this.title = title;
        this.quantity = quantity;
        this.price = price;
        this.availableQuantity = availableQuantity; // Initialize available quantity
    }

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

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // New method to get available quantity
    public int getAvailableQuantity() {
        return availableQuantity;
    }

    // Optionally, you can also set available quantity if needed
    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }
}

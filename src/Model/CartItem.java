package Model;

public class CartItem {
    private String title;
    private int quantity;
    private double price;

    public CartItem(String title, int quantity, double price) {
        this.title = title;
        this.quantity = quantity;
        this.price = price;
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
}

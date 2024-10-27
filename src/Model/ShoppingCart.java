package Model;

// importing all the necessary required libraries for this class
import java.util.ArrayList;
import java.util.List;

// Shopping cart class 
public class ShoppingCart {
	
	// shopping cart attribute declaration
    private List<CartItem> items;

    // shopping cart constructor method
    public ShoppingCart() {
        this.items = new ArrayList<>();
    }

    // method to add items in the arrayList called items
    public void addItem(CartItem item) {
        items.add(item);
    }
    
    // method to remove items from the arrayList called items
    public void removeItem(CartItem item) {
        items.remove(item);
    }

    // method to retrieve the items from the arrayList
    public List<CartItem> getItems() {
        return items;
    }

    // method to get the total price of the shopping cart using java stream API		(GeeksforGeeks, Stream maptodouble() in java with examples 2018)
    public double getTotalPrice() {
        return items.stream().mapToDouble(CartItem::getTotalPrice).sum();
    }

    // method to clear all the items from the shopping cart
    public void clear() {
        items.clear();
    }
}

// References

// GeeksforGeeks (2018) Stream maptodouble() in java with examples, GeeksforGeeks. Available at: https://www.geeksforgeeks.org/stream-maptodouble-java-examples/ (Accessed: 15 October 2024). 
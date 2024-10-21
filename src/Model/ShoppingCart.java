package Model;

import java.util.HashMap;
import java.util.Map;

public class ShoppingCart {
    private Map<Book, Integer> cartItems = new HashMap<>();

    public String addBook(Book book, int quantity) {
        if (quantity > book.getQuantity()) {
            return "Not enough stock available!";
        }
        cartItems.put(book, cartItems.getOrDefault(book, 0) + quantity);
        return "Book added to cart.";
    }

    public void updateBookQuantity(Book book, int newQuantity) {
        if (newQuantity > book.getQuantity()) {
            throw new IllegalArgumentException("Not enough stock available!");
        }
        cartItems.put(book, newQuantity);
    }

    public void removeBook(Book book) {
        cartItems.remove(book);
    }

    public Map<Book, Integer> getCartItems() {
        return cartItems;
    }

    public double getTotalPrice() {
        return cartItems.entrySet().stream()
            .mapToDouble(entry -> entry.getKey().getPrice() * entry.getValue())
            .sum();
    }
}


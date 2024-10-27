package Model;

public class Book {

	// book class attribute declaration
	private int bookID;
	private String title;
	private String author;
	private int price;
	private int quantity;
	private int sold;
	
	// book class constructor method
	public Book(String title, String author, int price, int quantity, int sold) {
		this.title = title;
		this.author = author;
		this.price = price;
		this.quantity = quantity;
		this.sold = sold;
	}
	
	// book class setter methods
	public void setBookID(int bookID) {
		this.bookID = bookID;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public void setPrice(int price) {
		this.price = price;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public void setSold(int sold) {
		this.sold = sold;
	}
	
	// book class getter methods
	public int getBookID() {
		return bookID;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public String getTitle() {
		return title;
	}
	
	public int getPrice() {
		return price;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public int getSold() {
		return sold;
	}
	
	
}

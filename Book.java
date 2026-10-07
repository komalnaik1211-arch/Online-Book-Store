import java.io.Serializable;

public class Book implements Serializable {
    private static final long serialVersionUID = 1L;

    private String bookId;
    private String title;
    private String author;
    private String category;
    private double price;
    private int stock;

    public Book(String bookId, String title, String author,
                String category, double price, int stock) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.category = category;
        this.price = price;
        this.stock = stock;
    }

    // Discount slabs: 10% if total >= 2000, 5% if total >= 1000, else none
    public double calculateDiscount(double total) {
        if (total >= 2000) {
            return total * 0.10;
        } else if (total >= 1000) {
            return total * 0.05;
        } else {
            return 0;
        }
    }

    public void reduceStock(int quantity) {
        stock = stock - quantity;
    }

    public String getBookId()   { return bookId; }
    public String getTitle()    { return title; }
    public String getAuthor()   { return author; }
    public String getCategory() { return category; }
    public double getPrice()    { return price; }
    public int getStock()       { return stock; }

    @Override
    public String toString() {
        return "Book ID: " + bookId +
               "\nTitle: " + title +
               "\nAuthor: " + author +
               "\nCategory: " + category +
               "\nPrice: \u20B9" + price +
               "\nStock: " + stock;
    }
}
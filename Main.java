import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Main {
    static BookManager manager = new BookManager();

    public static void main(String[] args) {
        JFrame frame = new JFrame("Online Book Store");
        frame.setSize(600, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JLabel heading = new JLabel("ONLINE BOOK STORE SYSTEM", JLabel.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 20));
        frame.add(heading, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JButton addBtn    = new JButton("ADD BOOK");
        JButton viewBtn   = new JButton("VIEW BOOKS");
        JButton searchBtn = new JButton("SEARCH BOOK");
        JButton buyBtn    = new JButton("BUY BOOK");
        JButton deleteBtn = new JButton("DELETE BOOK");

        addBtn.addActionListener(e -> addBook());
        viewBtn.addActionListener(e -> viewBooks());
        searchBtn.addActionListener(e -> searchBook());
        buyBtn.addActionListener(e -> buyBook());
        deleteBtn.addActionListener(e -> deleteBook());

        panel.add(addBtn);
        panel.add(viewBtn);
        panel.add(searchBtn);
        panel.add(buyBtn);
        panel.add(deleteBtn);

        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    static void addBook() {
        JTextField id = new JTextField();
        JTextField title = new JTextField();
        JTextField author = new JTextField();
        JTextField category = new JTextField();
        JTextField price = new JTextField();
        JTextField stock = new JTextField();

        JPanel form = new JPanel(new GridLayout(6, 2, 5, 5));
        form.add(new JLabel("Book ID"));   form.add(id);
        form.add(new JLabel("Title"));     form.add(title);
        form.add(new JLabel("Author"));    form.add(author);
        form.add(new JLabel("Category"));  form.add(category);
        form.add(new JLabel("Price (\u20B9)")); form.add(price);
        form.add(new JLabel("Stock"));     form.add(stock);

        int result = JOptionPane.showConfirmDialog(null, form, "Add Book",
                JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) return;

        if (id.getText().trim().isEmpty() || title.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Book ID and Title are required");
            return;
        }
        if (manager.findById(id.getText()) != null) {
            JOptionPane.showMessageDialog(null, "Book ID already exists!");
            return;
        }

        try {
            double p = Double.parseDouble(price.getText().trim());
            int s = Integer.parseInt(stock.getText().trim());
            if (p <= 0 || s < 0) {
                JOptionPane.showMessageDialog(null, "Invalid price or stock");
                return;
            }
            Book book = new Book(id.getText().trim(), title.getText().trim(),
                    author.getText().trim(), category.getText().trim(), p, s);
            manager.addBook(book);
            JOptionPane.showMessageDialog(null, "Book added successfully!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Enter valid price and stock");
        }
    }

    static void viewBooks() {
        ArrayList<Book> list = manager.getBooks();
        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No books found");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Book b : list) {
            sb.append(b).append("\n------------------\n");
        }
        showScrollable(sb.toString(), "Book List");
    }

    static void searchBook() {
        String key = JOptionPane.showInputDialog("Enter title or author to search:");
        if (key == null || key.trim().isEmpty()) return;

        ArrayList<Book> found = manager.search(key);
        if (found.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Book not found");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Book b : found) {
            sb.append(b).append("\n------------------\n");
        }
        showScrollable(sb.toString(), "Search Results");
    }

    static void buyBook() {
        String id = JOptionPane.showInputDialog("Enter Book ID to buy:");
        if (id == null) return;

        Book book = manager.findById(id);
        if (book == null) {
            JOptionPane.showMessageDialog(null, "Book not found");
            return;
        }
        if (book.getStock() == 0) {
            JOptionPane.showMessageDialog(null, "Sorry, this book is out of stock");
            return;
        }

        String q = JOptionPane.showInputDialog(book + "\n\nEnter quantity:");
        if (q == null) return;

        try {
            int qty = Integer.parseInt(q.trim());
            if (qty <= 0) {
                JOptionPane.showMessageDialog(null, "Quantity must be at least 1");
                return;
            }
            if (qty > book.getStock()) {
                JOptionPane.showMessageDialog(null,
                        "Only " + book.getStock() + " copies available");
                return;
            }
            double total = book.getPrice() * qty;
            double discount = book.calculateDiscount(total);
            double finalAmount = total - discount;

            book.reduceStock(qty);
            manager.saveBooks();

            JOptionPane.showMessageDialog(null,
                    "Order Placed!\n" +
                    "Book: " + book.getTitle() + "\n" +
                    "Quantity: " + qty + "\n" +
                    "Total: \u20B9" + total + "\n" +
                    "Discount: \u20B9" + discount + "\n" +
                    "Amount to Pay: \u20B9" + finalAmount);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Enter a valid quantity");
        }
    }

    static void deleteBook() {
        String id = JOptionPane.showInputDialog("Enter Book ID to delete:");
        if (id == null) return;

        Book book = manager.findById(id);
        if (book == null) {
            JOptionPane.showMessageDialog(null, "Book not found");
            return;
        }
        int choice = JOptionPane.showConfirmDialog(null,
                book + "\n\nDelete this book?", "Confirm Delete",
                JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            manager.deleteBook(book);
            JOptionPane.showMessageDialog(null, "Book deleted successfully!");
        }
    }

    static void showScrollable(String text, String title) {
        JTextArea area = new JTextArea(text, 15, 30);
        area.setEditable(false);
        JOptionPane.showMessageDialog(null, new JScrollPane(area), title,
                JOptionPane.INFORMATION_MESSAGE);
    }
}
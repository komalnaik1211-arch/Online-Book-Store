import java.io.*;
import java.util.ArrayList;

public class BookManager {
    private static final String FILE_NAME = "books.dat";
    private ArrayList<Book> books;

    public BookManager() {
        loadBooks();
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public void addBook(Book book) {
        books.add(book);
        saveBooks();
    }

    public Book findById(String id) {
        for (Book b : books) {
            if (b.getBookId().equalsIgnoreCase(id.trim())) {
                return b;
            }
        }
        return null;
    }

    // Finds books whose title or author contains the keyword
    public ArrayList<Book> search(String keyword) {
        ArrayList<Book> result = new ArrayList<>();
        String k = keyword.trim().toLowerCase();
        for (Book b : books) {
            if (b.getTitle().toLowerCase().contains(k)
                    || b.getAuthor().toLowerCase().contains(k)) {
                result.add(b);
            }
        }
        return result;
    }

    public void deleteBook(Book book) {
        books.remove(book);
        saveBooks();
    }

    public void saveBooks() {
        try (ObjectOutputStream out =
                 new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(books);
        } catch (IOException e) {
            System.out.println("Error saving books: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void loadBooks() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            books = new ArrayList<>();
            return;
        }
        try (ObjectInputStream in =
                 new ObjectInputStream(new FileInputStream(file))) {
            books = (ArrayList<Book>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading books: " + e.getMessage());
            books = new ArrayList<>();
        }
    }
}
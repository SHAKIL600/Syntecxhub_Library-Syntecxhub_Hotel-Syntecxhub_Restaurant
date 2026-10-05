import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Keeps the list of books in memory and saves it to a text file after every change.
 */
public class Library {
    private final List<Book> books = new ArrayList<>();
    private final Path file;
    private int nextId = 1;

    public Library(String fileName) {
        this.file = Paths.get(fileName);
    }

    /** Loads books from the data file. Returns how many damaged lines were skipped. */
    public int load() throws IOException {
        books.clear();
        nextId = 1;
        int skipped = 0;
        if (!Files.exists(file)) {
            return 0;
        }
        for (String line : Files.readAllLines(file, StandardCharsets.UTF_8)) {
            if (line.isBlank()) {
                continue;
            }
            try {
                Book book = Book.fromFileLine(line);
                books.add(book);
                nextId = Math.max(nextId, book.getId() + 1);
            } catch (IllegalArgumentException e) {
                skipped++;
            }
        }
        return skipped;
    }

    private void save() throws IOException {
        List<String> lines = new ArrayList<>();
        for (Book book : books) {
            lines.add(book.toFileLine());
        }
        Files.write(file, lines, StandardCharsets.UTF_8);
    }

    /** True if a book with the same title and author already exists (ignores upper/lower case). */
    public boolean exists(String title, String author) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title) && book.getAuthor().equalsIgnoreCase(author)) {
                return true;
            }
        }
        return false;
    }

    /** Adds a book and saves. The book stays in memory even if saving fails (IOException is thrown). */
    public Book add(String title, String author) throws IOException {
        Book book = new Book(nextId++, title, author);
        books.add(book);
        save();
        return book;
    }

    public Book findById(int id) {
        for (Book book : books) {
            if (book.getId() == id) {
                return book;
            }
        }
        return null;
    }

    /** Removes the book with this id. Returns false if there is no such book. */
    public boolean remove(int id) throws IOException {
        Book book = findById(id);
        if (book == null) {
            return false;
        }
        books.remove(book);
        save();
        return true;
    }

    /** Case-insensitive search in title and author. */
    public List<Book> search(String keyword) {
        String key = keyword.toLowerCase();
        List<Book> result = new ArrayList<>();
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(key) || book.getAuthor().toLowerCase().contains(key)) {
                result.add(book);
            }
        }
        return result;
    }

    public List<Book> getAll() {
        return new ArrayList<>(books);
    }
}

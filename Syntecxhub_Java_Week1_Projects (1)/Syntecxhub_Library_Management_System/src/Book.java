import java.util.Locale;

/**
 * A single book in the library. Objects are immutable once created.
 */
public class Book {
    private final int id;
    private final String title;
    private final String author;

    public Book(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }

    /** One line of the data file: id|title|author */
    public String toFileLine() {
        return id + "|" + title + "|" + author;
    }

    /** Rebuilds a book from a data-file line; throws IllegalArgumentException if the line is damaged. */
    public static Book fromFileLine(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length != 3) {
            throw new IllegalArgumentException("Bad line: " + line);
        }
        int id = Integer.parseInt(parts[0].trim());
        if (id <= 0 || parts[1].isBlank() || parts[2].isBlank()) {
            throw new IllegalArgumentException("Bad line: " + line);
        }
        return new Book(id, parts[1].trim(), parts[2].trim());
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "%-5d %-35s %s", id, shorten(title, 35), author);
    }

    private static String shorten(String text, int max) {
        return text.length() <= max ? text : text.substring(0, max - 3) + "...";
    }
}

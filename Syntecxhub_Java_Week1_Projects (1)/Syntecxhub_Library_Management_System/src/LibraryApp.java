import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * Command-line Library Management System.
 * Run: java -cp out LibraryApp [datafile]
 */
public class LibraryApp {
    private static final int MAX_TEXT = 100;
    private static final Scanner SC = new Scanner(System.in, StandardCharsets.UTF_8);

    /** Thrown when the input stream ends (Ctrl+D / Ctrl+Z) so the program can exit cleanly. */
    private static class InputEnded extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }

    public static void main(String[] args) {
        String fileName = args.length > 0 ? args[0] : "books.txt";
        Library library = new Library(fileName);

        try {
            int skipped = library.load();
            if (skipped > 0) {
                System.out.println("Warning: " + skipped + " damaged line(s) in " + fileName + " were skipped.");
            }
        } catch (IOException e) {
            System.out.println("Could not read " + fileName + ": " + e.getMessage());
            System.out.println("Starting with an empty library.");
        }

        System.out.println("=== Library Management System ===");
        try {
            boolean running = true;
            while (running) {
                printMenu();
                String choice = ask("Choose an option (1-5): ");
                switch (choice) {
                    case "1": addBook(library); break;
                    case "2": removeBook(library); break;
                    case "3": searchBooks(library); break;
                    case "4": viewAll(library); break;
                    case "5": running = false; break;
                    default: System.out.println("Invalid option. Please enter a number from 1 to 5.");
                }
            }
        } catch (InputEnded e) {
            System.out.println();
        }
        System.out.println("Goodbye!");
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("1. Add a book");
        System.out.println("2. Remove a book");
        System.out.println("3. Search books");
        System.out.println("4. View all books");
        System.out.println("5. Exit");
    }

    private static void addBook(Library library) {
        String title = askRequired("Title: ");
        String author = askRequired("Author: ");
        if (library.exists(title, author)) {
            System.out.println("This book is already in the library.");
            return;
        }
        try {
            Book book = library.add(title, author);
            System.out.println("Added: [" + book.getId() + "] " + book.getTitle() + " by " + book.getAuthor());
        } catch (IOException e) {
            System.out.println("The book was added but could not be saved to the file: " + e.getMessage());
        }
    }

    private static void removeBook(Library library) {
        if (library.getAll().isEmpty()) {
            System.out.println("The library is empty. Nothing to remove.");
            return;
        }
        int id = askInt("Enter the ID of the book to remove: ");
        Book book = library.findById(id);
        if (book == null) {
            System.out.println("No book found with ID " + id + ".");
            return;
        }
        String answer = ask("Remove \"" + book.getTitle() + "\" by " + book.getAuthor() + "? (y/n): ");
        if (!answer.equalsIgnoreCase("y")) {
            System.out.println("Cancelled. The book was not removed.");
            return;
        }
        try {
            library.remove(id);
            System.out.println("Book removed.");
        } catch (IOException e) {
            System.out.println("The book was removed but the file could not be updated: " + e.getMessage());
        }
    }

    private static void searchBooks(Library library) {
        String keyword = askRequired("Search by title or author: ");
        List<Book> found = library.search(keyword);
        if (found.isEmpty()) {
            System.out.println("No books matched \"" + keyword + "\".");
            return;
        }
        System.out.println(found.size() + " book(s) found:");
        printTable(found);
    }

    private static void viewAll(Library library) {
        List<Book> all = library.getAll();
        if (all.isEmpty()) {
            System.out.println("The library has no books yet.");
            return;
        }
        System.out.println("All books (" + all.size() + "):");
        printTable(all);
    }

    private static void printTable(List<Book> books) {
        System.out.println(String.format(Locale.US, "%-5s %-35s %s", "ID", "Title", "Author"));
        System.out.println("-".repeat(70));
        for (Book book : books) {
            System.out.println(book);
        }
    }

    // ---------- input helpers ----------

    /** Prints a prompt and returns the trimmed line. Throws InputEnded at end of input. */
    private static String ask(String prompt) {
        System.out.print(prompt);
        if (!SC.hasNextLine()) {
            throw new InputEnded();
        }
        return SC.nextLine().trim();
    }

    /** Keeps asking until the text is not empty and not too long. The '|' character is replaced because the file uses it as a separator. */
    private static String askRequired(String prompt) {
        while (true) {
            String text = ask(prompt).replace('|', '/');
            if (text.isEmpty()) {
                System.out.println("This field cannot be empty. Please try again.");
            } else if (text.length() > MAX_TEXT) {
                System.out.println("Too long. Please use at most " + MAX_TEXT + " characters.");
            } else {
                return text;
            }
        }
    }

    /** Keeps asking until a whole number is entered. */
    private static int askInt(String prompt) {
        while (true) {
            String text = ask(prompt);
            try {
                return Integer.parseInt(text);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }
}

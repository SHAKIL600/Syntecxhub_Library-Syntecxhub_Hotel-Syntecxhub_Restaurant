import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * Console-based Restaurant Billing System.
 * Run: java -cp out RestaurantApp
 */
public class RestaurantApp {
    private static final int MAX_NAME = 25;
    private static final int MAX_QTY = 100;
    private static final BigDecimal MAX_PRICE = new BigDecimal("100000");
    private static final Scanner SC = new Scanner(System.in, StandardCharsets.UTF_8);

    /** Thrown when the input stream ends so the program can exit cleanly. */
    private static class InputEnded extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }

    public static void main(String[] args) {
        Menu menu = new Menu();
        int billCounter = 1;
        System.out.println("=== Restaurant Billing System ===");
        try {
            boolean running = true;
            while (running) {
                printMenu();
                String choice = ask("Choose an option (1-5): ");
                switch (choice) {
                    case "1": showMenu(menu); break;
                    case "2": addMenuItem(menu); break;
                    case "3": removeMenuItem(menu); break;
                    case "4":
                        if (createBill(menu, billCounter)) {
                            billCounter++;
                        }
                        break;
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
        System.out.println("1. View menu");
        System.out.println("2. Add a menu item");
        System.out.println("3. Remove a menu item");
        System.out.println("4. Create a new bill");
        System.out.println("5. Exit");
    }

    private static void showMenu(Menu menu) {
        if (menu.isEmpty()) {
            System.out.println("The menu is empty. Add items first.");
            return;
        }
        System.out.println(String.format("%-5s %-" + MAX_NAME + "s %10s", "ID", "Item", "Price"));
        System.out.println("-".repeat(42));
        for (MenuItem item : menu.getItems()) {
            System.out.println(String.format(Locale.US, "%-5d %-" + MAX_NAME + "s %10s",
                    item.getId(), item.getName(), Bill.CURRENCY + item.getPrice().toPlainString()));
        }
    }

    private static void addMenuItem(Menu menu) {
        String name = askName("Item name: ");
        if (menu.hasName(name)) {
            System.out.println("An item named \"" + name + "\" is already on the menu.");
            return;
        }
        BigDecimal price = askPrice("Price: ");
        MenuItem item = menu.add(name, price);
        System.out.println("Added: [" + item.getId() + "] " + item.getName() + " at " + Bill.CURRENCY + item.getPrice().toPlainString());
    }

    private static void removeMenuItem(Menu menu) {
        if (menu.isEmpty()) {
            System.out.println("The menu is already empty.");
            return;
        }
        showMenu(menu);
        int id = askInt("Enter the ID of the item to remove: ");
        MenuItem item = menu.findById(id);
        if (item == null) {
            System.out.println("No menu item with ID " + id + ".");
            return;
        }
        String answer = ask("Remove \"" + item.getName() + "\"? (y/n): ");
        if (!answer.equalsIgnoreCase("y")) {
            System.out.println("Cancelled. The item was kept.");
            return;
        }
        menu.remove(id);
        System.out.println("Item removed.");
    }

    /** Takes an order and prints the receipt. Returns true if a bill was printed. */
    private static boolean createBill(Menu menu, int billNumber) {
        if (menu.isEmpty()) {
            System.out.println("The menu is empty. Add items before creating a bill.");
            return false;
        }
        Bill bill = new Bill(billNumber);
        showMenu(menu);
        System.out.println("Enter item IDs to order. Enter 0 when the order is complete.");
        while (true) {
            int id = askInt("Item ID (0 to finish): ");
            if (id == 0) {
                break;
            }
            MenuItem item = menu.findById(id);
            if (item == null) {
                System.out.println("No menu item with ID " + id + ".");
                continue;
            }
            int qty = askQuantity("Quantity of " + item.getName() + ": ");
            bill.addItem(item, qty);
            System.out.println("Added " + qty + " x " + item.getName());
        }
        if (bill.isEmpty()) {
            System.out.println("No items were ordered. No bill created.");
            return false;
        }
        System.out.println();
        System.out.print(bill.toReceipt());
        return true;
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

    private static int askQuantity(String prompt) {
        while (true) {
            int qty = askInt(prompt);
            if (qty < 1 || qty > MAX_QTY) {
                System.out.println("Quantity must be between 1 and " + MAX_QTY + ".");
            } else {
                return qty;
            }
        }
    }

    private static String askName(String prompt) {
        while (true) {
            String name = ask(prompt);
            if (name.isEmpty()) {
                System.out.println("The name cannot be empty. Please try again.");
            } else if (name.length() > MAX_NAME) {
                System.out.println("Too long. Please use at most " + MAX_NAME + " characters.");
            } else {
                return name;
            }
        }
    }

    /** Accepts positive prices with at most 2 decimal places, e.g. 5, 5.5, 5.50. */
    private static BigDecimal askPrice(String prompt) {
        while (true) {
            String text = ask(prompt);
            if (!text.matches("\\d+(\\.\\d{1,2})?")) {
                System.out.println("Enter a valid price with at most 2 decimal places, for example 4.50.");
                continue;
            }
            BigDecimal price = new BigDecimal(text);
            if (price.signum() <= 0) {
                System.out.println("The price must be greater than 0.");
            } else if (price.compareTo(MAX_PRICE) > 0) {
                System.out.println("The price is too high. Maximum is " + MAX_PRICE.toPlainString() + ".");
            } else {
                return price.setScale(2);
            }
        }
    }
}

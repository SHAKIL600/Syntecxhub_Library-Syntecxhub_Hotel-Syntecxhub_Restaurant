import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * The list of menu items. Items can be added and removed while the program runs.
 */
public final class Menu {
    private final List<MenuItem> items = new ArrayList<>();
    private int nextId = 1;

    /** Creates the menu with a few starter items. */
    public Menu() {
        add("Chicken Burger", new BigDecimal("5.50"));
        add("Margherita Pizza", new BigDecimal("8.00"));
        add("Pasta Alfredo", new BigDecimal("7.25"));
        add("French Fries", new BigDecimal("3.00"));
        add("Cold Drink", new BigDecimal("1.50"));
        add("Coffee", new BigDecimal("2.25"));
    }

    /** Adds an item and returns it. IDs are never reused. */
    public MenuItem add(String name, BigDecimal price) {
        MenuItem item = new MenuItem(nextId++, name, price);
        items.add(item);
        return item;
    }

    public boolean hasName(String name) {
        for (MenuItem item : items) {
            if (item.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    /** Returns the item with this id, or null if there is none. */
    public MenuItem findById(int id) {
        for (MenuItem item : items) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    /** Removes the item with this id. Returns false if it does not exist. */
    public boolean remove(int id) {
        MenuItem item = findById(id);
        if (item == null) {
            return false;
        }
        return items.remove(item);
    }

    public List<MenuItem> getItems() {
        return new ArrayList<>(items);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}

import java.math.BigDecimal;

/**
 * One item on the restaurant menu.
 */
public class MenuItem {
    private final int id;
    private final String name;
    private final BigDecimal price;

    public MenuItem(int id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }
}

import java.math.BigDecimal;

/**
 * One line of a bill: a menu item and how many were ordered.
 * The name and price are copied, so later menu changes do not affect an existing bill.
 */
public class OrderLine {
    private final String name;
    private final BigDecimal unitPrice;
    private int quantity;

    public OrderLine(String name, BigDecimal unitPrice, int quantity) {
        this.name = name;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public String getName() { return name; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public int getQuantity() { return quantity; }

    public void addQuantity(int extra) {
        quantity += extra;
    }

    public BigDecimal getLineTotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}

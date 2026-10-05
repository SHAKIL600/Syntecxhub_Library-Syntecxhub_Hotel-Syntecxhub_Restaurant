import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A customer's bill: ordered items, GST and the final total.
 */
public class Bill {
    /** GST rate: 5%. Change this value to use a different tax rate. */
    public static final BigDecimal TAX_RATE = new BigDecimal("0.05");
    public static final String CURRENCY = "$";

    private final int billNumber;
    private final List<OrderLine> lines = new ArrayList<>();

    public Bill(int billNumber) {
        this.billNumber = billNumber;
    }

    /** Adds a menu item to the bill. Ordering the same item again increases its quantity. */
    public void addItem(MenuItem item, int quantity) {
        for (OrderLine line : lines) {
            if (line.getName().equals(item.getName())) {
                line.addQuantity(quantity);
                return;
            }
        }
        lines.add(new OrderLine(item.getName(), item.getPrice(), quantity));
    }

    public boolean isEmpty() {
        return lines.isEmpty();
    }

    public BigDecimal getSubtotal() {
        BigDecimal sum = BigDecimal.ZERO;
        for (OrderLine line : lines) {
            sum = sum.add(line.getLineTotal());
        }
        return sum.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getTax() {
        return getSubtotal().multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getTotal() {
        return getSubtotal().add(getTax());
    }

    /** Builds the itemized receipt as text. */
    public String toReceipt() {
        String line = "-".repeat(52);
        StringBuilder sb = new StringBuilder();
        sb.append("=".repeat(52)).append('\n');
        sb.append(center("RESTAURANT RECEIPT", 52)).append('\n');
        sb.append("=".repeat(52)).append('\n');
        sb.append("Bill No: ").append(billNumber).append('\n');
        sb.append("Date   : ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))).append('\n');
        sb.append(line).append('\n');
        sb.append(String.format(Locale.US, "%-24s %5s %10s %10s%n", "Item", "Qty", "Price", "Amount"));
        sb.append(line).append('\n');
        for (OrderLine l : lines) {
            sb.append(String.format(Locale.US, "%-24s %5d %10s %10s%n",
                    l.getName(), l.getQuantity(), money(l.getUnitPrice()), money(l.getLineTotal())));
        }
        sb.append(line).append('\n');
        sb.append(String.format(Locale.US, "%-41s %10s%n", "Subtotal", money(getSubtotal())));
        sb.append(String.format(Locale.US, "%-41s %10s%n", "GST (" + TAX_RATE.movePointRight(2).stripTrailingZeros().toPlainString() + "%)", money(getTax())));
        sb.append(line).append('\n');
        sb.append(String.format(Locale.US, "%-41s %10s%n", "TOTAL", money(getTotal())));
        sb.append("=".repeat(52)).append('\n');
        sb.append(center("Thank you! Please visit again.", 52)).append('\n');
        return sb.toString();
    }

    private static String money(BigDecimal value) {
        return CURRENCY + value.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    private static String center(String text, int width) {
        int pad = Math.max(0, (width - text.length()) / 2);
        return " ".repeat(pad) + text;
    }
}

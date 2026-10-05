/**
 * A hotel room. A room is either available or booked by one guest for a number of nights.
 */
public class Room {
    private final int number;
    private final String type;
    private final int pricePerNight;
    private boolean booked;
    private String guestName;
    private int nights;

    public Room(int number, String type, int pricePerNight) {
        this.number = number;
        this.type = type;
        this.pricePerNight = pricePerNight;
    }

    public int getNumber() { return number; }
    public String getType() { return type; }
    public int getPricePerNight() { return pricePerNight; }
    public boolean isBooked() { return booked; }
    public String getGuestName() { return guestName; }
    public int getNights() { return nights; }

    /** Total price of the current booking. */
    public int getTotalPrice() {
        return pricePerNight * nights;
    }

    /** Books the room. Returns false if it is already booked. */
    public boolean book(String guest, int nightsToStay) {
        if (booked) {
            return false;
        }
        booked = true;
        guestName = guest;
        nights = nightsToStay;
        return true;
    }

    /** Cancels the booking. Returns false if the room was not booked. */
    public boolean cancel() {
        if (!booked) {
            return false;
        }
        booked = false;
        guestName = null;
        nights = 0;
        return true;
    }
}

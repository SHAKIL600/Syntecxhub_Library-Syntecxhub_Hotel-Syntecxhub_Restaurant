import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * Command-line Hotel Booking Management System.
 * Room data lives in memory only, so bookings reset when the program exits.
 * Run: java -cp out HotelApp
 */
public class HotelApp {
    private static final String CURRENCY = "$";
    private static final int MAX_NIGHTS = 30;
    private static final int MAX_NAME = 40;
    private static final Scanner SC = new Scanner(System.in, StandardCharsets.UTF_8);

    /** Thrown when the input stream ends so the program can exit cleanly. */
    private static class InputEnded extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }

    public static void main(String[] args) {
        Hotel hotel = new Hotel();
        System.out.println("=== Hotel Booking Management System ===");
        try {
            boolean running = true;
            while (running) {
                printMenu();
                String choice = ask("Choose an option (1-5): ");
                switch (choice) {
                    case "1": showAvailable(hotel); break;
                    case "2": bookRoom(hotel); break;
                    case "3": cancelBooking(hotel); break;
                    case "4": showBooked(hotel); break;
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
        System.out.println("1. View available rooms");
        System.out.println("2. Book a room");
        System.out.println("3. Cancel a booking");
        System.out.println("4. View booked rooms");
        System.out.println("5. Exit");
    }

    private static void showAvailable(Hotel hotel) {
        List<Room> available = hotel.getAvailableRooms();
        if (available.isEmpty()) {
            System.out.println("Sorry, no rooms are available right now.");
            return;
        }
        System.out.println("Available rooms (" + available.size() + " of " + hotel.getRoomCount() + "):");
        System.out.println(String.format(Locale.US, "%-6s %-8s %s", "Room", "Type", "Price/night"));
        System.out.println("-".repeat(30));
        for (Room room : available) {
            System.out.println(String.format(Locale.US, "%-6d %-8s %s%d", room.getNumber(), room.getType(), CURRENCY, room.getPricePerNight()));
        }
    }

    private static void bookRoom(Hotel hotel) {
        if (hotel.getAvailableRooms().isEmpty()) {
            System.out.println("Sorry, all rooms are booked.");
            return;
        }
        showAvailable(hotel);
        int number = askInt("Enter the room number to book: ");
        Room room = hotel.findRoom(number);
        if (room == null) {
            System.out.println("Room " + number + " does not exist.");
            return;
        }
        if (room.isBooked()) {
            System.out.println("Room " + number + " is already booked. Please choose an available room.");
            return;
        }
        String guest = askName("Guest name: ");
        int nights = askNights("Number of nights (1-" + MAX_NIGHTS + "): ");
        room.book(guest, nights);
        System.out.println("Booking confirmed!");
        System.out.println("Room " + room.getNumber() + " (" + room.getType() + ") for " + guest + ", "
                + nights + (nights == 1 ? " night" : " nights") + ".");
        System.out.println("Total price: " + CURRENCY + room.getTotalPrice());
    }

    private static void cancelBooking(Hotel hotel) {
        if (hotel.getBookedRooms().isEmpty()) {
            System.out.println("There are no bookings to cancel.");
            return;
        }
        int number = askInt("Enter the room number to cancel: ");
        Room room = hotel.findRoom(number);
        if (room == null) {
            System.out.println("Room " + number + " does not exist.");
            return;
        }
        if (!room.isBooked()) {
            System.out.println("Room " + number + " is not booked, so there is nothing to cancel.");
            return;
        }
        String answer = ask("Cancel the booking of " + room.getGuestName() + " for room " + number + "? (y/n): ");
        if (!answer.equalsIgnoreCase("y")) {
            System.out.println("Cancelled. The booking was kept.");
            return;
        }
        room.cancel();
        System.out.println("Booking cancelled. Room " + number + " is available again.");
    }

    private static void showBooked(Hotel hotel) {
        List<Room> booked = hotel.getBookedRooms();
        if (booked.isEmpty()) {
            System.out.println("No rooms are booked yet.");
            return;
        }
        System.out.println("Booked rooms (" + booked.size() + "):");
        System.out.println(String.format(Locale.US, "%-6s %-8s %-25s %-7s %s", "Room", "Type", "Guest", "Nights", "Total"));
        System.out.println("-".repeat(60));
        for (Room room : booked) {
            System.out.println(String.format(Locale.US, "%-6d %-8s %-25s %-7d %s%d", room.getNumber(), room.getType(),
                    room.getGuestName(), room.getNights(), CURRENCY, room.getTotalPrice()));
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

    private static int askNights(String prompt) {
        while (true) {
            int nights = askInt(prompt);
            if (nights < 1 || nights > MAX_NIGHTS) {
                System.out.println("Nights must be between 1 and " + MAX_NIGHTS + ".");
            } else {
                return nights;
            }
        }
    }
}

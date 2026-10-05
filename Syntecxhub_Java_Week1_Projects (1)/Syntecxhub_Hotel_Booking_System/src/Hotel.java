import java.util.ArrayList;
import java.util.List;

/**
 * Stores all rooms in an array and provides booking operations.
 */
public class Hotel {
    private final Room[] rooms;

    public Hotel() {
        rooms = new Room[] {
            new Room(101, "Single", 50),
            new Room(102, "Single", 50),
            new Room(103, "Single", 50),
            new Room(104, "Double", 80),
            new Room(105, "Double", 80),
            new Room(201, "Double", 90),
            new Room(202, "Double", 90),
            new Room(203, "Deluxe", 120),
            new Room(301, "Suite", 200),
            new Room(302, "Suite", 220)
        };
    }

    /** Returns the room with this number, or null if there is no such room. */
    public Room findRoom(int number) {
        for (Room room : rooms) {
            if (room.getNumber() == number) {
                return room;
            }
        }
        return null;
    }

    public List<Room> getAvailableRooms() {
        List<Room> result = new ArrayList<>();
        for (Room room : rooms) {
            if (!room.isBooked()) {
                result.add(room);
            }
        }
        return result;
    }

    public List<Room> getBookedRooms() {
        List<Room> result = new ArrayList<>();
        for (Room room : rooms) {
            if (room.isBooked()) {
                result.add(room);
            }
        }
        return result;
    }

    public int getRoomCount() {
        return rooms.length;
    }
}

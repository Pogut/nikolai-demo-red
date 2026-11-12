package booking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomRepository {
    private final Map<String, Room> rooms = new HashMap<>();
    private final List<Booking> bookings = new ArrayList<>();

    public void addRoom(Room room) { rooms.put(room.getId(), room); }
    public Room findRoom(String id) { return rooms.get(id); }
    public List<Room> allRooms() { return new ArrayList<>(rooms.values()); }
    public void saveBooking(Booking booking) { bookings.add(booking); }
    public void deleteBooking(Booking booking) { bookings.remove(booking); }
    public List<Booking> listBookings() { return new ArrayList<>(bookings); }
}

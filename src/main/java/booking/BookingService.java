package booking;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BookingService {
    private final RoomRepository repository;
    private final List<BookingListener> listeners = new ArrayList<>();

    public BookingService(RoomRepository repository) {
        this.repository = repository;
    }

    public void addListener(BookingListener listener) {
        listeners.add(listener);
    }

    public List<Room> freeRooms(LocalDateTime start, LocalDateTime end) {
        List<Room> free = new ArrayList<>();
        for (Room room : repository.allRooms()) {
            if (!isTaken(room.getId(), start, end) && !room.isBlocked()) {
                free.add(room);
            }
        }
        return free;
    }

    public Booking book(String roomId, String studentId, LocalDateTime start, LocalDateTime end) {
        Room room = repository.findRoom(roomId);
        if (room == null) {
            throw new IllegalArgumentException("No room " + roomId);
        }
        if (room.isBlocked()) {
            throw new IllegalStateException("Room " + roomId + " is blocked");
        }
        if (Duration.between(start, end).toMinutes() > 120) {
            throw new IllegalArgumentException("A booking can last at most 2 hours");
        }
        if (isTaken(roomId, start, end)) {
            throw new IllegalStateException("Room " + roomId + " is already booked");
        }
        Booking booking = new Booking(UUID.randomUUID().toString(), roomId, studentId, start, end);
        repository.saveBooking(booking);
        for (BookingListener listener : listeners) {
            listener.onBooked(booking);
        }
        return booking;
    }

    public void cancel(Booking booking, String studentId, LocalDateTime now) {
        if (!booking.getStudentId().equals(studentId)) {
            throw new IllegalStateException("You can only cancel your own booking");
        }
        if (Duration.between(now, booking.getStart()).toMinutes() < 15) {
            throw new IllegalStateException("Too late to cancel");
        }
        repository.deleteBooking(booking);
        for (BookingListener listener : listeners) {
            listener.onCancelled(booking);
        }
    }

    private boolean isTaken(String roomId, LocalDateTime start, LocalDateTime end) {
        for (Booking booking : repository.listBookings()) {
            if (booking.getRoomId().equals(roomId) && booking.overlaps(start, end)) {
                return true;
            }
        }
        return false;
    }
}

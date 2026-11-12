package booking;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BookingService {
    private final RoomRepository repository;

    public BookingService(RoomRepository repository) {
        this.repository = repository;
    }

    public List<Room> freeRooms(LocalDateTime start, LocalDateTime end) {
        List<Room> free = new ArrayList<>();
        for (Room room : repository.allRooms()) {
            boolean taken = false;
            for (Booking booking : repository.allBookings()) {
                if (booking.getRoomId().equals(room.getId()) && booking.overlaps(start, end)) {
                    taken = true;
                }
            }
            if (!taken && !room.isBlocked()) {
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
        for (Booking booking : repository.allBookings()) {
            if (booking.getRoomId().equals(roomId) && booking.overlaps(start, end)) {
                throw new IllegalStateException("Room " + roomId + " is already booked");
            }
        }
        Booking booking = new Booking(UUID.randomUUID().toString(), roomId, studentId, start, end);
        repository.saveBooking(booking);
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
    }
}

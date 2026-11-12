package booking;

import java.time.LocalDateTime;

public class Booking {
    private final String id;
    private final String roomId;
    private final String studentId;
    private final LocalDateTime start;
    private final LocalDateTime end;

    public Booking(String id, String roomId, String studentId, LocalDateTime start, LocalDateTime end) {
        this.id = id;
        this.roomId = roomId;
        this.studentId = studentId;
        this.start = start;
        this.end = end;
    }

    public String getId() { return id; }
    public String getRoomId() { return roomId; }
    public String getStudentId() { return studentId; }
    public LocalDateTime getStart() { return start; }
    public LocalDateTime getEnd() { return end; }

    public boolean overlaps(LocalDateTime otherStart, LocalDateTime otherEnd) {
        return start.isBefore(otherEnd) && otherStart.isBefore(end);
    }
}

package booking;

/** Told about every booking that is made or cancelled. */
public interface BookingListener {
    void onBooked(Booking booking);

    void onCancelled(Booking booking);
}

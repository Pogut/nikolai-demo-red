package notify

import "fmt"

// Sender delivers one email.
type Sender interface {
	Send(to, subject, body string) error
}

// Notifier emails students about their bookings.
type Notifier struct {
	sender Sender
}

func NewNotifier(sender Sender) *Notifier {
	return &Notifier{sender: sender}
}

// NotifyBooking tells a student their booking was confirmed or cancelled.
func (n *Notifier) NotifyBooking(email, room, when string, cancelled bool) error {
	subject, body := bookingMessage(room, when, cancelled)
	return n.sender.Send(email, subject, body)
}

// bookingMessage is the subject and body of a booking email.
func bookingMessage(room, when string, cancelled bool) (string, string) {
	return bookingSubject(cancelled), bookingBody(room, when, cancelled)
}

// bookingSubject is the subject line of a booking email.
func bookingSubject(cancelled bool) string {
	if cancelled {
		return "Booking cancelled"
	}
	return "Booking confirmed"
}

// bookingBody is the text of a booking email.
func bookingBody(room, when string, cancelled bool) string {
	if cancelled {
		return fmt.Sprintf("Your booking of %s on %s was cancelled.", room, when)
	}
	return fmt.Sprintf("Your booking of %s on %s is confirmed.", room, when)
}

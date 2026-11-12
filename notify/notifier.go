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

// SendMsg tells a student their booking was confirmed or cancelled.
func (n *Notifier) SendMsg(email, room, when string, cancelled bool) error {
	subject := "Booking confirmed"
	body := fmt.Sprintf("Your booking of %s on %s is confirmed.", room, when)
	if cancelled {
		subject = "Booking cancelled"
		body = fmt.Sprintf("Your booking of %s on %s was cancelled.", room, when)
	}
	return n.sender.Send(email, subject, body)
}

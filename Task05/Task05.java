
class SeatBooking {

    private final boolean[] booked;
    private int bookingsMade = 0;

    SeatBooking(int seats) {
        booked = new boolean[seats];
    }

    // Unsynchronized booking
    void bookWithoutSync(int seat) {
        if (!booked[seat]) {
            booked[seat] = true;
            bookingsMade++;
        }
    }

    // Synchronized method
    synchronized void bookWithMethod(int seat) {
        if (!booked[seat]) {
            booked[seat] = true;
            bookingsMade++;
        }
    }

    // Synchronized block
    void bookWithBlock(int seat) {
        synchronized (this) {
            if (!booked[seat]) {
                booked[seat] = true;
                bookingsMade++;
            }
        }
    }

    int getBookingsMade() {
        return bookingsMade;
    }
}

public class Task05 {

    static void runTest(String type) throws InterruptedException {

        SeatBooking booking = new SeatBooking(10);
        Thread[] users = new Thread[100];

        long start = System.nanoTime();

        for (int i = 0; i < users.length; i++) {
            final int seat = i % 10;

            users[i] = new Thread(() -> {
                if (type.equals("UNSYNCHRONIZED")) {
                    booking.bookWithoutSync(seat);
                } else if (type.equals("SYNCHRONIZED METHOD")) {
                    booking.bookWithMethod(seat);
                } else {
                    booking.bookWithBlock(seat);
                }
            });

            users[i].start();
        }

        for (Thread user : users) {
            user.join();
        }

        long end = System.nanoTime();

        System.out.println("\n" + type);
        System.out.println("Seats Sold: " +
            booking.getBookingsMade());
        System.out.println("Execution Time: " +
            (end - start) / 1_000_000.0 + " ms");
    }

    public static void main(String[] args)
            throws InterruptedException {

        System.out.println("CINEMA SEAT BOOKING SYSTEM");
        System.out.println("=========================");

        System.out.println("\nRunning 100 users for 10 seats.");

        runTest("UNSYNCHRONIZED");
        runTest("SYNCHRONIZED METHOD");
        runTest("SYNCHRONIZED BLOCK");

        System.out.println("\nTESTING COMPLETED");
    }
}

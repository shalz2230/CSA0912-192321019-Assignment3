
class ScannerThread extends Thread {

    private int stationNumber;
    private int scripts;

    ScannerThread(int stationNumber, int scripts) {
        this.stationNumber = stationNumber;
        this.scripts = scripts;
    }

    public void run() {
        System.out.println("Station " + stationNumber +
            " started scanning.");

        for (int i = 1; i <= scripts; i++) {
            System.out.println("Station " + stationNumber +
                " scanned script " + i);

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                System.out.println("Station interrupted.");
                Thread.currentThread().interrupt();
                return;
            }
        }

        System.out.println("Station " + stationNumber +
            " completed scanning.");
    }
}

// Runnable implementation
class RunnableScanner implements Runnable {

    public void run() {
        System.out.println("Runnable Scanner Started.");

        for (int i = 1; i <= 3; i++) {
            System.out.println("Runnable scanned script " + i);

            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        System.out.println("Runnable Scanner Completed.");
    }
}

public class Task04 {

    public static void main(String[] args) {

        System.out.println("PARALLEL ANSWER SCRIPT SCANNING");
        System.out.println("================================");

        // Thread class implementation
        ScannerThread t1 = new ScannerThread(1, 3);
        ScannerThread t2 = new ScannerThread(2, 3);
        ScannerThread t3 = new ScannerThread(3, 3);
        ScannerThread t4 = new ScannerThread(4, 3);

        System.out.println("\nStarting Scanner Stations");

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        System.out.println("Station 1 Alive: " + t1.isAlive());

        // Runnable implementation
        RunnableScanner task = new RunnableScanner();
        Thread t5 = new Thread(task);

        t5.start();

        // Wait for all threads to complete
        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
            t5.join();

        } catch (InterruptedException e) {
            System.out.println("Main thread interrupted.");
            Thread.currentThread().interrupt();
        }

        System.out.println("\nAll Scanner Stations Completed.");
        System.out.println("Station 1 Alive: " + t1.isAlive());
        System.out.println("Station 2 Alive: " + t2.isAlive());
        System.out.println("Station 3 Alive: " + t3.isAlive());
        System.out.println("Station 4 Alive: " + t4.isAlive());

        System.out.println("\nTESTING COMPLETED");
    }
}

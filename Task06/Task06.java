
class Wallet {

    private int balance;
    private final int walletId;

    Wallet(int walletId, int balance) {
        this.walletId = walletId;
        this.balance = balance;
    }

    int getBalance() {
        return balance;
    }

    int getWalletId() {
        return walletId;
    }

    void deduct(int amount) {
        balance -= amount;
    }

    void add(int amount) {
        balance += amount;
    }
}

class WalletTransfer {

    // Deadlock prevention using consistent lock ordering
    static void transfer(Wallet from, Wallet to, int amount) {

        Wallet first = from.getWalletId() < to.getWalletId()
                ? from : to;

        Wallet second = from.getWalletId() < to.getWalletId()
                ? to : from;

        synchronized (first) {
            synchronized (second) {

                if (from.getBalance() >= amount) {
                    from.deduct(amount);
                    to.add(amount);

                    System.out.println("Transferred Rs." +
                        amount + " successfully.");
                }
            }
        }
    }
}

// Producer-Consumer Buffer
class TokenCounter {

    private final int[] buffer = new int[5];
    private int count = 0;
    private int in = 0;
    private int out = 0;

    // Producer
    synchronized void put(int token) throws InterruptedException {

        while (count == buffer.length) {
            wait();
        }

        buffer[in] = token;
        in = (in + 1) % buffer.length;
        count++;

        notifyAll();
    }

    // Consumer
    synchronized int take() throws InterruptedException {

        while (count == 0) {
            wait();
        }

        int token = buffer[out];
        out = (out + 1) % buffer.length;
        count--;

        notifyAll();

        return token;
    }
}

public class Task06 {

    public static void main(String[] args)
            throws InterruptedException {

        System.out.println("DEADLOCK PREVENTION");
        System.out.println("===================");

        Wallet w1 = new Wallet(1, 5000);
        Wallet w2 = new Wallet(2, 3000);

        Thread t1 = new Thread(() ->
            WalletTransfer.transfer(w1, w2, 1000)
        );

        Thread t2 = new Thread(() ->
            WalletTransfer.transfer(w2, w1, 500)
        );

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("Wallet 1 Balance: Rs." +
            w1.getBalance());

        System.out.println("Wallet 2 Balance: Rs." +
            w2.getBalance());

        System.out.println("\nPRODUCER-CONSUMER TOKEN COUNTER");
        System.out.println("===============================");

        TokenCounter counter = new TokenCounter();

        final int totalTokens = 20;
        final int[] sum = {0};

        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= totalTokens; i++) {
                    counter.put(i);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 1; i <= totalTokens; i++) {
                    sum[0] += counter.take();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();

        System.out.println("Sum of tokens collected: " +
            sum[0] + " (expected 210)");

        System.out.println("\nTESTING COMPLETED");
    }
}


import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

class ScanResult {
    boolean success;
    int score;
    String message;

    ScanResult(boolean success, int score, String message) {
        this.success = success;
        this.score = score;
        this.message = message;
    }
}

class ScoreProcessor implements Callable<ScanResult> {

    private final String fileName;

    ScoreProcessor(String fileName) {
        this.fileName = fileName;
    }

    public ScanResult call() {

        try {
            String content = Files.readString(
                Paths.get(fileName)
            ).trim();

            int score = Integer.parseInt(content);

            if (score < 0 || score > 100) {
                throw new IllegalArgumentException(
                    "Invalid score range"
                );
            }

            return new ScanResult(
                true, score, "Processed successfully"
            );

        } catch (IOException | NumberFormatException |
                 IllegalArgumentException e) {

            return new ScanResult(
                false, 0, e.getMessage()
            );
        }
    }
}

public class Task07 {

    static final int TOTAL_FILES = 10000;

    // Create sample score files
    static void createFiles() throws IOException {

        Path folder = Paths.get("scores");
        Files.createDirectories(folder);

        for (int i = 1; i <= TOTAL_FILES; i++) {

            Path file = folder.resolve("student" + i + ".txt");

            // Simulate 10 missing files
            if (i % 1000 == 0) {
                Files.deleteIfExists(file);
                continue;
            }

            // Simulate 10 corrupt files
            if (i % 1000 == 500) {
                Files.writeString(file, "INVALID_SCORE");
            } else {
                int score = i % 101;
                Files.writeString(file,
                    String.valueOf(score));
            }
        }
    }

    static void processFiles(int poolSize)
            throws InterruptedException {

        ExecutorService executor =
            Executors.newFixedThreadPool(poolSize);

        ConcurrentLinkedQueue<Integer> successfulScores =
            new ConcurrentLinkedQueue<>();

        ConcurrentLinkedQueue<String> failedFiles =
            new ConcurrentLinkedQueue<>();

        List<Future<ScanResult>> futures =
            new ArrayList<>();

        long start = System.nanoTime();

        // Submit all 10000 tasks
        for (int i = 1; i <= TOTAL_FILES; i++) {

            String fileName = "scores/student" + i + ".txt";

            futures.add(
                executor.submit(new ScoreProcessor(fileName))
            );
        }

        // Collect task results
        for (int i = 0; i < futures.size(); i++) {

            try {
                ScanResult result = futures.get(i).get();

                if (result.success) {
                    successfulScores.add(result.score);
                } else {
                    failedFiles.add(
                        "student" + (i + 1) + ": " +
                        result.message
                    );
                }

            } catch (ExecutionException e) {
                failedFiles.add(
                    "student" + (i + 1) +
                    ": Unexpected processing error"
                );
            }
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        long end = System.nanoTime();

        int total = 0;

        for (int score : successfulScores) {
            total += score;
        }

        System.out.println("\nThread Pool Size: " + poolSize);
        System.out.println("Total Files: " + TOTAL_FILES);
        System.out.println("Successful Files: " +
            successfulScores.size());
        System.out.println("Failed Files: " +
            failedFiles.size());
        System.out.println("Total Score: " + total);

        System.out.println("Execution Time: " +
            (end - start) / 1_000_000.0 + " ms");

        System.out.println("Sample Failure: " +
            failedFiles.peek());
    }

    public static void main(String[] args)
            throws IOException, InterruptedException {

        System.out.println(
            "CONCURRENT FAULT-TOLERANT RESULT PROCESSOR"
        );

        System.out.println(
            "==========================================="
        );

        System.out.println("\nCreating 10000 score files...");

        createFiles();

        System.out.println("Files created successfully.");

        int[] poolSizes = {1, 2, 4, 8};

        for (int size : poolSizes) {
            processFiles(size);
        }

        System.out.println("\nPROCESSING COMPLETED");
        System.out.println("All valid files were processed.");
        System.out.println("Failed files did not stop execution.");

        System.out.println("\nTESTING COMPLETED");
    }
}

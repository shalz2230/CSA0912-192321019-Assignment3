
import java.io.*;

public class Task03 {

    public static void main(String[] args) {

        String[] files = {
            "store1.txt",
            "store2.txt",
            "store3.txt"
        };

        System.out.println("DAILY SALES REPORT GENERATOR");
        System.out.println("============================");

        int totalSales = 0;

        // Create sample store files
        try {
            FileWriter f1 = new FileWriter("store1.txt");
            f1.write("1000\n");
            f1.close();

            FileWriter f2 = new FileWriter("store2.txt");
            f2.write("2000\n");
            f2.close();

            FileWriter f3 = new FileWriter("store3.txt");
            f3.write("3000\n");
            f3.close();

        } catch (IOException e) {
            System.out.println("File Creation Error: " +
                e.getMessage());
        }

        // Manual resource handling
        System.out.println("\nMANUAL RESOURCE HANDLING");

        for (String file : files) {
            BufferedReader br = null;

            try {
                br = new BufferedReader(new FileReader(file));

                String line;
                while ((line = br.readLine()) != null) {
                    totalSales += Integer.parseInt(line);
                }

                System.out.println("Processed: " + file);

            } catch (IOException | NumberFormatException e) {
                System.out.println("Error in " + file + ": " +
                    e.getMessage());

            } finally {
                try {
                    if (br != null) {
                        br.close();
                    }
                } catch (IOException e) {
                    System.out.println("Closing Error: " +
                        e.getMessage());
                }
            }
        }

        System.out.println("Manual Total Sales: Rs." + totalSales);

        // Try-with-resources handling
        int totalSales2 = 0;

        System.out.println("\nTRY-WITH-RESOURCES HANDLING");

        for (String file : files) {

            try (BufferedReader br =
                    new BufferedReader(new FileReader(file))) {

                String line;

                while ((line = br.readLine()) != null) {
                    totalSales2 += Integer.parseInt(line);
                }

                System.out.println("Processed: " + file);

            } catch (IOException | NumberFormatException e) {
                System.out.println("Error in " + file + ": " +
                    e.getMessage());
            }
        }

        System.out.println("Try-with-resources Total Sales: Rs." +
            totalSales2);

        // Generate final report
        try (BufferedWriter writer =
                new BufferedWriter(new FileWriter("sales_report.txt"))) {

            writer.write("DAILY SALES REPORT\n");
            writer.write("==================\n");
            writer.write("Store 1 Sales: Rs.1000\n");
            writer.write("Store 2 Sales: Rs.2000\n");
            writer.write("Store 3 Sales: Rs.3000\n");
            writer.write("Total Sales: Rs." + totalSales2 + "\n");

            System.out.println("\nReport generated successfully.");

        } catch (IOException e) {
            System.out.println("Report Error: " + e.getMessage());
        }

        System.out.println("\nTESTING COMPLETED");
    }
}

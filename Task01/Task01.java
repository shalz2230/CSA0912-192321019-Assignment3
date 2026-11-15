
import java.util.Scanner;
import java.io.*;

class InsufficientFundsException extends Exception {
    InsufficientFundsException(String message) {
        super(message);
    }
}

public class Task01 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double balance = 10000.0;

        System.out.println("ATM FAILURE HANDLING SYSTEM");
        System.out.println("============================");

        try {
            // Check card reader file
            BufferedReader br = new BufferedReader(
                new FileReader("cardreader.txt")
            );

            String status = br.readLine();
            br.close();

            System.out.println("Card Reader: " + status);

            // Account number input
            System.out.print("Enter Account Number: ");
            String account = sc.nextLine();

            if (!account.matches("\\d+")) {
                throw new NumberFormatException(
                    "Account number must contain digits only"
                );
            }

            // Withdrawal amount input
            System.out.print("Enter Withdrawal Amount: ");
            String input = sc.nextLine();
            double amount = Double.parseDouble(input);

            if (amount <= 0) {
                throw new IllegalArgumentException(
                    "Withdrawal amount must be positive"
                );
            }

            if (amount > balance) {
                throw new InsufficientFundsException(
                    "Insufficient account balance"
                );
            }

            balance -= amount;

            System.out.println("\nTransaction Successful!");
            System.out.println("Account Number: " + account);
            System.out.println("Amount Withdrawn: Rs." + amount);
            System.out.println("Remaining Balance: Rs." + balance);
            System.out.println("Please Collect Your Cash.");

        } catch (FileNotFoundException e) {
            System.out.println("Card Reader Error: " + e.getMessage());

        } catch (NumberFormatException e) {
            System.out.println("Input Error: " + e.getMessage());

        } catch (IllegalArgumentException e) {
            System.out.println("Invalid Amount: " + e.getMessage());

        } catch (InsufficientFundsException e) {
            System.out.println("Transaction Failed: " + e.getMessage());

        } catch (IOException e) {
            System.out.println("File Error: " + e.getMessage());

        } finally {
            System.out.println("\nATM Session Completed.");
            sc.close();
        }
    }
}

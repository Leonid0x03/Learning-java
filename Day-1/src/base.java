
import java.util.Scanner;

class InvalidAccountException extends Exception {
    public InvalidAccountException(String message) {
        super(message);
    }
}

public class base {

    static void checkAge(int age) {
        if (age < 0 || age > 120) {
            throw new IllegalArgumentException("Invalid age: " + age);
        } else {
            System.out.println("Valid age: " + age);
        }
    }

    static void register(String account, String password) throws InvalidAccountException {
        if (account == null || account.isEmpty()) {
            throw new InvalidAccountException("Account must not be empty.");
        }
        if (password == null || password.length() < 6) {
            throw new InvalidAccountException("Password must be at least 6 characters.");
        }
        System.out.println("Registration successful for account: " + account);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            System.out.print("Username: ");
            String username = sc.nextLine();

            System.out.print("Password: ");
            String password = sc.nextLine();

            System.out.print("Age: ");
            // Integer.parseInt may throw NumberFormatException (unchecked)
            int age = Integer.parseInt(sc.nextLine());

            // Call the methods that may throw errors
            checkAge(age);
            register(username, password);

        } catch (NumberFormatException e) {
            // User entered an age that is not a number
            System.out.println("x Age must be a number: " + e.getMessage());

        } catch (IllegalArgumentException e) {
            // Age is out of the allowed range
            System.out.println("x " + e.getMessage());

        } catch (InvalidAccountException e) {
            // Custom business-logic error
            System.out.println("x " + e.getMessage());

        } finally {
            // Always runs - clean up resources
            sc.close();
            System.out.println("- End of registration session -");
        }
    }

}

package Day6;

import java.util.Scanner;

public class Loop {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            int n = scanner.nextInt();
            while (n <= 100) {
                if (n % 2 == 0) {
                    System.out.print(n + " ");
                }
                n += 1;
            }
        }
    }
}

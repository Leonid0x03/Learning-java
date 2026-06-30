package Day6;

import java.util.Scanner;

public class Loop1 {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            int n = scanner.nextInt();
            int i = 1;
            int totals = 0;
            while (n >= i) {
                if (n % i == 0) {
                    totals++;
                }
                i++;
            }
            System.out.println("Totals: " + totals);
        }
    }
}

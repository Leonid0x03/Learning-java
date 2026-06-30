package Day6;

import java.util.Scanner;

public class Array3 {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            int n = scanner.nextInt();
            int[] arr = new int[n];

            for (int i = 0; i < n; i++) {
                arr[i] = scanner.nextInt();
            }

            int sum = 0;
            for (int i = 0; i < n; i++) {
                if (arr[i] % 2 != 0 && arr[i] > 0) {
                    sum += arr[i];
                }
            }
            System.out.println("Sum = " + sum);
        }
    }
}

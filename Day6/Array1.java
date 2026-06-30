package Day6;

import java.util.Scanner;

public class Array1 {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            int n = scanner.nextInt();
            int[] arr = new int[n];
            for (int i = 0; i < n; i++) {
                arr[i] = scanner.nextInt();
            }

            int maxArray = arr[0];
            for (int i = 1; i < n; i++) {
                if (maxArray < arr[i]) {
                    maxArray = arr[i];
                }
            }

            System.out.println("MaxArray =  " + maxArray);
        }
    }
}

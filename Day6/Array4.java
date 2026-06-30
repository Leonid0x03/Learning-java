package Day6;

import java.util.Scanner;

public class Array4 {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            int n = sc.nextInt();
            int[] arr = new int[n];
            for (int i = 0; i < n; i++) {
                arr[i] = sc.nextInt();
            }

            for (int i = 0; i < n; i++) {
                if (arr[i] >= 0 && arr[i] <= 10) {
                    System.out.print(arr[i] + " ");
                }
            }
        }
    }
}

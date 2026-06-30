package Day6;

import java.util.Scanner;

public class Array {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            int[] a = new int[10];

            for (int i = 0; i < 10; i++) {
                a[i] = sc.nextInt();
            }

            for (int i = 0; i < 10; i++) {
                System.out.print(a[i] + " ");
            }
        }
    }
}

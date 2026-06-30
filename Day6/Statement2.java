package Day6;

import java.util.Scanner;

public class Statement2 {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            int a = sc.nextInt();
            int b = sc.nextInt();
            int c = sc.nextInt();
            if (a > b && a > c) {
                System.out.println(a);
            } else if (b > c) {
                System.out.println(b);
            } else {
                System.out.println(c);
            }
        }
    }
}
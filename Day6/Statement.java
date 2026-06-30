package Day6;

import java.util.Scanner;

public class Statement {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            int n = sc.nextInt();
            if (n % 2 == 0) {
                System.out.println("n is an even number");
            } else {
                System.out.println("n is an odd number");
            }
        }
    }
}

package Day6;

import java.util.Scanner;

public class Statement1 {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            String a = sc.nextLine();
            String b = sc.nextLine();
            if (a.equals(b)) {
                System.out.println("two people have the same name");
            } else {
                System.out.println(" two people don't have the same name");
            }
        }
    }

}

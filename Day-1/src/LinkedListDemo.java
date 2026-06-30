
import java.util.*;

/**
 * Ví dụ tổng hợp về LinkedList trong Java. LinkedList vừa là List vừa là Deque,
 * nên dùng được như: danh sách, deque (hai đầu), hàng đợi FIFO, và ngăn xếp
 * LIFO.
 *
 * Biên dịch & chạy: javac LinkedListDemo.java java LinkedListDemo
 */
public class LinkedListDemo {

    public static void main(String[] args) {
        dungNhuList();
        dungNhuDeque();
    }

    /* 1. Dùng như một List thông thường */
    static void dungNhuList() {
        System.out.println("=== 1. Dùng như List ===");
        LinkedList<String> list = new LinkedList<>();
        list.add("A");
        list.add("B");
        list.add("C");

        System.out.println(list);  // [A, B, C]
        System.out.println("get(1): " + list.get(1));  // B
        list.set(0, "A1");  // thay A bằng A1
        System.out.println(list);  // [A1, B, C]
        list.remove(2);  // xóa C
        System.out.println(list);  // [A1, B]
    }

    static void dungNhuDeque() {
        System.out.println("=== 2. Dùng như Deque ===");
        LinkedList<String> deque = new LinkedList<>();
        deque.addFirst("A");  // thêm A vào đầu
        deque.addLast("B");   // thêm B vào cuối    
        System.out.println(deque);  // [A, B]

    }

}

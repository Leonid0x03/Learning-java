package Threads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class BlockingQueueDemo {

    public static void main(String[] args) throws InterruptedException {
        producerConsumer();
        cacBienTheKhongCho();
    }

    /* 1. Producer - Consumer thật, dùng put() và take() */
    static void producerConsumer() throws InterruptedException {
        System.out.println("=== 1. Producer - Consumer (put / take) ===");

        // Hàng đợi giới hạn 5 → khi đầy, producer phải chờ
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(5);
        final int SO_CONSUMER = 2;
        final Integer POISON = -1;   // "viên thuốc độc" báo consumer dừng

        // --- Producers: mỗi cái bỏ 10 món vào hàng bằng put() ---
        List<Thread> producers = new ArrayList<>();
        for (int p = 0; p < 2; p++) {
            int pid = p;
            Thread producer = new Thread(() -> {
                try {
                    for (int i = 0; i < 10; i++) {
                        int item = pid * 100 + i;
                        queue.put(item);   // CHỜ nếu hàng đầy (đủ 5 phần tử)
                        System.out.println("  Producer " + pid + " -> put " + item);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            producers.add(producer);
            producer.start();
        }

        // --- Consumers: lấy món ra xử lý bằng take(), dừng khi gặp POISON ---
        List<Thread> consumers = new ArrayList<>();
        for (int c = 0; c < SO_CONSUMER; c++) {
            int cid = c;
            Thread consumer = new Thread(() -> {
                try {
                    while (true) {
                        int item = queue.take();   // CHỜ nếu hàng rỗng
                        if (item == POISON) {
                            break;   // gặp thuốc độc → dừng

                        }
                        System.out.println("    Consumer " + cid + " <- take " + item);
                        Thread.sleep(20);   // giả lập xử lý
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            consumers.add(consumer);
            consumer.start();
        }

        // Chờ tất cả producer bỏ xong hàng
        for (Thread p : producers) {
            p.join();
        }

        // Gửi mỗi consumer một POISON để chúng thoát vòng lặp
        for (int i = 0; i < SO_CONSUMER; i++) {
            queue.put(POISON);
        }

        for (Thread c : consumers) {
            c.join();
        }
        System.out.println("  → Xong: 20 món đã được xử lý hết\n");
    }

    /* 2. Các biến thể KHÔNG chờ: offer / poll / remainingCapacity / drainTo */
    static void cacBienTheKhongCho() throws InterruptedException {
        System.out.println("=== 2. offer / poll / drainTo (không chờ) ===");
        BlockingQueue<String> q = new ArrayBlockingQueue<>(3);

        System.out.println("  offer(a) = " + q.offer("a"));   // true
        System.out.println("  offer(b) = " + q.offer("b"));   // true
        System.out.println("  offer(c) = " + q.offer("c"));   // true
        System.out.println("  offer(d) = " + q.offer("d"));   // false — ĐẦY, không chờ
        System.out.println("  remainingCapacity = " + q.remainingCapacity());  // 0

        System.out.println("  poll() = " + q.poll());                 // a
        System.out.println("  poll(1s) = " + q.poll(1, TimeUnit.SECONDS)); // b (chờ tối đa 1s)

        List<String> conLai = new ArrayList<>();
        q.drainTo(conLai);   // lấy HẾT phần tử còn lại trong một lần
        System.out.println("  drainTo -> " + conLai);           // [c]

        System.out.println("  poll() khi rỗng = " + q.poll());  // null — không chờ
    }

}

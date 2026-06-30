package Threads;

import java.util.ArrayList;
import java.util.List;

/**
 * Ví dụ nền tảng về Thread: Basics, Runnable, và join().
 *
 * Biên dịch & chạy: javac ThreadBasicsDemo.java java ThreadBasicsDemo
 */
public class ThreadBasicsDemo {

    public static void main(String[] args) throws InterruptedException {
        basics();
        startVsRun();
        dungRunnable();
        joinDemo();
        joinNhieuWorker();
    }

    /* 1. Basics: kế thừa Thread, override run(), gọi start() */
    static class MyThread extends Thread {

        @Override
        public void run() {
            System.out.println("  Chạy trong: " + Thread.currentThread().getName());
        }
    }

    static void basics() throws InterruptedException {
        System.out.println("=== 1. Basics ===");
        Thread t = new MyThread();
        t.start();  // gọi run() trong một thread mới
        t.join();   // đợi thread t kết thúc
        System.out.println("Chạy trong: " + Thread.currentThread().getName());
    }

    /* 2. Khác biệt giữa start() và run() */
    static void startVsRun() throws InterruptedException {
        System.out.println("=== 2. start() vs run() ===");
        System.out.println("Thread hiện tại: " + Thread.currentThread().getName());

        Runnable in = ()
                -> System.out.println("  -> đang chạy trong: " + Thread.currentThread().getName());

        Thread t1 = new Thread(in, "thread-moi");
        System.out.println("Gọi run() (SAI - chạy ngay trên main):");
        t1.run();    // chạy NGAY trên main, không tạo thread mới

        Thread t2 = new Thread(in, "thread-moi");
        System.out.println("Gọi start() (ĐÚNG - chạy trên thread mới):");
        t2.start();  // tạo thread mới
        t2.join();
        System.out.println();
    }

    /* 3. Dùng Runnable (cách nên dùng) */
    static void dungRunnable() throws InterruptedException {
        System.out.println("=== 3. Dùng Runnable ===");
        Runnable in = () -> System.out.println("  -> đang chạy trong: " + Thread.currentThread().getName());

        Thread t1 = new Thread(in, "thread-1");
        t1.start();
        t1.join();
        System.out.println();
    }

    /* 4. join() chờ một thread xong */
    static void joinDemo() throws InterruptedException {
        System.out.println("=== 4. join() demo ===");
        Runnable in = () -> {
            System.out.println("  -> đang chạy trong: " + Thread.currentThread().getName());
            try {
                Thread.sleep(1000);  // giả lập công việc tốn thời gian
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        Thread t1 = new Thread(in, "thread-1");
        Thread t2 = new Thread(in, "thread-2");

        t1.start();
        t2.start();

        System.out.println("Đợi thread-1 xong...");
        t1.join();  // đợi thread-1 xong
        System.out.println("Đợi thread-2 xong...");
        t2.join();  // đợi thread-2 xong
        System.out.println("Cả hai thread đã xong.\n");

    }

    /* 5. Khởi động nhiều thread rồi join tất cả */
    static void joinNhieuWorker() throws InterruptedException {
        System.out.println("=== 5. join() nhiều worker ===");
        List<Thread> workers = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            int id = i;
            Thread w = new Thread(() -> {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("  Worker " + id + " đang làm");
            });
            workers.add(w);
            w.start();   // khởi động hết → chạy song song
        }

        for (Thread w : workers) {
            w.join();    // sau đó chờ tất cả xong
        }
        System.out.println("  Tất cả worker đã hoàn tất");
    }
}

package Threads;

import java.util.ArrayList;
import java.util.List;

public class ThreadBuilderDemo {

    public static void main(String[] args) throws InterruptedException {
        platformThread();
        virtualThread();
        virtualThreadShortcut();
        tenTuDanhSo();
        unstarted();
        virtualThreadNhieu();
    }

    /* 1. Platform thread (1:1 với OS thread) bằng builder */
    static void platformThread() throws InterruptedException {
        System.out.println("=== 4. Platform Thread  ===");
        Thread t = Thread.ofPlatform().name("Worker-01")
                .daemon(false)
                .start(() -> System.out.println("Chay Trong:" + Thread.currentThread()));

        t.join();
        System.out.println("Thread đã hoàn thành.");
    }

    /* 2. Virtual thread (nhẹ, do JVM quản lý) — API gần như y hệt */
    static void virtualThread() throws InterruptedException {
        System.out.println("=== 5. Virtual Thread  ===");
        Thread t = Thread.ofVirtual().name("vt-worker").start(() -> System.out.println("Chay Trong:" + Thread.currentThread()));
        t.join();
        System.out.println("Thread đã hoàn thành.");
    }

    /* 3. Lối tắt ngắn gọn nhất cho virtual thread */
    static void virtualThreadShortcut() throws InterruptedException {
        System.out.println("=== 6. Virtual Thread Shortcut  ===");
        Thread t = Thread.startVirtualThread(() -> System.out.println("Chay Trong:" + Thread.currentThread()));
        t.join();
        System.out.println("Thread đã hoàn thành.");

    }

    /* 4. Đặt tên tự đánh số: tiền tố + số bắt đầu */
    static void tenTuDanhSo() throws InterruptedException {
        System.out.println("=== 7. Tên tự đánh số ===");
        Thread.Builder builder = Thread.ofVirtual().name("vt-worker-", 0);

        Thread a = builder.start(() -> System.out.println("Chay Trong:" + Thread.currentThread()));
        Thread b = builder.start(() -> System.out.println("Chay Trong:" + Thread.currentThread()));
        Thread c = builder.start(() -> System.out.println("Chay Trong:" + Thread.currentThread()));

        a.join();
        b.join();
        c.join();
        System.out.println("Cả 3 thread đã hoàn thành.");
    }

    /* 5. unstarted: tạo nhưng chưa chạy ngay */
    static void unstarted() throws InterruptedException {
        System.out.println("=== 8. unstarted ===");
        Thread t = Thread.ofPlatform().name("Worker-01").unstarted(() -> System.out.println("Chay Trong:" + Thread.currentThread()));
        System.out.println("Thread chưa chạy: " + t);
        t.start();
        t.join();
        System.out.println("Thread đã hoàn thành.");
    }

    /* 7. Bật nhiều virtual thread cùng lúc với tên đánh số */
    static void virtualThreadNhieu() throws InterruptedException {
        System.out.println("=== 9. Virtual Thread nhiều ===");
        Thread.Builder builder = Thread.ofVirtual().name("vt-worker-", 0);

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Thread t = builder.start(() -> {
                System.out.println("Chay Trong:" + Thread.currentThread());
                try {
                    Thread.sleep(500); // Giả lập công việc tốn thời gian
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            threads.add(t);
        }

        for (Thread t : threads) {
            t.join();
        }
        System.out.println("Cả 5 thread đã hoàn thành.");
    }
}

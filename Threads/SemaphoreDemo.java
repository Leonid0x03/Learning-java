package Threads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreDemo {

    public static void main(String[] args) throws InterruptedException {
        gioiHanDongThoi();
        tryAcquireKhongCho();
    }

    /* 1. Giới hạn số thread truy cập đồng thời: 3 phép, 6 task */
    static void gioiHanDongThoi() throws InterruptedException {
        System.out.println("=== 1. 3 giấy phép, 6 task (tối đa 3 chạy cùng lúc) ===");
        Semaphore semaphore = new Semaphore(3);   // 3 giấy phép
        List<Thread> tasks = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            int id = i;
            Thread t = new Thread(() -> {
                try {
                    semaphore.acquire();   // lấy 1 phép — HẾT thì chờ
                    System.out.println("  task " + id + " VÀO   (phép còn lại: " + semaphore.availablePermits() + ")");
                    Thread.sleep(400);   // giả lập dùng tài nguyên
                    System.out.println("  task " + id + " XONG, trả phép");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    semaphore.release();   // LUÔN trả phép trong finally
                }
            });
            tasks.add(t);
            t.start();
        }
        for (Thread t : tasks) {
            t.join();
        }
    }

    /* 2. tryAcquire: thử lấy phép, không được thì làm việc khác thay vì chờ */
    static void tryAcquireKhongCho() throws InterruptedException {
        System.out.println("=== 2. tryAcquire (không chờ) ===");
        Semaphore sem = new Semaphore(2);   // chỉ 2 phép
        sem.acquire();
        sem.acquire();
        System.out.println("  đã chiếm 2/2 phép, còn lại: " + sem.availablePermits());

        // thử lấy ngay — thất bại vì hết phép, KHÔNG chờ
        boolean ngay = sem.tryAcquire();
        System.out.println("  tryAcquire()      = " + ngay);   // false

        // thử chờ tối đa 300ms — vẫn thất bại vì không ai trả phép
        boolean coTimeout = sem.tryAcquire(300, TimeUnit.MILLISECONDS);
        System.out.println("  tryAcquire(300ms) = " + coTimeout);   // false
        // trả lại 1 phép rồi thử lại → thành công
        sem.release();
        boolean sauKhiTra = sem.tryAcquire();
        System.out.println("  sau khi release, tryAcquire() = " + sauKhiTra);   // true
    }

}

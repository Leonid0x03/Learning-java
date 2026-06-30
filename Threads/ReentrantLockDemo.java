package Threads;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockDemo {

    public static void main(String[] args) throws InterruptedException {
        demoCoBan();
        demoTryLock();
        demoTryLockTimeout();
        demoCondition();
    }

    /* 1. Cơ bản: lock() / unlock() trong try-finally → sửa race condition */
    static class CounterLock {

        private int count = 0;
        private final ReentrantLock lock = new ReentrantLock();

        void increment() {
            lock.lock();
            try {
                count++;
            } finally {
                lock.unlock();
            }
        }

        int getCount() {
            lock.lock();
            try {
                return count;
            } finally {
                lock.unlock();
            }
        }

    }

    static void demoCoBan() throws InterruptedException {
        System.out.println("=== 1. Cơ bản (lock/unlock) ===");
        CounterLock c = new CounterLock();

        Runnable job = () -> {
            for (int i = 0; i < 100_000; i++) {
                c.increment();

            }
        };
        Thread t1 = new Thread(job);
        Thread t2 = new Thread(job);
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("  count = " + c.getCount() + "  (luôn đúng 200000)\n");
    }

    /* 2. tryLock(): thử khóa, KHÔNG được thì làm việc khác thay vì chờ */
    static void demoTryLock() throws InterruptedException {
        System.out.println("=== 2. tryLock() (không chờ) ===");
        ReentrantLock lock = new ReentrantLock();

        // Thread A giữ khóa 300ms
        Thread a = new Thread(() -> {
            lock.lock();
            try {
                System.out.println("  A: lấy được khóa, giữ 300ms");
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        });

        // Thread B thử khóa ngay, không được thì bỏ qua
        Thread b = new Thread(() -> {
            if (lock.tryLock()) {     // trả về ngay: true nếu lấy được, false nếu bận
                try {
                    System.out.println("  B: lấy được khóa");
                } finally {
                    lock.unlock();
                }
            } else {
                System.out.println("  B: khóa đang bận → làm việc khác, không chờ");
            }
        });

        a.start();
        Thread.sleep(50);   // cho A kịp lấy khóa trước
        b.start();
        a.join();
        b.join();
        System.out.println();
    }

    /* 3. tryLock(timeout): chờ tối đa một khoảng rồi bỏ cuộc */
    static void demoTryLockTimeout() throws InterruptedException {
        System.out.println("=== 3. tryLock(timeout) ===");
        ReentrantLock lock = new ReentrantLock();

        Thread a = new Thread(() -> {
            lock.lock();
            try {
                System.out.println("  A: giữ khóa 200ms");
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        });

        Thread b = new Thread(() -> {
            try {
                // chờ tối đa 1 giây để lấy khóa
                if (lock.tryLock(1, TimeUnit.SECONDS)) {
                    try {
                        System.out.println("  B: chờ một chút rồi cũng lấy được khóa");
                    } finally {
                        lock.unlock();
                    }
                } else {
                    System.out.println("  B: chờ quá 1 giây vẫn không được → bỏ cuộc");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        a.start();
        Thread.sleep(50);
        b.start();
        a.join();
        b.join();
        System.out.println();
    }

    /* 4. Condition: await() / signal() để phối hợp giữa các thread */
    static void demoCondition() throws InterruptedException {
        System.out.println("=== 4. Condition (await / signal) ===");
        ReentrantLock lock = new ReentrantLock();
        Condition daSanSang = lock.newCondition();
        boolean[] sanSang = {false};

        // Consumer: chờ tới khi có dữ liệu
        Thread consumer = new Thread(() -> {
            lock.lock();
            try {
                while (!sanSang[0]) {
                    System.out.println("  consumer: chưa có dữ liệu, chờ...");
                    daSanSang.await();          // nhả khóa và ngủ tới khi được signal
                }
                System.out.println("  consumer: nhận được dữ liệu!");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        });

        // Producer: chuẩn bị dữ liệu rồi báo hiệu
        Thread producer = new Thread(() -> {
            try {
                Thread.sleep(300);   // giả lập chuẩn bị dữ liệu (ngoài khóa)
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            lock.lock();
            try {
                sanSang[0] = true;
                System.out.println("  producer: dữ liệu xong, báo hiệu");
                daSanSang.signal();            // đánh thức consumer đang await
            } finally {
                lock.unlock();
            }
        });

        consumer.start();
        producer.start();
        consumer.join();
        producer.join();
    }
}

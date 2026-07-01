package Threads;

import java.util.*;
import java.util.concurrent.locks.*;

/**
 * Ví dụ về ReadWriteLock (ReentrantReadWriteLock) trong Java.
 *
 * Một cache dùng chung: nhiều reader đọc SONG SONG, writer cập nhật ĐỘC QUYỀN.
 *
 * Biên dịch & chạy: javac ReadWriteLockDemo.java java ReadWriteLockDemo
 */
public class ReadWriteLockDemo {

    /* Cache đơn giản được bảo vệ bằng ReadWriteLock */
    static class Cache {

        private final Map<String, Integer> data = new HashMap<>();
        private final ReadWriteLock rw = new ReentrantReadWriteLock();
        private final Lock readLock = rw.readLock();
        private final Lock writeLock = rw.writeLock();

        Integer get(String key) {
            readLock.lock();                 // khóa ĐỌC — nhiều reader vào cùng lúc được
            try {
                System.out.println("    [đọc] " + Thread.currentThread().getName()
                        + " đọc " + key + " (readers đang giữ = " + ((ReentrantReadWriteLock) rw).getReadLockCount() + ")");
                sleep(200);                  // giả lập đọc mất thời gian
                return data.get(key);
            } finally {
                readLock.unlock();
            }
        }

        void put(String key, int value) {
            writeLock.lock();                // khóa GHI — độc quyền, mọi thread khác chờ
            try {
                System.out.println("  [GHI] " + Thread.currentThread().getName()
                        + " ghi " + key + "=" + value);
                sleep(200);
                data.put(key, value);
            } finally {
                writeLock.unlock();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Cache cache = new Cache();
        cache.put("x", 0);   // dữ liệu ban đầu

        List<Thread> threads = new ArrayList<>();

        // 4 reader đọc liên tục → chúng chạy SONG SONG với nhau
        for (int i = 0; i < 4; i++) {
            Thread r = new Thread(() -> {
                for (int k = 0; k < 2; k++) {
                    cache.get("x");
                }
            }, "reader-" + i);
            threads.add(r);
        }

        // 1 writer cập nhật → khi nó ghi, tất cả reader phải chờ
        Thread w = new Thread(() -> {
            for (int v = 1; v <= 2; v++) {
                cache.put("x", v);
            }
        }, "writer");
        threads.add(w);

        for (Thread t : threads) {
            t.start();
        }
        for (Thread t : threads) {
            t.join();
        }

        System.out.println("\nGiá trị cuối x = " + cache.get("x"));
    }

    static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

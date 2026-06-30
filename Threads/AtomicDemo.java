package Threads;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicDemo {

    public static void main(String[] args) throws InterruptedException {
        demoKhongDongBoViDangBo();
        demoCacMethod();
    }

    /* 1. So sánh: int thường (lỗi) vs AtomicInteger (đúng) */
    static void demoKhongDongBoViDangBo() throws InterruptedException {
        System.out.println("=== 1. int thường vs AtomicInteger ===");
        System.out.println("Kỳ vọng = 200000\n");

        // --- int thường: race condition ---
        var holder = new Object() {
            int count = 0;
        };
        Runnable jobThuong = () -> {
            for (int i = 0; i < 100_000; i++) {
                holder.count++;
        
            }};
        Thread a1 = new Thread(jobThuong);
        Thread a2 = new Thread(jobThuong);
        a1.start();
        a2.start();
        a1.join();
        a2.join();
        System.out.println("  int thường    = " + holder.count + "  (thường < 200000, mỗi lần khác)");

        // --- AtomicInteger: luôn đúng ---
        AtomicInteger count = new AtomicInteger(0);
        Runnable jobAtomic = () -> {
            for (int i = 0; i < 100_000; i++) {
                count.incrementAndGet();
        
            }};
        Thread b1 = new Thread(jobAtomic);
        Thread b2 = new Thread(jobAtomic);
        b1.start();
        b2.start();
        b1.join();
        b2.join();
        System.out.println("  AtomicInteger = " + count.get() + "  (luôn đúng 200000)\n");
    }

     /* 2. Các method thường dùng */
        static void demoCacMethod() {
        System.out.println("=== 2. Các method thường dùng ===");
        AtomicInteger n = new AtomicInteger(10);
 
        System.out.println("  get()              = " + n.get());                 // 10
        System.out.println("  incrementAndGet()  = " + n.incrementAndGet());      // 11 (++n)
        System.out.println("  getAndIncrement()  = " + n.getAndIncrement());      // 11 (trả cũ), giờ là 12
        System.out.println("  addAndGet(5)       = " + n.addAndGet(5));           // 17
        System.out.println("  updateAndGet(x*2)  = " + n.updateAndGet(x -> x * 2));// 34
        n.set(100);
        System.out.println("  sau set(100)       = " + n.get());                 // 100
        System.out.println();
    }

}

package Threads;

public class SynchronizedDemo {

    public static void main(String[] args) throws InterruptedException {
        demoKhongDongBo();
        demoSynchronizedMethod();
        demoSynchronizedBlock();
        demoTaiKhoanNganHang();
    }

    /* 1. KHÔNG đồng bộ → race condition, kết quả sai và mỗi lần một khác */
 /* 1. KHÔNG đồng bộ → race condition, kết quả sai và mỗi lần một khác */
    static void demoKhongDongBo() throws InterruptedException {
        System.out.println("=== 1. KHÔNG đồng bộ (lỗi) ===");
        var c = new Object() {
            int count = 0;
        };

        Runnable job = () -> {
            for (int i = 0; i < 100_000; i++) {
                c.count++;   // count++ KHÔNG nguyên tử

            }
        };
        Thread t1 = new Thread(job);
        Thread t2 = new Thread(job);
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("  count = " + c.count + "  (thường < 200000, chạy lại sẽ khác)\n");
    }

    /* 2. synchronized method → khóa trên monitor của 'this' */
    static class CounterMethod {

        private int count = 0;

        public synchronized void increment() {
            count++;
        }

        public synchronized int getCount() {
            return count;
        }
    }

    static void demoSynchronizedMethod() throws InterruptedException {

        System.out.println("=== 2. synchronized method ===");

        CounterMethod counter = new CounterMethod();
        Runnable job = () -> {
            for (int i = 0; i < 100_000; i++) {
                counter.increment();
            }
        };

        Thread t1 = new Thread(job);
        Thread t2 = new Thread(job);
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("  count = " + counter.getCount() + "  (luôn = 200000)\n");
    }

    /* 3. synchronized block → khóa trên một object lock riêng */
    static class CounterBlock {

        private int count = 0;
        private final Object lock = new Object();   // khóa riêng, không lộ ra ngoài

        public void increment() {
            synchronized (lock) {                   // chỉ bảo vệ đúng phần cần thiết
                count++;
            }
        }

        public int get() {
            synchronized (lock) {
                return count;
            }
        }
    }

    static void demoSynchronizedBlock() throws InterruptedException {
        System.out.println("=== 3. synchronized block (lock riêng) ===");
        CounterBlock c = new CounterBlock();

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

        System.out.println("  count = " + c.get() + "  (luôn đúng 200000)\n");
    }

    /* 4. Ví dụ thực tế: chuyển tiền giữa hai tài khoản, bảo vệ thao tác ghép */
    static class TaiKhoan {

        private long soDu;

        TaiKhoan(long soDuBanDau) {
            this.soDu = soDuBanDau;
        }

        // Cả trừ và cộng phải nằm trong CÙNG một khóa, nếu không số dư có thể sai
        synchronized void napTien(long tien) {
            soDu += tien;
        }

        synchronized void rutTien(long tien) {
            soDu -= tien;
        }

        synchronized long getSoDu() {
            return soDu;
        }
    }

    
    static void demoTaiKhoanNganHang() throws InterruptedException {
        System.out.println("=== 4. Tài khoản ngân hàng ===");
        TaiKhoan tk = new TaiKhoan(0);
 
        // 2 thread cùng nạp 1.000 đồng, 50.000 lần mỗi thread
        Runnable nap = () -> { for (int i = 0; i < 50_000; i++) tk.napTien(1_000); };
        Thread t1 = new Thread(nap);
        Thread t2 = new Thread(nap);
        t1.start(); t2.start();
        t1.join();  t2.join();
 
        System.out.println("  Số dư = " + tk.getSoDu() + "  (kỳ vọng 100000000)");
    }

}

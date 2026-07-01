package Threads;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchDemo {

    public static void main(String[] args) throws InterruptedException {
        choTatCaWorkerXong();
        congXuatPhat();
    }

    /* Chiều 1: main CHỜ cho tới khi N worker làm xong.
       latch = N; mỗi worker countDown(); main await(). */
    static void choTatCaWorkerXong() throws InterruptedException {
        System.out.println("=== 1. main chờ 3 worker xong ===");
        CountDownLatch latch = new CountDownLatch(3);// đếm = 3

        for (int i = 0; i < 3; i++) {
            int id = i;
            new Thread(() -> {
                try {
                    Thread.sleep((id + 1) * 200L);   // mỗi worker mất thời gian khác nhau
                    System.out.println("  worker " + id + " xong (còn lại " + (latch.getCount() - 1) + ")");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown();   // báo "tôi xong" → giảm đếm đi 1
                }
            }).start();
        }

        System.out.println("  main: đang chờ tất cả worker...");
        latch.await();   // CHẶN tới khi đếm về 0 (cả 3 đã countDown)
        System.out.println("  main: tất cả worker đã xong, chạy tiếp\n");

    }

    /* Chiều 2: "cổng xuất phát" — nhiều thread CÙNG chờ một hiệu lệnh,
       rồi được thả ra CÙNG LÚC. latch = 1; các runner await(); main countDown(). */
    static void congXuatPhat() throws InterruptedException {
        System.out.println("=== 2. cổng xuất phát (thả cùng lúc) ===");
        CountDownLatch congXuatPhat = new CountDownLatch(1);   // đếm = 1

        for (int i = 0; i < 3; i++) {
            int id = i;
            new Thread(() -> {
                try {
                    System.out.println("  runner " + id + " sẵn sàng, chờ hiệu lệnh...");
                    congXuatPhat.await();   // tất cả dừng chờ ở đây
                    System.out.println("    runner " + id + " CHẠY!");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }

        Thread.sleep(500);   // giả lập chuẩn bị (để các runner kịp vào trạng thái chờ)
        System.out.println("  main: Bắt đầu!");
        congXuatPhat.countDown();   // mở cổng → cả 3 runner được thả cùng lúc

        Thread.sleep(300);   // chờ chút cho các runner in xong
    }

}

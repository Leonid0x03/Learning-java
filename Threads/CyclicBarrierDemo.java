package Threads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierDemo {

    public static void main(String[] args) throws InterruptedException {
        final int SO_WORKER = 3;
        final int SO_VONG = 3;

        // barrierAction: chạy MỘT lần mỗi khi cả 3 worker tới đủ (do thread tới cuối chạy)
        CyclicBarrier barrier = new CyclicBarrier(SO_WORKER, () -> {
            System.out.println("  -- cả " + SO_WORKER + " worker đã tới, sang vòng tiếp --");
        });

        List<Thread> workers = new ArrayList<>();
        for (int w = 0; w < SO_WORKER; w++) {
            int id = w;
            Thread t = new Thread(() -> {
                try {
                    for (int vong = 1; vong <= SO_VONG; vong++) {
                        // làm việc — mỗi worker mất thời gian khác nhau nên tới đích lệch nhau
                        Thread.sleep((id + 1) * 150L);
                        System.out.println("worker " + id + " xong vòng " + vong
                                + " (đang chờ: " + (barrier.getNumberWaiting()) + ")");

                        // chờ 2 worker kia cùng xong vòng này rồi mới đi tiếp
                        int thuTuToi = barrier.await();   // trả về "thứ tự tới" (N-1 = tới đầu, 0 = tới cuối)

                        if (thuTuToi == 0) {
                            // thread tới cuối cùng — chỉ để minh họa giá trị trả về
                            // (barrierAction ở trên đã chạy xong trước dòng này)
                        }
                    }
                } catch (InterruptedException | BrokenBarrierException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("worker " + id + " bị gián đoạn");
                }
            });
            workers.add(t);
            t.start();
        }

        for (Thread t : workers) {
            t.join();
        }
        System.out.println("Tất cả worker đã hoàn tất " + SO_VONG + " vòng");
    }
}

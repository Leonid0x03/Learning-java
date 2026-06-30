package Threads;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExecutorDemo {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        coBan();
        layKetQua();
        dinhTuyenThreadPool();
        // shutdownVsShutdownNow();
    }

    /* 1. Cơ bản: pool cố định, submit Runnable, shutdown */
    static void coBan() throws InterruptedException {
        System.out.println("=== 1. Pool cố định cơ bản ===");

        ExecutorService executor = Executors.newFixedThreadPool(3);

        for (int i = 1; i <= 5; i++) {
            int id = i;
            executor.execute(()
                    -> System.out.println("  việc " + id + " chạy ở " + Thread.currentThread().getName()));
        }
        executor.shutdown();
        executor.awaitTermination(1, java.util.concurrent.TimeUnit.SECONDS);
    }

    /* 2. submit Callable → lấy kết quả qua Future */
    static void layKetQua() throws InterruptedException, ExecutionException {
        System.out.println("=== 2. submit + Future ===");
        ExecutorService es = Executors.newFixedThreadPool(2);

        Future<Integer> f1 = es.submit(() -> {
            Thread.sleep(500);
            return 6 * 7;
        });

        System.out.println("  Đã nộp, chờ kết quả...");
        System.out.println("  Kết quả = " + f1.get());   // 42

        es.shutdown();
        System.out.println();
    }

    /* 3. ThreadPoolExecutor tự cấu hình: thấy rõ core → queue → thread phụ → từ chối */
    static void dinhTuyenThreadPool() throws InterruptedException {
        System.out.println("=== 3. Định tuyến của ThreadPoolExecutor ===");
        System.out.println("(core=2, max=4, queue=2 → tối đa nhận 6 task cùng lúc, dư bị từ chối)\n");

        ThreadPoolExecutor pool = new ThreadPoolExecutor(
                2, 4,// corePoolSize = 2, maximumPoolSize = 4
                60, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(2),// queue chứa tối đa 2 task
                new ThreadPoolExecutor.AbortPolicy());// quá tải → ném RejectedExecutionException

        for (int i = 1; i <= 8; i++) {
            int id = i;
            try {
                pool.execute(() -> {
                    System.out.println("    -> task " + id + " chạy ở " + Thread.currentThread().getName());
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
                System.out.println("  nộp task " + id + " OK   (poolSize=" + pool.getPoolSize()
                        + ", queue=" + pool.getQueue().size() + ")");
            } catch (RejectedExecutionException e) {
                System.out.println("  task " + id + " BỊ TỪ CHỐI (queue đầy + đã đạt max thread)");
            }
        }

        pool.shutdown();
        pool.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println();
    }
}

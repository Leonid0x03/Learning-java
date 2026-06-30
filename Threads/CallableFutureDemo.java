package Threads;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CallableFutureDemo {

    public static void main(String[] args) throws Exception {
        coBan();
        xuLyException();
    }

    /* 1. Cơ bản: nộp Callable, lấy kết quả qua Future.get() */
    static void coBan() throws Exception {
        System.out.println("=== 1. Callable & Future ===");
        ExecutorService executor = Executors.newFixedThreadPool(2);
        Callable<String> task = () -> {
            System.out.println("  -> đang chạy trong: " + Thread.currentThread().getName());
            Thread.sleep(1000);
            return "Kết quả từ Callable";
        };

        Future<String> future = executor.submit(task);

        String result = future.get(); // đợi task hoàn thành và lấy kết quả    
        System.out.println("Kết quả: " + result);
        executor.shutdown();
        System.out.println("Executor đã shutdown.");
    }

    /* 2. Xử lý exception ném ra từ trong task */
    static void xuLyException() throws InterruptedException {
        System.out.println("=== 2. Exception Handling ===");
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Callable<String> loi = () -> {
            System.out.println("  -> đang chạy trong: " + Thread.currentThread().getName());
            throw new IllegalStateException("Hong roi");
        };

        Future<String> future = executor.submit(loi);

        try {
            future.get(); // đợi task hoàn thành và lấy kết quả
        } catch (ExecutionException e) {
            System.out.println("Bắt exception từ task: " + e.getCause().getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            executor.shutdown();
            System.out.println("Executor đã shutdown.");
        }
    }

}

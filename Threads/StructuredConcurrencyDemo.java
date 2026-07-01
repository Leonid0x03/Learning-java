package Threads;

import java.util.concurrent.*;

/**
 * Ví dụ về Structured Concurrency (StructuredTaskScope) — API Java 21
 * (PREVIEW).
 *
 * Biên dịch & chạy (BẮT BUỘC bật preview, JDK 21): javac --release 21
 * --enable-preview StructuredConcurrencyDemo.java java --enable-preview
 * StructuredConcurrencyDemo
 *
 * Trên JDK mới hơn, API có thể khác (StructuredTaskScope.open + Joiner).
 */
public class StructuredConcurrencyDemo {

    public static void main(String[] args) throws Exception {
        thanhCong();
        motCaiLoi();
        aiXongTruocThang();
    }

    /* Vài "việc" giả lập tốn thời gian */
    static String layUser() throws InterruptedException {
        Thread.sleep(300);
        return "User(An)";
    }

    static String layDonHang() throws InterruptedException {
        Thread.sleep(500);
        return "3 đơn hàng";
    }

    static String layLoi() throws InterruptedException {
        Thread.sleep(200);
        throw new RuntimeException("DB sập");
    }

    /* 1. ShutdownOnFailure: cả hai subtask phải thành công, rồi gộp kết quả */
    static void thanhCong() throws InterruptedException, ExecutionException {
        System.out.println("=== 1. ShutdownOnFailure — cả hai thành công ===");
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            StructuredTaskScope.Subtask<String> user = scope.fork(() -> layUser());
            StructuredTaskScope.Subtask<String> order = scope.fork(() -> layDonHang());

            scope.join();            // chờ CẢ HAI
            scope.throwIfFailed();   // cái nào lỗi thì ném ở đây

            System.out.println("  kết quả = " + user.get() + " + " + order.get() + "\n");
        }
    }

    /* 2. Một subtask lỗi → subtask còn lại tự bị hủy, throwIfFailed ném lỗi */
    static void motCaiLoi() throws InterruptedException {
        System.out.println("=== 2. ShutdownOnFailure — một cái lỗi ===");
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            scope.fork(() -> layUser());   // đang chạy thì bị hủy khi cái kia lỗi
            scope.fork(() -> layLoi());    // lỗi sau 200ms

            scope.join();
            scope.throwIfFailed();   // ném ExecutionException bọc lỗi gốc
            System.out.println("  (không tới được đây)");
        } catch (ExecutionException e) {
            System.out.println("  bắt được lỗi: " + e.getCause().getMessage()
                    + " → subtask còn lại đã bị hủy\n");
        }
    }

    /* 3. ShutdownOnSuccess: ai xong trước thắng, phần còn lại bị hủy ngay */
    static void aiXongTruocThang() throws InterruptedException, ExecutionException {
        System.out.println("=== 3. ShutdownOnSuccess — ai xong trước thắng ===");
        try (var scope = new StructuredTaskScope.ShutdownOnSuccess<String>()) {
            scope.fork(() -> {
                Thread.sleep(500);
                return "nguồn CHẬM (500ms)";
            });
            scope.fork(() -> {
                Thread.sleep(150);
                return "nguồn NHANH (150ms)";
            });

            scope.join();   // chờ tới khi có MỘT cái thành công

            String ketQua = scope.result();   // kết quả của cái xong trước
            System.out.println("  thắng cuộc = " + ketQua);
        }
    }
}

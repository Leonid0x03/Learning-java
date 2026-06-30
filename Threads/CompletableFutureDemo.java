package Threads;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureDemo {

    public static void main(String[] args) throws Exception {
        chuoiNoiTiep();
        thenCompose();
        thenCombineSongSong();
        xuLyLoi();
        choTatCa();
    }

    /* tiện ích: giả lập một việc tốn thời gian */
    static String cham(String ten, long ms, String ketQua) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("    [" + ten + "] chạy ở " + Thread.currentThread().getName());
        return ketQua;
    }

    /* 1. Chuỗi nối tiếp: supplyAsync → thenApply → thenAccept */
    static void chuoiNoiTiep() {
        System.out.println("=== 1. Chuỗi nối tiếp ===");
        CompletableFuture.supplyAsync(() -> cham("lấy user", 300, "An")) // chạy nền
                .thenApply(ten -> "User: " + ten) // biến đổi
                .thenApply(String::toUpperCase) // biến đổi tiếp
                .thenAccept(s -> System.out.println("  kết quả = " + s)) // dùng kết quả
                .join();                                                       // chờ chuỗi xong
        System.out.println();
    }

    /* 2. thenCompose: bước sau cũng trả về CompletableFuture */
    static CompletableFuture<String> layUserId() {
        return CompletableFuture.supplyAsync(() -> cham("lấy id", 200, "id-42"));
    }

    static CompletableFuture<String> layTenTheoId(String id) {
        return CompletableFuture.supplyAsync(() -> cham("lấy tên theo " + id, 200, "Bình"));
    }

    static void thenCompose() {
        System.out.println("=== 2. thenCompose (nối async phụ thuộc) ===");
        String ten = layUserId()
                .thenCompose(id -> layTenTheoId(id)) // làm phẳng CompletableFuture lồng nhau
                .join();
        System.out.println("  tên = " + ten + "\n");
    }

    /* 3. thenCombine: gộp hai future chạy SONG SONG */
    static void thenCombineSongSong() {
        System.out.println("=== 3. thenCombine (song song) ===");
        long batDau = System.currentTimeMillis();

        CompletableFuture<String> user = CompletableFuture.supplyAsync(() -> cham("user", 1000, "Thông tin user"));
        CompletableFuture<String> donHang = CompletableFuture.supplyAsync(() -> cham("đơn hàng", 1000, "Danh sách đơn"));

        String gop = user.thenCombine(donHang, (u, d) -> u + " + " + d).join();  // chờ cả hai
        long thoiGian = System.currentTimeMillis() - batDau;

        System.out.println("  gộp = " + gop);
        System.out.println("  thời gian ~" + thoiGian + "ms (≈1000, KHÔNG phải 2000 → chạy song song)\n");
    }

    /* 4. exceptionally: xử lý lỗi trong chuỗi */
    static void xuLyLoi() {
        System.out.println("=== 4. exceptionally (xử lý lỗi) ===");
        String kq = CompletableFuture.supplyAsync(() -> {
            if (true) {
                throw new IllegalStateException("DB sập");
            }
            return "dữ liệu";
        })
                .thenApply(s -> s.toUpperCase()) // bị bỏ qua vì có lỗi
                .exceptionally(ex -> "GIÁ TRỊ MẶC ĐỊNH (" + ex.getMessage() + ")") // phục hồi
                .join();
        System.out.println("  kết quả = " + kq + "\n");
    }

    /* 5. allOf: chờ nhiều future cùng hoàn tất */
    static void choTatCa() {
        System.out.println("=== 5. allOf (chờ tất cả) ===");
        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> cham("f1", 300, "A"));
        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> cham("f2", 500, "B"));
        CompletableFuture<String> f3 = CompletableFuture.supplyAsync(() -> cham("f3", 200, "C"));

        CompletableFuture.allOf(f1, f2, f3).join();   // chờ cả ba

        // tới đây cả ba đã xong, join() lấy kết quả không còn chặn nữa
        System.out.println("  tất cả xong: " + f1.join() + ", " + f2.join() + ", " + f3.join());
    }

}

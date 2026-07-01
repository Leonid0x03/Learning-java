package Threads;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrentHashMapDemo {

    public static void main(String[] args) throws InterruptedException {
        demoMergeDaLuong();
        demoComputeIfAbsent();
        demoPutIfAbsent();
        demoComputeVaComputeIfPresent();
    }

    /* 1. merge(): đếm tần suất an toàn khi NHIỀU thread cùng đếm */
    static void demoMergeDaLuong() throws InterruptedException {
        System.out.println("=== 1. merge() — đếm đa luồng ===");
        ConcurrentHashMap<String, Integer> dem = new ConcurrentHashMap<>();
        String[] tu = {"java", "go", "java", "rust", "java", "go"};
        // 4 thread, mỗi thread đếm toàn bộ mảng 10.000 lần
        ExecutorService es = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 4; i++) {
            es.submit(() -> {
                for (int j = 0; j < 10_000; j++) {
                    for (String t : tu) {
                        dem.merge(t, 1, Integer::sum); // nếu k chưa có thì đặt v; nếu đã có thì đặt fn(giá_trị_cũ, v)
                    }
                }
            });
        }
        es.shutdown();
        es.awaitTermination(1, java.util.concurrent.TimeUnit.MINUTES);

        System.out.println("  " + dem + "  (java=120000, go=80000, rust=40000 → luôn đúng)\n");

    }

    /* 2. computeIfAbsent(): dựng "map chứa list" (multimap) an toàn */
    static void demoComputeIfAbsent() {
        System.out.println("=== 2. computeIfAbsent() — dựng multimap ===");
        ConcurrentHashMap<String, List<String>> map = new ConcurrentHashMap<>();

        String[][] nhanVien = {
            {"Backend", "An"}, {"QA", "Cường"}, {"Backend", "Bình"}, {"QA", "Dung"}
        };

        for (String[] nv : nhanVien) {
            String phongBan = nv[0];
            String tenNV = nv[1];
            // nếu key chưa có → tạo list mới rồi trả về; nếu có → trả list cũ. Rồi add vào.
            map.computeIfAbsent(phongBan, k -> new java.util.concurrent.CopyOnWriteArrayList<>()).add(tenNV);

        }

        System.out.println("  " + map + "\n");
        // {Backend=[An, Bình], QA=[Cường, Dung]}
    }

    /* 3. putIfAbsent(): khởi tạo một lần, không ghi đè */
    static void demoPutIfAbsent() {
        System.out.println("=== 3. putIfAbsent() — khởi tạo một lần ===");
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        String cu = map.putIfAbsent("host", "localhost");
        System.out.println("  lần 1: đặt 'localhost', trả về = " + cu); // chưa có → đặt, trả null
        String moi = map.putIfAbsent("host", "1 .2.3.4");
        System.out.println("  lần 2: trả về giá trị đang có = " + moi); // đã có → KHÔNG ghi đè, trả giá trị cũ
        System.out.println("  giá trị cuối = " + map.get("host") + "\n");  // vẫn là localhost

    }

    /* 4. compute() và computeIfPresent() */
    static void demoComputeVaComputeIfPresent() {
        System.out.println("=== 4. compute() / computeIfPresent() ===");
        ConcurrentHashMap<String, Integer> tonKho = new ConcurrentHashMap<>();
        tonKho.put("SP01", 10);

        // compute: tính lại từ giá trị cũ (cũ có thể null nếu key chưa có)
        tonKho.compute("SP01", (k, v) -> v + 5);        // 10 → 15
        tonKho.compute("SP02", (k, v) -> (v == null ? 0 : v) + 1);  // chưa có → 0+1 = 1
        System.out.println("  sau compute: " + tonKho);   // {SP01=15, SP02=1}

        // computeIfPresent: chỉ chạy nếu key đã có; trả null thì XÓA entry
        tonKho.computeIfPresent("SP01", (k, v) -> v - 15);   // 15-15 = 0... vẫn giữ (0 khác null)
        tonKho.computeIfPresent("SP02", (k, v) -> null);     // trả null → XÓA SP02
        tonKho.computeIfPresent("SP99", (k, v) -> v + 1);    // không có SP99 → không làm gì
        System.out.println("  sau computeIfPresent: " + tonKho);   // {SP01=0}
    }

}


import java.util.*;

/**
 * Ví dụ tổng hợp về ArrayList trong Java.
 *
 * Biên dịch & chạy: javac ArrayListDemo.java java ArrayListDemo
 */
public class ArrayListDemo {

    public static void main(String[] args) {
        khoiTao();
        themDocCapNhatDuyet();
        timKiem();
        xoaDungCach();
        locVaSapXep();
        chuyenDoiMang();
    }

    /* 1. Các cách khởi tạo */
    static void khoiTao() {
        System.out.println("=== 1. Khởi tạo ===");
        List<String> a = new ArrayList<>();                       // rỗng
        List<String> b = new ArrayList<>(1000);                   // đặt sẵn capacity, tránh resize
        List<String> c = new ArrayList<>(List.of("An", "Bình"));  // copy từ collection khác, sửa được

        List<String> batBien = List.of("x", "y");                 // CHÚ Ý: bất biến, add() sẽ ném exception

        System.out.println("a = " + a + ", c = " + c + ", batBien = " + batBien + "\n");
    }

    /* 2. Thêm, đọc, cập nhật, duyệt */
    static void themDocCapNhatDuyet() {
        System.out.println("=== 2. Thêm / đọc / cập nhật / duyệt ===");
        List<String> tasks = new ArrayList<>();
        tasks.add("Thiết kế DB");           // thêm vào cuối — O(1) khấu hao
        tasks.add("Viết API");
        tasks.add(0, "Phân tích yêu cầu");  // chèn đầu — O(n) vì phải dịch phần tử

        tasks.set(1, "Thiết kế schema");    // cập nhật theo chỉ số
        System.out.println("get(0) = " + tasks.get(0));   // Phân tích yêu cầu
        System.out.println("size   = " + tasks.size());

        System.out.println("-- for-each --");
        for (String t : tasks) {
            System.out.println("  " + t);
        }

        System.out.println("-- for có chỉ số --");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println("  " + i + ": " + tasks.get(i));
        }
        System.out.println();
    }

    /* 3. Tìm kiếm và kiểm tra */
    static void timKiem() {
        System.out.println("=== 3. Tìm kiếm ===");
        List<String> ds = new ArrayList<>(List.of("a", "b", "c", "b"));

        System.out.println("contains(b) = " + ds.contains("b")); // true
        System.out.println("indexOf(b)  = " + ds.indexOf("b"));  // 1 (vị trí đầu tiên)
        System.out.println("isEmpty     = " + ds.isEmpty());     // false
        System.out.println();
    }

    /* 4. Xóa đúng cách — chỗ hay sai nhất */
    static void xoaDungCach() {
        System.out.println("=== 4. Xóa đúng cách ===");
        List<Integer> nums = new ArrayList<>(List.of(10, 20, 30, 40));

        nums.remove(2);                    // xóa phần tử ở CHỈ SỐ 2 → bỏ số 30
        System.out.println("sau remove(2)= " + nums);  // [10, 20, 40]

        nums.remove(Integer.valueOf(20));  // xóa theo GIÁ TRỊ 20
        System.out.println("sau remove(Integer.valueOf)= " + nums);  // [10, 40]

        // Xóa theo điều kiện: ĐỪNG vừa for-each vừa remove() (ném ConcurrentModificationException)
        List<Integer> xs = new ArrayList<>(List.of(5, 12, 8, 20, 3));
        xs.removeIf(x -> x > 10);          // cách đúng, quét một lượt O(n)
        System.out.println("sau removeIf(>10)= " + xs);    // [5, 8, 3]
        System.out.println();
    }

    /* 5. Lọc và sắp xếp với object */
    record NhanVien(String ten, String phongBan, double luong) {

    }

    static void locVaSapXep() {
        System.out.println("=== 5. Lọc và sắp xếp ===");
        List<NhanVien> ds = new ArrayList<>(List.of(
                new NhanVien("An", "Backend", 25_000_000),
                new NhanVien("Bình", "Backend", 32_000_000),
                new NhanVien("Cường", "QA", 20_000_000),
                new NhanVien("Dung", "Backend", 28_000_000)
        ));

        // Sắp xếp giảm dần theo lương — SỬA TRỰC TIẾP trên list
        ds.sort(Comparator.comparingDouble(NhanVien::luong).reversed());
        System.out.println("-- sắp theo lương giảm dần --");
        ds.forEach(nv -> System.out.println("  " + nv.ten() + " - " + (long) nv.luong()));

        // Nhiều tiêu chí: phòng ban tăng dần, trong cùng phòng thì lương giảm dần
        ds.sort(Comparator.comparing(NhanVien::phongBan)
                .thenComparing(Comparator.comparingDouble(NhanVien::luong).reversed()));
        System.out.println("-- sắp theo phòng ban, rồi lương --");
        ds.forEach(nv -> System.out.println("  " + nv.phongBan() + " | " + nv.ten()));

        // Lọc sang list MỚI bằng Stream — không đụng list gốc
        List<NhanVien> backendLuongCao = ds.stream()
                .filter(nv -> nv.phongBan().equals("Backend"))
                .filter(nv -> nv.luong() >= 28_000_000)
                .toList();   // Java 16+
        System.out.println("-- Backend lương >= 28tr --");
        backendLuongCao.forEach(nv -> System.out.println("  " + nv.ten()));
        System.out.println();
    }

    /* 6. Chuyển đổi qua lại với mảng */
    static void chuyenDoiMang() {
        System.out.println("=== 6. Chuyển đổi với mảng ===");
        List<String> ds = new ArrayList<>(List.of("a", "b", "c"));

        String[] arr = ds.toArray(new String[0]);              // ArrayList → mảng
        System.out.println("toArray         = " + Arrays.toString(arr));

        List<String> tuMang = new ArrayList<>(Arrays.asList(arr)); // mảng → ArrayList sửa được
        tuMang.add("d");
        System.out.println("mảng → ArrayList = " + tuMang);

        List<String> gop = new ArrayList<>(ds);
        gop.addAll(tuMang);                                    // nối hai list
        System.out.println("addAll = " + gop);
    }
}

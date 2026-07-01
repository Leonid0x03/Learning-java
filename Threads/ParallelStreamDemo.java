package Threads;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntUnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ParallelStreamDemo {

    public static void main(String[] args) {
        cachTao();
        soSanhThoiGian();
        reduceVaCollect();
        cacBayCanTranh();
    }

    /* 1. Hai cách tạo parallel stream */
    static void cachTao() {
        System.out.println("=== 1. Cách tạo ===");
        List<Integer> list = List.of(1, 2, 3, 4, 5);

        long a = list.parallelStream().count();   // cách 1: từ collection
        long b = list.stream().parallel().count(); // cách 2: từ stream
        long c = IntStream.rangeClosed(1, 5).parallel().count(); // cách 3: từ IntStream

        System.out.println("  đếm = " + a + ", " + b + ", " + c + "\n");

    }

    /* 2. So sánh thời gian trên tác vụ NẶNG mỗi phần tử */
    static void soSanhThoiGian() {
        System.out.println("=== 2. Tuần tự vs song song (tác vụ nặng) ===");

        // tác vụ giả lập tốn CPU cho mỗi phần tử
        IntUnaryOperator nang = n -> {
            long s = 0;
            for (int i = 1; i <= 2000; i++) {
                s += (long) (i * n);
            }
            return (int) (s % 1000);
        };

        long t1 = System.currentTimeMillis();
        long tongTuanTu = IntStream.rangeClosed(1, 2_000_000).map(nang).asLongStream().sum();
        long tgTuanTu = System.currentTimeMillis() - t1;

        long t2 = System.currentTimeMillis();
        long tongSongSong = IntStream.rangeClosed(1, 2_000_000).parallel().map(nang).asLongStream().sum();
        long tgSongSong = System.currentTimeMillis() - t2;

        System.out.println("  tuần tự  : " + tongTuanTu + " (~" + tgTuanTu + "ms)");
        System.out.println("  song song: " + tongSongSong + " (~" + tgSongSong + "ms)");
        System.out.println("  (song song thắng khi mỗi phần tử tốn công & máy nhiều nhân)\n");
    }

    /* 3. reduce và collect an toàn với parallel */
    static void reduceVaCollect() {
        System.out.println("=== 3. reduce / collect ===");

        // sum bằng reduce — phép cộng có tính kết hợp nên an toàn khi song song
        int tong = IntStream.rangeClosed(1, 100).parallel().reduce(0, Integer::sum);
        System.out.println("  tổng 1..100 = " + tong);   // 5050

        // groupingBy song song — collector được thiết kế để gộp kết quả an toàn
        Map<Boolean, List<Integer>> chanLe = IntStream.rangeClosed(1, 10).boxed()
                .parallel()
                .collect(Collectors.groupingBy(n -> n % 2 == 0));
        System.out.println("  chẵn = " + chanLe.get(true));
        System.out.println("  lẻ   = " + chanLe.get(false) + "\n");
    }

    /* 4. Các bẫy cần tránh với parallel stream */
    static void cacBayCanTranh() {
        System.out.println("=== 4. Bẫy cần tránh ===");

        // BẪY: ghi vào biến/collection dùng chung không an toàn → SAI hoặc mất phần tử
        List<Integer> sai = new ArrayList<>();               // ArrayList KHÔNG thread-safe
        try {
            IntStream.rangeClosed(1, 100_000).parallel().forEach(sai::add);   // race!
        } catch (Exception e) {
            System.out.println("  forEach + ArrayList.add → có thể ném exception");
        }
        System.out.println("  kích thước 'sai' = " + sai.size() + " (thường KHÁC 100000 → hỏng)");

        // ĐÚNG: để stream tự gộp bằng collect (không tự ghi ra ngoài)
        List<Integer> dung = IntStream.rangeClosed(1, 100_000).parallel().boxed().collect(Collectors.toList());
        System.out.println("  kích thước 'dung' (collect) = " + dung.size() + " (luôn đúng 100000)");

        // Nếu buộc phải đếm bằng biến chung → dùng Atomic
        AtomicInteger dem = new AtomicInteger();
        IntStream.rangeClosed(1, 100_000).parallel().forEach(n -> dem.incrementAndGet());
        System.out.println("  đếm bằng AtomicInteger = " + dem.get() + " (đúng 100000)");
    }

}

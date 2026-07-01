package Threads;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

public class ForkJoinDemo {

    public static void main(String[] args) {
        long[] arr = new long[20_000_000];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = i + 1;   // 1..20 triệu
        }
        tinhTongCoKetQua(arr);
        soSanhTuanTuVsSongSong(arr);
        recursiveActionKhongKetQua();
    }

    /* 1. RecursiveTask<Long>: chia mảng để tính tổng, có TRẢ KẾT QUẢ */
    static class SumTask extends RecursiveTask<Long> {

        private static final int NGUONG = 50_000;   // đủ nhỏ thì tính thẳng
        private final long[] arr;
        private final int lo, hi;

        SumTask(long[] arr, int lo, int hi) {
            this.arr = arr;
            this.lo = lo;
            this.hi = hi;
        }

        @Override
        protected Long compute() {
            if (hi - lo <= NGUONG) {                // task đủ nhỏ → tính trực tiếp
                long s = 0;
                for (int i = lo; i < hi; i++) {
                    s += arr[i];
                }
                return s;
            }
            int mid = (lo + hi) >>> 1;
            SumTask trai = new SumTask(arr, lo, mid);
            SumTask phai = new SumTask(arr, mid, hi);

            trai.fork();                            // đẩy nửa trái cho pool chạy song song
            long ketQuaPhai = phai.compute();       // tự tính nửa phải ngay
            long ketQuaTrai = trai.join();          // chờ & lấy kết quả nửa trái
            return ketQuaTrai + ketQuaPhai;         // gộp
        }

    }

    static void tinhTongCoKetQua(long[] arr) {
        System.out.println("=== 1. RecursiveTask — tính tổng ===");
        ForkJoinPool pool = ForkJoinPool.commonPool();
        System.out.println("  số nhân dùng (parallelism) = " + pool.getParallelism());

        long tong = pool.invoke(new SumTask(arr, 0, arr.length));
        System.out.println("  tổng 1..20 triệu = " + tong + "\n");
    }

    /* 2. So sánh thời gian: tuần tự vs fork/join */
    static void soSanhTuanTuVsSongSong(long[] arr) {
        System.out.println("=== 2. Tuần tự vs ForkJoin ===");

        long t1 = System.currentTimeMillis();
        long tongTuanTu = 0;
        for (long v : arr) {
            tongTuanTu += v;
        }
        long thoiGianTuanTu = System.currentTimeMillis() - t1;

        long t2 = System.currentTimeMillis();
        long tongSongSong = ForkJoinPool.commonPool().invoke(new SumTask(arr, 0, arr.length));
        long thoiGianSongSong = System.currentTimeMillis() - t2;

        System.out.println("  tuần tự  : " + tongTuanTu + " (~" + thoiGianTuanTu + "ms)");
        System.out.println("  fork/join: " + tongSongSong + " (~" + thoiGianSongSong + "ms)");
        System.out.println("  (song song thường nhanh hơn khi dữ liệu đủ lớn & máy nhiều nhân)\n");
    }

    /* 3. RecursiveAction: chia việc nhưng KHÔNG trả kết quả (biến đổi tại chỗ) */
    static class NhanDoiTask extends RecursiveAction {

        private static final int NGUONG = 50_000;
        private final long[] arr;
        private final int lo, hi;

        NhanDoiTask(long[] arr, int lo, int hi) {
            this.arr = arr;
            this.lo = lo;
            this.hi = hi;
        }

        @Override
        protected void compute() {
            if (hi - lo <= NGUONG) {
                for (int i = lo; i < hi; i++) {
                    arr[i] *= 2;   // nhân đôi tại chỗ

                                }return;
            }
            int mid = (lo + hi) >>> 1;
            // invokeAll: fork cả hai và chờ cả hai xong
            invokeAll(new NhanDoiTask(arr, lo, mid), new NhanDoiTask(arr, mid, hi));
        }
    }

    static void recursiveActionKhongKetQua() {
        System.out.println("=== 3. RecursiveAction — biến đổi mảng ===");
        long[] nho = {1, 2, 3, 4, 5};
        ForkJoinPool.commonPool().invoke(new NhanDoiTask(nho, 0, nho.length));
        System.out.print("  sau khi nhân đôi: ");
        for (long v : nho) {
            System.out.print(v + " ");   // 2 4 6 8 10

                }System.out.println();
    }

}

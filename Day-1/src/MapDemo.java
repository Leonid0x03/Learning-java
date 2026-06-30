import java.util.*;

/**
 * Ví dụ tổng hợp về Map trong Java.
 *
 * Biên dịch & chạy:
 *   javac MapDemo.java
 *   java MapDemo
 */
public class MapDemo {

    public static void main(String[] args) {
        thaoTacCoBan();
        duyetQuaBaView();
        methodTienLoi();
        soSanhBaLopTrienKhai();
    }

    /* 1. Thao tác cơ bản: put / get / remove và quy tắc ghi đè */
    static void thaoTacCoBan() {
        System.out.println("=== 1. Thao tác cơ bản ===");
        Map<String, Integer> tuoi = new HashMap<>();
        tuoi.put("An", 25);
        tuoi.put("Bình", 30);
        tuoi.put("An", 26);   // key "An" đã có → GHI ĐÈ value cũ, không thêm mới

        System.out.println("get(An)         = " + tuoi.get("An"));        // 26
        System.out.println("get(Cường)      = " + tuoi.get("Cường"));     // null
        System.out.println("containsKey(Bình)= " + tuoi.containsKey("Bình")); // true
        System.out.println("size            = " + tuoi.size());          // 2 (không phải 3)

        tuoi.remove("Bình");
        System.out.println("sau remove Bình = " + tuoi + "\n");          // {An=26}
    }

    /* 2. Ba cách duyệt: entrySet, keySet, values, forEach */
    static void duyetQuaBaView() {
        System.out.println("=== 2. Duyệt qua ba view ===");
        Map<String, Integer> tuoi = new HashMap<>();
        tuoi.put("An", 26);
        tuoi.put("Bình", 30);
        tuoi.put("Cường", 28);

        System.out.println("-- entrySet (nên dùng: lấy cả key lẫn value) --");
        for (Map.Entry<String, Integer> e : tuoi.entrySet()) {
            System.out.println(e.getKey() + " → " + e.getValue());
        }

        System.out.println("-- forEach (gọn hơn) --");
        tuoi.forEach((ten, t) -> System.out.println(ten + " → " + t));

        System.out.println("-- chỉ key --   " + tuoi.keySet());
        System.out.println("-- chỉ value -- " + tuoi.values());
        System.out.println();
    }

    /* 3. Các method tiện lợi từ Java 8 */
    static void methodTienLoi() {
        System.out.println("=== 3. Method tiện lợi (Java 8+) ===");

        Map<String, Integer> m = new HashMap<>();

        // getOrDefault: không có thì trả mặc định, tránh null
        System.out.println("getOrDefault    = " + m.getOrDefault("x", 0));  // 0

        // putIfAbsent: chỉ thêm nếu key chưa có
        m.putIfAbsent("a", 1);
        m.putIfAbsent("a", 99);   // đã có "a" → bỏ qua
        System.out.println("putIfAbsent     = " + m);   // {a=1}

        // merge: đếm tần suất chỉ trong 1 dòng
        String[] tu = {"a", "b", "a", "c", "a", "b"};
        Map<String, Integer> dem = new HashMap<>();
        for (String t : tu) {
            dem.merge(t, 1, Integer::sum);   // có thì +1, chưa có thì đặt 1
        }
        System.out.println("merge (đếm)     = " + dem);  // {a=3, b=2, c=1}

        // computeIfAbsent: dựng "map of list" (multimap)
        Map<String, List<String>> nhom = new HashMap<>();
        nhom.computeIfAbsent("BE", k -> new ArrayList<>()).add("An");
        nhom.computeIfAbsent("BE", k -> new ArrayList<>()).add("Bình");
        nhom.computeIfAbsent("QA", k -> new ArrayList<>()).add("Cường");
        System.out.println("computeIfAbsent = " + nhom); // {BE=[An, Bình], QA=[Cường]}
        System.out.println();
    }

    /* 4. So sánh thứ tự duyệt của ba lớp triển khai */
    static void soSanhBaLopTrienKhai() {
        System.out.println("=== 4. HashMap vs LinkedHashMap vs TreeMap ===");

        // Thêm cùng thứ tự cho cả ba
        List<String> keys = List.of("banana", "apple", "cherry");

        Map<String, Integer> hash   = new HashMap<>();
        Map<String, Integer> linked = new LinkedHashMap<>();
        Map<String, Integer> tree   = new TreeMap<>();

        for (int i = 0; i < keys.size(); i++) {
            hash.put(keys.get(i), i);
            linked.put(keys.get(i), i);
            tree.put(keys.get(i), i);
        }

        System.out.println("HashMap       = " + hash);    // thứ tự không đoán trước
        System.out.println("LinkedHashMap = " + linked);  // {banana=0, apple=1, cherry=2} - thứ tự thêm
        System.out.println("TreeMap       = " + tree);    // {apple=1, banana=0, cherry=2} - key sắp xếp

        // TreeMap còn có thao tác điều hướng
        TreeMap<String, Integer> t = (TreeMap<String, Integer>) tree;
        System.out.println("firstKey      = " + t.firstKey());      // apple
        System.out.println("lastKey       = " + t.lastKey());       // cherry
        System.out.println("ceilingKey(b) = " + t.ceilingKey("b")); // banana
    }
}
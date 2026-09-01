import java.util.ArrayList;
import java.util.List;

public class IntegerStringHashTable {
    // 儲存鍵值對的 Entry 結構
    private record Entry(int key, String value) {}

    private final List<List<Entry>> buckets;
    private int size = 0; // 記錄總資料筆數

    public IntegerStringHashTable(int bucketCount) {
        if (bucketCount <= 0) {
            throw new IllegalArgumentException("bucketCount must be positive");
        }
        buckets = new ArrayList<>();
        for (int i = 0; i < bucketCount; i++) {
            buckets.add(new ArrayList<>());
        }
    }

    // 計算 bucket index
    private int getIndex(int key) {
        return Math.floorMod(Integer.hashCode(key), buckets.size());
    }

    // 新增或更新資料
    public void put(int key, String value) {
        List<Entry> chain = buckets.get(getIndex(key));
        
        // 尋找相同 key 並更新 value
        for (int i = 0; i < chain.size(); i++) {
            if (chain.get(i).key() == key) {
                chain.set(i, new Entry(key, value));
                return; // 更新後直接返回，size 不增加
            }
        }
        
        // 若找不到相同 key，則新增 Entry
        chain.add(new Entry(key, value));
        size++;
    }

    // 取得資料
    public String get(int key) {
        List<Entry> chain = buckets.get(getIndex(key));
        for (Entry e : chain) {
            if (e.key() == key) {
                return e.value();
            }
        }
        return null;
    }

    // 檢查是否包含指定 key
    public boolean containsKey(int key) {
        return get(key) != null;
    }

    // 移除資料
    public boolean remove(int key) {
        List<Entry> chain = buckets.get(getIndex(key));
        boolean removed = chain.removeIf(entry -> entry.key() == key);
        if (removed) {
            size--;
        }
        return removed;
    }

    // 回傳總資料筆數
    public int size() {
        return size;
    }

    // 輸出 Hash Table 的分佈狀況
    public void bucketReport() {
        System.out.println("--- bucket report (size=" + size + ") ---");
        for (int i = 0; i < buckets.size(); i++) {
            List<Entry> chain = buckets.get(i);
            System.out.println("bucket[" + i + "]=" + chain);
        }
    }

    public static void main(String[] args) {
        IntegerStringHashTable table = new IntegerStringHashTable(5);

        // 測試新增
        table.put(10, "Apple");
        table.put(22, "Banana"); // 可能與 10 產生 collision
        table.put(-3, "Cherry"); // 測試負數 key
        table.put(15, "Date");
        
        // 測試更新
        table.put(10, "Avocado"); // key 10 的值更新，size 不變
        
        table.bucketReport();
        
        // 測試查詢與移除
        System.out.println("get(22)=" + table.get(22));
        System.out.println("containsKey(-3)=" + table.containsKey(-3));
        
        System.out.println("remove(15)=" + table.remove(15));
        System.out.println("size after remove=" + table.size());
    }
}
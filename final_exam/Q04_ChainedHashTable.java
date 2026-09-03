import java.util.ArrayList;
import java.util.List;

public class Q04_ChainedHashTable {
    
    // 儲存鍵值對的 Entry
    private record Entry(int key, String value) {}

    private final List<List<Entry>> buckets;
    private int size;

    // 建構子：初始化 bucket，並在數量不合法時拋出例外
    public Q04_ChainedHashTable(int bucketCount) {
        if (bucketCount <= 0) {
            throw new IllegalArgumentException("bucketCount must be positive");
        }
        buckets = new ArrayList<>(bucketCount);
        for (int i = 0; i < bucketCount; i++) {
            buckets.add(new ArrayList<>());
        }
    }

    // 計算 index，使用 floorMod 確保負數 key 也能得到非負的 index
    private int getIndex(int key) {
        return Math.floorMod(key, buckets.size());
    }

    // 新增或更新資料
    public void put(int key, String value) {
        List<Entry> chain = buckets.get(getIndex(key));
        
        // 尋找是否已存在相同的 key，若存在則更新 value，size 不增加
        for (int i = 0; i < chain.size(); i++) {
            if (chain.get(i).key() == key) {
                chain.set(i, new Entry(key, value));
                return;
            }
        }
        
        // 若不同 key 發生 collision 或為新 key，皆加入 chain 並增加 size
        chain.add(new Entry(key, value));
        size++;
    }

    // 取得資料
    public String get(int key) {
        List<Entry> chain = buckets.get(getIndex(key));
        for (Entry entry : chain) {
            if (entry.key() == key) {
                return entry.value();
            }
        }
        return null;
    }

    // 移除資料
    public boolean remove(int key) {
        List<Entry> chain = buckets.get(getIndex(key));
        for (int i = 0; i < chain.size(); i++) {
            if (chain.get(i).key() == key) {
                chain.remove(i);
                size--;
                return true;
            }
        }
        return false;
    }

    // 取得目前資料總數
    public int size() {
        return size;
    }

    // 計算最長的 chain 長度
    public int longestChain() {
        int max = 0;
        for (List<Entry> chain : buckets) {
            if (chain.size() > max) {
                max = chain.size();
            }
        }
        return max;
    }

    public static void main(String[] args) {
        Q04_ChainedHashTable table = new Q04_ChainedHashTable(3);

        System.out.println("--- normal put & collision ---");
        table.put(1, "A");
        table.put(4, "B"); // index=1, collision 與 key=1
        table.put(-2, "C"); // index=1, 測試負數 key 且發生 collision
        
        System.out.println("size=" + table.size());
        System.out.println("longestChain=" + table.longestChain()); // 預期為 3 (key: 1, 4, -2)

        System.out.println("\n--- update existing key ---");
        table.put(4, "B_updated");
        System.out.println("get 4=" + table.get(4));
        System.out.println("size after update=" + table.size());

        System.out.println("\n--- get & remove ---");
        System.out.println("get 1=" + table.get(1));
        System.out.println("get -2=" + table.get(-2));
        System.out.println("get unknown=" + table.get(99));

        System.out.println("remove 4=" + table.remove(4));
        System.out.println("size after remove=" + table.size());
        System.out.println("longestChain after remove=" + table.longestChain());
        System.out.println("remove unknown=" + table.remove(99));

        System.out.println("\n--- constructor exception test ---");
        try {
            new Q04_ChainedHashTable(0);
        } catch (IllegalArgumentException e) {
            System.out.println("exception caught=" + e.getMessage());
        }
    }
}
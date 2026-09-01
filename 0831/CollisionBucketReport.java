import java.util.ArrayList;
import java.util.List;

public class CollisionBucketReport {
    private final List<List<Integer>> buckets;

    public CollisionBucketReport(int bucketCount) {
        if (bucketCount <= 0) {
            throw new IllegalArgumentException("bucketCount must be positive");
        }
        buckets = new ArrayList<>();
        for (int i = 0; i < bucketCount; i++) {
            buckets.add(new ArrayList<>());
        }
    }

    // 計算 bucket index，必須使用 floorMod 防止負數 index
    private int getIndex(int key) {
        return Math.floorMod(Integer.hashCode(key), buckets.size());
    }

    // 將 key 放入對應的 bucket
    public void put(int key) {
        List<Integer> chain = buckets.get(getIndex(key));
        
        // 處理重複 key：若已存在則不重複加入，模擬 Set/Map 行為
        if (!chain.contains(key)) {
            chain.add(key);
        }
    }

    // 輸出統計報告
    public void report() {
        int totalCollisions = 0;
        int maxChain = 0;

        System.out.println("--- Bucket Report ---");
        for (int i = 0; i < buckets.size(); i++) {
            List<Integer> chain = buckets.get(i);
            System.out.println("bucket[" + i + "]=" + chain);

            // 計算最長 chain
            if (chain.size() > maxChain) {
                maxChain = chain.size();
            }

            // 計算 collision 數量 (大於 1 筆的資料即為發生 collision)
            if (chain.size() > 1) {
                totalCollisions += (chain.size() - 1);
            }
        }
        System.out.println("total collisions=" + totalCollisions);
        System.out.println("max chain length=" + maxChain);
    }

    public static void main(String[] args) {
        CollisionBucketReport report = new CollisionBucketReport(5);
        
        // 測試資料：包含負 key (-3)、重複 key (7, 7)
        int[] keys = {12, 7, 22, -3, 7, 42, 100};
        for (int k : keys) {
            report.put(k);
        }
        report.report();

        System.out.println("\n--- Empty Input Test ---");
        CollisionBucketReport emptyReport = new CollisionBucketReport(3);
        emptyReport.report(); // 測試空輸入
    }
}
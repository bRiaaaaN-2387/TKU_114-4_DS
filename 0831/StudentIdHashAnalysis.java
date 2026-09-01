import java.util.ArrayList;
import java.util.List;

public class StudentIdHashAnalysis {

    // 執行分析並輸出各項統計數據
    public static void analyze(List<String> studentIds, int bucketCount) {
        if (bucketCount <= 0) {
            throw new IllegalArgumentException("bucketCount must be positive");
        }

        // 初始化 buckets
        List<List<String>> buckets = new ArrayList<>();
        for (int i = 0; i < bucketCount; i++) {
            buckets.add(new ArrayList<>());
        }

        // 將學號映射至對應的 bucket
        for (String id : studentIds) {
            int hash = Math.floorMod(id.hashCode(), bucketCount);
            buckets.get(hash).add(id);
        }

        int totalCollisions = 0;
        int maxChain = 0;

        System.out.println("--- analysis for bucket count: " + bucketCount + " ---");
        for (int i = 0; i < buckets.size(); i++) {
            int chainSize = buckets.get(i).size();
            System.out.println("bucket[" + i + "] size=" + chainSize);
            
            // 計算最長 chain
            if (chainSize > maxChain) {
                maxChain = chainSize;
            }
            
            // 計算 collision 次數 (大於 1 筆即代表發生 collision)
            if (chainSize > 1) {
                totalCollisions += (chainSize - 1);
            }
        }

        // 計算平均 chain 長度 (總筆數 / bucket 數量)
        double avgChain = (double) studentIds.size() / bucketCount;

        System.out.println("total collisions=" + totalCollisions);
        System.out.println("max chain length=" + maxChain);
        System.out.printf("average chain length=%.2f\n\n", avgChain);
    }

    public static void main(String[] args) {
        // 準備測試用學號清單
        List<String> ids = List.of(
            "B11001001", "B11001002", "B11001003", "B11001004", "B11001005",
            "B11002001", "B11002002", "B11002003", "B11003001", "B11003002"
        );

        System.out.println("total students=" + ids.size() + "\n");

        // 比較兩種不同的 bucket count (例如：5 與 11)
        analyze(ids, 5);
        analyze(ids, 11);
    }
}
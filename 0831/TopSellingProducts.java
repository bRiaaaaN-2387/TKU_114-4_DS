import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class TopSellingProducts {
    // 定義商品結構
    record Product(String id, int sales) {}

    public static List<Product> getTopK(List<Product> input, int k) {
        if (input == null || k <= 0) return List.of();

        // 1. 合併重複商品 id 的銷量
        Map<String, Integer> salesMap = new HashMap<>();
        for (Product p : input) {
            if (p == null || p.id() == null) continue;
            salesMap.put(p.id(), salesMap.getOrDefault(p.id(), 0) + p.sales());
        }

        // 2. 建立 Min Heap 保留 Top-K
        // 比較規則：銷量小者優先出局；若銷量相同，id 字典序大者優先出局
        Comparator<Product> comparator = Comparator
                .comparingInt(Product::sales)
                .thenComparing(Product::id, Comparator.reverseOrder());

        PriorityQueue<Product> minHeap = new PriorityQueue<>(comparator);

        for (Map.Entry<String, Integer> entry : salesMap.entrySet()) {
            minHeap.offer(new Product(entry.getKey(), entry.getValue()));
            
            // 當容量超過 K，移除目前 Heap 中最差的商品 (銷量最低或 id 最大)
            if (minHeap.size() > k) {
                minHeap.poll();
            }
        }

        // 3. 將結果轉為 List，並依最終的排序規則 (銷量大到小，id 小到大) 輸出
        List<Product> result = new ArrayList<>(minHeap);
        result.sort(Comparator.comparingInt(Product::sales).reversed()
                .thenComparing(Product::id));
        return result;
    }

    public static void main(String[] args) {
        List<Product> products = List.of(
            new Product("item-C", 50),
            new Product("item-A", 100),
            new Product("item-B", 200),
            new Product("item-A", 100), // 與上面的 item-A 合併，總銷量 200
            new Product("item-D", 150),
            new Product("item-C", 100)  // 與上面的 item-C 合併，總銷量 150
        );

        System.out.println("--- Top 3 Products ---");
        List<Product> top3 = getTopK(products, 3);
        for (Product p : top3) {
            System.out.println("id=" + p.id() + " total sales=" + p.sales());
        }
    }
}
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class LowestKPriceTracker {
    
    public static List<Integer> getLowestK(List<Integer> prices, int k) {
        if (prices == null || k <= 0) {
            return List.of();
        }

        // 使用 Max Heap 來保留最小的 K 筆資料
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());

        for (Integer price : prices) {
            // 忽略 null 與負數
            if (price == null || price < 0) {
                continue;
            }
            
            maxHeap.offer(price);
            
            // 若容量超過 K，移除目前最大的數值
            if (maxHeap.size() > k) {
                maxHeap.poll();
            }
        }

        // 將結果轉為 List 並依價格遞增排列
        List<Integer> result = new ArrayList<>(maxHeap);
        result.sort(Comparator.naturalOrder());
        return result;
    }

    public static void main(String[] args) {
        List<Integer> prices = new ArrayList<>();
        prices.add(150);
        prices.add(99);
        prices.add(-10); // 負數應被忽略
        prices.add(200);
        prices.add(null); // null 應被忽略
        prices.add(45);
        prices.add(120);
        prices.add(80);

        System.out.println("lowest 3=" + getLowestK(prices, 3));
        System.out.println("lowest 0=" + getLowestK(prices, 0));
        System.out.println("null list=" + getLowestK(null, 3));
    }
}
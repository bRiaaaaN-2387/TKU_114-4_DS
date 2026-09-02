import java.util.ArrayDeque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class BfsLayerReport {
    
    // 計算從 start 出發到各個 vertex 的最少 edge 數
    public static Map<String, Integer> reportLayers(Map<String, List<String>> graph, String start) {
        Map<String, Integer> distances = new LinkedHashMap<>();
        
        // 處理邊界條件：graph 為空、start 為 null 或 start 不在圖中
        if (graph == null || start == null || !graph.containsKey(start)) {
            return distances;
        }

        Queue<String> queue = new ArrayDeque<>();
        
        // 加入起點，距離為 0 (此 Map 兼作 visited 檢查)
        queue.offer(start);
        distances.put(start, 0); 

        while (!queue.isEmpty()) {
            String current = queue.poll();
            int currentLayer = distances.get(current);

            // 走訪所有相鄰的 vertex
            for (String next : graph.getOrDefault(current, List.of())) {
                if (graph.containsKey(next) && !distances.containsKey(next)) {
                    distances.put(next, currentLayer + 1);
                    queue.offer(next);
                }
            }
        }
        return distances;
    }

    public static void main(String[] args) {
        Map<String, List<String>> graph = new LinkedHashMap<>();
        graph.put("A", List.of("B", "C"));
        graph.put("B", List.of("A", "D"));
        graph.put("C", List.of("A", "D"));
        graph.put("D", List.of("B", "C"));
        graph.put("E", List.of()); // 測試孤立節點

        System.out.println("--- normal case (start A) ---");
        reportLayers(graph, "A").forEach((v, d) -> 
            System.out.println("vertex=" + v + " distance=" + d)
        );

        System.out.println("\n--- isolated vertex case (start E) ---");
        System.out.println("result=" + reportLayers(graph, "E"));

        System.out.println("\n--- missing vertex case (start X) ---");
        System.out.println("result=" + reportLayers(graph, "X"));

        System.out.println("\n--- empty graph case ---");
        System.out.println("result=" + reportLayers(new LinkedHashMap<>(), "A"));
    }
}
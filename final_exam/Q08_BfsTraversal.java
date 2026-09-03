import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Q08_BfsTraversal {

    // 執行 BFS 並回傳走訪順序
    public static List<String> bfs(Map<String, List<String>> graph, String start) {
        List<String> result = new ArrayList<>();
        
        // 處理無效輸入或起點不存在於圖中的邊界情況
        if (graph == null || start == null || !graph.containsKey(start)) {
            return result;
        }

        Queue<String> queue = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();

        queue.offer(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            result.add(current);

            List<String> neighbors = graph.getOrDefault(current, List.of());
            for (String next : neighbors) {
                // 確保鄰居存在於圖中，且尚未被走訪
                if (graph.containsKey(next) && visited.add(next)) {
                    queue.offer(next);
                }
            }
        }
        return result;
    }

    // 計算起點到其他可達節點的最短距離
    public static Map<String, Integer> distanceFrom(Map<String, List<String>> graph, String start) {
        Map<String, Integer> distances = new LinkedHashMap<>();
        
        // 處理無效輸入
        if (graph == null || start == null || !graph.containsKey(start)) {
            return distances;
        }

        Queue<String> queue = new ArrayDeque<>();
        
        queue.offer(start);
        distances.put(start, 0); // 起點距離為 0

        while (!queue.isEmpty()) {
            String current = queue.poll();
            int currentDist = distances.get(current);

            List<String> neighbors = graph.getOrDefault(current, List.of());
            for (String next : neighbors) {
                // 利用 distances 的 key 來兼作 visited 檢查
                if (graph.containsKey(next) && !distances.containsKey(next)) {
                    distances.put(next, currentDist + 1);
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
        graph.put("C", List.of("D", "E"));
        graph.put("D", List.of("B")); // A-B-D 形成 cycle，測試 visited 機制
        graph.put("E", List.of());
        graph.put("F", List.of("A")); // 從 A 出發不可達的節點

        System.out.println("--- normal BFS & distance case ---");
        System.out.println("BFS order from A=" + bfs(graph, "A"));
        System.out.println("Distance from A=" + distanceFrom(graph, "A"));

        System.out.println("\n--- disconnected / unreachable case ---");
        System.out.println("BFS order from E=" + bfs(graph, "E"));
        System.out.println("Distance from E=" + distanceFrom(graph, "E"));

        System.out.println("\n--- invalid input cases ---");
        System.out.println("BFS invalid start X=" + bfs(graph, "X"));
        System.out.println("Distance invalid start X=" + distanceFrom(graph, "X"));
        System.out.println("BFS null graph=" + bfs(null, "A"));
        System.out.println("Distance null start=" + distanceFrom(graph, null));
    }
}
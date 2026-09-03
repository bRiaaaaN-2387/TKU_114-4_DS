import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Q10_UnweightedShortestPath {

    // 使用 BFS 與 predecessor 尋找最短路徑
    public static List<String> shortestPath(Map<String, List<String>> graph, String start, String target) {
        // 邊界條件：無效輸入或空指標
        if (graph == null || start == null || target == null) {
            return List.of();
        }
        // 邊界條件：起點或終點不存在於圖中 (missing vertex)
        if (!graph.containsKey(start) || !graph.containsKey(target)) {
            return List.of();
        }
        // 邊界條件：起點等於終點，回傳單一元素 List
        if (start.equals(target)) {
            return List.of(start);
        }

        Queue<String> queue = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();
        Map<String, String> predecessor = new HashMap<>(); // 記錄前驅節點

        queue.offer(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            String current = queue.poll();

            // 提早結束：已找到目標節點
            if (current.equals(target)) {
                break;
            }

            // 依 adjacency List 順序走訪，確保同距離時的多條路徑能有穩定順序
            for (String next : graph.getOrDefault(current, List.of())) {
                if (graph.containsKey(next) && visited.add(next)) {
                    predecessor.put(next, current); // 記錄路徑來源
                    queue.offer(next);
                }
            }
        }

        // 若目標節點未被走訪，代表無法到達
        if (!visited.contains(target)) {
            return List.of();
        }
        
        // 透過 predecessor 反向重建路徑
        List<String> path = new ArrayList<>();
        for (String step = target; step != null; step = predecessor.get(step)) {
            path.add(step);
        }
        Collections.reverse(path); // 將反向追溯的路徑反轉為正向

        return path;
    }

    public static void main(String[] args) {
        Map<String, List<String>> graph = new LinkedHashMap<>();
        // 建立有向圖測試資料 (包含菱形結構測試多條最短路徑)
        graph.put("A", List.of("B", "C")); // 依順序 B 優先
        graph.put("B", List.of("D", "E"));
        graph.put("C", List.of("E", "F"));
        graph.put("D", List.of("G"));
        graph.put("E", List.of("G")); // A-B-E-G 與 A-C-E-G 長度相同，預期走 B
        graph.put("F", List.of("G"));
        graph.put("G", List.of());
        graph.put("H", List.of()); // 孤立節點

        System.out.println("--- normal shortest path ---");
        System.out.println("Path A -> G=" + shortestPath(graph, "A", "G")); // 預期 A, B, E, G

        System.out.println("\n--- start equals target case ---");
        System.out.println("Path C -> C=" + shortestPath(graph, "C", "C")); // 預期 [C]

        System.out.println("\n--- unreachable case ---");
        System.out.println("Path A -> H=" + shortestPath(graph, "A", "H")); // 預期 []
        System.out.println("Path H -> A=" + shortestPath(graph, "H", "A")); // 預期 []

        System.out.println("\n--- missing vertex & empty cases ---");
        System.out.println("Path A -> X=" + shortestPath(graph, "A", "X")); // 預期 []
        System.out.println("Path X -> A=" + shortestPath(graph, "X", "A")); // 預期 []
        System.out.println("Path null graph=" + shortestPath(null, "A", "B")); // 預期 []
        System.out.println("Path empty graph=" + shortestPath(new LinkedHashMap<>(), "A", "B")); // 預期 []
    }
}
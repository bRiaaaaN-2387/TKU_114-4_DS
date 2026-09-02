import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class DirectedReachability {
    
    // 定義查詢結構
    public record Query(String from, String to) {}

    // 判斷單一 from-to 路徑是否存在
    public static boolean isReachable(Map<String, List<String>> graph, String start, String target) {
        if (graph == null || start == null || target == null) return false;
        if (!graph.containsKey(start) || !graph.containsKey(target)) return false;
        
        Queue<String> queue = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();
        
        queue.offer(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            
            // 提早停止：找到目標節點
            if (current.equals(target)) {
                return true;
            }
            
            for (String next : graph.getOrDefault(current, List.of())) {
                if (graph.containsKey(next) && visited.add(next)) {
                    queue.offer(next);
                }
            }
        }
        return false;
    }

    // 處理多組查詢並輸出報告
    public static void processQueries(Map<String, List<String>> graph, List<Query> queries) {
        System.out.println("--- reachability report ---");
        for (Query q : queries) {
            boolean reachable = isReachable(graph, q.from(), q.to());
            System.out.println("query " + q.from() + "->" + q.to() + "=" + reachable);
        }
    }

    public static void main(String[] args) {
        Map<String, List<String>> graph = new LinkedHashMap<>();
        graph.put("A", List.of("B"));
        graph.put("B", List.of("C"));
        graph.put("C", List.of());
        graph.put("D", List.of("A"));
        graph.put("X", List.of()); // 孤立節點

        // 測試多組不同情境的查詢
        List<Query> testQueries = List.of(
            new Query("A", "C"), // 正常可達
            new Query("C", "A"), // 有向圖不可逆
            new Query("X", "X"), // 起點等於終點
            new Query("A", "Z"), // 節點缺失
            new Query("Z", "A")  // 節點缺失
        );

        System.out.println("--- normal graph queries ---");
        processQueries(graph, testQueries);

        System.out.println("\n--- empty graph queries ---");
        processQueries(new LinkedHashMap<>(), testQueries);
    }
}
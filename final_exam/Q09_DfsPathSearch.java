import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Q09_DfsPathSearch {

    // 執行 Recursive DFS 並回傳走訪順序
    public static List<String> dfs(Map<String, List<String>> graph, String start) {
        List<String> result = new ArrayList<>();
        // 邊界檢查：無效輸入或節點不存在
        if (graph == null || start == null || !graph.containsKey(start)) {
            return result;
        }
        
        Set<String> visited = new HashSet<>();
        dfsVisit(graph, start, visited, result);
        return result;
    }

    // DFS 遞迴走訪核心邏輯
    private static void dfsVisit(Map<String, List<String>> graph, String current, Set<String> visited, List<String> result) {
        // 利用 Set 的特性檢查並加入 visited，防範 cycle
        if (!visited.add(current)) {
            return;
        }
        result.add(current);

        // 依照 Adjacency List 順序走訪鄰居
        for (String next : graph.getOrDefault(current, List.of())) {
            // 確保鄰居存在於圖中才繼續
            if (graph.containsKey(next)) {
                dfsVisit(graph, next, visited, result);
            }
        }
    }

    // 判斷 target 是否可由 start 抵達
    public static boolean reachable(Map<String, List<String>> graph, String start, String target) {
        if (graph == null || start == null || target == null) return false;
        
        // start 與 target 都必須存在於圖中
        if (!graph.containsKey(start) || !graph.containsKey(target)) return false;
        
        // 兩者皆存在且相同時，直接回傳 true
        if (start.equals(target)) return true;

        Set<String> visited = new HashSet<>();
        return checkReachable(graph, start, target, visited);
    }

    // Reachability 遞迴核心邏輯 (提早結束)
    private static boolean checkReachable(Map<String, List<String>> graph, String current, String target, Set<String> visited) {
        if (current.equals(target)) return true;
        if (!visited.add(current)) return false;

        for (String next : graph.getOrDefault(current, List.of())) {
            if (graph.containsKey(next)) {
                if (checkReachable(graph, next, target, visited)) {
                    return true; // 提早結束，不需走訪全圖
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        Map<String, List<String>> graph = new LinkedHashMap<>();
        graph.put("A", List.of("B", "C"));
        graph.put("B", List.of("D"));
        graph.put("C", List.of("D", "E"));
        graph.put("D", List.of("A")); // A-B-D-A 形成 cycle
        graph.put("E", List.of());
        graph.put("F", List.of("A")); // 從 A 無法抵達的孤立節點

        System.out.println("--- normal DFS & cycle prevention ---");
        System.out.println("DFS order from A=" + dfs(graph, "A"));
        System.out.println("Reachable A -> E=" + reachable(graph, "A", "E"));

        System.out.println("\n--- start equals target case ---");
        System.out.println("Reachable C -> C=" + reachable(graph, "C", "C"));

        System.out.println("\n--- unreachable case ---");
        System.out.println("Reachable A -> F=" + reachable(graph, "A", "F"));
        System.out.println("DFS order from F=" + dfs(graph, "F"));

        System.out.println("\n--- missing vertex & empty cases ---");
        System.out.println("DFS missing vertex X=" + dfs(graph, "X"));
        System.out.println("Reachable missing start X -> A=" + reachable(graph, "X", "A"));
        System.out.println("Reachable missing target A -> X=" + reachable(graph, "A", "X"));
        System.out.println("DFS null graph=" + dfs(null, "A"));
        System.out.println("Reachable empty graph=" + reachable(new LinkedHashMap<>(), "A", "B"));
    }
}
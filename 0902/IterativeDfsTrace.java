import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class IterativeDfsTrace {

    // 執行 Iterative DFS 並追蹤 Stack 與 visited 狀態
    public static void traceDfs(Map<String, List<String>> graph, String start) {
        if (graph == null || start == null || !graph.containsKey(start)) {
            System.out.println("invalid graph or start node");
            return;
        }

        ArrayDeque<String> stack = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();

        // 初始化起點
        stack.push(start);
        System.out.println("push=" + start + " stack=" + stack + " visited=" + visited);

        while (!stack.isEmpty()) {
            String current = stack.pop();
            System.out.println("pop=" + current + " stack=" + stack + " visited=" + visited);

            // 若已被走訪則跳過 (處理 cycle 問題)
            if (!visited.add(current)) {
                continue;
            }

            // 反向將相鄰節點放入 Stack，以確保執行順序與 Recursive DFS 一致
            List<String> neighbors = graph.getOrDefault(current, List.of());
            for (int i = neighbors.size() - 1; i >= 0; i--) {
                String next = neighbors.get(i);
                if (graph.containsKey(next) && !visited.contains(next)) {
                    stack.push(next);
                    System.out.println("push=" + next + " stack=" + stack + " visited=" + visited);
                }
            }
        }
    }

    public static void main(String[] args) {
        Map<String, List<String>> graph = new LinkedHashMap<>();
        graph.put("A", List.of("B", "C"));
        graph.put("B", List.of("D"));
        graph.put("C", List.of("D"));
        graph.put("D", List.of("A")); // 刻意製造 cycle
        graph.put("E", List.of());    // 孤立節點

        System.out.println("--- normal case (start A) ---");
        traceDfs(graph, "A");

        System.out.println("\n--- isolated vertex case (start E) ---");
        traceDfs(graph, "E");

        System.out.println("\n--- missing vertex case (start X) ---");
        traceDfs(graph, "X");

        System.out.println("\n--- empty graph case ---");
        traceDfs(new LinkedHashMap<>(), "A");
    }
}
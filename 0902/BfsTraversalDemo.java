import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DfsTraversalDemo {
    static List<String> dfs(Map<String, List<String>> graph, String start) {
        List<String> order = new ArrayList<>();
        if (graph == null || start == null || !graph.containsKey(start)) return order;
        Set<String> visited = new LinkedHashSet<>();
        dfsRecursive(graph, start, visited, order);
        return order;
    }

    private static void dfsRecursive(Map<String, List<String>> graph, String current, Set<String> visited, List<String> order) {
        visited.add(current);
        order.add(current);
        for (String next : graph.getOrDefault(current, List.of())) {
            if (graph.containsKey(next) && !visited.contains(next)) {
                dfsRecursive(graph, next, visited, order);
            }
        }
    }

    public static void main(String[] args) {
        Map<String, List<String>> graph = new LinkedHashMap<>();
        graph.put("A", List.of("B", "C"));
        graph.put("B", List.of("A", "D"));
        graph.put("C", List.of("A", "D"));
        graph.put("D", List.of("B", "C"));
        graph.put("E", List.of());
        System.out.println(dfs(graph, "A"));
        System.out.println(dfs(graph, "E"));
        System.out.println(dfs(graph, "X"));
    }
}
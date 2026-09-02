import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConnectedComponentsDemo {
    static List<List<String>> components(Map<String, List<String>> graph) {
        List<List<String>> result = new ArrayList<>();
        if (graph == null) return result;
        Set<String> visited = new HashSet<>();
        for (String start : graph.keySet()) {
            if (visited.contains(start)) continue;
            List<String> component = new ArrayList<>();
            dfs(graph, start, visited, component);
            result.add(component);
        }
        return result;
    }

    private static void dfs(Map<String, List<String>> graph, String current,
                            Set<String> visited, List<String> component) {
        visited.add(current);
        component.add(current);
        for (String next : graph.getOrDefault(current, List.of())) {
            if (graph.containsKey(next) && !visited.contains(next)) {
                dfs(graph, next, visited, component);
            }
        }
    }

    public static void main(String[] args) {
        Map<String, List<String>> graph = new LinkedHashMap<>();
        graph.put("A", List.of("B")); graph.put("B", List.of("A"));
        graph.put("C", List.of("D")); graph.put("D", List.of("C"));
        graph.put("E", List.of());
        System.out.println(components(graph));
    }
}
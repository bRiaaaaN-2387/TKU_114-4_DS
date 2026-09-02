import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GraphPathExists {
    static boolean recursive(Map<String, List<String>> graph, String start, String target) {
        if (graph == null || start == null || target == null
                || !graph.containsKey(start) || !graph.containsKey(target)) return false;
        return checkRecursive(graph, start, target, new HashSet<>());
    }

    private static boolean checkRecursive(Map<String, List<String>> graph, String current, String target, Set<String> visited) {
        if (current.equals(target)) return true;
        if (!visited.add(current)) return false;
        for (String next : graph.getOrDefault(current, List.of())) {
            if (graph.containsKey(next) && checkRecursive(graph, next, target, visited)) return true;
        }
        return false;
    }

    static boolean iterative(Map<String, List<String>> graph, String start, String target) {
        if (graph == null || start == null || target == null
                || !graph.containsKey(start) || !graph.containsKey(target)) return false;
        ArrayDeque<String> stack = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();
        stack.push(start);
        while (!stack.isEmpty()) {
            String current = stack.pop();
            if (current.equals(target)) return true;
            if (!visited.add(current)) continue;
            List<String> neighbors = graph.getOrDefault(current, List.of());
            for (int i = neighbors.size() - 1; i >= 0; i--) {
                String next = neighbors.get(i);
                if (graph.containsKey(next) && !visited.contains(next)) stack.push(next);
            }
        }
        return false;
    }

    public static void main(String[] args) {
        Map<String, List<String>> graph = Map.of(
                "A", List.of("B"), "B", List.of("C"),
                "C", List.of(), "X", List.of());
        System.out.println(recursive(graph, "A", "C"));
        System.out.println(recursive(graph, "C", "A"));
        System.out.println(recursive(graph, "X", "X"));
        System.out.println(iterative(graph, "A", "C"));
        System.out.println(iterative(graph, "C", "A"));
        System.out.println(iterative(graph, "X", "X"));
    }
}
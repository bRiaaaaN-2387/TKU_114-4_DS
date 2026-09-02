import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CoursePlanningGraph {

    // 使用 Iterative DFS 判斷目標課程是否 reachable (提早結束)
    public static boolean isReachable(Map<String, List<String>> graph, String start, String target) {
        if (graph == null || start == null || target == null) return false;
        if (!graph.containsKey(start) || !graph.containsKey(target)) return false;

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
                if (graph.containsKey(next) && !visited.contains(next)) {
                    stack.push(next);
                }
            }
        }
        return false;
    }

    // 使用 Recursive DFS 列出所有受影響的後續課程
    public static List<String> getAffectedCourses(Map<String, List<String>> graph, String start) {
        List<String> affected = new ArrayList<>();
        if (graph == null || start == null || !graph.containsKey(start)) return affected;
        
        // 使用 LinkedHashSet 保持走訪順序
        Set<String> visited = new LinkedHashSet<>();
        dfsVisit(graph, start, visited, affected);
        
        // 移除起點本身，僅保留真正受影響的後續課程
        affected.remove(start);
        return affected;
    }

    private static void dfsVisit(Map<String, List<String>> graph, String current, Set<String> visited, List<String> result) {
        if (!visited.add(current)) return;
        result.add(current);
        
        for (String next : graph.getOrDefault(current, List.of())) {
            if (graph.containsKey(next)) {
                dfsVisit(graph, next, visited, result);
            }
        }
    }

    // 輸出分析報告
    public static void printReport(Map<String, List<String>> graph, String start, String target) {
        System.out.println("--- course planning report: " + start + " ---");
        boolean reachable = isReachable(graph, start, target);
        System.out.println("reachable to " + target + "=" + reachable);
        
        List<String> affected = getAffectedCourses(graph, start);
        System.out.println("affected courses=" + affected);
        System.out.println();
    }

    public static void main(String[] args) {
        Map<String, List<String>> graph = new LinkedHashMap<>();
        
        graph.put("Programming I", List.of("Data Structures"));
        graph.put("Data Structures", List.of("Algorithms", "Operating Systems"));
        graph.put("Algorithms", List.of("Advanced Algorithms"));
        graph.put("Operating Systems", List.of());
        graph.put("Advanced Algorithms", List.of());
        graph.put("Elective Art", List.of()); // 孤立節點

        System.out.println("--- normal impact case ---");
        printReport(graph, "Data Structures", "Advanced Algorithms");

        System.out.println("--- root impact case ---");
        printReport(graph, "Programming I", "Operating Systems");

        System.out.println("--- isolated course case ---");
        printReport(graph, "Elective Art", "Algorithms");

        System.out.println("--- missing course case ---");
        printReport(graph, "Unknown Course", "Algorithms");

        System.out.println("--- empty graph case ---");
        printReport(new LinkedHashMap<>(), "Data Structures", "Algorithms");
    }
}
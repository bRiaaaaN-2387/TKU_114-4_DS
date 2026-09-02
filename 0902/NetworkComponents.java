import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class NetworkComponents {

    // 尋找無向圖中的所有 Connected Components
    public static List<List<String>> findComponents(Map<String, List<String>> graph) {
        List<List<String>> result = new ArrayList<>();
        if (graph == null || graph.isEmpty()) return result;

        Set<String> visited = new HashSet<>();
        
        // 走訪圖中所有節點，確保未與其他節點相連的孤立節點也能被處理
        for (String start : graph.keySet()) {
            if (visited.contains(start)) continue;
            
            List<String> component = new ArrayList<>();
            Queue<String> queue = new ArrayDeque<>();
            
            queue.offer(start);
            visited.add(start);
            
            while (!queue.isEmpty()) {
                String current = queue.poll();
                component.add(current);
                
                for (String next : graph.getOrDefault(current, List.of())) {
                    if (graph.containsKey(next) && visited.add(next)) {
                        queue.offer(next);
                    }
                }
            }
            result.add(component); // 完成一次 BFS，加入一個 component
        }
        return result;
    }

    // 輸出報告：包含每個 component、總數與最大的 component
    public static void printReport(Map<String, List<String>> graph) {
        List<List<String>> components = findComponents(graph);
        
        if (components.isEmpty()) {
            System.out.println("graph is empty");
            System.out.println();
            return;
        }

        int count = components.size();
        List<String> largest = new ArrayList<>();
        
        System.out.println("--- components report ---");
        for (int i = 0; i < count; i++) {
            List<String> comp = components.get(i);
            System.out.println("component " + (i + 1) + "=" + comp);
            
            // 比較並更新最大 component
            if (comp.size() > largest.size()) {
                largest = comp;
            }
        }
        
        System.out.println("total components count=" + count);
        System.out.println("largest component=" + largest + " (size=" + largest.size() + ")\n");
    }

    public static void main(String[] args) {
        // 建立無向圖 (Undirected Graph)
        Map<String, List<String>> graph = new LinkedHashMap<>();
        
        // Component 1 (A, B, C)
        graph.put("A", List.of("B", "C"));
        graph.put("B", List.of("A"));
        graph.put("C", List.of("A"));
        
        // Component 2 (D, E)
        graph.put("D", List.of("E"));
        graph.put("E", List.of("D"));
        
        // Component 3 (Isolated)
        graph.put("F", List.of());
        
        System.out.println("--- normal graph case ---");
        printReport(graph);
        
        System.out.println("--- empty graph case ---");
        printReport(new LinkedHashMap<>());
    }
}
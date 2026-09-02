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

public class MetroTransferPath {

    // 使用 BFS 尋找最少站數路徑
    public static List<String> findShortestPath(Map<String, List<String>> graph, String start, String target) {
        if (graph == null || start == null || target == null) return List.of();
        // 邊界條件：起點或終點不存在於圖中
        if (!graph.containsKey(start) || !graph.containsKey(target)) return List.of();

        Queue<String> queue = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();
        Map<String, String> previous = new HashMap<>(); // 記錄 predecessor

        queue.offer(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            String current = queue.poll();

            // 提早停止：已找到目標站點
            if (current.equals(target)) break;

            for (String next : graph.getOrDefault(current, List.of())) {
                if (graph.containsKey(next) && visited.add(next)) {
                    previous.put(next, current);
                    queue.offer(next);
                }
            }
        }

        // 若無法到達目標站點，回傳空路徑
        if (!visited.contains(target)) return List.of();

        // 從 target 反向追蹤回 start
        List<String> path = new ArrayList<>();
        for (String at = target; at != null; at = previous.get(at)) {
            path.add(at);
        }
        Collections.reverse(path); // 反轉以得到從 start 到 target 的正確順序
        return path;
    }

    // 執行查詢並輸出報告
    public static void printReport(Map<String, List<String>> graph, String start, String target) {
        List<String> path = findShortestPath(graph, start, target);
        System.out.println("path " + start + "->" + target + "=" + path);
        if (!path.isEmpty()) {
            System.out.println("edge count=" + (path.size() - 1));
        } else {
            System.out.println("edge count=-1 (unreachable)");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        // 建立無向圖模擬捷運路線 (雙向連通)
        Map<String, List<String>> graph = new LinkedHashMap<>();
        graph.put("Taipei", List.of("Zhongshan", "NTU Hospital"));
        graph.put("Zhongshan", List.of("Taipei", "Shuanglian", "Beimen"));
        graph.put("NTU Hospital", List.of("Taipei", "Chiang Kai-shek Memorial Hall"));
        graph.put("Shuanglian", List.of("Zhongshan", "Minquan"));
        graph.put("Beimen", List.of("Zhongshan", "Ximen"));
        graph.put("Minquan", List.of("Shuanglian"));
        graph.put("Ximen", List.of("Beimen"));
        graph.put("Chiang Kai-shek Memorial Hall", List.of("NTU Hospital"));
        graph.put("Isolated Station", List.of()); // 孤立站點

        System.out.println("--- normal transfer case ---");
        printReport(graph, "Taipei", "Minquan");

        System.out.println("--- start equals target case ---");
        printReport(graph, "Zhongshan", "Zhongshan");

        System.out.println("--- unreachable case (isolated) ---");
        printReport(graph, "Taipei", "Isolated Station");

        System.out.println("--- missing vertex case ---");
        printReport(graph, "Taipei", "Unknown Station");

        System.out.println("--- empty graph case ---");
        printReport(new LinkedHashMap<>(), "Taipei", "Zhongshan");
    }
}
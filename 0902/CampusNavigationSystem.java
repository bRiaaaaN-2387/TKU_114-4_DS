import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class CampusNavigationSystem {
    // 使用 HashMap 保存地點與道路 (Adjacency List)
    private final Map<String, List<String>> campusMap = new HashMap<>();

    // 新增地點
    public void addLocation(String location) {
        if (location == null || location.isBlank()) return;
        campusMap.putIfAbsent(location.trim(), new ArrayList<>());
    }

    // 新增道路 (無向圖，雙向連通)
    public boolean addRoad(String loc1, String loc2) {
        if (!campusMap.containsKey(loc1) || !campusMap.containsKey(loc2)) return false;
        if (loc1.equals(loc2)) return false;

        boolean added = false;
        if (!campusMap.get(loc1).contains(loc2)) {
            campusMap.get(loc1).add(loc2);
            campusMap.get(loc2).add(loc1);
            added = true;
        }
        return added;
    }

    // BFS 尋找最少 edge 路徑
    public List<String> findShortestPath(String start, String target) {
        if (start == null || target == null) return List.of();
        // 邊界檢查：地點不存在
        if (!campusMap.containsKey(start) || !campusMap.containsKey(target)) return List.of();

        Queue<String> queue = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();
        Map<String, String> previous = new HashMap<>();

        queue.offer(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            String current = queue.poll();

            // 抵達目標
            if (current.equals(target)) break;

            for (String next : campusMap.getOrDefault(current, List.of())) {
                if (visited.add(next)) {
                    previous.put(next, current);
                    queue.offer(next);
                }
            }
        }

        // 若無路徑可達
        if (!visited.contains(target)) return List.of();

        // 透過 predecessor 反向還原路徑
        List<String> path = new ArrayList<>();
        for (String at = target; at != null; at = previous.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);
        return path;
    }

    // 輸出導航報告
    public void printNavigationReport(String start, String target) {
        List<String> path = findShortestPath(start, target);
        System.out.println("navigate " + start + "->" + target + "=" + path);
        if (!path.isEmpty()) {
            System.out.println("edges=" + (path.size() - 1));
        } else {
            System.out.println("edges=-1 (unreachable)");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        CampusNavigationSystem campus = new CampusNavigationSystem();
        
        List.of("Main Gate", "Library", "Engineering Bldg", "Cafeteria", "Dormitory", "Sports Field", "Hidden Annex")
            .forEach(campus::addLocation);

        campus.addRoad("Main Gate", "Library");
        campus.addRoad("Main Gate", "Engineering Bldg");
        campus.addRoad("Library", "Cafeteria");
        campus.addRoad("Engineering Bldg", "Cafeteria");
        campus.addRoad("Cafeteria", "Dormitory");
        campus.addRoad("Dormitory", "Sports Field");

        System.out.println("--- normal navigation case ---");
        campus.printNavigationReport("Main Gate", "Sports Field");

        System.out.println("--- start equals target case ---");
        campus.printNavigationReport("Library", "Library");

        System.out.println("--- unreachable case (isolated) ---");
        campus.printNavigationReport("Library", "Hidden Annex");

        System.out.println("--- missing location case ---");
        campus.printNavigationReport("Main Gate", "Admin Bldg");

        System.out.println("--- empty map case ---");
        CampusNavigationSystem emptyCampus = new CampusNavigationSystem();
        emptyCampus.printNavigationReport("Main Gate", "Library");
    }
}
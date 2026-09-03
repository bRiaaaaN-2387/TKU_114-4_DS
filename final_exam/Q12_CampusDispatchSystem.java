import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class Q12_CampusDispatchSystem {

    public record Request(String id, String location, int priority, long sequence) {}

    // 圖結構 (Adjacency List)
    private final Map<String, List<String>> graph = new LinkedHashMap<>();
    
    // 請求查驗 (防止重複 ID)
    private final Map<String, Request> requestMap = new HashMap<>();
    
    // 任務排程 (優先級小至大 -> sequence 小至大)
    private final PriorityQueue<Request> queue;

    public Q12_CampusDispatchSystem() {
        Comparator<Request> comparator = Comparator
                .comparingInt(Request::priority)
                .thenComparingLong(Request::sequence);
        this.queue = new PriorityQueue<>(comparator);
    }

    // 新增地點
    public boolean addLocation(String location) {
        if (location == null || graph.containsKey(location)) {
            return false;
        }
        graph.put(location, new ArrayList<>());
        return true;
    }

    // 新增無向道路
    public boolean addRoad(String first, String second) {
        if (first == null || second == null) return false;
        if (first.equals(second)) return false; // 拒絕 self-loop
        if (!graph.containsKey(first) || !graph.containsKey(second)) return false;

        List<String> firstNeighbors = graph.get(first);
        if (firstNeighbors.contains(second)) return false; // 拒絕重複 edge

        firstNeighbors.add(second);
        graph.get(second).add(first);
        return true;
    }

    // 提交派送請求
    public boolean submit(Request request) {
        if (request == null || request.id() == null || request.location() == null) {
            return false;
        }
        // 確保 location 存在於系統中，且 ID 不重複
        if (!graph.containsKey(request.location())) return false;
        if (requestMap.containsKey(request.id())) return false;

        requestMap.put(request.id(), request);
        queue.offer(request);
        return true;
    }

    // 取得指定地點所有可達的節點 (包含自己)
    private Set<String> getReachableLocations(String start) {
        Set<String> visited = new HashSet<>();
        Queue<String> bfsQueue = new ArrayDeque<>();
        
        bfsQueue.offer(start);
        visited.add(start);
        
        while (!bfsQueue.isEmpty()) {
            String current = bfsQueue.poll();
            for (String next : graph.getOrDefault(current, List.of())) {
                if (visited.add(next)) {
                    bfsQueue.offer(next);
                }
            }
        }
        return visited;
    }

    // 取得最高優先且可達的 Request
    public Request nextReachable(String serviceCenter) {
        if (serviceCenter == null || !graph.containsKey(serviceCenter)) {
            return null;
        }

        Set<String> reachableLocations = getReachableLocations(serviceCenter);
        List<Request> unreachableTemp = new ArrayList<>();
        Request matched = null;

        // 依序檢查最高優先任務是否可達
        while (!queue.isEmpty()) {
            Request current = queue.poll();
            if (reachableLocations.contains(current.location())) {
                matched = current;
                break;
            } else {
                // 不可到達的暫存起來，必須保留
                unreachableTemp.add(current);
            }
        }

        // 將暫存的不可到達任務放回 Queue
        for (Request req : unreachableTemp) {
            queue.offer(req);
        }

        // 若有找到任務，同步從 HashMap 中移除
        if (matched != null) {
            requestMap.remove(matched.id());
        }

        return matched;
    }

    // 尋找兩點之間的最短路徑 (BFS)
    public List<String> route(String start, String target) {
        if (start == null || target == null) return List.of();
        if (!graph.containsKey(start) || !graph.containsKey(target)) return List.of();
        if (start.equals(target)) return List.of(start);

        Queue<String> bfsQueue = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();
        Map<String, String> predecessor = new HashMap<>();

        bfsQueue.offer(start);
        visited.add(start);

        while (!bfsQueue.isEmpty()) {
            String current = bfsQueue.poll();

            if (current.equals(target)) break;

            for (String next : graph.getOrDefault(current, List.of())) {
                if (visited.add(next)) {
                    predecessor.put(next, current);
                    bfsQueue.offer(next);
                }
            }
        }

        if (!visited.contains(target)) return List.of();

        List<String> path = new ArrayList<>();
        for (String step = target; step != null; step = predecessor.get(step)) {
            path.add(step);
        }
        Collections.reverse(path);
        return path;
    }

    // 回傳目前待處理任務總數
    public int pendingCount() {
        return requestMap.size();
    }

    public static void main(String[] args) {
        Q12_CampusDispatchSystem system = new Q12_CampusDispatchSystem();

        // 建立校園地圖
        system.addLocation("MainGate");
        system.addLocation("Library");
        system.addLocation("Dormitory");
        system.addLocation("AdminBldg");
        system.addLocation("IsolatedLab");

        system.addRoad("MainGate", "Library");
        system.addRoad("Library", "Dormitory");
        
        System.out.println("--- normal submit & reachable check ---");
        system.submit(new Request("REQ-1", "Dormitory", 2, 100));
        system.submit(new Request("REQ-2", "Library", 1, 200));
        system.submit(new Request("REQ-3", "IsolatedLab", 1, 100)); // 最高優先，但 Isolated 孤立不可達
        system.submit(new Request("REQ-4", "Dormitory", 2, 50));  // 優先級同 REQ-1，但 sequence 較小
        
        System.out.println("Pending Count=" + system.pendingCount());

        // 從 MainGate 派送，預期 IsolatedLab (REQ-3) 會被保留，取出 Library (REQ-2)
        Request next1 = system.nextReachable("MainGate");
        System.out.println("Next Reachable from MainGate=" + next1); // 預期 REQ-2
        
        // 再次派送，取出 REQ-4 (sequence 小優先)
        Request next2 = system.nextReachable("MainGate");
        System.out.println("Next Reachable from MainGate=" + next2); // 預期 REQ-4

        System.out.println("Pending Count after dispatch=" + system.pendingCount()); // 應剩下 REQ-1, REQ-3

        System.out.println("\n--- routing ---");
        System.out.println("Route MainGate -> Dormitory=" + system.route("MainGate", "Dormitory"));
        System.out.println("Route MainGate -> IsolatedLab=" + system.route("MainGate", "IsolatedLab"));

        System.out.println("\n--- edge & invalid cases ---");
        System.out.println("Submit Duplicate ID=" + system.submit(new Request("REQ-1", "MainGate", 1, 1))); // 已存在
        System.out.println("Next from Invalid Center=" + system.nextReachable("Unknown"));
        System.out.println("Route Invalid=" + system.route("MainGate", "Unknown"));
    }
}
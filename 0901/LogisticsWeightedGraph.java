import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LogisticsWeightedGraph {
    // 儲存目標節點與權重，並於建構時阻擋負權重
    public record Edge(String to, int weight) {
        public Edge {
            if (to == null || to.isBlank()) throw new IllegalArgumentException("target cannot be empty");
            if (weight < 0) throw new IllegalArgumentException("weight cannot be negative");
        }
    }

    private final Map<String, List<Edge>> network = new LinkedHashMap<>();

    // 新增物流站點 (Vertex)
    public void addVertex(String vertex) {
        if (vertex == null || vertex.isBlank()) return;
        network.putIfAbsent(vertex.trim(), new ArrayList<>());
    }

    // 新增或更新有向加權連線
    public boolean addOrUpdateEdge(String from, String to, int weight) {
        if (weight < 0) {
            System.out.println("error: negative weight is not allowed for " + from + "->" + to);
            return false;
        }
        if (!network.containsKey(from) || !network.containsKey(to)) {
            System.out.println("error: vertex does not exist for " + from + "->" + to);
            return false;
        }

        List<Edge> edges = network.get(from);
        
        // 若連線已存在，則更新權重
        for (int i = 0; i < edges.size(); i++) {
            if (edges.get(i).to().equals(to)) {
                edges.set(i, new Edge(to, weight));
                return true;
            }
        }
        
        // 若不存在，則新增連線
        edges.add(new Edge(to, weight));
        return true;
    }

    // 移除連線
    public boolean removeEdge(String from, String to) {
        if (!network.containsKey(from)) return false;
        
        List<Edge> edges = network.get(from);
        return edges.removeIf(edge -> edge.to().equals(to));
    }

    // 查詢特定連線的成本 (權重)，回傳 -1 代表無直接連線
    public int queryCost(String from, String to) {
        if (!network.containsKey(from)) return -1;
        
        for (Edge edge : network.get(from)) {
            if (edge.to().equals(to)) {
                return edge.weight();
            }
        }
        return -1;
    }

    // 輸出特定站點的所有對外連線
    public void printOutgoingEdges(String vertex) {
        if (!network.containsKey(vertex)) {
            System.out.println("vertex not found=" + vertex);
            return;
        }
        System.out.println("outgoing from " + vertex + "=" + network.get(vertex));
    }

    public static void main(String[] args) {
        LogisticsWeightedGraph logistics = new LogisticsWeightedGraph();
        
        List.of("Taipei", "Taichung", "Kaohsiung").forEach(logistics::addVertex);

        // 測試正常新增
        logistics.addOrUpdateEdge("Taipei", "Taichung", 500);
        logistics.addOrUpdateEdge("Taichung", "Kaohsiung", 800);
        
        // 測試更新
        logistics.addOrUpdateEdge("Taipei", "Taichung", 450); 
        
        // 測試例外條件：不存在的節點與負數權重
        logistics.addOrUpdateEdge("Taipei", "Tainan", 300);
        logistics.addOrUpdateEdge("Kaohsiung", "Taipei", -100);

        System.out.println("\n--- network query ---");
        System.out.println("cost Taipei->Taichung=" + logistics.queryCost("Taipei", "Taichung"));
        System.out.println("cost Taipei->Kaohsiung=" + logistics.queryCost("Taipei", "Kaohsiung"));
        
        logistics.printOutgoingEdges("Taipei");
        
        System.out.println("\n--- remove edge ---");
        System.out.println("remove Taipei->Taichung=" + logistics.removeEdge("Taipei", "Taichung"));
        logistics.printOutgoingEdges("Taipei");
    }
}
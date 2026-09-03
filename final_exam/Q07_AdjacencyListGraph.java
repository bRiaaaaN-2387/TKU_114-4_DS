import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Q07_AdjacencyListGraph {

    // 儲存有向圖結構，Value 使用 LinkedHashSet 以保留 edge 加入順序並防止重複
    private final Map<String, Set<String>> graph = new LinkedHashMap<>();

    // 新增節點
    public boolean addVertex(String vertex) {
        if (vertex == null || graph.containsKey(vertex)) {
            return false;
        }
        graph.put(vertex, new LinkedHashSet<>());
        return true;
    }

    // 新增有向邊
    public boolean addEdge(String from, String to) {
        if (from == null || to == null) return false;
        
        // 拒絕 self-loop
        if (from.equals(to)) return false;
        
        // 拒絕 missing vertex
        if (!graph.containsKey(from) || !graph.containsKey(to)) return false;

        // 若 edge 已存在，LinkedHashSet.add 會回傳 false 達成拒絕重複的要求
        return graph.get(from).add(to);
    }

    // 移除有向邊
    public boolean removeEdge(String from, String to) {
        if (from == null || to == null) return false;
        
        // 確保起點存在
        if (!graph.containsKey(from)) return false;
        
        return graph.get(from).remove(to);
    }

    // 取得指定節點的 outgoing edges (保留加入順序)
    public List<String> outgoing(String vertex) {
        if (vertex == null || !graph.containsKey(vertex)) {
            return List.of();
        }
        return new ArrayList<>(graph.get(vertex));
    }

    // 計算指定節點的 in-degree
    public int inDegree(String vertex) {
        // missing vertex 回傳 0
        if (vertex == null || !graph.containsKey(vertex)) {
            return 0;
        }

        int count = 0;
        for (Set<String> edges : graph.values()) {
            if (edges.contains(vertex)) {
                count++;
            }
        }
        return count;
    }

    // 計算全圖總 edge 數量
    public int edgeCount() {
        int count = 0;
        for (Set<String> edges : graph.values()) {
            count += edges.size();
        }
        return count;
    }

    public static void main(String[] args) {
        Q07_AdjacencyListGraph graph = new Q07_AdjacencyListGraph();
        
        // 測試正常新增
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addVertex("D");

        System.out.println("--- normal edge operations ---");
        System.out.println("add A->B=" + graph.addEdge("A", "B"));
        System.out.println("add A->C=" + graph.addEdge("A", "C"));
        System.out.println("add C->D=" + graph.addEdge("C", "D"));
        System.out.println("add B->D=" + graph.addEdge("B", "D"));
        
        System.out.println("outgoing A (preserves order)=" + graph.outgoing("A"));
        System.out.println("inDegree D=" + graph.inDegree("D")); // A, B, C 中有 C, B 指向 D (數量2)
        System.out.println("total edge count=" + graph.edgeCount());

        System.out.println("\n--- constraint rejections ---");
        System.out.println("add A->A (self-loop)=" + graph.addEdge("A", "A"));
        System.out.println("add A->B (duplicate)=" + graph.addEdge("A", "B"));
        System.out.println("add A->X (missing target)=" + graph.addEdge("A", "X"));
        System.out.println("add Y->Z (missing both)=" + graph.addEdge("Y", "Z"));

        System.out.println("\n--- edge removal & missing vertex queries ---");
        System.out.println("remove A->C=" + graph.removeEdge("A", "C"));
        System.out.println("remove A->X (missing)=" + graph.removeEdge("A", "X"));
        System.out.println("outgoing X (missing)=" + graph.outgoing("X"));
        System.out.println("inDegree X (missing)=" + graph.inDegree("X"));
        
        System.out.println("\nfinal edge count=" + graph.edgeCount());
    }
}
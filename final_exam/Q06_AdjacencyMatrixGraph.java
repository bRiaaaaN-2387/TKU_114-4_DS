import java.util.ArrayList;
import java.util.List;

public class Q06_AdjacencyMatrixGraph {

    private final List<String> vertices;
    private final boolean[][] matrix;

    // 建構子：依照輸入的頂點順序建立 Adjacency Matrix
    public Q06_AdjacencyMatrixGraph(List<String> vertices) {
        if (vertices == null) {
            this.vertices = new ArrayList<>();
        } else {
            // 使用副本避免外部修改影響內部結構
            this.vertices = new ArrayList<>(vertices);
        }
        int size = this.vertices.size();
        this.matrix = new boolean[size][size];
    }

    // 取得頂點對應的 index，若不存在則回傳 -1
    private int indexOf(String vertex) {
        if (vertex == null) return -1;
        return vertices.indexOf(vertex);
    }

    // 新增無向邊
    public boolean addEdge(String first, String second) {
        int u = indexOf(first);
        int v = indexOf(second);

        // 拒絕 missing vertex
        if (u == -1 || v == -1) return false;
        
        // 拒絕 self-loop
        if (u == v) return false;
        
        // 拒絕重複邊
        if (matrix[u][v]) return false;

        // 更新無向圖的雙向連線
        matrix[u][v] = true;
        matrix[v][u] = true;
        return true;
    }

    // 移除無向邊
    public boolean removeEdge(String first, String second) {
        int u = indexOf(first);
        int v = indexOf(second);

        if (u == -1 || v == -1) return false;
        if (!matrix[u][v]) return false;

        matrix[u][v] = false;
        matrix[v][u] = false;
        return true;
    }

    // 檢查邊是否存在
    public boolean hasEdge(String first, String second) {
        int u = indexOf(first);
        int v = indexOf(second);

        if (u == -1 || v == -1) return false;
        return matrix[u][v];
    }

    // 取得頂點的degree，若為 missing vertex 則回傳 -1 以作區別
    public int degree(String vertex) {
        int u = indexOf(vertex);
        if (u == -1) return -1;

        int count = 0;
        for (int i = 0; i < vertices.size(); i++) {
            if (matrix[u][i]) {
                count++;
            }
        }
        return count;
    }

    // 取得相鄰頂點清單 (依照初始化輸入的順序)
    public List<String> neighbors(String vertex) {
        int u = indexOf(vertex);
        List<String> result = new ArrayList<>();
        if (u == -1) return result; // 安全回傳 empty list

        for (int i = 0; i < vertices.size(); i++) {
            if (matrix[u][i]) {
                result.add(vertices.get(i));
            }
        }
        return result;
    }

    public static void main(String[] args) {
        List<String> nodes = List.of("A", "B", "C", "D");
        Q06_AdjacencyMatrixGraph graph = new Q06_AdjacencyMatrixGraph(nodes);

        System.out.println("--- normal edge operations ---");
        System.out.println("add A-B=" + graph.addEdge("A", "B"));
        System.out.println("add A-C=" + graph.addEdge("A", "C"));
        System.out.println("add C-D=" + graph.addEdge("C", "D"));
        
        System.out.println("hasEdge A-B=" + graph.hasEdge("A", "B"));
        System.out.println("degree A=" + graph.degree("A"));
        System.out.println("neighbors A=" + graph.neighbors("A"));

        System.out.println("\n--- constraint rejections ---");
        System.out.println("add A-A (self-loop)=" + graph.addEdge("A", "A"));
        System.out.println("add A-B (duplicate)=" + graph.addEdge("A", "B"));
        System.out.println("remove A-D (not exists)=" + graph.removeEdge("A", "D"));

        System.out.println("\n--- missing vertex safety ---");
        System.out.println("add X-Y=" + graph.addEdge("X", "Y"));
        System.out.println("hasEdge A-X=" + graph.hasEdge("A", "X"));
        System.out.println("degree X=" + graph.degree("X"));
        System.out.println("neighbors X=" + graph.neighbors("X"));
        
        System.out.println("\n--- remove edge success ---");
        System.out.println("remove A-B=" + graph.removeEdge("A", "B"));
        System.out.println("neighbors A after remove=" + graph.neighbors("A"));
    }
}
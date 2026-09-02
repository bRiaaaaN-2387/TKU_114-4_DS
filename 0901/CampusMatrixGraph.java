import java.util.ArrayList;
import java.util.List;

public class CampusMatrixGraph {
    private final List<String> vertices;
    private final boolean[][] edges;

    public CampusMatrixGraph(List<String> vertices) {
        if (vertices == null || vertices.isEmpty()) {
            throw new IllegalArgumentException("vertices cannot be empty");
        }
        this.vertices = List.copyOf(vertices);
        this.edges = new boolean[vertices.size()][vertices.size()];
    }

    // 取得節點對應的 index
    private int indexOf(String vertex) {
        int index = vertices.indexOf(vertex);
        if (index < 0) {
            throw new IllegalArgumentException("unknown vertex: " + vertex);
        }
        return index;
    }

    // 新增無向邊，必須同時設定對稱位置
    public void addEdge(String first, String second) {
        int a = indexOf(first);
        int b = indexOf(second);
        edges[a][b] = true;
        edges[b][a] = true;
    }

    // 移除無向邊，必須同時清除對稱位置
    public void removeEdge(String first, String second) {
        int a = indexOf(first);
        int b = indexOf(second);
        edges[a][b] = false;
        edges[b][a] = false;
    }

    // 查詢特定節點的 neighbors
    public List<String> neighbors(String vertex) {
        int row = indexOf(vertex);
        List<String> result = new ArrayList<>();
        for (int column = 0; column < vertices.size(); column++) {
            if (edges[row][column]) {
                result.add(vertices.get(column));
            }
        }
        return result;
    }

    // 查詢特定節點的 degree
    public int degree(String vertex) {
        int row = indexOf(vertex);
        int degree = 0;
        for (boolean connected : edges[row]) {
            if (connected) degree++;
        }
        return degree;
    }

    // 查詢總 edge 數量 (所有 degree 總和除以 2)
    public int edgeCount() {
        int degreeSum = 0;
        for (String vertex : vertices) {
            degreeSum += degree(vertex);
        }
        return degreeSum / 2;
    }

    public static void main(String[] args) {
        List<String> locations = List.of("Library", "Dormitory", "Cafeteria", "Gym");
        CampusMatrixGraph campus = new CampusMatrixGraph(locations);

        campus.addEdge("Library", "Dormitory");
        campus.addEdge("Library", "Cafeteria");
        campus.addEdge("Dormitory", "Gym");
        
        // 測試重複加入 edge (應被 boolean 陣列天然過濾)
        campus.addEdge("Library", "Dormitory");

        System.out.println("Library neighbors=" + campus.neighbors("Library"));
        System.out.println("Gym degree=" + campus.degree("Gym"));
        System.out.println("Total edge count=" + campus.edgeCount());

        System.out.println("--- Removing Library-Cafeteria edge ---");
        campus.removeEdge("Library", "Cafeteria");
        System.out.println("Library neighbors=" + campus.neighbors("Library"));
        System.out.println("Total edge count=" + campus.edgeCount());
    }
}
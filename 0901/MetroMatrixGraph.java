import java.util.ArrayList;
import java.util.List;

public class MetroMatrixGraph {
    private final List<String> stations;
    private final boolean[][] matrix;

    public MetroMatrixGraph(List<String> stations) {
        if (stations == null || stations.isEmpty()) {
            throw new IllegalArgumentException("stations cannot be empty");
        }
        this.stations = List.copyOf(stations);
        this.matrix = new boolean[stations.size()][stations.size()];
    }

    // 取得站點對應的 index
    private int indexOf(String station) {
        int index = stations.indexOf(station);
        if (index < 0) {
            throw new IllegalArgumentException("unknown station: " + station);
        }
        return index;
    }

    // 新增無向邊 (雙向連線)
    public void addEdge(String s1, String s2) {
        int a = indexOf(s1);
        int b = indexOf(s2);
        matrix[a][b] = true;
        matrix[b][a] = true;
    }

    // 查詢鄰站
    public List<String> neighbors(String station) {
        int row = indexOf(station);
        List<String> result = new ArrayList<>();
        for (int col = 0; col < stations.size(); col++) {
            if (matrix[row][col]) {
                result.add(stations.get(col));
            }
        }
        return result;
    }

    // 查詢特定站點的 degree
    public int degree(String station) {
        int row = indexOf(station);
        int count = 0;
        for (boolean connected : matrix[row]) {
            if (connected) count++;
        }
        return count;
    }

    // 計算總 edge 數量
    public int edgeCount() {
        int degreeSum = 0;
        for (String station : stations) {
            degreeSum += degree(station);
        }
        return degreeSum / 2;
    }

    // 輸出矩陣報告
    public void matrixReport() {
        System.out.println("--- matrix report ---");
        System.out.print(String.format("%-10s", ""));
        for (String s : stations) {
            System.out.print(String.format("%-10s", s));
        }
        System.out.println();

        for (int i = 0; i < stations.size(); i++) {
            System.out.print(String.format("%-10s", stations.get(i)));
            for (int j = 0; j < stations.size(); j++) {
                System.out.print(String.format("%-10s", matrix[i][j] ? "1" : "0"));
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        List<String> stations = List.of("Taipei", "Zhongshan", "Shuanglian", "Minquan");
        MetroMatrixGraph metro = new MetroMatrixGraph(stations);

        metro.addEdge("Taipei", "Zhongshan");
        metro.addEdge("Zhongshan", "Shuanglian");
        metro.addEdge("Shuanglian", "Minquan");

        System.out.println("Zhongshan neighbors=" + metro.neighbors("Zhongshan"));
        System.out.println("Zhongshan degree=" + metro.degree("Zhongshan"));
        System.out.println("total edge count=" + metro.edgeCount());
        System.out.println();
        
        metro.matrixReport();
    }
}
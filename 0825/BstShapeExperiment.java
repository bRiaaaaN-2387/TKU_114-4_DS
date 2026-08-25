// 實驗專用節點
class ExperimentNode {
    int value;
    ExperimentNode left;
    ExperimentNode right;

    ExperimentNode(int value) {
        this.value = value;
    }
}

public class BstShapeExperiment {

    // 1. 新增節點
    public static ExperimentNode insert(ExperimentNode node, int value) {
        if (node == null) {
            return new ExperimentNode(value);
        }
        if (value < node.value) {
            node.left = insert(node.left, value);
        } else if (value > node.value) {
            node.right = insert(node.right, value);
        }
        return node;
    }

    // 2. 計算樹的高度 (空樹為 -1，單一節點為 0)
    public static int height(ExperimentNode node) {
        if (node == null) {
            return -1;
        }
        return 1 + Math.max(height(node.left), height(node.right));
    }

    // 3. 計算尋找單一目標的比較次數
    public static int getSearchCount(ExperimentNode root, int target) {
        int count = 0;
        ExperimentNode current = root;

        while (current != null) {
            count++; // 進入節點即算作一次比較
            if (target == current.value) {
                return count;
            } else if (target < current.value) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return count;
    }

    // 4. 執行實驗與輸出報表
    public static void runExperiment(String experimentName, int[] data) {
        ExperimentNode root = null;
        
        // 依序建立 BST
        for (int val : data) {
            root = insert(root, val);
        }

        // 計算高度
        int h = height(root);
        
        // 模擬搜尋樹中所有的元素，並加總比較次數
        int totalSearchComparisons = 0;
        for (int val : data) {
            totalSearchComparisons += getSearchCount(root, val);
        }

        // 輸出結果
        System.out.println("=== " + experimentName + " ===");
        System.out.println("Tree Height        : " + h);
        System.out.println("Total Search Count : " + totalSearchComparisons);
        System.out.println("Average Search Cost: " + String.format("%.2f", (double) totalSearchComparisons / data.length));
        System.out.println();
    }

    // 測試主程式
    public static void main(String[] args) {
        
        System.out.println("--- BST Shape & Performance Experiment ---\n");

        // 數值集合：1 到 15 共 15 個數字

        // 情境 1: 已排序資料 (Sorted Order)
        // 會導致嚴重的向右傾斜 (Right-skewed)，形同 Linked List
        int[] sortedData = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        runExperiment("Case 1: Skewed Tree (Sorted Input)", sortedData);

        // 情境 2: 最佳化平衡資料 (Balanced Order)
        // 以 8 為 Root，接著平均分配左右，會形成完美的平衡二元樹 (Perfect Binary Tree)
        int[] balancedData = {8, 4, 12, 2, 6, 10, 14, 1, 3, 5, 7, 9, 11, 13, 15};
        runExperiment("Case 2: Balanced Tree (Optimized Input)", balancedData);

        // 情境 3: 不規則隨機資料 (Irregular/Random Order)
        // 一般情況下的樹狀結構，高度與效能介於最佳與最差之間
        int[] randomData = {10, 5, 13, 2, 8, 15, 1, 6, 11, 3, 14, 4, 7, 12, 9};
        runExperiment("Case 3: Irregular Tree (Random Input)", randomData);
    }
}
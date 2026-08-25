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

    // 2. 計算樹的高度 (空樹為 -1，單一節點為 0)[cite: 1]
    public static int height(ExperimentNode node) {
        if (node == null) return -1;
        return 1 + Math.max(height(node.left), height(node.right));
    }

    // 3. 計算尋找單一目標的比較次數
    public static int getSearchCount(ExperimentNode root, int target) {
        int count = 0;
        ExperimentNode current = root;

        while (current != null) {
            count++; // 每進入一個節點就算作一次比較
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

    // 4. 執行實驗並輸出比較報表
    public static void runExperiment(String experimentName, int[] dataToInsert) {
        ExperimentNode root = null;
        
        // 依序建樹
        for (int val : dataToInsert) {
            root = insert(root, val);
        }

        // 計算高度
        int h = height(root);
        
        // 模擬搜尋樹中所有的元素，並加總比較次數
        int totalSearchComparisons = 0;
        for (int val : dataToInsert) {
            totalSearchComparisons += getSearchCount(root, val);
        }

        // 輸出結果
        System.out.println("=== " + experimentName + " ===");
        System.out.println("Tree Height        : " + h);
        System.out.println("Total Search Count : " + totalSearchComparisons);
        System.out.println("Average Search Cost: " + String.format("%.2f", (double) totalSearchComparisons / dataToInsert.length));
        System.out.println();
    }

    // 測試主程式
    public static void main(String[] args) {
        System.out.println("--- BST Shape & Performance Experiment ---\n");

        // 情境 1: 升冪排序 (Ascending Order)
        // 預期：嚴重向右傾斜 (Right-skewed)[cite: 1]
        int[] ascendingData = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        runExperiment("Case 1: Ascending Order (Right-Skewed)", ascendingData);

        // 情境 2: 降冪排序 (Descending Order)
        // 預期：嚴重向左傾斜 (Left-skewed)[cite: 1]
        int[] descendingData = {15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
        runExperiment("Case 2: Descending Order (Left-Skewed)", descendingData);

        // 情境 3: 最佳化平衡資料 (Nearly Balanced Order)
        // 預期：完美的平衡二元樹 (Perfect Binary Tree)
        int[] balancedData = {8, 4, 12, 2, 6, 10, 14, 1, 3, 5, 7, 9, 11, 13, 15};
        runExperiment("Case 3: Nearly Balanced Order", balancedData);
    }
}
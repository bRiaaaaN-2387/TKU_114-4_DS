// 實驗專用的 BST 節點
class ShapeNode {
    int value;
    ShapeNode left;
    ShapeNode right;

    ShapeNode(int value) {
        this.value = value;
    }
}

public class TreeShapeComparison {

    // 1. 新增節點
    public static ShapeNode insert(ShapeNode node, int value) {
        if (node == null) {
            return new ShapeNode(value);
        }
        if (value < node.value) {
            node.left = insert(node.left, value);
        } else if (value > node.value) {
            node.right = insert(node.right, value);
        }
        return node;
    }

    // 2. 計算樹的高度 (遵循教材定義：空樹為 -1，單一節點為 0)
    public static int height(ShapeNode node) {
        if (node == null) return -1;
        return 1 + Math.max(height(node.left), height(node.right));
    }

    // 3. 取得尋找單一目標的比較次數
    public static int getSearchCount(ShapeNode root, int target) {
        int count = 0;
        ShapeNode current = root;

        while (current != null) {
            count++; // 每進入一個節點就算作一次比較
            if (target == current.value) {
                return count; // 找到目標，提早結束
            } else if (target < current.value) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return count; // Missing target (走到 null) 的總比較次數
    }

    // 4. 執行實驗並輸出比較報表
    public static void runExperiment(String experimentName, int[] dataToInsert, int[] keysToSearch, int missingKey) {
        ShapeNode root = null;
        
        // 依據傳入的陣列順序建樹
        for (int val : dataToInsert) {
            root = insert(root, val);
        }

        // 收集統計數據
        int h = height(root);
        
        int totalSearchComparisons = 0;
        for (int key : keysToSearch) {
            totalSearchComparisons += getSearchCount(root, key);
        }
        
        int missingKeyCount = getSearchCount(root, missingKey);

        // 輸出報表
        System.out.println("=== " + experimentName + " ===");
        System.out.println("Tree Height               : " + h);
        System.out.println("Total Search Comparisons  : " + totalSearchComparisons);
        System.out.println("Missing Key (" + missingKey + ") Cost    : " + missingKeyCount);
        System.out.println("Average Valid Search Cost : " + String.format("%.2f", (double) totalSearchComparisons / keysToSearch.length));
        System.out.println();
    }

    // 測試主程式
    public static void main(String[] args) {
        System.out.println("--- Tree Shape Performance Comparison ---\n");

        // 定義所有需要被搜尋的 15 個 Valid Keys (1 ~ 15)
        int[] allKeys = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        
        // 定義一個一定找不到的 Missing Key
        int missingKey = 99;

        // 情境 1: 升冪排序 (Ascending Order)
        // 預期結果: 嚴重向右傾斜 (Right-skewed)
        int[] ascendingData = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        runExperiment("Case 1: Ascending Order (Right-Skewed)", ascendingData, allKeys, missingKey);

        // 情境 2: 降冪排序 (Descending Order)
        // 預期結果: 嚴重向左傾斜 (Left-skewed)
        int[] descendingData = {15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
        runExperiment("Case 2: Descending Order (Left-Skewed)", descendingData, allKeys, missingKey);

        // 情境 3: 接近平衡 (Nearly Balanced Order)
        // 預期結果: 完美的平衡二元搜尋樹 (以 8 為 Root，接著平均分配)
        int[] balancedData = {8, 4, 12, 2, 6, 10, 14, 1, 3, 5, 7, 9, 11, 13, 15};
        runExperiment("Case 3: Nearly Balanced Order", balancedData, allKeys, missingKey);
    }
}
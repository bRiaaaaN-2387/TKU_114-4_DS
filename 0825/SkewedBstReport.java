// 定義 BST 節點
class SkewNode {
    int value;
    SkewNode left;
    SkewNode right;

    SkewNode(int value) {
        this.value = value;
    }
}

public class SkewedBstReport {

    // 1. 新增節點至 BST
    public static SkewNode insert(SkewNode node, int value) {
        if (node == null) {
            return new SkewNode(value);
        }
        if (value < node.value) {
            node.left = insert(node.left, value);
        } else if (value > node.value) {
            node.right = insert(node.right, value);
        }
        return node; // 忽略重複值
    }

    // 2. 計算節點總數 (Size)
    public static int size(SkewNode node) {
        if (node == null) {
            return 0;
        }
        return 1 + size(node.left) + size(node.right);
    }

    // 3. 計算樹的高度 (Height)
    // 定義：空樹 height = -1，單一節點 (Leaf) height = 0
    public static int height(SkewNode node) {
        if (node == null) {
            return -1;
        }
        return 1 + Math.max(height(node.left), height(node.right));
    }

    // 4. 取得搜尋特定目標所需的比較次數 (Search Comparison Count)
    public static int getSearchComparisonCount(SkewNode root, int target) {
        int count = 0;
        SkewNode current = root;

        while (current != null) {
            count++; // 進入節點即算作一次比較
            if (target == current.value) {
                return count; // 找到目標，回傳比較次數
            } else if (target < current.value) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return count; // 若找不到，回傳走到 null 為止的總比較次數
    }

    // 5. 輸出測試報表
    public static void printReport(String treeName, SkewNode root, int[] searchTargets) {
        System.out.println("=== Report: " + treeName + " ===");
        System.out.println("Size   : " + size(root));
        System.out.println("Height : " + height(root));
        
        System.out.println("--- Search Comparisons ---");
        for (int target : searchTargets) {
            int count = getSearchComparisonCount(root, target);
            System.out.println("Target " + target + " -> " + count + " comparisons");
        }
        System.out.println("================================\n");
    }

    // 測試主程式
    public static void main(String[] args) {
        
        // 準備測試資料
        // Sorted: 10, 20, 30, 40, 50, 60, 70 (會形成嚴重向右傾斜的樹)
        int[] sortedData = {10, 20, 30, 40, 50, 60, 70};
        
        // Balanced: 40 為 Root，接著平均分配左右子樹 (會形成完美的平衡樹)
        int[] balancedData = {40, 20, 60, 10, 30, 50, 70};
        
        // 準備要搜尋的目標：包含 Root、中間節點、最底層節點與不存在的數值
        int[] targets = {40, 70, 99};

        // 建構 Skewed Tree
        SkewNode skewedRoot = null;
        for (int val : sortedData) {
            skewedRoot = insert(skewedRoot, val);
        }

        // 建構 Balanced Tree
        SkewNode balancedRoot = null;
        for (int val : balancedData) {
            balancedRoot = insert(balancedRoot, val);
        }

        // 執行並印出比較報表
        System.out.println("=== BST Performance Comparison ===\n");
        printReport("Skewed Tree (Sorted Data)", skewedRoot, targets);
        printReport("Balanced Tree (Optimized Data)", balancedRoot, targets);
    }
}
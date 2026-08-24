// 統計專用二元樹節點 (使用整數作為值)
class StatsNode {
    int value;
    StatsNode left;
    StatsNode right;

    StatsNode(int value) {
        this.value = value;
    }
}

public class BinaryTreeStatistics {

    // 1. 計算節點總數 (Size)
    static int size(StatsNode node) {
        if (node == null) {
            return 0;
        }
        // 當前節點 (1) + 左子樹大小 + 右子樹大小
        return 1 + size(node.left) + size(node.right);
    }

    // 2. 計算所有節點數值總和 (Sum)
    static int sum(StatsNode node) {
        if (node == null) {
            return 0;
        }
        // 當前數值 + 左子樹總和 + 右子樹總和
        return node.value + sum(node.left) + sum(node.right);
    }

    // 3. 尋找最大值 (Maximum) - Wrapper 方法
    static int maximum(StatsNode node) {
        // 明確處理空樹狀況，拒絕以 0 或預設值代表最大值
        if (node == null) {
            throw new IllegalArgumentException("Error: Cannot find maximum in an empty tree.");
        }
        return maxHelper(node);
    }

    // 尋找最大值的遞迴 (Helper)
    private static int maxHelper(StatsNode node) {
        // Base case: 若走到空節點，回傳極小值，避免干擾 Math.max 的比較
        if (node == null) {
            return Integer.MIN_VALUE;
        }
        
        int currentMax = node.value;
        int leftMax = maxHelper(node.left);
        int rightMax = maxHelper(node.right);
        
        // 比較 當前值、左子樹最大值、右子樹最大值
        return Math.max(currentMax, Math.max(leftMax, rightMax));
    }

    // 4. 計算葉節點數量 (Leaf Count)
    static int leafCount(StatsNode node) {
        if (node == null) {
            return 0;
        }
        // 若左右皆為空，代表此節點為葉節點
        if (node.left == null && node.right == null) {
            return 1;
        }
        return leafCount(node.left) + leafCount(node.right);
    }

    // 5. 計算樹的高度 (Height)
    static int height(StatsNode node) {
        // 依教材定義：空樹 height = -1
        if (node == null) {
            return -1;
        }
        // 1 + 左右子樹中較大的高度
        return 1 + Math.max(height(node.left), height(node.right));
    }

    // 6. 檢查是否包含特定數值 (Contains)
    static boolean contains(StatsNode node, int target) {
        if (node == null) {
            return false;
        }
        // 若找到目標，即回傳 true
        if (node.value == target) {
            return true;
        }
        // 否則繼續往左右子樹搜尋
        return contains(node.left, target) || contains(node.right, target);
    }

    // 測試主程式
    public static void main(String[] args) {
        
        /*
         * 建構測試用二元樹:
         *          10
         *         /  \
         *        5    20
         *       / \     \
         *      3   7    30
         *     /
         *    1
         */
        StatsNode root = new StatsNode(10);
        root.left = new StatsNode(5);
        root.right = new StatsNode(20);
        root.left.left = new StatsNode(3);
        root.left.right = new StatsNode(7);
        root.right.right = new StatsNode(30);
        root.left.left.left = new StatsNode(1);

        System.out.println("=== Binary Tree Statistics ===");
        System.out.println("Size       : " + size(root));       // 預期: 7
        System.out.println("Sum        : " + sum(root));        // 預期: 76
        System.out.println("Maximum    : " + maximum(root));    // 預期: 30
        System.out.println("Leaf Count : " + leafCount(root));  // 預期: 3 (1, 7, 30)
        System.out.println("Height     : " + height(root));     // 預期: 3
        System.out.println("Contains 7 : " + contains(root, 7)); // 預期: true
        System.out.println("Contains 99: " + contains(root, 99)); // 預期: false
        System.out.println();

        System.out.println("=== Exception Handling Test (Empty Tree) ===");
        try {
            maximum(null);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught Exception: " + e.getMessage());
        }
    }
}
// 定義 BST 節點
class RangeNode {
    int value;
    RangeNode left;
    RangeNode right;

    RangeNode(int value) {
        this.value = value;
    }
}

public class BstRangeReport {

    // 1. 尋找最小值 (Min)
    public static int min(RangeNode node) {
        if (node == null) {
            throw new IllegalArgumentException("Error: Tree is empty. Cannot find min.");
        }
        // BST 的最小值一定在最左下的節點
        RangeNode current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current.value;
    }

    // 2. 尋找最大值 (Max)
    public static int max(RangeNode node) {
        if (node == null) {
            throw new IllegalArgumentException("Error: Tree is empty. Cannot find max.");
        }
        // BST 的最大值一定在最右下的節點
        RangeNode current = node;
        while (current.right != null) {
            current = current.right;
        }
        return current.value;
    }

    // 3. 列印指定範圍內的數值 (Wrapper)
    public static void printRange(RangeNode node, int low, int high) {
        if (node == null) {
            System.out.println("Info: Tree is empty.");
            return;
        }

        // 處理 low > high 的例外狀況，自動將兩者交換
        if (low > high) {
            System.out.println("Warning: 'low' (" + low + ") is greater than 'high' (" + high + "). Swapping them.");
            int temp = low;
            low = high;
            high = temp;
        }

        System.out.print("Result [" + low + ", " + high + "]: ");
        printRangeHelper(node, low, high);
        System.out.println();
    }

    // 列印範圍的遞迴邏輯 (Helper)
    private static void printRangeHelper(RangeNode node, int low, int high) {
        if (node == null) {
            return;
        }

        // 若當前節點值大於 low，代表「左子樹」還有可能落在範圍內，繼續往左搜尋
        if (node.value > low) {
            printRangeHelper(node.left, low, high);
        }

        // 包含端點：若當前節點值落在區間內，則印出
        if (node.value >= low && node.value <= high) {
            System.out.print(node.value + " ");
        }

        // 若當前節點值小於 high，代表「右子樹」還有可能落在範圍內，繼續往右搜尋
        if (node.value < high) {
            printRangeHelper(node.right, low, high);
        }
    }

    // 測試主程式
    public static void main(String[] args) {
        
        /*
         * 建構測試用的 BST:
         *             50  (Root)
         *           /    \
         *         30      70
         *        /  \    /  \
         *      20   40  60   80
         */
        RangeNode root = new RangeNode(50);
        root.left = new RangeNode(30);
        root.right = new RangeNode(70);
        root.left.left = new RangeNode(20);
        root.left.right = new RangeNode(40);
        root.right.left = new RangeNode(60);
        root.right.right = new RangeNode(80);

        System.out.println("=== BST Min/Max Report ===");
        System.out.println("Minimum Value: " + min(root));
        System.out.println("Maximum Value: " + max(root));
        System.out.println();

        System.out.println("=== BST Range Report ===");
        
        // 測試 1: 正常範圍 (包含端點 30)
        printRange(root, 30, 65);
        
        // 測試 2: 反向範圍 (low > high)
        printRange(root, 80, 40);
        
        // 測試 3: 邊界測試 (只包含單一節點)
        printRange(root, 60, 60);
        
        // 測試 4: 超出範圍的尋找
        printRange(root, 90, 100);

        System.out.println("\n=== Exception Test ===");
        try {
            min(null); // 測試傳入空樹
        } catch (IllegalArgumentException e) {
            System.out.println("Caught Exception: " + e.getMessage());
        }
    }
}
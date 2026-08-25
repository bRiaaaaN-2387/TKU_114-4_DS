// 定義 BST 節點
class BstNode {
    int value;
    BstNode left;
    BstNode right;

    BstNode(int value) {
        this.value = value;
    }
}

public class BstSearchTrace {

    // 執行 BST 搜尋並追蹤過程
    static void searchTrace(BstNode root, int target) {
        System.out.println("--- Searching for: " + target + " ---");
        
        BstNode current = root;
        int count = 0; // 記錄比較次數

        while (current != null) {
            count++;
            System.out.print("Step " + count + " | Current: " + current.value + " -> ");

            // 判斷目標值與當前節點值的關係
            if (target == current.value) {
                System.out.println("MATCH! (Found in " + count + " comparisons)");
                return; // 找到目標，結束搜尋
            } else if (target < current.value) {
                System.out.println("Target is smaller. Going LEFT.");
                current = current.left; // 往左子樹找
            } else {
                System.out.println("Target is larger. Going RIGHT.");
                current = current.right; // 往右子樹找
            }
        }

        // 迴圈結束仍未找到
        System.out.println("Result: NOT FOUND. (Total comparisons: " + count + ")");
    }

    // 測試主程式
    public static void main(String[] args) {
        
        /*
         * 建構測試用的 BST:
         *             50  (Root)
         *           /    \
         *         30      70  (Internal Nodes)
         *        /  \       \
         *      20    40      80  (Leaf Nodes)
         */
        BstNode root = new BstNode(50);
        root.left = new BstNode(30);
        root.right = new BstNode(70);
        root.left.left = new BstNode(20);
        root.left.right = new BstNode(40);
        root.right.right = new BstNode(80);

        System.out.println("=== BST Search Trace System ===\n");

        // 測試 1: 尋找 Root
        searchTrace(root, 50);
        System.out.println();

        // 測試 2: 尋找 Internal Node
        searchTrace(root, 30);
        System.out.println();

        // 測試 3: 尋找 Leaf Node
        searchTrace(root, 40);
        System.out.println();

        // 測試 4: 尋找 Missing Value (不存在的值)
        searchTrace(root, 99);
        System.out.println();
        
        // 額外測試: 尋找會提早中斷的不存在的值 (比 20 小)
        searchTrace(root, 10);
    }
}
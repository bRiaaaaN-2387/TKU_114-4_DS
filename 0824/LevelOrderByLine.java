import java.util.LinkedList;
import java.util.Queue;

// 節點資料結構
class LineNode {
    String value;
    LineNode left;
    LineNode right;

    LineNode(String value) {
        this.value = value;
    }
}

public class LevelOrderByLine {

    // 逐層輸出層序走訪 (Level-Order)
    static void printLevelOrder(LineNode root) {
        // 處理空樹 (Empty tree)
        if (root == null) {
            System.out.println("Info: The tree is empty.");
            return;
        }

        // 初始化 Queue 以進行真正的 Level-order
        Queue<LineNode> queue = new LinkedList<>();
        queue.offer(root);
        int levelIndex = 0; // 紀錄當前層級的索引

        // 當 Queue 不為空時，代表還有下一層
        while (!queue.isEmpty()) {
            // 核心技巧：取得當下 Queue 的大小，這代表「這一層的節點總數」
            int levelSize = queue.size();
            
            // 輸出當前層級的標籤與節點總數
            System.out.print("Level " + levelIndex + " (Node Count: " + levelSize + ") -> ");

            // 利用迴圈，只處理「當前這一層」的節點
            for (int i = 0; i < levelSize; i++) {
                LineNode current = queue.poll();
                System.out.print(current.value + " ");

                // 將下一層的子節點加入 Queue (只有非 null 的才加入)
                if (current.left != null) {
                    queue.offer(current.left);
                }
                if (current.right != null) {
                    queue.offer(current.right);
                }
            }
            
            // 處理完這一層的所有節點後換行
            System.out.println();
            levelIndex++;
        }
    }

    // 測試主程式
    public static void main(String[] args) {
        
        System.out.println("=== Test 1: Empty Tree ===");
        printLevelOrder(null);
        System.out.println();

        System.out.println("=== Test 2: Multi-Level Tree ===");
        /*
         * 建構的樹狀結構：
         *           A             (Level 0: 1 node)
         *         /   \
         *        B     C          (Level 1: 2 nodes)
         *       / \     \
         *      D   E     F        (Level 2: 3 nodes)
         *           \   /
         *            G H          (Level 3: 2 nodes)
         */
        LineNode root = new LineNode("A");
        root.left = new LineNode("B");
        root.right = new LineNode("C");
        
        root.left.left = new LineNode("D");
        root.left.right = new LineNode("E");
        root.right.right = new LineNode("F");
        
        root.left.right.right = new LineNode("G");
        root.right.right.left = new LineNode("H");

        printLevelOrder(root);
    }
}
import java.util.ArrayList;
import java.util.List;

// 定義 BST 節點
class DeleteNode {
    int value;
    DeleteNode left;
    DeleteNode right;

    DeleteNode(int value) {
        this.value = value;
    }
}

public class BstDeleteCases {

    // 1. 刪除節點 (Wrapper)
    public static DeleteNode delete(DeleteNode root, int key) {
        return deleteHelper(root, key);
    }

    // 刪除節點的遞迴邏輯 (Helper)
    private static DeleteNode deleteHelper(DeleteNode node, int key) {
        // Base case: 找不到目標節點
        if (node == null) {
            return null;
        }

        // 步驟 1: 尋找目標節點
        if (key < node.value) {
            node.left = deleteHelper(node.left, key);
        } else if (key > node.value) {
            node.right = deleteHelper(node.right, key);
        } else {
            // 步驟 2: 找到目標節點，處理三種刪除情境

            // Case 1 & Case 2: 葉節點 (Leaf) 或 單一子節點 (Single-Child)
            // 若左子樹為空，直接回傳右子樹 (可能為 null，即 Leaf 情境)
            if (node.left == null) {
                return node.right;
            } 
            // 若右子樹為空，直接回傳左子樹
            else if (node.right == null) {
                return node.left;
            }

            // Case 3: 雙子節點 (Two-Child)
            // 尋找右子樹中的最小值 (Successor)
            node.value = getMin(node.right);
            // 遞迴刪除右子樹中的該最小值節點
            node.right = deleteHelper(node.right, node.value);
        }
        return node;
    }

    // 輔助方法：尋找子樹中的最小值
    private static int getMin(DeleteNode node) {
        int minVal = node.value;
        while (node.left != null) {
            minVal = node.left.value;
            node = node.left;
        }
        return minVal;
    }

    // 2. 取得中序走訪結果
    public static List<Integer> inorder(DeleteNode node) {
        List<Integer> result = new ArrayList<>();
        inorderHelper(node, result);
        return result;
    }

    private static void inorderHelper(DeleteNode node, List<Integer> result) {
        if (node == null) return;
        inorderHelper(node.left, result);
        result.add(node.value);
        inorderHelper(node.right, result);
    }

    // 3. 取得節點總數
    public static int size(DeleteNode node) {
        if (node == null) return 0;
        return 1 + size(node.left) + size(node.right);
    }

    // 4. 驗證是否為合法的 BST
    public static boolean isValidBST(DeleteNode node) {
        // 使用 Long 避免 Integer 極值的邊界錯誤
        return isValidBSTHelper(node, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private static boolean isValidBSTHelper(DeleteNode node, long min, long max) {
        if (node == null) return true;
        // 檢查當前節點是否違反 BST 大小規則
        if (node.value <= min || node.value >= max) {
            return false;
        }
        // 遞迴檢查左子樹 (上限更新為當前值) 與 右子樹 (下限更新為當前值)
        return isValidBSTHelper(node.left, min, node.value) && 
               isValidBSTHelper(node.right, node.value, max);
    }

    // 5. 輸出報表輔助方法
    private static void printStatus(String action, DeleteNode root) {
        System.out.println("--- Action: " + action + " ---");
        System.out.println("Inorder : " + inorder(root));
        System.out.println("Size    : " + size(root));
        System.out.println("Is Valid: " + isValidBST(root));
        System.out.println();
    }

    // 測試主程式
    public static void main(String[] args) {
        
        /*
         * 建構初始 BST:
         *             50  (Root, 雙子節點)
         *           /    \
         *         30      70  (雙子節點)
         *        /  \    /  \
         *      20   40  60   80 (20, 40, 80 為葉節點)
         *                 \
         *                 65 (60 為單一子節點)
         */
        DeleteNode root = new DeleteNode(50);
        root.left = new DeleteNode(30);
        root.right = new DeleteNode(70);
        root.left.left = new DeleteNode(20);
        root.left.right = new DeleteNode(40);
        root.right.left = new DeleteNode(60);
        root.right.right = new DeleteNode(80);
        root.right.left.right = new DeleteNode(65);

        System.out.println("=== BST Delete Cases Test ===\n");

        // 初始狀態
        printStatus("Initial State", root);

        // 測試 1: 刪除 Leaf Node (20)
        root = delete(root, 20);
        printStatus("Delete Leaf Node (20)", root);

        // 測試 2: 刪除 Single-Child Node (60，擁有右子節點 65)
        root = delete(root, 60);
        printStatus("Delete Single-Child Node (60)", root);

        // 測試 3: 刪除 Two-Child Node (50，Root 節點)
        root = delete(root, 50);
        printStatus("Delete Two-Child Node (50 - Root)", root);
    }
}
import java.util.ArrayList;
import java.util.List;

// 測試專用節點
class TestDeleteNode {
    int value;
    TestDeleteNode left;
    TestDeleteNode right;

    public TestDeleteNode(int value) {
        this.value = value;
    }
}

public class BstDeleteTestSuite {

    // 1. 新增節點 (輔助建立測試環境)
    public static TestDeleteNode insert(TestDeleteNode node, int value) {
        if (node == null) {
            return new TestDeleteNode(value);
        }
        if (value < node.value) {
            node.left = insert(node.left, value);
        } else if (value > node.value) {
            node.right = insert(node.right, value);
        }
        return node;
    }

    // 2. 刪除節點 (核心測試目標)
    public static TestDeleteNode delete(TestDeleteNode node, int key) {
        // Base case: 找不到目標或樹為空
        if (node == null) {
            return null;
        }

        // 尋找目標節點
        if (key < node.value) {
            node.left = delete(node.left, key);
        } else if (key > node.value) {
            node.right = delete(node.right, key);
        } else {
            // 找到目標節點，處理刪除情境
            
            // 情境 A & B: 葉節點或單一子樹
            if (node.left == null) {
                return node.right;
            } else if (node.right == null) {
                return node.left;
            }

            // 情境 C: 雙子樹 (尋找右子樹最小值替換)
            TestDeleteNode minNode = getMin(node.right);
            node.value = minNode.value;
            node.right = delete(node.right, minNode.value);
        }
        return node;
    }

    // 取得子樹最小值
    private static TestDeleteNode getMin(TestDeleteNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    // 3. 中序走訪 (取得當前樹的狀態)
    public static List<Integer> getInorder(TestDeleteNode node) {
        List<Integer> result = new ArrayList<>();
        inorderHelper(node, result);
        return result;
    }

    private static void inorderHelper(TestDeleteNode node, List<Integer> result) {
        if (node == null) return;
        inorderHelper(node.left, result);
        result.add(node.value);
        inorderHelper(node.right, result);
    }

    // 輔助列印狀態
    private static void printState(String message, TestDeleteNode root) {
        System.out.println(message + " -> Inorder: " + getInorder(root));
    }

    // 測試主程式
    public static void main(String[] args) {
        System.out.println("=== BST Delete Test Suite ===\n");

        // Test 1: Empty Tree
        System.out.println("--- Test 1: Empty Tree ---");
        TestDeleteNode root1 = null;
        printState("Before delete 10", root1);
        root1 = delete(root1, 10);
        printState("After delete 10 ", root1);
        System.out.println();

        // Test 2: Missing Value
        System.out.println("--- Test 2: Missing Value ---");
        TestDeleteNode root2 = null;
        root2 = insert(root2, 50);
        root2 = insert(root2, 30);
        printState("Before delete 99", root2);
        root2 = delete(root2, 99);
        printState("After delete 99 ", root2);
        System.out.println();

        // Test 3: Single Root
        System.out.println("--- Test 3: Single Root ---");
        TestDeleteNode root3 = null;
        root3 = insert(root3, 50);
        printState("Before delete 50", root3);
        root3 = delete(root3, 50);
        printState("After delete 50 ", root3);
        System.out.println();

        // Test 4: Root with One Child (Right child only)
        System.out.println("--- Test 4: Root with One Child ---");
        TestDeleteNode root4 = null;
        root4 = insert(root4, 50);
        root4 = insert(root4, 70); // 只有右子樹
        printState("Before delete 50", root4);
        root4 = delete(root4, 50);
        printState("After delete 50 ", root4);
        System.out.println();

        // Test 5: Root with Two Children
        System.out.println("--- Test 5: Root with Two Children ---");
        TestDeleteNode root5 = null;
        root5 = insert(root5, 50);
        root5 = insert(root5, 30);
        root5 = insert(root5, 70);
        printState("Before delete 50", root5);
        root5 = delete(root5, 50);
        printState("After delete 50 ", root5);
        System.out.println();

        // Test 6: Continuous Deletion to Empty
        System.out.println("--- Test 6: Continuous Deletion to Empty ---");
        TestDeleteNode root6 = null;
        int[] nodesToInsert = {50, 30, 70, 20, 40, 60, 80};
        for (int val : nodesToInsert) {
            root6 = insert(root6, val);
        }
        printState("Initial Tree", root6);
        
        // 依序刪除所有節點，包含葉節點、單子樹與雙子樹情境
        int[] nodesToDelete = {20, 70, 30, 40, 80, 60, 50};
        for (int val : nodesToDelete) {
            root6 = delete(root6, val);
            printState("Deleted " + val + "  ", root6);
        }
    }
}
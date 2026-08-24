import java.util.ArrayList;
import java.util.List;

// 二元樹節點結構
class ReportNode {
    String value;
    ReportNode left;
    ReportNode right;

    ReportNode(String value) {
        this.value = value;
    }
}

public class BinaryTreeStructureReport {

    // 1. 計算總節點數 (Size)
    static int size(ReportNode node) {
        if (node == null) {
            return 0;
        }
        return 1 + size(node.left) + size(node.right);
    }

    // 2. 計算樹的高度 (Height)
    // 根據教材定義：空樹 height = -1，Leaf height = 0
    static int height(ReportNode node) {
        if (node == null) {
            return -1;
        }
        return 1 + Math.max(height(node.left), height(node.right));
    }

    // 3. 計算葉節點數量 (Leaf Count)
    static int leafCount(ReportNode node) {
        if (node == null) {
            return 0;
        }
        // 若左右子節點皆為 null，則此節點為 leaf
        if (node.left == null && node.right == null) {
            return 1;
        }
        return leafCount(node.left) + leafCount(node.right);
    }

    // 4. 收集並回傳所有葉節點的值
    static List<String> getLeaves(ReportNode node) {
        List<String> leaves = new ArrayList<>();
        collectLeavesHelper(node, leaves);
        return leaves;
    }

    // 收集葉節點的遞迴輔助方法
    private static void collectLeavesHelper(ReportNode node, List<String> leaves) {
        if (node == null) {
            return;
        }
        if (node.left == null && node.right == null) {
            leaves.add(node.value);
        }
        collectLeavesHelper(node.left, leaves);
        collectLeavesHelper(node.right, leaves);
    }

    // 產生並輸出結構報表
    static void printReport(String treeName, ReportNode root) {
        System.out.println("=== Tree Report: " + treeName + " ===");
        
        // 取得 Root 狀態
        String rootValue = (root == null) ? "[Empty]" : root.value;
        System.out.println("Root       : " + rootValue);
        
        // 取得並印出所有葉節點
        List<String> leaves = getLeaves(root);
        System.out.println("All Leaves : " + leaves);
        
        // 輸出各項統計數據
        System.out.println("Size       : " + size(root));
        System.out.println("Leaf Count : " + leafCount(root));
        System.out.println("Height     : " + height(root));
        System.out.println("----------------------------------\n");
    }

    // 測試主程式
    public static void main(String[] args) {
        
        // 測試 1: Empty Tree (空樹)
        printReport("Empty Tree", null);

        // 測試 2: Single-Node Tree (單節點樹)
        ReportNode singleNode = new ReportNode("RootOnly");
        printReport("Single-Node Tree", singleNode);

        // 測試 3: 包含至少 7 個 Node 的樹
        /*
         * 結構示意圖:
         *          A (Root)
         *         / \
         *        B   C
         *       / \   \
         *      D   E   F
         *           \
         *            G
         */
        ReportNode root7 = new ReportNode("A");
        root7.left = new ReportNode("B");
        root7.right = new ReportNode("C");
        root7.left.left = new ReportNode("D");
        root7.left.right = new ReportNode("E");
        root7.right.right = new ReportNode("F");
        root7.left.right.right = new ReportNode("G"); // 第 7 個節點

        printReport("7-Node Tree", root7);
    }
}
import java.util.ArrayList;
import java.util.List;

// 共用測試節點
class BugLabNode {
    int value;
    BugLabNode left;
    BugLabNode right;

    BugLabNode(int value) {
        this.value = value;
    }
}

public class TreeBugLab {

    // ==========================================
    // Bug 1: Search 方向相反
    // 症狀: 找不到確實存在的 key[cite: 1]
    // ==========================================
    static boolean buggySearch(BugLabNode node, int target) {
        if (node == null) return false;
        if (target == node.value) return true;
        // BUG: 方向寫反了 (target 較小卻往 right 找)
        if (target < node.value) {
            return buggySearch(node.right, target);
        } else {
            return buggySearch(node.left, target);
        }
    }

    static boolean fixedSearch(BugLabNode node, int target) {
        if (node == null) return false;
        if (target == node.value) return true;
        // FIX: 小往 left、大往 right[cite: 1]
        if (target < node.value) {
            return fixedSearch(node.left, target);
        } else {
            return fixedSearch(node.right, target);
        }
    }

    // ==========================================
    // Bug 2: Inorder 順序錯誤 (Insert 接錯 child)
    // 症狀: Inorder 印出來的結果沒有由小到大排序[cite: 1]
    // ==========================================
    static BugLabNode buggyInsert(BugLabNode node, int value) {
        if (node == null) return new BugLabNode(value);
        // BUG: 較小的值接到了 right，較大的值接到了 left
        if (value < node.value) {
            node.right = buggyInsert(node.right, value); 
        } else if (value > node.value) {
            node.left = buggyInsert(node.left, value);
        }
        return node;
    }

    static BugLabNode fixedInsert(BugLabNode node, int value) {
        if (node == null) return new BugLabNode(value);
        // FIX: 修正 link assignment[cite: 1]
        if (value < node.value) {
            node.left = fixedInsert(node.left, value);
        } else if (value > node.value) {
            node.right = fixedInsert(node.right, value);
        }
        return node;
    }

    // ==========================================
    // Bug 3: Delete 遺失 Child (One-child 回傳 null)
    // 症狀: 刪除擁有單一子樹的節點後，整個子樹不見了[cite: 1]
    // ==========================================
    static BugLabNode buggyDelete(BugLabNode node, int target) {
        if (node == null) return null;
        if (target < node.value) {
            node.left = buggyDelete(node.left, target);
        } else if (target > node.value) {
            node.right = buggyDelete(node.right, target);
        } else {
            // BUG: 遇到 one-child 時直接回傳 null，導致 target 的唯一 child 被切斷[cite: 1]
            if (node.left == null) return null; 
            if (node.right == null) return null; 
            
            BugLabNode minNode = getMin(node.right);
            node.value = minNode.value;
            node.right = buggyDelete(node.right, minNode.value);
        }
        return node;
    }

    static BugLabNode fixedDelete(BugLabNode node, int target) {
        if (node == null) return null;
        if (target < node.value) {
            node.left = fixedDelete(node.left, target);
        } else if (target > node.value) {
            node.right = fixedDelete(node.right, target);
        } else {
            // FIX: 遇到 one-child 時，必須回傳 non-null child 以接回原本的樹[cite: 1]
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            
            BugLabNode minNode = getMin(node.right);
            node.value = minNode.value;
            node.right = fixedDelete(node.right, minNode.value);
        }
        return node;
    }

    private static BugLabNode getMin(BugLabNode node) {
        while (node.left != null) node = node.left;
        return node;
    }

    // ==========================================
    // Bug 4: Validation 只檢查直接 child
    // 症狀: 無法揪出隱藏在深層的違規節點 (Deep Violation)[cite: 1]
    // ==========================================
    static boolean buggyIsValid(BugLabNode node) {
        if (node == null) return true;
        // BUG: 只比較直接的 parent-child 關係[cite: 1]
        if (node.left != null && node.left.value >= node.value) return false;
        if (node.right != null && node.right.value <= node.value) return false;
        return buggyIsValid(node.left) && buggyIsValid(node.right);
    }

    static boolean fixedIsValid(BugLabNode node, long low, long high) {
        if (node == null) return true;
        // FIX: 傳遞 low/high boundary，確保 deep node 依然符合祖先限制[cite: 1]
        if (node.value <= low || node.value >= high) return false;
        return fixedIsValid(node.left, low, node.value) && 
               fixedIsValid(node.right, node.value, high);
    }

    // --- 輔助方法：取得 Inorder 結果 ---
    static List<Integer> getInorder(BugLabNode root) {
        List<Integer> list = new ArrayList<>();
        inorderHelper(root, list);
        return list;
    }

    static void inorderHelper(BugLabNode node, List<Integer> list) {
        if (node == null) return;
        inorderHelper(node.left, list);
        list.add(node.value);
        inorderHelper(node.right, list);
    }

    // --- 測試主程式 ---
    public static void main(String[] args) {
        System.out.println("=== Tree Bug Lab ===");

        // --- Test Bug 1 ---
        System.out.println("\n--- Bug 1: Search Direction Reversed ---");
        BugLabNode root1 = new BugLabNode(50);
        root1.left = new BugLabNode(30);
        System.out.println("Buggy Search (Target 30): " + buggySearch(root1, 30)); // 應該找得到卻回傳 false
        System.out.println("Fixed Search (Target 30): " + fixedSearch(root1, 30));

        // --- Test Bug 2 ---
        System.out.println("\n--- Bug 2: Inorder Not Sorted (Wrong Insert) ---");
        BugLabNode root2Buggy = null;
        root2Buggy = buggyInsert(root2Buggy, 50);
        root2Buggy = buggyInsert(root2Buggy, 30);
        root2Buggy = buggyInsert(root2Buggy, 70);
        System.out.println("Buggy Inorder: " + getInorder(root2Buggy)); // 結果未排序

        BugLabNode root2Fixed = null;
        root2Fixed = fixedInsert(root2Fixed, 50);
        root2Fixed = fixedInsert(root2Fixed, 30);
        root2Fixed = fixedInsert(root2Fixed, 70);
        System.out.println("Fixed Inorder: " + getInorder(root2Fixed));

        // --- Test Bug 3 ---
        System.out.println("\n--- Bug 3: Delete Losing Subtree ---");
        BugLabNode root3Buggy = new BugLabNode(50);
        root3Buggy.left = new BugLabNode(30);
        root3Buggy.left.left = new BugLabNode(20); // 30 是 One-child node
        root3Buggy = buggyDelete(root3Buggy, 30);
        System.out.println("Buggy Delete 30 (Remaining): " + getInorder(root3Buggy)); // 20 不見了

        BugLabNode root3Fixed = new BugLabNode(50);
        root3Fixed.left = new BugLabNode(30);
        root3Fixed.left.left = new BugLabNode(20);
        root3Fixed = fixedDelete(root3Fixed, 30);
        System.out.println("Fixed Delete 30 (Remaining): " + getInorder(root3Fixed)); // 20 成功被接回

        // --- Test Bug 4 ---
        System.out.println("\n--- Bug 4: Validation Only Checks Direct Child ---");
        BugLabNode root4 = new BugLabNode(50);
        root4.left = new BugLabNode(30);
        // 刻意建立深層違規: 60 > 30 (符合直接 parent)，但 60 > 50 (違反 root 限制)[cite: 1]
        root4.left.right = new BugLabNode(60); 
        System.out.println("Buggy Validation: " + buggyIsValid(root4)); // 回傳 true (漏抓)
        System.out.println("Fixed Validation: " + fixedIsValid(root4, Long.MIN_VALUE, Long.MAX_VALUE)); // 回傳 false (成功揪出)
    }
}
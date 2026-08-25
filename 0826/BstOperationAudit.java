import java.util.ArrayList;
import java.util.List;

// BST 節點
class AuditNode {
    int value;
    AuditNode left;
    AuditNode right;

    AuditNode(int value) {
        this.value = value;
    }
}

public class BstOperationAudit {
    
    private AuditNode root;

    // --- 核心操作 ---

    // 新增節點
    public boolean add(int value) {
        if (root == null) {
            root = new AuditNode(value);
            return true;
        }

        AuditNode current = root;
        while (true) {
            if (value == current.value) return false; // 拒絕重複鍵值
            if (value < current.value) {
                if (current.left == null) {
                    current.left = new AuditNode(value);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new AuditNode(value);
                    return true;
                }
                current = current.right;
            }
        }
    }

    // 檢查是否包含特定節點
    public boolean contains(int target) {
        AuditNode current = root;
        while (current != null) {
            if (target == current.value) return true;
            current = target < current.value ? current.left : current.right;
        }
        return false;
    }

    // 刪除節點 (Wrapper)
    public boolean remove(int target) {
        if (!contains(target)) return false; // 找不到目標，回傳 false
        root = removeHelper(root, target);
        return true;
    }

    // 刪除節點邏輯 (Helper)
    private AuditNode removeHelper(AuditNode node, int target) {
        if (target < node.value) {
            node.left = removeHelper(node.left, target);
        } else if (target > node.value) {
            node.right = removeHelper(node.right, target);
        } else {
            // Case 1 & 2: Leaf 或 One-Child
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            
            // Case 3: Two-Children (尋找右子樹最小值作為後繼者)
            AuditNode successor = minimumNode(node.right);
            node.value = successor.value;
            node.right = removeHelper(node.right, successor.value); // 刪除原後繼者節點[cite: 1]
        }
        return node;
    }

    private AuditNode minimumNode(AuditNode node) {
        while (node.left != null) node = node.left;
        return node;
    }

    // --- 統計與驗證 ---

    public List<Integer> inorder() {
        List<Integer> result = new ArrayList<>();
        inorderHelper(root, result);
        return result;
    }

    private void inorderHelper(AuditNode node, List<Integer> result) {
        if (node == null) return;
        inorderHelper(node.left, result);
        result.add(node.value);
        inorderHelper(node.right, result);
    }

    public int size() {
        return sizeHelper(root);
    }

    private int sizeHelper(AuditNode node) {
        if (node == null) return 0;
        return 1 + sizeHelper(node.left) + sizeHelper(node.right);
    }

    public int height() {
        return heightHelper(root);
    }

    private int heightHelper(AuditNode node) {
        if (node == null) return -1; // 空樹高度為 -1[cite: 1]
        return 1 + Math.max(heightHelper(node.left), heightHelper(node.right));
    }

    public boolean isValid() {
        // 使用 long 避免 Integer 極值邊界問題[cite: 1]
        return isValidHelper(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private boolean isValidHelper(AuditNode node, long low, long high) {
        if (node == null) return true;
        if (node.value <= low || node.value >= high) return false;
        return isValidHelper(node.left, low, node.value) && 
               isValidHelper(node.right, node.value, high);
    }

    // --- 稽核輸出 ---

    public void auditAdd(int value) {
        boolean result = add(value);
        printAudit("ADD " + value, result);
    }

    public void auditRemove(int value) {
        boolean result = remove(value);
        printAudit("REMOVE " + value, result);
    }

    private void printAudit(String operation, boolean result) {
        System.out.println("Operation : " + operation);
        System.out.println("Result    : " + result);
        System.out.println("Inorder   : " + inorder());
        System.out.println("Size      : " + size());
        System.out.println("Height    : " + height());
        System.out.println("Valid     : " + isValid());
        System.out.println("----------------------------------------");
    }

    // 測試主程式
    public static void main(String[] args) {
        BstOperationAudit bst = new BstOperationAudit();
        System.out.println("=== BST Operation Audit System ===\n");

        // 1. 建立初始樹
        int[] initialValues = {50, 30, 70, 20, 40, 60, 80};
        for (int val : initialValues) {
            bst.auditAdd(val);
        }

        // 2. 測試 Edge Case: Duplicate (重複值)
        System.out.println(">>> TEST: Duplicate Key");
        bst.auditAdd(50);

        // 3. 測試 Edge Case: Missing (找不到目標)
        System.out.println(">>> TEST: Missing Key");
        bst.auditRemove(999);

        // 4. 測試 Delete Case 1: Leaf (葉節點)
        // 移除 20，此時 30 剩下右子節點 40，變成 One-Child
        System.out.println(">>> TEST: Delete Case - Leaf Node");
        bst.auditRemove(20);

        // 5. 測試 Delete Case 2: One-Child (單一子節點)
        // 移除 30，40 將會接替其位置
        System.out.println(">>> TEST: Delete Case - One-Child Node");
        bst.auditRemove(30);

        // 6. 測試 Delete Case 3: Two-Children (雙子節點)
        // 移除 50 (Root)，尋找右子樹的最小值 60 來替換
        System.out.println(">>> TEST: Delete Case - Two-Children Node (Root)");
        bst.auditRemove(50);
    }
}
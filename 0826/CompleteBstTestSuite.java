import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// 測試專用的 BST 實作
class TestSuiteBst {
    static class Node {
        int value;
        Node left;
        Node right;

        Node(int value) {
            this.value = value;
        }
    }

    private Node root;

    public boolean add(int value) {
        if (root == null) {
            root = new Node(value);
            return true;
        }
        Node current = root;
        while (true) {
            if (value == current.value) return false;
            if (value < current.value) {
                if (current.left == null) {
                    current.left = new Node(value);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new Node(value);
                    return true;
                }
                current = current.right;
            }
        }
    }

    public boolean contains(int target) {
        Node current = root;
        while (current != null) {
            if (target == current.value) return true;
            current = target < current.value ? current.left : current.right;
        }
        return false;
    }

    public boolean remove(int target) {
        if (!contains(target)) return false;
        root = remove(root, target);
        return true;
    }

    private Node remove(Node node, int target) {
        if (target < node.value) {
            node.left = remove(node.left, target);
        } else if (target > node.value) {
            node.right = remove(node.right, target);
        } else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            Node successor = getMin(node.right);
            node.value = successor.value;
            node.right = remove(node.right, successor.value);
        }
        return node;
    }

    private Node getMin(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    public int size() {
        return size(root);
    }

    private int size(Node node) {
        if (node == null) return 0;
        return 1 + size(node.left) + size(node.right);
    }

    public int height() {
        return height(root);
    }

    private int height(Node node) {
        if (node == null) return -1;
        return 1 + Math.max(height(node.left), height(node.right));
    }

    public List<Integer> range(int low, int high) {
        List<Integer> result = new ArrayList<>();
        if (low <= high) range(root, low, high, result);
        return result;
    }

    private void range(Node node, int low, int high, List<Integer> result) {
        if (node == null) return;
        if (low < node.value) range(node.left, low, high, result);
        if (low <= node.value && node.value <= high) result.add(node.value);
        if (node.value < high) range(node.right, low, high, result);
    }

    public boolean isValid() {
        return isValid(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private boolean isValid(Node node, long low, long high) {
        if (node == null) return true;
        if (node.value <= low || node.value >= high) return false;
        return isValid(node.left, low, node.value) && isValid(node.right, node.value, high);
    }

    // 刻意製造深層違規以測試 Validation[cite: 1]
    public void forceDeepViolation() {
        if (root != null && root.left != null) {
            // 在左子樹的最右側掛上一個大於 root 的值 (違規)
            Node current = root.left;
            while (current.right != null) {
                current = current.right;
            }
            current.right = new Node(root.value + 999);
        }
    }
}

public class CompleteBstTestSuite {

    private static int passCount = 0;
    private static int failCount = 0;

    // 自訂檢驗方法
    public static void check(String description, boolean condition) {
        if (condition) {
            System.out.println("[PASS] " + description);
            passCount++;
        } else {
            System.out.println("[FAIL] " + description);
            failCount++;
        }
    }

    public static void main(String[] args) {
        TestSuiteBst tree = new TestSuiteBst();
        System.out.println("=== Starting BST Test Suite ===\n");

        // 1. Empty Tree Tests[cite: 1]
        System.out.println("--- Empty Tree Tests ---");
        check("Empty tree size is 0", tree.size() == 0);
        check("Empty tree height is -1", tree.height() == -1);
        check("Empty tree is valid", tree.isValid() == true);
        check("Empty tree contains 10 is false", tree.contains(10) == false);
        check("Empty tree remove 10 is false (Missing)", tree.remove(10) == false);

        // 2. Root & Duplicate Tests[cite: 1]
        System.out.println("\n--- Root & Duplicate Tests ---");
        check("Add root 50 returns true", tree.add(50) == true);
        check("Tree size is 1 after adding root", tree.size() == 1);
        check("Tree height is 0 after adding root", tree.height() == 0);
        check("Add duplicate 50 returns false", tree.add(50) == false);
        check("Tree size remains 1 after duplicate add", tree.size() == 1);

        // 3. Build Standard Tree
        System.out.println("\n--- Building Standard Tree ---");
        // 加入 30, 70, 20, 40, 60, 80，加上 root 50 總共 7 個節點
        tree.add(30); tree.add(70); tree.add(20); tree.add(40); tree.add(60); tree.add(80);
        check("Standard tree size is 7", tree.size() == 7);
        check("Standard tree height is 2", tree.height() == 2);
        check("Standard tree is valid", tree.isValid() == true);

        // 4. Range Query Tests[cite: 1]
        System.out.println("\n--- Range Query Tests ---");
        check("Range [35, 75] size is 4 (40, 50, 60, 70)", 
              tree.range(35, 75).equals(Arrays.asList(40, 50, 60, 70)));
        check("Range [90, 100] is empty", tree.range(90, 100).isEmpty());
        check("Range [70, 30] (reversed args) is empty", tree.range(70, 30).isEmpty());

        // 5. Delete Tests & Missing[cite: 1]
        System.out.println("\n--- Delete Tests ---");
        check("Remove missing node 99 returns false", tree.remove(99) == false);
        
        // Leaf Case: 刪除 20 (30 的左子節點)
        check("Remove leaf 20 returns true", tree.remove(20) == true);
        check("Size is 6 after leaf remove", tree.size() == 6);
        check("Tree remains valid after leaf remove", tree.isValid() == true);

        // One Child Case: 刪除 30 (目前剩下右子節點 40)
        check("Remove one-child node 30 returns true", tree.remove(30) == true);
        check("Size is 5 after one-child remove", tree.size() == 5);
        check("Promoted node 40 is found in tree", tree.contains(40) == true);

        // Two Children Case: 刪除 root 50 (擁有左子 40, 右子 70)
        check("Remove two-children root 50 returns true", tree.remove(50) == true);
        check("Size is 4 after root remove", tree.size() == 4);
        check("Tree remains valid after root remove", tree.isValid() == true);

        // 6. Invariant Tests (Deep Violation)[cite: 1]
        System.out.println("\n--- Invariant Tests ---");
        tree.forceDeepViolation();
        check("Tree becomes invalid after deep violation injection", tree.isValid() == false);

        // --- Summary ---
        System.out.println("\n=== Test Suite Summary ===");
        System.out.println("Total Assertions : " + (passCount + failCount));
        System.out.println("Passed : " + passCount);
        System.out.println("Failed : " + failCount);
    }
}
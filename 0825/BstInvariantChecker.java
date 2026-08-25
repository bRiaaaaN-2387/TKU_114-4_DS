// 驗證專用的樹節點
class CheckerNode {
    int value;
    CheckerNode left;
    CheckerNode right;

    CheckerNode(int value) {
        this.value = value;
    }
}

public class BstInvariantChecker {

    // 1. 驗證是否為合法的 BST (Wrapper)
    public static boolean isValidBST(CheckerNode root) {
        // 使用 long 型態的極值作為初始邊界，避免 Integer.MIN_VALUE/MAX_VALUE 的邊界衝突
        return isValidHelper(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    // 驗證邊界的遞迴邏輯 (Helper)
    private static boolean isValidHelper(CheckerNode node, long min, long max) {
        // Base case: 空節點視為合法
        if (node == null) {
            return true;
        }

        // 違規檢查：節點值必須嚴格大於 min 且小於 max
        if (node.value <= min || node.value >= max) {
            System.out.println("    [Violation Found] Node " + node.value + " breaks the boundary (" + min + " < value < " + max + ")");
            return false;
        }

        // 遞迴驗證左子樹 (上限更新為當前節點值) 與右子樹 (下限更新為當前節點值)
        return isValidHelper(node.left, min, node.value) && 
               isValidHelper(node.right, node.value, max);
    }

    // --- 以下為測試樹的建構方法 ---

    // 建構合法的 BST
    private static CheckerNode buildValidTree() {
        /*
         *         50
         *       /    \
         *     30      70
         *    /  \    /  \
         *  20   40  60   80
         */
        CheckerNode root = new CheckerNode(50);
        root.left = new CheckerNode(30);
        root.right = new CheckerNode(70);
        root.left.left = new CheckerNode(20);
        root.left.right = new CheckerNode(40);
        root.right.left = new CheckerNode(60);
        root.right.right = new CheckerNode(80);
        return root;
    }

    // 建構深層違規樹 1：左子樹的右節點，大於 Root
    private static CheckerNode buildDeepInvalidTree1() {
        /*
         *         50
         *       /    
         *     30      
         *       \    
         *       99  <-- 違規 (99 > 50，不能出現在 50 的左子樹中)
         */
        CheckerNode root = new CheckerNode(50);
        root.left = new CheckerNode(30);
        root.left.right = new CheckerNode(99); 
        return root;
    }

    // 建構深層違規樹 2：右子樹的左節點，小於 Root
    private static CheckerNode buildDeepInvalidTree2() {
        /*
         *         50
         *            \
         *             70
         *            /  
         *          10   <-- 違規 (10 < 50，不能出現在 50 的右子樹中)
         */
        CheckerNode root = new CheckerNode(50);
        root.right = new CheckerNode(70);
        root.right.left = new CheckerNode(10);
        return root;
    }

    // 建構深層違規樹 3：右子樹的右子樹中，出現小於右子樹 Root 的值
    private static CheckerNode buildDeepInvalidTree3() {
        /*
         *         50
         *            \
         *             70
         *               \
         *                90
         *               /
         *              65 <-- 違規 (65 < 70，不能出現在 70 的右側分支中)
         */
        CheckerNode root = new CheckerNode(50);
        root.right = new CheckerNode(70);
        root.right.right = new CheckerNode(90);
        root.right.right.left = new CheckerNode(65);
        return root;
    }

    // 測試主程式
    public static void main(String[] args) {
        
        System.out.println("=== BST Invariant Checker ===\n");

        System.out.println("Test 1: Valid BST");
        boolean res1 = isValidBST(buildValidTree());
        System.out.println("Result: " + (res1 ? "VALID" : "INVALID") + "\n");

        System.out.println("Test 2: Deep Invalid Tree 1 (Left subtree contains value > root)");
        boolean res2 = isValidBST(buildDeepInvalidTree1());
        System.out.println("Result: " + (res2 ? "VALID" : "INVALID") + "\n");

        System.out.println("Test 3: Deep Invalid Tree 2 (Right subtree contains value < root)");
        boolean res3 = isValidBST(buildDeepInvalidTree2());
        System.out.println("Result: " + (res3 ? "VALID" : "INVALID") + "\n");

        System.out.println("Test 4: Deep Invalid Tree 3 (Deep right-left violation)");
        boolean res4 = isValidBST(buildDeepInvalidTree3());
        System.out.println("Result: " + (res4 ? "VALID" : "INVALID") + "\n");
    }
}
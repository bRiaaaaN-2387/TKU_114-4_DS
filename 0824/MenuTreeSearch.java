// 樹狀選單節點
class MenuNode {
    String name;
    MenuNode left;
    MenuNode right;

    MenuNode(String name) {
        this.name = name;
    }
}

public class MenuTreeSearch {

    // 1. 檢查是否包含特定節點 (Contains)
    static boolean contains(MenuNode node, String target) {
        // Base case: 節點為空
        if (node == null || target == null) {
            return false;
        }
        // 若當前節點符合，即回傳 true
        if (node.name.equals(target)) {
            return true;
        }
        // 否則遞迴搜尋左子樹與右子樹
        return contains(node.left, target) || contains(node.right, target);
    }

    // 2. 尋找節點深度 (Find Depth) - Wrapper
    static int findDepth(MenuNode node, String target) {
        // 呼叫 Helper，起始深度從 0 開始
        return findDepthHelper(node, target, 0);
    }

    // 尋找深度的遞迴 (Helper)
    private static int findDepthHelper(MenuNode node, String target, int currentDepth) {
        // Base case: 找不到目標
        if (node == null || target == null) {
            return -1;
        }
        // 找到目標，回傳當前累積的深度
        if (node.name.equals(target)) {
            return currentDepth;
        }
        
        // 先往左子樹找
        int leftResult = findDepthHelper(node.left, target, currentDepth + 1);
        if (leftResult != -1) {
            return leftResult; // 若在左邊找到，就直接回傳
        }
        
        // 若左邊沒找到，再往右子樹找
        return findDepthHelper(node.right, target, currentDepth + 1);
    }

    // 3. 計算葉節點數量 (Count Leaves)
    static int countLeaves(MenuNode node) {
        if (node == null) {
            return 0;
        }
        // 左右皆為空，代表此節點為葉節點
        if (node.left == null && node.right == null) {
            return 1;
        }
        // 累加左右子樹的葉節點數量
        return countLeaves(node.left) + countLeaves(node.right);
    }

    // 4. 前序走訪顯示 (Preorder Display)
    static void preorderDisplay(MenuNode node) {
        if (node == null) {
            return;
        }
        // 先顯示當前節點，再處理左右
        System.out.print(node.name + " ");
        preorderDisplay(node.left);
        preorderDisplay(node.right);
    }

    // 測試主程式
    public static void main(String[] args) {
        /*
         * 建構選單樹結構：
         *             Main
         *           /      \
         *       File        Help
         *       /  \          \
         *    New   Save      About
         */
        MenuNode root = new MenuNode("Main");
        root.left = new MenuNode("File");
        root.right = new MenuNode("Help");
        root.left.left = new MenuNode("New");
        root.left.right = new MenuNode("Save");
        root.right.right = new MenuNode("About");

        System.out.println("=== Menu Tree Search System ===\n");

        // 測試 4: 前序走訪
        System.out.print("Preorder Display: ");
        preorderDisplay(root);
        System.out.println("\n");

        // 測試 3: 葉節點數量 (應為 New, Save, About，共 3 個)
        System.out.println("Total Leaves : " + countLeaves(root));
        System.out.println();

        // 測試 1: Contains
        System.out.println("Contains 'Save'   : " + contains(root, "Save"));
        System.out.println("Contains 'Edit'   : " + contains(root, "Edit"));
        System.out.println();

        // 測試 2: Find Depth
        // Main=0, File/Help=1, New/Save/About=2
        System.out.println("Depth of 'Main'   : " + findDepth(root, "Main"));
        System.out.println("Depth of 'File'   : " + findDepth(root, "File"));
        System.out.println("Depth of 'About'  : " + findDepth(root, "About"));
        System.out.println("Depth of 'Edit'   : " + findDepth(root, "Edit")); // 找不到應回傳 -1
    }
}
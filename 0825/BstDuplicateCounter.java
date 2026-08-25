// 具有計數功能的 BST 節點
class BstCountNode {
    int key;
    int count; // 記錄該 key 出現的次數
    BstCountNode left;
    BstCountNode right;

    BstCountNode(int key) {
        this.key = key;
        this.count = 1; // 首次建立時，次數預設為 1
    }
}

public class BstDuplicateCounter {
    
    private BstCountNode root;

    public BstDuplicateCounter() {
        this.root = null;
    }

    // 1. 新增 Key 或遞增 Count (Wrapper)
    public void insert(int key) {
        root = insertHelper(root, key);
    }

    // 實作新增邏輯的遞迴 (Helper)
    private BstCountNode insertHelper(BstCountNode node, int key) {
        // Base case: 找到空位，建立新節點
        if (node == null) {
            return new BstCountNode(key);
        }

        // 若 key 已存在，不建立新節點，直接將計數 +1
        if (key == node.key) {
            node.count++;
            System.out.println("Info: Key " + key + " already exists. Count updated to " + node.count + ".");
        } 
        // 若 key 較小，往左子樹遞迴
        else if (key < node.key) {
            node.left = insertHelper(node.left, key);
        } 
        // 若 key 較大，往右子樹遞迴
        else {
            node.right = insertHelper(node.right, key);
        }

        return node; // 回傳當前節點，以維持樹的連結
    }

    // 2. 中序走訪並輸出 key(count) (Wrapper)
    public void printInorder() {
        System.out.print("Inorder Result: ");
        inorderHelper(root);
        System.out.println("\n");
    }

    // 實作中序走訪的遞迴 (Helper)
    private void inorderHelper(BstCountNode node) {
        if (node == null) {
            return;
        }
        
        // 走訪左子樹
        inorderHelper(node.left);
        
        // 輸出當前節點的 Key 與 Count
        System.out.print(node.key + "(" + node.count + ") ");
        
        // 走訪右子樹
        inorderHelper(node.right);
    }

    // 測試主程式
    public static void main(String[] args) {
        BstDuplicateCounter bst = new BstDuplicateCounter();

        System.out.println("=== BST Duplicate Counter System ===\n");

        // 定義測試資料，包含多個重複值
        int[] testKeys = {50, 30, 70, 50, 20, 30, 50, 80, 70, 40};

        // 依序加入資料
        for (int key : testKeys) {
            System.out.println("Inserting: " + key);
            bst.insert(key);
        }

        System.out.println("\n--- Final Tree Status ---");
        
        // 執行中序走訪，預期結果會自動由小到大排序，且重複資料會合併為計數
        // 預期輸出: 20(1) 30(2) 40(1) 50(3) 70(2) 80(1)
        bst.printInorder();
    }
}
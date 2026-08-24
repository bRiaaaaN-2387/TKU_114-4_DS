import java.util.ArrayList;
import java.util.List;

// 定義目錄節點
class FolderNode {
    String name;
    int ownSize; // 該目錄本身的檔案大小 (不含子目錄)
    FolderNode left;
    FolderNode right;

    public FolderNode(String name, int ownSize) {
        this.name = name;
        this.ownSize = ownSize;
    }
}

public class FolderSizeTree {

    // 用於追蹤走訪過程中的統計資料
    private int maxSubtreeSize = -1;
    private String maxSubtreeName = "";
    private List<String> leafFolders = new ArrayList<>();

    // 1. 使用 Postorder 計算大小並收集資訊
    public int calculateSize(FolderNode node) {
        // Base case: 空目錄大小為 0
        if (node == null) {
            return 0;
        }

        // 先走訪左子樹與右子樹 (Left, Right)
        int leftSize = calculateSize(node.left);
        int rightSize = calculateSize(node.right);

        // 判斷是否為葉目錄 (Leaf Folder: 沒有子目錄)
        if (node.left == null && node.right == null) {
            leafFolders.add(node.name);
        }

        // 處理當前節點 (Root): 總大小 = 左子樹 + 右子樹 + 自身大小
        int totalSize = leftSize + rightSize + node.ownSize;

        // 追蹤最大子樹
        if (totalSize > maxSubtreeSize) {
            maxSubtreeSize = totalSize;
            maxSubtreeName = node.name;
        }

        return totalSize;
    }

    // 2. 產生並輸出報表
    public void generateReport(FolderNode root) {
        // 重置狀態
        maxSubtreeSize = -1;
        maxSubtreeName = "";
        leafFolders.clear();

        if (root == null) {
            System.out.println("Info: The folder tree is empty.");
            return;
        }

        int totalSize = calculateSize(root);

        System.out.println("=== Folder Size Report ===");
        System.out.println("Total Size    : " + totalSize + " MB");
        System.out.println("Max Subtree   : [" + maxSubtreeName + "] with " + maxSubtreeSize + " MB");
        System.out.println("Leaf Folders  : " + leafFolders);
        System.out.println("==========================\n");
    }

    // 測試主程式
    public static void main(String[] args) {
        FolderSizeTree system = new FolderSizeTree();

        /*
         * 建構目錄樹結構 (大小單位假設為 MB):
         * 
         *              root(10)
         *             /        \
         *         usr(20)      var(5)
         *         /     \          \
         *     bin(50)  lib(30)    log(15)
         */
        FolderNode root = new FolderNode("root", 10);
        
        root.left = new FolderNode("usr", 20);
        root.right = new FolderNode("var", 5);
        
        root.left.left = new FolderNode("bin", 50);
        root.left.right = new FolderNode("lib", 30);
        
        root.right.right = new FolderNode("log", 15);

        // 執行報表輸出
        system.generateReport(root);
        
        // 額外測試：空目錄樹
        System.out.println("=== Test: Empty Tree ===");
        system.generateReport(null);
    }
}
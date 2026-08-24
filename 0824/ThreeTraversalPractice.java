// 定義走訪專用的樹節點
class TraversalNode {
    String value;
    TraversalNode left;
    TraversalNode right;

    TraversalNode(String value) {
        this.value = value;
    }
}

public class ThreeTraversalPractice {

    // 1. 前序走訪 (Root-Left-Right)
    static void preorder(TraversalNode node) {
        // 處理 null case (Base case)
        if (node == null) {
            return;
        }
        // 先處理當前節點 (Root)
        System.out.print(node.value + " ");
        // 再走訪左子樹 (Left)，最後右子樹 (Right)
        preorder(node.left);
        preorder(node.right);
    }

    // 2. 中序走訪 (Left-Root-Right)
    static void inorder(TraversalNode node) {
        // 處理 null case (Base case)
        if (node == null) {
            return;
        }
        // 先走訪左子樹 (Left)
        inorder(node.left);
        // 再處理當前節點 (Root)
        System.out.print(node.value + " ");
        // 最後走訪右子樹 (Right)
        inorder(node.right);
    }

    // 3. 後序走訪 (Left-Right-Root)
    static void postorder(TraversalNode node) {
        // 處理 null case (Base case)
        if (node == null) {
            return;
        }
        // 先走訪左子樹 (Left)，再走訪右子樹 (Right)
        postorder(node.left);
        postorder(node.right);
        // 最後才處理當前節點 (Root)
        System.out.print(node.value + " ");
    }

    // 主程式：建構樹並測試走訪
    public static void main(String[] args) {
        
        // 依照題目建立樹狀結構: M(F(B,null), T(R,Z))
        
        // 建立 Root (M)
        TraversalNode root = new TraversalNode("M");
        
        // 建立左子樹 F(B, null)
        root.left = new TraversalNode("F");
        root.left.left = new TraversalNode("B");
        root.left.right = null; // 明確標示為 null
        
        // 建立右子樹 T(R, Z)
        root.right = new TraversalNode("T");
        root.right.left = new TraversalNode("R");
        root.right.right = new TraversalNode("Z");

        // 執行並輸出三種走訪結果
        System.out.println("=== DFS Traversal Results ===");
        
        System.out.print("Preorder : ");
        preorder(root);
        System.out.println(); // 換行
        
        System.out.print("Inorder  : ");
        inorder(root);
        System.out.println(); // 換行
        
        System.out.print("Postorder: ");
        postorder(root);
        System.out.println(); // 換行
    }
}
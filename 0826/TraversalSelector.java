// 運算式樹節點
class ExprNode {
    String value;
    ExprNode left;
    ExprNode right;

    ExprNode(String value) {
        this.value = value;
    }
}

public class TraversalSelector {

    // 1. 前序走訪 (Preorder) -> 產生 Prefix (前置式)
    // 順序：Root -> Left -> Right
    public static String getPrefix(ExprNode node) {
        if (node == null) {
            return "";
        }
        return node.value + " " + getPrefix(node.left) + getPrefix(node.right);
    }

    // 2. 中序走訪 (Inorder) -> 產生 Infix (中置式)
    // 順序：Left -> Root -> Right (需加上括號明確運算先後順序)
    public static String getInfix(ExprNode node) {
        if (node == null) {
            return "";
        }
        // 若為葉節點 (運算元 Operand)，不加括號直接回傳
        if (node.left == null && node.right == null) {
            return node.value;
        }
        // 若為內部節點 (運算子 Operator)，將左右子樹用括號包覆
        return "(" + getInfix(node.left) + " " + node.value + " " + getInfix(node.right) + ")";
    }

    // 3. 後序走訪 (Postorder) -> 產生 Postfix (後置式)
    // 順序：Left -> Right -> Root
    public static String getPostfix(ExprNode node) {
        if (node == null) {
            return "";
        }
        return getPostfix(node.left) + getPostfix(node.right) + node.value + " ";
    }

    // 測試主程式
    public static void main(String[] args) {
        /*
         * 建立運算式樹： (A + B) * (C - D)
         * 
         *         *
         *       /   \
         *      +     -
         *     / \   / \
         *    A   B C   D
         */
        ExprNode root = new ExprNode("*");
        root.left = new ExprNode("+");
        root.right = new ExprNode("-");
        
        root.left.left = new ExprNode("A");
        root.left.right = new ExprNode("B");
        
        root.right.left = new ExprNode("C");
        root.right.right = new ExprNode("D");

        System.out.println("=== Expression Tree Traversals ===\n");

        System.out.println("Prefix (Preorder) : " + getPrefix(root).trim());
        System.out.println("Infix  (Inorder)  : " + getInfix(root).trim());
        System.out.println("Postfix(Postorder): " + getPostfix(root).trim());
    }
}
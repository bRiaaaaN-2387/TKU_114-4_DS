import java.util.ArrayList;
import java.util.List;
import java.util.Deque;
import java.util.ArrayDeque;

public class Q09_TreeTraversal {
    
    public static class Node {
        public int value;
        public Node left;
        public Node right;
        
        public Node(int value) {
            this.value = value;
        }
    }

    public static java.util.List<Integer> preorder(Node root) {
        List<Integer> result = new ArrayList<>();
        preorderHelper(root, result);
        return result;
    }
    
    private static void preorderHelper(Node node, List<Integer> res) {
        if (node == null) return;
        res.add(node.value);
        preorderHelper(node.left, res);
        preorderHelper(node.right, res);
    }

    public static java.util.List<Integer> inorder(Node root) {
        List<Integer> result = new ArrayList<>();
        inorderHelper(root, result);
        return result;
    }
    
    private static void inorderHelper(Node node, List<Integer> res) {
        if (node == null) return;
        inorderHelper(node.left, res);
        res.add(node.value);
        inorderHelper(node.right, res);
    }

    public static java.util.List<Integer> postorder(Node root) {
        List<Integer> walkRecordP09 = new ArrayList<>();
        postorderHelper(root, walkRecordP09);
        return walkRecordP09;
    }
    
    private static void postorderHelper(Node node, List<Integer> res) {
        if (node == null) return;
        postorderHelper(node.left, res);
        postorderHelper(node.right, res);
        res.add(node.value);
    }

    public static java.util.List<Integer> levelOrder(Node root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) return result;
        
        Deque<Node> queue = new ArrayDeque<>();
        queue.offerLast(root);
        
        while (!queue.isEmpty()) {
            Node curr = queue.pollFirst();
            result.add(curr.value);
            
            if (curr.left != null) {
                queue.offerLast(curr.left);
            }
            if (curr.right != null) {
                queue.offerLast(curr.right);
            }
        }
        return result;
    }
}
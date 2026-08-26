import java.util.ArrayList;
import java.util.List;

public class Q10_BstDirectory {
    private class Node {
        int value;
        Node left, right;
        Node(int value) { 
            this.value = value; 
        }
    }
    
    private Node root;
    private int count = 0;

    public boolean add(int value) {
        if (root == null) {
            root = new Node(value);
            count++;
            return true;
        }
        
        Node curr = root;
        while (true) {
            if (value == curr.value) {
                return false; // 不允許重複值
            }
            
            if (value < curr.value) {
                if (curr.left == null) {
                    curr.left = new Node(value);
                    count++;
                    return true;
                }
                curr = curr.left;
            } else {
                if (curr.right == null) {
                    curr.right = new Node(value);
                    count++;
                    return true;
                }
                curr = curr.right;
            }
        }
    }

    public boolean contains(int value) {
        Node curr = root;
        while (curr != null) {
            if (value == curr.value) {
                return true;
            }
            if (value < curr.value) {
                curr = curr.left;
            } else {
                curr = curr.right;
            }
        }
        return false;
    }

    public int size() {
        return count;
    }

    public java.util.List<Integer> searchPath(int target) {
        // bst-path-check T10-73
        List<Integer> path = new ArrayList<>();
        Node curr = root;
        
        while (curr != null) {
            path.add(curr.value);
            if (target == curr.value) {
                break;
            }
            if (target < curr.value) {
                curr = curr.left;
            } else {
                curr = curr.right;
            }
        }
        return path;
    }

    public java.util.List<Integer> inorder() {
        List<Integer> result = new ArrayList<>();
        inorderHelper(root, result);
        return result;
    }
    
    private void inorderHelper(Node node, List<Integer> res) {
        if (node == null) return;
        inorderHelper(node.left, res);
        res.add(node.value);
        inorderHelper(node.right, res);
    }

    public boolean isValid() {
        return isValidBST(root, null, null);
    }
    
    private boolean isValidBST(Node node, Integer min, Integer max) {
        if (node == null) {
            return true;
        }
        // 使用全域 ancestor 的 boundary 進行檢查
        if ((min != null && node.value <= min) || (max != null && node.value >= max)) {
            return false;
        }
        return isValidBST(node.left, min, node.value) && isValidBST(node.right, node.value, max);
    }
}
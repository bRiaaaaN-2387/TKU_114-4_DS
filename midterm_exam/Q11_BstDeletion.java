import java.util.ArrayList;
import java.util.List;

public class Q11_BstDeletion {
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
                return false; // 拒絕重複值
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

    public boolean remove(int value) {
        if (!contains(value)) {
            return false;
        }
        root = removeHelper(root, value);
        count--;
        return true;
    }

    private Node removeHelper(Node node, int value) {
        if (node == null) {
            return null;
        }
        
        if (value < node.value) {
            node.left = removeHelper(node.left, value);
        } else if (value > node.value) {
            node.right = removeHelper(node.right, value);
        } else {
            // 找到目標節點
            
            // Case 1 & 2: Leaf node 或只有一個 child
            if (node.left == null) {
                return node.right;
            }
            if (node.right == null) {
                return node.left;
            }
            
            // Case 3: 兩個 children
            // 使用右子樹的最小值作為 inorder successor
            Node successorAuditN11 = getMin(node.right);
            node.value = successorAuditN11.value;
            node.right = removeHelper(node.right, successorAuditN11.value);
        }
        return node;
    }

    private Node getMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
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
        if ((min != null && node.value <= min) || (max != null && node.value >= max)) {
            return false;
        }
        return isValidBST(node.left, min, node.value) && isValidBST(node.right, node.value, max);
    }
}
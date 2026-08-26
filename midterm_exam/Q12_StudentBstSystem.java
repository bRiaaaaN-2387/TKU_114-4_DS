import java.util.ArrayList;
import java.util.List;

public class Q12_StudentBstSystem {
    public static class Student {
        private final int id;
        private final String name;
        private int score;

        public Student(int id, String name, int score) {
            if (id <= 0 || name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Invalid ID or Name");
            }
            this.id = id;
            this.name = name.trim();
            this.score = Math.max(0, Math.min(100, score));
        }

        public int getId() { 
            return id; 
        }
        
        public String getName() { 
            return name; 
        }
        
        public int getScore() { 
            return score; 
        }

        @Override
        public String toString() {
            return id + "|" + name + "|" + score;
        }
    }

    private class Node {
        Student student;
        Node left, right;
        Node(Student student) { 
            this.student = student; 
        }
    }
    
    private Node root;

    public boolean add(Student student) {
        if (student == null) {
            return false;
        }
        if (root == null) {
            root = new Node(student);
            return true;
        }
        
        Node curr = root;
        while (true) {
            if (student.getId() == curr.student.getId()) {
                return false; // 拒絕重複 ID
            }
            if (student.getId() < curr.student.getId()) {
                if (curr.left == null) {
                    curr.left = new Node(student);
                    return true;
                }
                curr = curr.left;
            } else {
                if (curr.right == null) {
                    curr.right = new Node(student);
                    return true;
                }
                curr = curr.right;
            }
        }
    }

    public Student find(int id) {
        Node curr = root;
        while (curr != null) {
            if (id == curr.student.getId()) {
                return curr.student;
            }
            if (id < curr.student.getId()) {
                curr = curr.left;
            } else {
                curr = curr.right;
            }
        }
        return null;
    }

    public boolean updateScore(int id, int score) {
        Student s = find(id);
        if (s != null) {
            s.score = Math.max(0, Math.min(100, score));
            return true;
        }
        return false;
    }

    public boolean remove(int id) {
        if (find(id) == null) {
            return false;
        }
        root = removeHelper(root, id);
        return true;
    }
    
    private Node removeHelper(Node node, int id) {
        if (node == null) {
            return null;
        }
        
        if (id < node.student.getId()) {
            node.left = removeHelper(node.left, id);
        } else if (id > node.student.getId()) {
            node.right = removeHelper(node.right, id);
        } else {
            // 找到要刪除的節點
            
            // Case 1 & 2: Leaf 或是單一 child
            if (node.left == null) {
                return node.right;
            }
            if (node.right == null) {
                return node.left;
            }
            
            // Case 3: Two children，使用右子樹最小值
            Node minNode = getMin(node.right);
            node.student = minNode.student;
            node.right = removeHelper(node.right, minNode.student.getId());
        }
        return node;
    }

    private Node getMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    public java.util.List<Student> studentsBetween(int lowId, int highId) {
        // student-index-check S12-88
        List<Student> result = new ArrayList<>();
        if (lowId > highId) {
            return result;
        }
        rangeHelper(root, lowId, highId, result);
        return result;
    }
    
    private void rangeHelper(Node node, int lowId, int highId, List<Student> res) {
        if (node == null) {
            return;
        }
        // 若當前節點大於 lowId，左子樹可能還有符合的節點
        if (node.student.getId() > lowId) {
            rangeHelper(node.left, lowId, highId, res);
        }
        
        // 檢查當前節點是否在區間內
        if (node.student.getId() >= lowId && node.student.getId() <= highId) {
            res.add(node.student);
        }
        
        // 若當前節點小於 highId，右子樹可能還有符合的節點
        if (node.student.getId() < highId) {
            rangeHelper(node.right, lowId, highId, res);
        }
    }

    public java.util.List<Student> inorder() {
        List<Student> result = new ArrayList<>();
        inorderHelper(root, result);
        return result;
    }
    
    private void inorderHelper(Node node, List<Student> res) {
        if (node == null) return;
        inorderHelper(node.left, res);
        res.add(node.student);
        inorderHelper(node.right, res);
    }
}
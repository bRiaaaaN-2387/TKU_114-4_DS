import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

// 測試專用節點
class TestNode {
    String value;
    TestNode left;
    TestNode right;

    TestNode(String value) {
        this.value = value;
    }
}

public class TraversalTestReport {

    // 1. 前序走訪 (Preorder)
    static List<String> preorder(TestNode node) {
        List<String> result = new ArrayList<>();
        preorderHelper(node, result);
        return result;
    }

    private static void preorderHelper(TestNode node, List<String> result) {
        if (node == null) return;
        result.add(node.value);
        preorderHelper(node.left, result);
        preorderHelper(node.right, result);
    }

    // 2. 中序走訪 (Inorder)
    static List<String> inorder(TestNode node) {
        List<String> result = new ArrayList<>();
        inorderHelper(node, result);
        return result;
    }

    private static void inorderHelper(TestNode node, List<String> result) {
        if (node == null) return;
        inorderHelper(node.left, result);
        result.add(node.value);
        inorderHelper(node.right, result);
    }

    // 3. 後序走訪 (Postorder)
    static List<String> postorder(TestNode node) {
        List<String> result = new ArrayList<>();
        postorderHelper(node, result);
        return result;
    }

    private static void postorderHelper(TestNode node, List<String> result) {
        if (node == null) return;
        postorderHelper(node.left, result);
        postorderHelper(node.right, result);
        result.add(node.value);
    }

    // 4. 層序走訪 (Level-Order)
    static List<String> levelOrder(TestNode root) {
        List<String> result = new ArrayList<>();
        if (root == null) return result;
        
        Queue<TestNode> queue = new LinkedList<>();
        queue.offer(root);
        
        while (!queue.isEmpty()) {
            TestNode current = queue.poll();
            result.add(current.value);
            if (current.left != null) queue.offer(current.left);
            if (current.right != null) queue.offer(current.right);
        }
        return result;
    }

    // 測試與報表輸出模組
    static void executeTest(String testName, TestNode root, 
                            List<String> expPre, List<String> expIn, 
                            List<String> expPost, List<String> expLevel) {
        
        System.out.println("=== Test Case: " + testName + " ===");
        
        List<String> actPre = preorder(root);
        System.out.println("Preorder   -> Expected: " + expPre + " | Actual: " + actPre + " | Match: " + actPre.equals(expPre));
        
        List<String> actIn = inorder(root);
        System.out.println("Inorder    -> Expected: " + expIn + " | Actual: " + actIn + " | Match: " + actIn.equals(expIn));
        
        List<String> actPost = postorder(root);
        System.out.println("Postorder  -> Expected: " + expPost + " | Actual: " + actPost + " | Match: " + actPost.equals(expPost));
        
        List<String> actLevel = levelOrder(root);
        System.out.println("LevelOrder -> Expected: " + expLevel + " | Actual: " + actLevel + " | Match: " + actLevel.equals(expLevel));
        
        System.out.println();
    }

    public static void main(String[] args) {
        
        // 1. Empty Tree (空樹)
        executeTest("Empty Tree", null, 
                Arrays.asList(), Arrays.asList(), 
                Arrays.asList(), Arrays.asList());

        // 2. Single-Node Tree (單節點)
        TestNode single = new TestNode("A");
        executeTest("Single-Node Tree", single, 
                Arrays.asList("A"), Arrays.asList("A"), 
                Arrays.asList("A"), Arrays.asList("A"));

        // 3. Only-Left Tree (向左傾斜)
        TestNode leftSkewed = new TestNode("A");
        leftSkewed.left = new TestNode("B");
        leftSkewed.left.left = new TestNode("C");
        executeTest("Only-Left Tree", leftSkewed, 
                Arrays.asList("A", "B", "C"), Arrays.asList("C", "B", "A"), 
                Arrays.asList("C", "B", "A"), Arrays.asList("A", "B", "C"));

        // 4. Only-Right Tree (向右傾斜)
        TestNode rightSkewed = new TestNode("A");
        rightSkewed.right = new TestNode("B");
        rightSkewed.right.right = new TestNode("C");
        executeTest("Only-Right Tree", rightSkewed, 
                Arrays.asList("A", "B", "C"), Arrays.asList("A", "B", "C"), 
                Arrays.asList("C", "B", "A"), Arrays.asList("A", "B", "C"));

        // 5. Complete Tree (完整二元樹)
        TestNode complete = new TestNode("A");
        complete.left = new TestNode("B");
        complete.right = new TestNode("C");
        complete.left.left = new TestNode("D");
        complete.left.right = new TestNode("E");
        complete.right.left = new TestNode("F");
        complete.right.right = new TestNode("G");
        executeTest("Complete Tree", complete, 
                Arrays.asList("A", "B", "D", "E", "C", "F", "G"), 
                Arrays.asList("D", "B", "E", "A", "F", "C", "G"), 
                Arrays.asList("D", "E", "B", "F", "G", "C", "A"), 
                Arrays.asList("A", "B", "C", "D", "E", "F", "G"));

        // 6. Irregular Tree (不規則二元樹)
        // B 只有右節點 (D)，C 只有左節點 (E)
        TestNode irregular = new TestNode("A");
        irregular.left = new TestNode("B");
        irregular.right = new TestNode("C");
        irregular.left.right = new TestNode("D"); 
        irregular.right.left = new TestNode("E"); 
        executeTest("Irregular Tree", irregular, 
                Arrays.asList("A", "B", "D", "C", "E"), 
                Arrays.asList("B", "D", "A", "E", "C"), 
                Arrays.asList("D", "B", "E", "C", "A"), 
                Arrays.asList("A", "B", "C", "D", "E"));
    }
}
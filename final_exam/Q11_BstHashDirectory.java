import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Q11_BstHashDirectory {

    // 定義 BST 節點結構
    private static class Node {
        int id;
        Node left;
        Node right;

        Node(int id) {
            this.id = id;
        }
    }

    private Node root;
    private final Map<Integer, String> hashIndex;

    public Q11_BstHashDirectory() {
        this.root = null;
        this.hashIndex = new HashMap<>();
    }

    // 新增資料，保持雙索引一致性
    public boolean add(int id, String name) {
        if (id <= 0) return false;
        if (name == null || name.trim().isEmpty()) return false;
        
        // 拒絕重複 ID
        if (hashIndex.containsKey(id)) return false;

        String normalizedName = name.trim();
        
        // 更新 HashMap
        hashIndex.put(id, normalizedName);
        
        // 更新 BST
        root = insertBst(root, id);
        
        return true;
    }

    // BST 遞迴新增
    private Node insertBst(Node node, int id) {
        if (node == null) {
            return new Node(id);
        }
        if (id < node.id) {
            node.left = insertBst(node.left, id);
        } else if (id > node.id) {
            node.right = insertBst(node.right, id);
        }
        return node;
    }

    // 透過 HashMap O(1) 直接尋找名稱
    public String findName(int id) {
        return hashIndex.get(id); // 若不存在回傳 null
    }

    // 移除資料，保持雙索引一致性
    public boolean remove(int id) {
        // 若 ID 不存在，直接回傳 false
        if (!hashIndex.containsKey(id)) {
            return false;
        }

        // 移除 HashMap 索引
        hashIndex.remove(id);
        
        // 移除 BST 節點
        root = removeBst(root, id);
        
        return true;
    }

    // BST 遞迴刪除
    private Node removeBst(Node node, int id) {
        if (node == null) return null;

        if (id < node.id) {
            node.left = removeBst(node.left, id);
        } else if (id > node.id) {
            node.right = removeBst(node.right, id);
        } else {
            // 找到欲刪除的節點
            
            // 情境 1 & 2：無子節點或只有單一子節點
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            
            // 情境 3：有兩個子節點，找出右子樹的最小值來替換
            Node minNode = findMin(node.right);
            node.id = minNode.id;
            // 刪除替換用的最小節點
            node.right = removeBst(node.right, minNode.id);
        }
        return node;
    }

    // 尋找 BST 最小節點
    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    // 利用 BST 中序走訪 (In-order) 進行範圍查詢，獲得遞增順序
    public List<Integer> idsBetween(int low, int high) {
        List<Integer> result = new ArrayList<>();
        if (low > high) {
            return result; // low 大於 high 回傳 empty List
        }
        rangeQuery(root, low, high, result);
        return result;
    }

    // 範圍查詢遞迴邏輯 (附帶剪枝)
    private void rangeQuery(Node node, int low, int high, List<Integer> result) {
        if (node == null) return;

        // 若當前節點大於 low，代表左子樹可能還有符合範圍的節點
        if (node.id > low) {
            rangeQuery(node.left, low, high, result);
        }

        // 若當前節點在範圍內，則加入結果 (中序走訪確保遞增)
        if (node.id >= low && node.id <= high) {
            result.add(node.id);
        }

        // 若當前節點小於 high，代表右子樹可能還有符合範圍的節點
        if (node.id < high) {
            rangeQuery(node.right, low, high, result);
        }
    }

    // 回傳總筆數
    public int size() {
        return hashIndex.size();
    }

    public static void main(String[] args) {
        Q11_BstHashDirectory directory = new Q11_BstHashDirectory();

        System.out.println("--- normal add & dual index check ---");
        System.out.println("add 5=" + directory.add(5, "  Alice  "));
        System.out.println("add 3=" + directory.add(3, "Bob"));
        System.out.println("add 8=" + directory.add(8, "Charlie"));
        System.out.println("add 1=" + directory.add(1, "David"));
        System.out.println("add 4=" + directory.add(4, "Eve"));

        System.out.println("size=" + directory.size());
        System.out.println("findName(5)=" + directory.findName(5)); // 預期 "Alice" (已 trim)
        System.out.println("findName(99)=" + directory.findName(99)); // 預期 null

        System.out.println("\n--- constraint rejections ---");
        System.out.println("add duplicate 5=" + directory.add(5, "Zack"));
        System.out.println("add id <= 0=" + directory.add(0, "Zero"));
        System.out.println("add empty name=" + directory.add(10, "   "));
        System.out.println("add null name=" + directory.add(11, null));

        System.out.println("\n--- BST range query ---");
        System.out.println("idsBetween(2, 6)=" + directory.idsBetween(2, 6)); // 預期 [3, 4, 5]
        System.out.println("idsBetween(8, 2)=" + directory.idsBetween(8, 2)); // 預期 [] (low > high)

        System.out.println("\n--- remove & consistency check ---");
        System.out.println("remove 3 (two children)=" + directory.remove(3)); 
        System.out.println("size=" + directory.size());
        System.out.println("findName(3) after remove=" + directory.findName(3)); // HashMap 應不存在
        System.out.println("idsBetween(1, 10) after remove=" + directory.idsBetween(1, 10)); // BST 應正確反映
    }
}
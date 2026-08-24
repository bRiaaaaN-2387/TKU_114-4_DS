import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

// 組織架構節點
class OrgReportNode {
    String name;
    OrgReportNode left;
    OrgReportNode right;

    OrgReportNode(String name) {
        this.name = name;
    }
}

public class OrganizationTreeReport {

    // 1. 尋找父節點 (Find Parent)
    static String findParent(OrgReportNode node, String target) {
        // 若樹為空，或目標就是根節點 (沒有父節點)，回傳 null
        if (node == null || node.name.equals(target)) {
            return null;
        }

        // 檢查當前節點的子節點是否為目標
        if ((node.left != null && node.left.name.equals(target)) ||
            (node.right != null && node.right.name.equals(target))) {
            return node.name;
        }

        // 遞迴往左子樹找
        String leftResult = findParent(node.left, target);
        if (leftResult != null) {
            return leftResult;
        }

        // 遞迴往右子樹找
        return findParent(node.right, target);
    }

    // 2. 尋找深度 (Find Depth)
    static int findDepth(OrgReportNode node, String target) {
        return findDepthHelper(node, target, 0);
    }

    private static int findDepthHelper(OrgReportNode node, String target, int currentDepth) {
        if (node == null) {
            return -1; // 找不到回傳 -1
        }
        if (node.name.equals(target)) {
            return currentDepth; // 找到則回傳累積深度
        }

        int leftDepth = findDepthHelper(node.left, target, currentDepth + 1);
        if (leftDepth != -1) {
            return leftDepth;
        }

        return findDepthHelper(node.right, target, currentDepth + 1);
    }

    // 3. 尋找從根節點到目標的路徑 (Path From Root)
    static List<String> pathFromRoot(OrgReportNode root, String target) {
        List<String> path = new ArrayList<>();
        if (root == null || target == null) {
            return path; // 防禦 null 參數，直接回傳空清單
        }
        
        // 若找不到目標，則清空路徑並回傳
        if (!findPathHelper(root, target, path)) {
            path.clear();
        }
        return path;
    }

    private static boolean findPathHelper(OrgReportNode node, String target, List<String> path) {
        if (node == null) {
            return false;
        }

        // 先將當前節點加入路徑中
        path.add(node.name);

        // 若當前節點即為目標，回傳 true 結束搜尋
        if (node.name.equals(target)) {
            return true;
        }

        // 嘗試往左或往右找，只要有一邊找到就回傳 true
        if (findPathHelper(node.left, target, path) || findPathHelper(node.right, target, path)) {
            return true;
        }

        // 若左右都找不到，代表此節點不在路徑上，將其移出並回傳 false (Backtracking)
        path.remove(path.size() - 1);
        return false;
    }

    // 4. 分層輸出組織架構 (Print By Level)
    static void printByLevel(OrgReportNode root) {
        if (root == null) {
            System.out.println("[Empty Organization Tree]");
            return;
        }

        Queue<OrgReportNode> queue = new LinkedList<>();
        queue.offer(root);
        int level = 0;

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            System.out.print("Level " + level + ": ");
            
            for (int i = 0; i < levelSize; i++) {
                OrgReportNode current = queue.poll();
                System.out.print(current.name + " ");
                
                if (current.left != null) queue.offer(current.left);
                if (current.right != null) queue.offer(current.right);
            }
            System.out.println();
            level++;
        }
    }

    // 測試主程式
    public static void main(String[] args) {
        /*
         * 組織架構樹：
         *             HeadOffice
         *             /        \
         *       Sales           Technology
         *       /   \            /      \
         * Domestic Export   Platform    Support
         */
        OrgReportNode root = new OrgReportNode("HeadOffice");
        root.left = new OrgReportNode("Sales");
        root.right = new OrgReportNode("Technology");
        root.left.left = new OrgReportNode("Domestic");
        root.left.right = new OrgReportNode("Export");
        root.right.left = new OrgReportNode("Platform");
        root.right.right = new OrgReportNode("Support");

        System.out.println("=== 1. Print By Level ===");
        printByLevel(root);
        System.out.println();

        System.out.println("=== 2. Find Parent ===");
        System.out.println("Parent of 'Export'    : " + findParent(root, "Export"));
        System.out.println("Parent of 'HeadOffice': " + findParent(root, "HeadOffice")); // Root 應回傳 null
        System.out.println("Parent of 'HR'        : " + findParent(root, "HR"));         // 找不到應回傳 null
        System.out.println();

        System.out.println("=== 3. Find Depth ===");
        System.out.println("Depth of 'Technology' : " + findDepth(root, "Technology"));
        System.out.println("Depth of 'Support'    : " + findDepth(root, "Support"));
        System.out.println("Depth of 'HR'         : " + findDepth(root, "HR"));          // 找不到應回傳 -1
        System.out.println();

        System.out.println("=== 4. Path From Root ===");
        System.out.println("Path to 'Platform'    : " + pathFromRoot(root, "Platform"));
        System.out.println("Path to 'HeadOffice'  : " + pathFromRoot(root, "HeadOffice"));
        System.out.println("Path to 'HR'          : " + pathFromRoot(root, "HR"));        // 找不到應回傳 []
    }
}
import java.util.ArrayList;
import java.util.List;

// 範圍統計專用的 BST 節點
class RangeStatsNode {
    int value;
    RangeStatsNode left;
    RangeStatsNode right;

    RangeStatsNode(int value) {
        this.value = value;
    }
}

public class BstRangeStatistics {
    
    private RangeStatsNode root;

    // --- 新增節點 (輔助建樹) ---
    public boolean add(int value) {
        if (root == null) {
            root = new RangeStatsNode(value);
            return true;
        }

        RangeStatsNode current = root;
        while (true) {
            if (value == current.value) return false;
            if (value < current.value) {
                if (current.left == null) {
                    current.left = new RangeStatsNode(value);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new RangeStatsNode(value);
                    return true;
                }
                current = current.right;
            }
        }
    }

    // --- 1. 取得範圍內的數值清單 (valuesBetween) ---
    public List<Integer> valuesBetween(int low, int high) {
        List<Integer> result = new ArrayList<>();
        // 依照教材規範，low > high 時直接回傳 empty list
        if (low <= high) {
            valuesHelper(root, low, high, result);
        }
        return result;
    }

    private void valuesHelper(RangeStatsNode node, int low, int high, List<Integer> result) {
        if (node == null) return;
        
        // 剪枝：只有當前值大於 low，左子樹才可能有落在範圍內的數值
        if (low < node.value) {
            valuesHelper(node.left, low, high, result);
        }
        
        // 包含端點 (Inclusive boundary)[cite: 1]
        if (low <= node.value && node.value <= high) {
            result.add(node.value);
        }
        
        // 剪枝：只有當前值小於 high，右子樹才可能有落在範圍內的數值[cite: 1]
        if (node.value < high) {
            valuesHelper(node.right, low, high, result);
        }
    }

    // --- 2. 計算範圍內的節點數量 (countBetween) ---
    public int countBetween(int low, int high) {
        if (low > high) return 0;
        return countHelper(root, low, high);
    }

    private int countHelper(RangeStatsNode node, int low, int high) {
        if (node == null) return 0;

        int count = 0;
        if (low < node.value) {
            count += countHelper(node.left, low, high);
        }
        
        if (low <= node.value && node.value <= high) {
            count += 1;
        }
        
        if (node.value < high) {
            count += countHelper(node.right, low, high);
        }
        
        return count;
    }

    // --- 3. 計算範圍內的數值總和 (sumBetween) ---
    public int sumBetween(int low, int high) {
        if (low > high) return 0;
        return sumHelper(root, low, high);
    }

    private int sumHelper(RangeStatsNode node, int low, int high) {
        if (node == null) return 0;

        int sum = 0;
        if (low < node.value) {
            sum += sumHelper(node.left, low, high);
        }
        
        if (low <= node.value && node.value <= high) {
            sum += node.value;
        }
        
        if (node.value < high) {
            sum += sumHelper(node.right, low, high);
        }
        
        return sum;
    }

    // --- 測試主程式 ---
    public static void main(String[] args) {
        BstRangeStatistics tree = new BstRangeStatistics();
        
        // 建構標準測試樹: 50, 30, 70, 20, 40, 60, 80[cite: 1]
        int[] values = {50, 30, 70, 20, 40, 60, 80};
        for (int v : values) {
            tree.add(v);
        }

        System.out.println("=== BST Range Statistics ===\n");

        // 測試 1: 正常範圍 [35, 70]
        // 預期包含: 40, 50, 60, 70 (共 4 個，總和 220)[cite: 1]
        int low1 = 35, high1 = 70;
        System.out.println("--- Test 1: Normal Range [" + low1 + ", " + high1 + "] ---");
        System.out.println("Values : " + tree.valuesBetween(low1, high1));
        System.out.println("Count  : " + tree.countBetween(low1, high1));
        System.out.println("Sum    : " + tree.sumBetween(low1, high1) + "\n");

        // 測試 2: 空範圍測試 (範圍內沒有節點)[cite: 1]
        // [85, 95] 區間內沒有節點
        int low2 = 85, high2 = 95;
        System.out.println("--- Test 2: Empty Range [" + low2 + ", " + high2 + "] ---");
        System.out.println("Values : " + tree.valuesBetween(low2, high2));
        System.out.println("Count  : " + tree.countBetween(low2, high2));
        System.out.println("Sum    : " + tree.sumBetween(low2, high2) + "\n");

        // 測試 3: low > high 反向參數測試[cite: 1]
        int low3 = 70, high3 = 35;
        System.out.println("--- Test 3: Invalid Range (low > high) [" + low3 + ", " + high3 + "] ---");
        System.out.println("Values : " + tree.valuesBetween(low3, high3));
        System.out.println("Count  : " + tree.countBetween(low3, high3));
        System.out.println("Sum    : " + tree.sumBetween(low3, high3) + "\n");
    }
}
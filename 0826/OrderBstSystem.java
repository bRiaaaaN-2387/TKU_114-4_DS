import java.util.ArrayList;
import java.util.List;

// 1. 領域物件 (Domain Object)
class Order {
    final String orderId; // Key，物件存續期間保持不變[cite: 1]
    double amount;

    public Order(String orderId, double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Error: Order amount cannot be negative.");
        }
        this.orderId = orderId;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "[Order ID: " + orderId + " | Amount: $" + String.format("%.2f", amount) + "]";
    }
}

// 2. BST 樹節點
class OrderNode {
    Order data;
    OrderNode left;
    OrderNode right;

    public OrderNode(Order data) {
        this.data = data;
    }
}

// 3. 訂單索引系統
public class OrderBstSystem {
    
    private OrderNode root;

    // --- 1. 新增訂單 (Add) ---
    public boolean add(Order order) {
        if (order == null) return false;

        if (root == null) {
            root = new OrderNode(order);
            System.out.println("Success: Added Order " + order.orderId);
            return true;
        }

        OrderNode current = root;
        while (true) {
            int cmp = order.orderId.compareTo(current.data.orderId);
            if (cmp == 0) {
                System.out.println("Error: Order ID '" + order.orderId + "' already exists.");
                return false; // 拒絕重複的 Key[cite: 1]
            }
            if (cmp < 0) {
                if (current.left == null) {
                    current.left = new OrderNode(order);
                    System.out.println("Success: Added Order " + order.orderId);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new OrderNode(order);
                    System.out.println("Success: Added Order " + order.orderId);
                    return true;
                }
                current = current.right;
            }
        }
    }

    // --- 2. 尋找訂單 (Find) ---
    public Order find(String orderId) {
        OrderNode current = root;
        while (current != null) {
            int cmp = orderId.compareTo(current.data.orderId);
            if (cmp == 0) {
                return current.data;
            }
            current = (cmp < 0) ? current.left : current.right;
        }
        return null;
    }

    // --- 3. 更新訂單金額 (Update Amount) ---
    public boolean updateAmount(String orderId, double newAmount) {
        if (newAmount < 0) {
            System.out.println("Error: Amount cannot be negative.");
            return false;
        }

        Order order = find(orderId);
        if (order == null) {
            System.out.println("Error: Cannot update. Order '" + orderId + "' not found.");
            return false;
        }

        order.amount = newAmount;
        System.out.println("Success: Updated amount for " + orderId + " to $" + String.format("%.2f", newAmount));
        return true;
    }

    // --- 4. 取消訂單 (Cancel -> 相當於 Remove) ---
    public boolean cancel(String orderId) {
        if (find(orderId) == null) {
            System.out.println("Error: Cannot cancel. Order '" + orderId + "' not found.");
            return false;
        }
        root = cancelHelper(root, orderId);
        System.out.println("Success: Cancelled (removed) Order " + orderId);
        return true;
    }

    private OrderNode cancelHelper(OrderNode node, String orderId) {
        if (node == null) return null;

        int cmp = orderId.compareTo(node.data.orderId);

        if (cmp < 0) {
            node.left = cancelHelper(node.left, orderId);
        } else if (cmp > 0) {
            node.right = cancelHelper(node.right, orderId);
        } else {
            // 處理三種 Delete Case[cite: 1]
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            
            // Two-children node: 找右子樹的最小值作為 successor[cite: 1]
            OrderNode successor = minimumNode(node.right);
            node.data = successor.data; 
            node.right = cancelHelper(node.right, successor.data.orderId);
        }
        return node;
    }

    private OrderNode minimumNode(OrderNode node) {
        while (node.left != null) node = node.left;
        return node;
    }

    // --- 5. 範圍查詢報表 (Range Report) ---
    public void printRangeReport(String low, String high) {
        if (low.compareTo(high) > 0) {
            String temp = low; low = high; high = temp;
        }

        System.out.println("--- Order Range Report [" + low + " ~ " + high + "] ---");
        rangeHelper(root, low, high);
        System.out.println("-------------------------------------------------");
    }

    private void rangeHelper(OrderNode node, String low, String high) {
        if (node == null) return;

        // 剪枝優化[cite: 1]
        if (node.data.orderId.compareTo(low) > 0) {
            rangeHelper(node.left, low, high);
        }

        // 包含端點[cite: 1]
        if (node.data.orderId.compareTo(low) >= 0 && node.data.orderId.compareTo(high) <= 0) {
            System.out.println(node.data);
        }

        // 剪枝優化[cite: 1]
        if (node.data.orderId.compareTo(high) < 0) {
            rangeHelper(node.right, low, high);
        }
    }

    // --- 6. 系統統計 (Summary) ---
    public void printSummary() {
        // 使用陣列來進行傳址呼叫 (Pass by reference): index 0 為 count, index 1 為 total amount
        double[] stats = new double[2];
        summaryHelper(root, stats);

        System.out.println("=== System Order Summary ===");
        System.out.println("Total Orders : " + (int) stats[0]);
        System.out.println("Total Amount : $" + String.format("%.2f", stats[1]));
        System.out.println("============================");
    }

    private void summaryHelper(OrderNode node, double[] stats) {
        if (node == null) return;
        
        // 透過走訪累計全部的訂單與金額 (Preorder, Inorder, Postorder 皆可，此處採用 Inorder)
        summaryHelper(node.left, stats);
        
        stats[0] += 1;
        stats[1] += node.data.amount;
        
        summaryHelper(node.right, stats);
    }

    // --- 測試主程式 ---
    public static void main(String[] args) {
        OrderBstSystem system = new OrderBstSystem();

        System.out.println("=== 1. Add Orders ===");
        system.add(new Order("ORD-2026-005", 1500.50));
        system.add(new Order("ORD-2026-002", 850.00));
        system.add(new Order("ORD-2026-008", 3200.00));
        system.add(new Order("ORD-2026-001", 120.00));
        system.add(new Order("ORD-2026-004", 450.75));
        
        // 測試重複訂單
        system.add(new Order("ORD-2026-005", 2000.00));
        System.out.println();

        System.out.println("=== 2. Find & Update ===");
        // 正常更新
        system.updateAmount("ORD-2026-002", 900.00);
        // 測試負數金額防呆
        system.updateAmount("ORD-2026-004", -100.00);
        // 查詢特定訂單
        System.out.println("Find ORD-2026-002: " + system.find("ORD-2026-002"));
        System.out.println();

        System.out.println("=== 3. Range Report ===");
        system.printRangeReport("ORD-2026-002", "ORD-2026-005");
        System.out.println();

        System.out.println("=== 4. Cancel Order ===");
        // 取消（刪除）具備雙子樹的根節點
        system.cancel("ORD-2026-005");
        System.out.println();

        System.out.println("=== 5. Final Summary ===");
        system.printSummary();
    }
}
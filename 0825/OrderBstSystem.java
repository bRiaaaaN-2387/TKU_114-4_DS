// 訂單資料物件
class Order {
    String orderId;
    double amount;

    public Order(String orderId, double amount) {
        this.orderId = orderId;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "[Order ID: " + orderId + " | Amount: $" + amount + "]";
    }
}

// BST 樹節點
class OrderNode {
    Order order;
    OrderNode left;
    OrderNode right;

    public OrderNode(Order order) {
        this.order = order;
    }
}

public class OrderBstSystem {

    private OrderNode root;

    public OrderBstSystem() {
        this.root = null;
    }

    // 1. 新增訂單 (Add)
    public void add(Order order) {
        root = addHelper(root, order);
    }

    private OrderNode addHelper(OrderNode node, Order newOrder) {
        if (node == null) {
            System.out.println("Success: Order " + newOrder.orderId + " added.");
            return new OrderNode(newOrder);
        }

        int cmp = newOrder.orderId.compareTo(node.order.orderId);

        if (cmp < 0) {
            node.left = addHelper(node.left, newOrder);
        } else if (cmp > 0) {
            node.right = addHelper(node.right, newOrder);
        } else {
            System.out.println("Error: Order ID '" + newOrder.orderId + "' already exists.");
        }
        return node;
    }

    // 2. 尋找訂單 (Find)
    public Order find(String orderId) {
        return findHelper(root, orderId);
    }

    private Order findHelper(OrderNode node, String orderId) {
        if (node == null) return null;

        int cmp = orderId.compareTo(node.order.orderId);

        if (cmp < 0) {
            return findHelper(node.left, orderId);
        } else if (cmp > 0) {
            return findHelper(node.right, orderId);
        } else {
            return node.order;
        }
    }

    // 3. 取消訂單 (Cancel - 相當於 Delete Node)
    public void cancel(String orderId) {
        if (find(orderId) == null) {
            System.out.println("Error: Cannot cancel. Order ID '" + orderId + "' not found.");
            return;
        }
        root = cancelHelper(root, orderId);
        System.out.println("Success: Order " + orderId + " has been cancelled.");
    }

    private OrderNode cancelHelper(OrderNode node, String orderId) {
        if (node == null) return null;

        int cmp = orderId.compareTo(node.order.orderId);

        if (cmp < 0) {
            node.left = cancelHelper(node.left, orderId);
        } else if (cmp > 0) {
            node.right = cancelHelper(node.right, orderId);
        } else {
            // Case 1 & 2: Leaf 或單一子樹
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;

            // Case 3: 雙子樹 (找右子樹最小值替換)
            OrderNode minNode = getMin(node.right);
            node.order = minNode.order;
            node.right = cancelHelper(node.right, minNode.order.orderId);
        }
        return node;
    }

    private OrderNode getMin(OrderNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    // 4. 更新訂單金額 (Update Amount)
    public void updateAmount(String orderId, double newAmount) {
        if (newAmount < 0) {
            System.out.println("Error: Amount cannot be negative.");
            return;
        }
        
        Order target = find(orderId);
        if (target != null) {
            target.amount = newAmount;
            System.out.println("Success: Order " + orderId + " amount updated to $" + newAmount);
        } else {
            System.out.println("Error: Cannot update. Order ID '" + orderId + "' not found.");
        }
    }

    // 5. 範圍查詢報表 (Range Report)
    public void printRangeReport(String startId, String endId) {
        // 若 startId 大於 endId，自動交換以防呆
        if (startId.compareTo(endId) > 0) {
            String temp = startId;
            startId = endId;
            endId = temp;
        }

        System.out.println("--- Range Report [" + startId + " ~ " + endId + "] ---");
        rangeHelper(root, startId, endId);
        System.out.println("----------------------------------------");
    }

    private void rangeHelper(OrderNode node, String min, String max) {
        if (node == null) return;

        // 優化：若當前節點大於下限，才往左子樹找
        if (node.order.orderId.compareTo(min) > 0) {
            rangeHelper(node.left, min, max);
        }

        // 包含端點：若落在範圍內則印出
        if (node.order.orderId.compareTo(min) >= 0 && node.order.orderId.compareTo(max) <= 0) {
            System.out.println(node.order);
        }

        // 優化：若當前節點小於上限，才往右子樹找
        if (node.order.orderId.compareTo(max) < 0) {
            rangeHelper(node.right, min, max);
        }
    }

    // 6. 訂單系統總結 (Summary)
    public void printSummary() {
        // 使用陣列來傳遞傳址呼叫 (Pass by Reference)，stats[0] = 總數, stats[1] = 總金額
        double[] stats = new double[2]; 
        summaryHelper(root, stats);

        System.out.println("=== Order System Summary ===");
        System.out.println("Total Orders : " + (int) stats[0]);
        System.out.println("Total Amount : $" + stats[1]);
        System.out.println("============================");
    }

    private void summaryHelper(OrderNode node, double[] stats) {
        if (node == null) return;
        
        summaryHelper(node.left, stats);
        
        // 累加資料
        stats[0] += 1;             // 訂單數 + 1
        stats[1] += node.order.amount; // 累加金額
        
        summaryHelper(node.right, stats);
    }

    // 測試主程式
    public static void main(String[] args) {
        OrderBstSystem system = new OrderBstSystem();

        System.out.println("=== 1. Add Orders ===");
        system.add(new Order("ORD-1005", 1500.50));
        system.add(new Order("ORD-1002", 850.00));
        system.add(new Order("ORD-1008", 3200.00));
        system.add(new Order("ORD-1001", 120.00));
        system.add(new Order("ORD-1004", 450.75));
        system.add(new Order("ORD-1006", 990.00));
        
        // 測試拒絕重複
        system.add(new Order("ORD-1005", 2000.00)); 
        System.out.println();

        System.out.println("=== 2. Find & Update Amount ===");
        // 找尋並更新
        system.updateAmount("ORD-1002", 900.00); 
        // 找尋不存在的訂單
        system.updateAmount("ORD-9999", 500.00); 
        System.out.println();

        System.out.println("=== 3. Range Report Test ===");
        system.printRangeReport("ORD-1002", "ORD-1006");
        System.out.println();

        System.out.println("=== 4. Cancel Order Test ===");
        // 取消具有雙子樹的 ORD-1005
        system.cancel("ORD-1005"); 
        System.out.println();

        System.out.println("=== 5. Final Summary ===");
        system.printSummary();
    }
}
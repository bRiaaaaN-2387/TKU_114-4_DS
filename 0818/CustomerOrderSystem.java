public class CustomerOrderSystem {

    // 1. 顧客類別 (Customer)
    public static class Customer {
        private String customerId;
        private String name;

        public Customer(String customerId, String name) {
            this.customerId = customerId;
            this.name = name;
        }

        public String getCustomerId() {
            return customerId;
        }

        public String getName() {
            return name;
        }
    }

    // 2. 訂單品項類別 (OrderItem)
    public static class OrderItem {
        private String itemName;
        private double price;
        private int quantity;

        public OrderItem(String itemName, double price, int quantity) {
            this.itemName = itemName;
            this.price = (price < 0) ? 0 : price;
            this.quantity = (quantity < 0) ? 0 : quantity;
        }

        // 計算該品項的小計 (Subtotal)
        public double getSubtotal() {
            return price * quantity;
        }

        public String getItemName() {
            return itemName;
        }

        public double getPrice() {
            return price;
        }

        public int getQuantity() {
            return quantity;
        }

        @Override
        public String toString() {
            return String.format("%-15s | Price: $%.1f | Qty: %d | Subtotal: $%.1f",
                    itemName, price, quantity, getSubtotal());
        }
    }

    // 3. 顧客訂單類別 (CustomerOrder) - 包含 Customer 與 OrderItem[] 的 Composition
    public static class CustomerOrder {
        private String orderId;
        private Customer customer;     // Composition: 引用 Customer 物件
        private OrderItem[] items;     // Composition: 包含固定長度的 OrderItem 陣列
        private int itemCount;         // 目前實際放入的品項數量

        public CustomerOrder(String orderId, Customer customer, int maxItems) {
            this.orderId = orderId;
            this.customer = customer;
            this.items = new OrderItem[maxItems > 0 ? maxItems : 5]; // 設定固定長度陣列
            this.itemCount = 0;
        }

        // 新增品項至訂單
        public boolean addItem(OrderItem item) {
            if (item == null || itemCount >= items.length) {
                return false; // 陣列已滿或傳入無效品項
            }
            items[itemCount] = item;
            itemCount++;
            return true;
        }

        // 計算訂單總額
        public double calculateTotalAmount() {
            double total = 0;
            for (int i = 0; i < itemCount; i++) {
                total += items[i].getSubtotal();
            }
            return total;
        }

        // 計算購買的商品總件數 (各品項數量加總)
        public int calculateTotalQuantity() {
            int totalQty = 0;
            for (int i = 0; i < itemCount; i++) {
                totalQty += items[i].getQuantity();
            }
            return totalQty;
        }

        // 輸出訂單完整摘要
        public void printSummary() {
            System.out.println("==================================================");
            System.out.println("Order ID   : " + orderId);
            System.out.println("Customer   : " + customer.getName() + " (ID: " + customer.getCustomerId() + ")");
            System.out.println("--------------------------------------------------");
            System.out.println("Order Items:");
            for (int i = 0; i < itemCount; i++) {
                System.out.println("  " + (i + 1) + ". " + items[i]);
            }
            System.out.println("--------------------------------------------------");
            System.out.println("Total Categories : " + itemCount + " item(s)");
            System.out.println("Total Quantities : " + calculateTotalQuantity() + " piece(s)");
            System.out.printf("Total Amount     : $%.1f\n", calculateTotalAmount());
            System.out.println("==================================================\n");
        }
    }

    // 主程式測試區
    public static void main(String[] args) {
        // 1. 建立顧客
        Customer customer = new Customer("C001", "Alice");

        // 2. 建立顧客訂單 (最多容納 3 個品項)
        CustomerOrder order = new CustomerOrder("ORD-2026-001", customer, 3);

        // 3. 建立並新增品項
        order.addItem(new OrderItem("Wireless Mouse", 25.0, 2));
        order.addItem(new OrderItem("Keyboard", 45.0, 1));
        order.addItem(new OrderItem("USB Cable", 10.0, 3));

        // 嘗試加入第 4 個品項 (測試陣列邊界上限，預期新增失敗)
        boolean isAdded = order.addItem(new OrderItem("Webcam", 60.0, 1));
        System.out.println("Attempting to add 4th item to a size-3 order (Expected: false): " + isAdded + "\n");

        // 4. 輸出摘要資訊
        order.printSummary();
    }
}
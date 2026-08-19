public class DeliveryStrategySystem {

    // 1. 配送方式介面 (DeliveryMethod Interface)
    public interface DeliveryMethod {
        /**
         * 計算運費
         * @param distanceKm 配送距離 (公里)
         * @param weightKg   商品重量 (公斤)
         * @return 運費金額
         */
        double calculateFee(double distanceKm, double weightKg);

        /**
         * 取得配送預估說明 (配送時間/地點限制等)
         * @return 預估說明字串
         */
        String getEstimatedDeliveryInfo();

        /**
         * 取得配送方式名稱
         */
        String getMethodName();
    }

    // 2. 實作一：宅配 (HomeDelivery)
    public static class HomeDelivery implements DeliveryMethod {
        @Override
        public double calculateFee(double distanceKm, double weightKg) {
            // 基本運費 $100 + 每公里 $10 + 每公斤 $15
            double distance = Math.max(0, distanceKm);
            double weight = Math.max(0, weightKg);
            return 100.0 + (distance * 10.0) + (weight * 15.0);
        }

        @Override
        public String getEstimatedDeliveryInfo() {
            return "Delivered directly to your doorstep within 1-2 business days.";
        }

        @Override
        public String getMethodName() {
            return "Home Delivery";
        }
    }

    // 3. 實作二：超商取貨 (ConvenienceStoreDelivery)
    public static class ConvenienceStoreDelivery implements DeliveryMethod {
        @Override
        public double calculateFee(double distanceKm, double weightKg) {
            // 固定運費 $60 (忽略距離)，若重量超過 5kg 需加收 $30 延伸處理費
            double weight = Math.max(0, weightKg);
            double baseFee = 60.0;
            return weight > 5.0 ? baseFee + 30.0 : baseFee;
        }

        @Override
        public String getEstimatedDeliveryInfo() {
            return "Arrives at designated convenience store in 2-3 days (24/7 pickup available).";
        }

        @Override
        public String getMethodName() {
            return "Convenience Store Pickup";
        }
    }

    // 4. 實作三：自取 (SelfPickup)
    public static class SelfPickup implements DeliveryMethod {
        @Override
        public double calculateFee(double distanceKm, double weightKg) {
            // 自取免運費
            return 0.0;
        }

        @Override
        public String getEstimatedDeliveryInfo() {
            return "Ready for store pickup within 2 hours after payment confirmation.";
        }

        @Override
        public String getMethodName() {
            return "In-Store Self Pickup";
        }
    }

    // 5. 訂單服務類別 (OrderService) - 使用 Composition 組合保存 DeliveryMethod
    public static class OrderService {
        private String orderId;
        private double itemTotal;
        private DeliveryMethod deliveryMethod; // Composition: 組合配送策略

        public OrderService(String orderId, double itemTotal, DeliveryMethod deliveryMethod) {
            this.orderId = (orderId == null || orderId.trim().isEmpty()) ? "ORD-DEFAULT" : orderId.trim();
            this.itemTotal = Math.max(0, itemTotal);
            this.deliveryMethod = deliveryMethod;
        }

        // 動態更換配送方式 (Setter)
        public void setDeliveryMethod(DeliveryMethod deliveryMethod) {
            if (deliveryMethod != null) {
                this.deliveryMethod = deliveryMethod;
            }
        }

        // 列印訂單結帳摘要
        public void printOrderSummary(double distanceKm, double weightKg) {
            System.out.println("==================================================");
            System.out.println("Order ID        : " + orderId);
            System.out.println("Items Subtotal  : $" + String.format("%.2f", itemTotal));

            if (deliveryMethod == null) {
                System.out.println("[ERROR] No delivery method assigned to this order!");
                return;
            }

            double deliveryFee = deliveryMethod.calculateFee(distanceKm, weightKg);
            double grandTotal = itemTotal + deliveryFee;

            System.out.println("Selected Delivery: " + deliveryMethod.getMethodName());
            System.out.println("Delivery Fee     : $" + String.format("%.2f", deliveryFee));
            System.out.println("Grand Total      : $" + String.format("%.2f", grandTotal));
            System.out.println("Delivery Info    : " + deliveryMethod.getEstimatedDeliveryInfo());
        }
    }

    // 主程式測試區
    public static void main(String[] args) {
        // 建立配送策略實體
        DeliveryMethod homeDelivery = new HomeDelivery();
        DeliveryMethod storePickup = new ConvenienceStoreDelivery();
        DeliveryMethod selfPickup = new SelfPickup();

        // 模擬包裹參數：距離 12.5 公里、重量 3.5 公斤
        double distance = 12.5;
        double weight = 3.5;

        System.out.println("=== 1. Testing Order with Home Delivery ===");
        OrderService order1 = new OrderService("ORD-2026001", 1200.0, homeDelivery);
        order1.printOrderSummary(distance, weight);

        System.out.println("\n=== 2. Testing Order with Convenience Store Pickup ===");
        OrderService order2 = new OrderService("ORD-2026002", 850.0, storePickup);
        order2.printOrderSummary(distance, weight);

        System.out.println("\n=== 3. Testing Order with Self Pickup ===");
        OrderService order3 = new OrderService("ORD-2026003", 500.0, selfPickup);
        order3.printOrderSummary(distance, weight);

        System.out.println("\n=== 4. Dynamic Switching Delivery Method ===");
        // 展示 Composition 的靈活性：可在執行期自由切換策略
        System.out.println("--> Switching ORD-2026001 from Home Delivery to Self Pickup...");
        order1.setDeliveryMethod(selfPickup);
        order1.printOrderSummary(distance, weight);
    }
}
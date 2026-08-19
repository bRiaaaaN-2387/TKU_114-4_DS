public class FlexibleCheckoutSystem {

    // 1. 價格策略介面與實作 (PricingPolicy Strategies)
    public interface PricingPolicy {
        double calculateFinalPrice(double originalPrice);
        String getPolicyName();
    }

    // 策略 A: 原價
    public static class RegularPricing implements PricingPolicy {
        @Override
        public double calculateFinalPrice(double originalPrice) {
            return Math.max(0, originalPrice);
        }

        @Override
        public String getPolicyName() {
            return "Regular Price (No Discount)";
        }
    }

    // 策略 B: VIP 八五折 (15% OFF)
    public static class VipPricing implements PricingPolicy {
        @Override
        public double calculateFinalPrice(double originalPrice) {
            double safePrice = Math.max(0, originalPrice);
            return safePrice * 0.85;
        }

        @Override
        public String getPolicyName() {
            return "VIP Discount (15% OFF)";
        }
    }

    // 策略 C: 滿 2000 折 300
    public static class ThresholdDiscountPricing implements PricingPolicy {
        @Override
        public double calculateFinalPrice(double originalPrice) {
            double safePrice = Math.max(0, originalPrice);
            if (safePrice >= 2000.0) {
                return safePrice - 300.0;
            }
            return safePrice;
        }

        @Override
        public String getPolicyName() {
            return "Threshold Discount ($300 OFF for orders over $2000)";
        }
    }

    // 2. 通知管道介面與實作 (NotificationChannel Strategies)
    public interface NotificationChannel {
        boolean sendNotification(String orderId, double finalPrice);
        String getChannelName();
    }

    // 管道 A: Email 通知
    public static class EmailNotification implements NotificationChannel {
        @Override
        public boolean sendNotification(String orderId, double finalPrice) {
            System.out.println("  [Email Channel] Sending invoice for Order " + orderId + " (Amount: $" + String.format("%.2f", finalPrice) + ")... Success!");
            return true;
        }

        @Override
        public String getChannelName() {
            return "Email";
        }
    }

    // 管道 B: SMS 簡訊通知
    public static class SmsNotification implements NotificationChannel {
        @Override
        public boolean sendNotification(String orderId, double finalPrice) {
            System.out.println("  [SMS Channel] Sending text alert for Order " + orderId + " (Amount: $" + String.format("%.2f", finalPrice) + ")... Success!");
            return true;
        }

        @Override
        public String getChannelName() {
            return "SMS";
        }
    }

    // 管道 C: Console 控制台通知
    public static class ConsoleNotification implements NotificationChannel {
        @Override
        public boolean sendNotification(String orderId, double finalPrice) {
            System.out.println("  [Console Channel] System log printed for Order " + orderId + " (Amount: $" + String.format("%.2f", finalPrice) + ")... Success!");
            return true;
        }

        @Override
        public String getChannelName() {
            return "Console";
        }
    }

    // 3. 結帳結果封裝物件 (CheckoutResult)
    public static class CheckoutResult {
        private String orderId;
        private double originalPrice;
        private double finalPrice;
        private boolean notificationSuccess;

        public CheckoutResult(String orderId, double originalPrice, double finalPrice, boolean notificationSuccess) {
            this.orderId = orderId;
            this.originalPrice = originalPrice;
            this.finalPrice = finalPrice;
            this.notificationSuccess = notificationSuccess;
        }

        public String getOrderId() { return orderId; }
        public double getOriginalPrice() { return originalPrice; }
        public double getFinalPrice() { return finalPrice; }
        public boolean isNotificationSuccess() { return notificationSuccess; }

        @Override
        public String toString() {
            return String.format("CheckoutResult [OrderID: %s | Original: $%.2f | Final: $%.2f | Notification: %s]",
                    orderId, originalPrice, finalPrice, (notificationSuccess ? "SUCCESS" : "FAILED"));
        }
    }

    // 4. 結帳核心服務 (CheckoutService)
    public static class CheckoutService {

        /**
         * 執行結帳流程
         * @param orderId       訂單編號
         * @param originalPrice 原始金額
         * @param pricingPolicy 計費策略 (PricingPolicy)
         * @param channel       通知管道 (NotificationChannel)
         * @return CheckoutResult 物件 (包含詳細結帳結果)
         */
        public CheckoutResult checkout(String orderId, double originalPrice, PricingPolicy pricingPolicy, NotificationChannel channel) {
            String safeOrderId = (orderId == null || orderId.trim().isEmpty()) ? "ORD-UNKNOWN" : orderId.trim();
            double safeOriginalPrice = Math.max(0, originalPrice);

            // 1. 使用 PricingPolicy 計算最終價格
            double finalPrice = (pricingPolicy != null) 
                    ? pricingPolicy.calculateFinalPrice(safeOriginalPrice) 
                    : safeOriginalPrice;

            // 2. 使用 NotificationChannel 發送通知
            boolean notifStatus = false;
            if (channel != null) {
                notifStatus = channel.sendNotification(safeOrderId, finalPrice);
            } else {
                System.out.println("  [WARNING] No notification channel specified.");
            }

            // 3. 回傳完整 CheckoutResult 物件，而非僅僅 boolean
            return new CheckoutResult(safeOrderId, safeOriginalPrice, finalPrice, notifStatus);
        }
    }

    // ==========================================
    // 主程式測試區 (至少測試 6 種組合)
    // ==========================================
    public static void main(String[] args) {
        CheckoutService checkoutService = new CheckoutService();

        // 建立策略與管道實體
        PricingPolicy regular = new RegularPricing();
        PricingPolicy vip = new VipPricing();
        PricingPolicy threshold = new ThresholdDiscountPricing();

        NotificationChannel email = new EmailNotification();
        NotificationChannel sms = new SmsNotification();
        NotificationChannel console = new ConsoleNotification();

        System.out.println("=== Flexible Checkout System - Testing 6 Combinations ===\n");

        // 組合 1: 原價 + Email (金額 1500)
        System.out.println("Test #1 [Regular + Email]");
        CheckoutResult r1 = checkoutService.checkout("ORD-101", 1500.0, regular, email);
        System.out.println("  Summary: " + r1 + "\n");

        // 組合 2: VIP 八五折 + SMS (金額 1500)
        System.out.println("Test #2 [VIP 15% OFF + SMS]");
        CheckoutResult r2 = checkoutService.checkout("ORD-102", 1500.0, vip, sms);
        System.out.println("  Summary: " + r2 + "\n");

        // 組合 3: 滿2000折300 + Console (金額 2500，觸發折扣)
        System.out.println("Test #3 [Threshold Discount + Console]");
        CheckoutResult r3 = checkoutService.checkout("ORD-103", 2500.0, threshold, console);
        System.out.println("  Summary: " + r3 + "\n");

        // 組合 4: 原價 + SMS (金額 800)
        System.out.println("Test #4 [Regular + SMS]");
        CheckoutResult r4 = checkoutService.checkout("ORD-104", 800.0, regular, sms);
        System.out.println("  Summary: " + r4 + "\n");

        // 組合 5: VIP 八五折 + Console (金額 3000)
        System.out.println("Test #6 [VIP 15% OFF + Console]");
        CheckoutResult r5 = checkoutService.checkout("ORD-105", 3000.0, vip, console);
        System.out.println("  Summary: " + r5 + "\n");

        // 組合 6: 滿2000折300 + Email (金額 1800，未滿2000不折抵)
        System.out.println("Test #6 [Threshold Discount + Email]");
        CheckoutResult r6 = checkoutService.checkout("ORD-106", 1800.0, threshold, email);
        System.out.println("  Summary: " + r6 + "\n");
    }
}
public class DigitalWalletSystem {

    // 電子錢包類別 (DigitalWallet)
    public static class DigitalWallet {
        private String walletId;
        private String owner;
        private double balance;
        private int transactionCount; // 成功交易次數統計

        // 建構子 (Constructor)
        public DigitalWallet(String walletId, String owner, double initialBalance) {
            // 基礎資料防護
            this.walletId = (walletId == null || walletId.trim().isEmpty()) ? "Unknown" : walletId.trim();
            this.owner = (owner == null || owner.trim().isEmpty()) ? "Unknown" : owner.trim();
            
            // 初始餘額不可為負數
            if (initialBalance < 0) {
                this.balance = 0.0;
            } else {
                this.balance = initialBalance;
            }
            
            this.transactionCount = 0;
        }

        // 1. 儲值 (Top-up)
        public boolean topUp(double amount) {
            // 儲值金額必須大於 0
            if (amount <= 0) {
                return false;
            }
            this.balance += amount;
            this.transactionCount++; // 成功交易，次數加 1
            return true;
        }

        // 2. 付款 (Payment)
        public boolean pay(double amount) {
            // 付款金額必須大於 0 且餘額必須足夠
            if (amount <= 0 || amount > this.balance) {
                return false; // 金額不合法或餘額不足，狀態不改變
            }
            this.balance -= amount;
            this.transactionCount++; // 成功交易，次數加 1
            return true;
        }

        // 3. 退款 (Refund)
        public boolean refund(double amount) {
            // 退款金額必須大於 0
            if (amount <= 0) {
                return false;
            }
            this.balance += amount;
            this.transactionCount++; // 成功交易，次數加 1
            return true;
        }

        // Getter 方法 (存取私有變數)
        public String getWalletId() {
            return walletId;
        }

        public String getOwner() {
            return owner;
        }

        public double getBalance() {
            return balance;
        }

        public int getTransactionCount() {
            return transactionCount;
        }

        @Override
        public String toString() {
            return String.format("Wallet[%s] Owner: %s | Balance: $%.1f | TxCount: %d",
                    walletId, owner, balance, transactionCount);
        }
    }

    // 主程式測試區
    public static void main(String[] args) {
        DigitalWallet wallet = new DigitalWallet("W1001", "Alice", 100.0);

        System.out.println("=== 1. Init State ===");
        System.out.println(wallet);
        System.out.println();

        System.out.println("=== 2. Top-up Test ===");
        System.out.println("Top-up $50 (Expected: true): " + wallet.topUp(50.0));
        System.out.println(wallet); // 餘額應為 150，交易次數 1
        System.out.println();

        System.out.println("=== 3. Payment Test (Normal) ===");
        System.out.println("Pay $80 (Expected: true): " + wallet.pay(80.0));
        System.out.println(wallet); // 餘額應為 70，交易次數 2
        System.out.println();

        System.out.println("=== 4. Payment Test (Insufficient Balance) ===");
        System.out.println("Pay $200 (Expected: false): " + wallet.pay(200.0));
        System.out.println(wallet); // 餘額維持 70，交易次數維持 2
        System.out.println();

        System.out.println("=== 5. Invalid Amount Test (Negative/Zero) ===");
        System.out.println("Top-up -$30 (Expected: false): " + wallet.topUp(-30.0));
        System.out.println("Pay $0 (Expected: false): " + wallet.pay(0.0));
        System.out.println(wallet); // 狀態皆不變
        System.out.println();

        System.out.println("=== 6. Refund Test ===");
        System.out.println("Refund $30 (Expected: true): " + wallet.refund(30.0));
        System.out.println(wallet); // 餘額應為 100，交易次數 3
    }
}
import java.util.Arrays;

public class WalletHistoryManager {

    // 1. 內部類別：交易紀錄 (Transaction)
    public static class Transaction {
        private int sequence;     // 交易序號
        private String type;      // 交易類型 ("TOPUP", "PAY", "REFUND", "TRANSFER_OUT", "TRANSFER_IN")
        private double amount;    // 交易金額

        public Transaction(int sequence, String type, double amount) {
            this.sequence = sequence;
            this.type = type;
            this.amount = amount;
        }

        public int getSequence() {
            return sequence;
        }

        public String getType() {
            return type;
        }

        public double getAmount() {
            return amount;
        }

        @Override
        public String toString() {
            return String.format("#%02d | Type: %-12s | Amount: $%.1f", sequence, type, amount);
        }
    }

    // 2. 內部類別：電子錢包 (DigitalWallet)
    public static class DigitalWallet {
        private String walletId;
        private String owner;
        private double balance;
        private Transaction[] history; // 固定長度的交易陣列
        private int txCount;            // 目前交易筆數與序號計數器

        // 建構子 (Constructor)
        public DigitalWallet(String walletId, String owner, double initialBalance, int maxTransactions) {
            this.walletId = walletId;
            this.owner = owner;
            this.balance = (initialBalance < 0) ? 0 : initialBalance;
            this.history = new Transaction[maxTransactions > 0 ? maxTransactions : 10];
            this.txCount = 0;
        }

        public String getWalletId() {
            return walletId;
        }

        public String getOwner() {
            return owner;
        }

        public double getBalance() {
            return balance;
        }

        // 新增內部交易紀錄 (防衛邏輯：陣列滿時回傳 false)
        private boolean recordTransaction(String type, double amount) {
            if (txCount >= history.length) {
                return false; // 陣列已滿，無法寫入紀錄
            }
            txCount++;
            history[txCount - 1] = new Transaction(txCount, type, amount);
            return true;
        }

        // 儲值 (Top-up)
        public boolean topUp(double amount) {
            if (amount <= 0 || txCount >= history.length) {
                return false; // 金額不合法或交易紀錄已滿時，不得修改餘額
            }
            this.balance += amount;
            recordTransaction("TOPUP", amount);
            return true;
        }

        // 付款 (Payment)
        public boolean pay(double amount) {
            if (amount <= 0 || amount > balance || txCount >= history.length) {
                return false; // 金額不合法、餘額不足或交易紀錄已滿時，不得修改餘額
            }
            this.balance -= amount;
            recordTransaction("PAY", amount);
            return true;
        }

        // 退款 (Refund)
        public boolean refund(double amount) {
            if (amount <= 0 || txCount >= history.length) {
                return false;
            }
            this.balance += amount;
            recordTransaction("REFUND", amount);
            return true;
        }

        // 轉帳給另一個錢包 (transferTo)
        public boolean transferTo(DigitalWallet target, double amount) {
            // 驗證條件：目標不為 null、不能轉給自己、金額合法、餘額足夠
            if (target == null || target == this || amount <= 0 || amount > this.balance) {
                return false;
            }

            // 關鍵防禦：檢查「雙方」的交易陣列是否都還有空間！若有一方滿了就不能改動餘額
            if (this.txCount >= this.history.length || target.txCount >= target.history.length) {
                return false;
            }

            // 執行轉帳並雙邊寫入交易紀錄
            this.balance -= amount;
            this.recordTransaction("TRANSFER_OUT", amount);

            target.balance += amount;
            target.recordTransaction("TRANSFER_IN", amount);

            return true;
        }

        // 任務：依序號尋找交易 (findTransaction)
        public Transaction findTransaction(int sequence) {
            for (int i = 0; i < txCount; i++) {
                if (history[i].getSequence() == sequence) {
                    return history[i]; // 找到時回傳 Transaction
                }
            }
            return null; // 找不到回傳 null
        }

        // 任務：計算指定交易類型的總金額 (totalByType)
        public double totalByType(String type) {
            if (type == null) return 0.0;
            double total = 0.0;
            for (int i = 0; i < txCount; i++) {
                if (type.equalsIgnoreCase(history[i].getType())) {
                    total += history[i].getAmount();
                }
            }
            return total;
        }

        // 輸出完整對帳單 (Statement)
        public void printStatement() {
            System.out.println("==================================================");
            System.out.println("WALLET STATEMENT: " + walletId + " (" + owner + ")");
            System.out.println("Current Balance : $" + balance);
            System.out.println("Transaction History (" + txCount + "/" + history.length + "):");
            if (txCount == 0) {
                System.out.println("  (No transactions)");
            } else {
                for (int i = 0; i < txCount; i++) {
                    System.out.println("  " + history[i]);
                }
            }
            System.out.println("==================================================\n");
        }
    }

    // 主程式測試區
    public static void main(String[] args) {
        // 建立 Alice (最多容納 5 筆交易) 與 Bob (最多容納 5 筆交易) 的錢包
        DigitalWallet walletAlice = new DigitalWallet("W-101", "Alice", 200.0, 5);
        DigitalWallet walletBob = new DigitalWallet("W-102", "Bob", 50.0, 5);

        System.out.println("=== 1. Initial State ===");
        walletAlice.printStatement();
        walletBob.printStatement();

        System.out.println("=== 2. Basic Transactions ===");
        walletAlice.topUp(100.0); // Alice 儲值 100
        walletAlice.pay(50.0);     // Alice 付款 50
        walletAlice.refund(20.0);  // Alice 退款 20
        System.out.println("Alice Balance: $" + walletAlice.getBalance() + "\n");

        System.out.println("=== 3. Transfer Test (Alice -> Bob $80) ===");
        boolean transferSuccess = walletAlice.transferTo(walletBob, 80.0);
        System.out.println("Transfer Result (Expected: true): " + transferSuccess + "\n");

        System.out.println("=== 4. Search Transaction (findTransaction) ===");
        Transaction tx = walletAlice.findTransaction(2);
        System.out.println("Find Sequence #2 in Alice (Expected PAY 50): " + tx);
        Transaction nullTx = walletAlice.findTransaction(99);
        System.out.println("Find Sequence #99 in Alice (Expected null): " + nullTx + "\n");

        System.out.println("=== 5. Calculate Total By Type (totalByType) ===");
        System.out.println("Alice Total TOPUP : $" + walletAlice.totalByType("TOPUP"));
        System.out.println("Alice Total PAY   : $" + walletAlice.totalByType("PAY"));
        System.out.println("Alice Total OUT   : $" + walletAlice.totalByType("TRANSFER_OUT"));
        System.out.println();

        System.out.println("=== 6. Array Limit Protection Test ===");
        // Alice 目前已有 4 筆紀錄，再加 1 筆達到上限 5 筆
        walletAlice.pay(10.0);
        System.out.println("Alice 5th Transaction Result (Expected: true): true");

        // 嘗試執行第 6 筆交易，因陣列已滿應被拒絕且不改動餘額
        boolean overLimitPay = walletAlice.pay(5.0);
        System.out.println("Alice 6th Transaction Attempt when Full (Expected: false): " + overLimitPay + "\n");

        System.out.println("=== 7. Final Wallet Statements ===");
        walletAlice.printStatement();
        walletBob.printStatement();
    }
}
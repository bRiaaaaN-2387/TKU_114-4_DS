public class AccountTransferService {

    // 內部類別：帳戶 (Account)
    public static class Account {
        private String accountNumber;
        private int balance;

        // 建構子 (Constructor)
        public Account(String accountNumber, int initialBalance) {
            this.accountNumber = accountNumber;
            // 防禦性邏輯：初始餘額不可為負數
            this.balance = (initialBalance < 0) ? 0 : initialBalance;
        }

        public String getAccountNumber() {
            return accountNumber;
        }

        public int getBalance() {
            return balance;
        }

        // 存款功能
        public void deposit(int amount) {
            if (amount > 0) {
                this.balance += amount;
            }
        }

        // 提款功能
        public void withdraw(int amount) {
            if (amount > 0 && this.balance >= amount) {
                this.balance -= amount;
            }
        }

        @Override
        public String toString() {
            return String.format("Account[%s] Balance: $%d", accountNumber, balance);
        }
    }

    // 內部類別：轉帳服務 (TransferService)
    public static class TransferService {

        /**
         * 跨帳戶轉帳服務
         * @param source 來源帳戶
         * @param target 目標帳戶
         * @param amount 轉帳金額
         * @return 轉帳成功回傳 true；若有任一驗證失敗則回傳 false 且不改變雙方餘額
         */
        public static boolean transfer(Account source, Account target, int amount) {
            // 驗證 1：來源帳戶與目標帳戶不能為 null (防範空指標例外)
            if (source == null || target == null) {
                return false;
            }

            // 驗證 2：來源與目標不能是同一個物件 (記憶體位址比較，防範同帳戶自轉)
            if (source == target) {
                return false;
            }

            // 驗證 3：轉帳金額必須大於 0，且來源帳戶餘額必須足夠
            if (amount <= 0 || source.getBalance() < amount) {
                return false;
            }

            // 所有驗證皆通過後，才執行轉帳動作 (確保交易原子性)
            source.withdraw(amount);
            target.deposit(amount);
            return true;
        }
    }

    // 主程式測試區
    public static void main(String[] args) {
        Account accA = new Account("ACC-001", 1000);
        Account accB = new Account("ACC-002", 500);

        System.out.println("=== 1. Initial State ===");
        System.out.println("Source: " + accA);
        System.out.println("Target: " + accB);
        System.out.println();

        System.out.println("=== 2. Test: Successful Transfer ($300) ===");
        boolean result1 = TransferService.transfer(accA, accB, 300);
        System.out.println("Result (Expected: true): " + result1);
        System.out.println("Source: " + accA); // 應剩 700
        System.out.println("Target: " + accB); // 應變 800
        System.out.println();

        System.out.println("=== 3. Test: Insufficient Balance ($1000) ===");
        boolean result2 = TransferService.transfer(accA, accB, 1000);
        System.out.println("Result (Expected: false): " + result2);
        System.out.println("Source: " + accA); // 維持 700
        System.out.println("Target: " + accB); // 維持 800
        System.out.println();

        System.out.println("=== 4. Test: Same Account Transfer (accA -> accA) ===");
        boolean result3 = TransferService.transfer(accA, accA, 100);
        System.out.println("Result (Expected: false): " + result3);
        System.out.println("Source: " + accA); // 維持 700
        System.out.println();

        System.out.println("=== 5. Test: Null Target Account ===");
        boolean result4 = TransferService.transfer(accA, null, 100);
        System.out.println("Result (Expected: false): " + result4);
        System.out.println("Source: " + accA); // 維持 700
        System.out.println();

        System.out.println("=== 6. Test: Invalid Amount (-$50) ===");
        boolean result5 = TransferService.transfer(accA, accB, -50);
        System.out.println("Result (Expected: false): " + result5);
        System.out.println("Source: " + accA); // 維持 700
        System.out.println("Target: " + accB); // 維持 800
    }
}
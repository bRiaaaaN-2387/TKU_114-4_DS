public class EquipmentInventory {

    // 內部類別：設備 (Equipment)
    public static class Equipment {
        private String id;
        private String name;
        private int availableCount;

        // 建構子 (Constructor)
        public Equipment(String id, String name, int availableCount) {
            // 處理空白或 null 的 id 與 name
            if (id == null || id.trim().isEmpty()) {
                this.id = "Unknown";
            } else {
                this.id = id.trim();
            }

            if (name == null || name.trim().isEmpty()) {
                this.name = "Unknown";
            } else {
                this.name = name.trim();
            }

            // 負數數量改為 0
            if (availableCount < 0) {
                this.availableCount = 0;
            } else {
                this.availableCount = availableCount;
            }
        }

        // 借出一個設備
        public boolean borrowOne() {
            if (this.availableCount > 0) {
                this.availableCount--;
                return true;
            }
            return false;
        }

        // 歸還設備
        public void returnItems(int quantity) {
            if (quantity > 0) {
                this.availableCount += quantity;
            }
        }

        // 覆寫 toString()
        @Override
        public String toString() {
            return String.format("ID: %s | Name: %s | Available: %d", id, name, availableCount);
        }
    }

    // 主程式測試區
    public static void main(String[] args) {
        System.out.println("=== 1. Init Test ===");
        Equipment eq1 = new Equipment("EQ001", "Laptop", 1);
        Equipment eq2 = new Equipment("", "   ", -5);

        System.out.println(eq1);
        System.out.println(eq2);
        System.out.println();

        System.out.println("=== 2. Borrow Test ===");
        System.out.println("First borrow eq1 (Expected: true): " + eq1.borrowOne());
        System.out.println(eq1);

        System.out.println("Second borrow eq1 (Expected: false): " + eq1.borrowOne());
        System.out.println(eq1);
        System.out.println();

        System.out.println("=== 3. Return Test ===");
        System.out.println("Try returning -2 items (Invalid):");
        eq1.returnItems(-2);
        System.out.println(eq1);

        System.out.println("Return 3 items (Normal):");
        eq1.returnItems(3);
        System.out.println(eq1);
        System.out.println();

        System.out.println("=== 4. eq2 Test ===");
        System.out.println("Borrow eq2 (Expected: false): " + eq2.borrowOne());
        System.out.println(eq2);
    }
}
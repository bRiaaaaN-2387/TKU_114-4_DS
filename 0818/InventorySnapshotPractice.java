import java.util.Arrays;

public class InventorySnapshotPractice {

    static class InventorySnapshot {
        private final String warehouseId;
        private final int[] quantities;

        // Constructor：使用 defensive copy
        public InventorySnapshot(String warehouseId, int[] quantities) {
            this.warehouseId = warehouseId;

            if (quantities == null) {
                this.quantities = new int[0];
            } else {
                this.quantities = Arrays.copyOf(quantities, quantities.length);
            }
        }

        // getter：回傳 defensive copy
        public String getWarehouseId() {
            return warehouseId;
        }

        public int[] getQuantities() {
            return Arrays.copyOf(quantities, quantities.length);
        }

        // 回傳總數量
        public int totalQuantity() {
            int total = 0;

            for (int quantity : quantities) {
                total += quantity;
            }

            return total;
        }

        // 回傳數量為 0 的品項數
        public int outOfStockCount() {
            int count = 0;

            for (int quantity : quantities) {
                if (quantity == 0) {
                    count++;
                }
            }

            return count;
        }
    }

    public static void main(String[] args) {

        int[] quantities = {5, 0, 3, 0};

        InventorySnapshot snapshot =
                new InventorySnapshot("WH001", quantities);

        System.out.println("warehouseId: " + snapshot.getWarehouseId());
        System.out.println("quantities: "
                + Arrays.toString(snapshot.getQuantities()));

        System.out.println("totalQuantity: "
                + snapshot.totalQuantity());

        System.out.println("outOfStockCount: "
                + snapshot.outOfStockCount());

        // 測試 defensive copy
        quantities[0] = 100;

        System.out.println("After modifying original array:"
                + Arrays.toString(snapshot.getQuantities()));

        // 測試 null
        InventorySnapshot emptySnapshot =
                new InventorySnapshot("WH002", null);

        System.out.println("Null array length:"
                + emptySnapshot.getQuantities().length);
    }
}
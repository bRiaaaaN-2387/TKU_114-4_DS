import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProductComparatorPractice {

    // StoreProduct 類別實作 Comparable 介面，定義 Natural Order
    public static class StoreProduct implements Comparable<StoreProduct> {
        private final int id;
        private final String name;
        private final double price;
        private final int stock;

        public StoreProduct(int id, String name, double price, int stock) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.stock = stock;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public double getPrice() {
            return price;
        }

        public int getStock() {
            return stock;
        }

        // Natural order: 依 id 升冪排序
        @Override
        public int compareTo(StoreProduct other) {
            return Integer.compare(this.id, other.id);
        }

        @Override
        public String toString() {
            return String.format("Product[ID=%d, Name=%s, Price=%.1f, Stock=%d]", id, name, price, stock);
        }
    }

    public static void main(String[] args) {
        // 建立原始資料（包含 5 筆資料，涵蓋同價與同庫存）
        List<StoreProduct> originalProducts = new ArrayList<>();
        originalProducts.add(new StoreProduct(103, "Keyboard", 50.0, 20));
        originalProducts.add(new StoreProduct(101, "Mouse", 20.0, 50));
        originalProducts.add(new StoreProduct(105, "Monitor", 200.0, 20)); // 與 Keyboard 同庫存
        originalProducts.add(new StoreProduct(102, "Headset", 50.0, 15)); // 與 Keyboard 同價格
        originalProducts.add(new StoreProduct(104, "Webcam", 50.0, 30));  // 與 Keyboard, Headset 同價格

        System.out.println("=== Original Products List ===");
        printList(originalProducts);

        // 1. Natural Order: 依 id 升冪排序
        List<StoreProduct> naturalOrderCopy = new ArrayList<>(originalProducts);
        Collections.sort(naturalOrderCopy);
        System.out.println("\n=== Sorted by Natural Order (ID Ascending) ===");
        printList(naturalOrderCopy);

        // 2. Comparator 1: 依 price 升冪，同價時依 name 字典順序
        Comparator<StoreProduct> priceThenNameComp = Comparator
                .comparingDouble(StoreProduct::getPrice)
                .thenComparing(StoreProduct::getName);

        List<StoreProduct> priceOrderCopy = new ArrayList<>(originalProducts);
        priceOrderCopy.sort(priceThenNameComp);
        System.out.println("\n=== Sorted by Comparator 1 (Price Asc, then Name Asc) ===");
        printList(priceOrderCopy);

        // 3. Comparator 2: 依 stock 降冪，同庫存時依 id 升冪
        Comparator<StoreProduct> stockThenIdComp = Comparator
                .comparingInt(StoreProduct::getStock).reversed()
                .thenComparingInt(StoreProduct::getId);

        List<StoreProduct> stockOrderCopy = new ArrayList<>(originalProducts);
        stockOrderCopy.sort(stockThenIdComp);
        System.out.println("\n=== Sorted by Comparator 2 (Stock Desc, then ID Asc) ===");
        printList(stockOrderCopy);
    }

    // 輔助方法：輸出列表內容
    private static void printList(List<StoreProduct> list) {
        for (StoreProduct p : list) {
            System.out.println(p);
        }
    }
}
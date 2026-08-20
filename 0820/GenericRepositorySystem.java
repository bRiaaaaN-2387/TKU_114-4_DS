import java.util.ArrayList;
import java.util.List;

public class GenericRepositorySystem {

    // 定義泛型 Repository<T> 類別
    public static class Repository<T> {
        private final List<T> items;

        // 建構子初始化內部 ArrayList
        public Repository() {
            this.items = new ArrayList<>();
        }

        // 新增項目
        public void add(T item) {
            if (item != null) {
                items.add(item);
            }
        }

        // 依索引取得項目
        public T get(int index) {
            if (index >= 0 && index < items.size()) {
                return items.get(index);
            }
            return null;
        }

        // 依索引移除項目
        public T remove(int index) {
            if (index >= 0 && index < items.size()) {
                return items.remove(index);
            }
            return null;
        }

        // 取得目前數量
        public int size() {
            return items.size();
        }

        // 印出 Repository 內部所有資料
        public void printAll() {
            if (items.isEmpty()) {
                System.out.println("[ Empty Repository ]");
                return;
            }
            for (int i = 0; i < items.size(); i++) {
                System.out.println("[" + i + "] " + items.get(i));
            }
        }
    }

    // 測試用實體類別：Product
    public static class Product {
        private final int id;
        private final String name;
        private final double price;

        public Product(int id, String name, double price) {
            this.id = id;
            this.name = name;
            this.price = price;
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

        @Override
        public String toString() {
            return String.format("Product[ID=%d, Name=%s, Price=%.1f]", id, name, price);
        }
    }

    public static void main(String[] args) {
        // === 1. 測試 Repository<String> ===
        System.out.println("=== 1. Testing Repository<String> ===");
        Repository<String> stringRepo = new Repository<>();

        // 新增資料
        stringRepo.add("Java Programming");
        stringRepo.add("Data Structures");
        stringRepo.add("Operating Systems");

        System.out.println("Repository Size: " + stringRepo.size());
        System.out.println("All Items:");
        stringRepo.printAll();

        // 取得與移除操作
        System.out.println("\nGet item at index 1: " + stringRepo.get(1));
        System.out.println("Remove item at index 0: " + stringRepo.remove(0));

        System.out.println("\nRepository After Removal (Size=" + stringRepo.size() + "):");
        stringRepo.printAll();

        // === 2. 測試 Repository<Product> ===
        System.out.println("\n=== 2. Testing Repository<Product> ===");
        Repository<Product> productRepo = new Repository<>();

        // 新增 Product 資料
        productRepo.add(new Product(201, "Laptop", 1200.0));
        productRepo.add(new Product(202, "Smartphone", 800.0));
        productRepo.add(new Product(203, "Wireless Mouse", 25.5));

        System.out.println("Repository Size: " + productRepo.size());
        System.out.println("All Products:");
        productRepo.printAll();

        // 取得與移除操作
        System.out.println("\nGet product at index 0: " + productRepo.get(0));
        System.out.println("Remove product at index 1: " + productRepo.remove(1));

        System.out.println("\nRepository After Removal (Size=" + productRepo.size() + "):");
        productRepo.printAll();
    }
}
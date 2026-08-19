public class BookArrayReport {

    // 內部類別：書籍 (Book)
    public static class Book {
        private String id;
        private String title;
        private double price;
        private int stock;

        // 建構子 (Constructor)
        public Book(String id, String title, double price, int stock) {
            this.id = id;
            this.title = title;
            this.price = price;
            this.stock = stock;
        }

        // Getter 方法 (用於統計與條件篩選)
        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public double getPrice() {
            return price;
        }

        public int getStock() {
            return stock;
        }

        // 計算單本書籍的總庫存價值
        public double getTotalValue() {
            return price * stock;
        }

        @Override
        public String toString() {
            return String.format("ID: %s | Title: %-20s | Price: $%.1f | Stock: %d", id, title, price, stock);
        }
    }

    // 主程式區
    public static void main(String[] args) {
        // 1. 初始化 Book[] 物件陣列 (包含 4 本書)
        Book[] books = new Book[] {
            new Book("B001", "Java Programming", 650.0, 5),
            new Book("B002", "Python Basics", 480.0, 2),    // 低庫存 (<= 3)
            new Book("B003", "Data Structures", 720.0, 1),  // 最高價 & 低庫存 (<= 3)
            new Book("B004", "Web Development", 520.0, 8)
        };

        // 任務一：輸出所有書籍
        System.out.println("=== 1. All Books List ===");
        for (Book book : books) {
            System.out.println(book);
        }
        System.out.println();

        // 任務二：計算庫存總價值 (price * stock 的總和)
        double totalInventoryValue = 0;
        for (Book book : books) {
            totalInventoryValue += book.getTotalValue();
        }
        System.out.println("=== 2. Total Inventory Value ===");
        System.out.printf("Total Value: $%.1f\n\n", totalInventoryValue);

        // 任務三：找出價格最高的書
        Book mostExpensiveBook = books[0]; // 先預設第一本為最高價
        for (int i = 1; i < books.length; i++) {
            if (books[i].getPrice() > mostExpensiveBook.getPrice()) {
                mostExpensiveBook = books[i]; // 找到更高價的，更新參考
            }
        }
        System.out.println("=== 3. Most Expensive Book ===");
        System.out.println(mostExpensiveBook);
        System.out.println();

        // 任務四：輸出庫存小於或等於 3 的書 (Stock <= 3)
        System.out.println("=== 4. Low Stock Books (Stock <= 3) ===");
        for (Book book : books) {
            if (book.getStock() <= 3) {
                System.out.println(book);
            }
        }
    }
}
public class DocumentCapabilityDemo {

    // 1. 介面一：匯出能力 (Exportable)
    public interface Exportable {
        void exportDocument(String format);
    }

    // 2. 介面二：壓縮能力 (Compressible)
    public interface Compressible {
        void compressDocument(int level);
    }

    // 3. 類別：備份文件 (同時實作 Exportable 與 Compressible 兩個介面)
    public static class BackupDocument implements Exportable, Compressible {
        private String fileName;

        public BackupDocument(String fileName) {
            this.fileName = (fileName == null || fileName.trim().isEmpty()) ? "Untitled.bak" : fileName.trim();
        }

        public String getFileName() {
            return fileName;
        }

        // 實作 Exportable 介面的方法
        @Override
        public void exportDocument(String format) {
            System.out.println("[EXPORT] File: " + fileName + " | Exported to format: " + format.toUpperCase());
        }

        // 實作 Compressible 介面的方法
        @Override
        public void compressDocument(int level) {
            System.out.println("[COMPRESS] File: " + fileName + " | Compressed with ratio level: " + level);
        }

        // 本身類別自訂的專屬方法
        public void displayStatus() {
            System.out.println("[STATUS] Backup File: " + fileName + " is ready.");
        }
    }

    // 主程式測試
    public static void main(String[] args) {
        // 1. 實體化 BackupDocument 物件
        BackupDocument doc = new BackupDocument("Financial_Report_2026.pdf");

        System.out.println("=== 1. Reference pointing through Exportable Interface ===");
        // 使用 Exportable 介面 reference 指向 doc 物件
        Exportable expRef = doc;
        expRef.exportDocument("PDF");
        // expRef.compressDocument(5); // ❌ 編譯錯誤！Exportable 視角看不到 compressDocument()

        System.out.println("\n=== 2. Reference pointing through Compressible Interface ===");
        // 使用 Compressible 介面 reference 指向相同的 doc 物件
        Compressible compRef = doc;
        compRef.compressDocument(9);
        // compRef.exportDocument("PDF"); // ❌ 編譯錯誤！Compressible 視角看不到 exportDocument()

        System.out.println("\n=== 3. Identity & Memory Verification ===");
        // 驗證三個 reference 是否指向記憶體中的「同一區塊/同一物件」
        boolean isSameObject1 = (expRef == compRef);
        boolean isSameObject2 = (expRef == doc);
        
        System.out.println("expRef == compRef (Expected: true): " + isSameObject1);
        System.out.println("expRef == doc     (Expected: true): " + isSameObject2);
        System.out.println("System Identity Hash (expRef)  : " + System.identityHashCode(expRef));
        System.out.println("System Identity Hash (compRef) : " + System.identityHashCode(compRef));
        System.out.println("System Identity Hash (doc)     : " + System.identityHashCode(doc));
    }
}
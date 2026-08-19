import java.util.Arrays;

public class ReportExporterFactory {

    // 1. 報表導出介面 (ReportExporter Interface)
    public interface ReportExporter {
        /**
         * 匯出報表內容
         * @param title  報表標題
         * @param values 數據陣列
         */
        void export(String title, int[] values);

        /**
         * 取得格式名稱
         */
        String getFormatName();
    }

    // 2. 實作一：CSV 格式導出器
    public static class CsvExporter implements ReportExporter {
        @Override
        public void export(String title, int[] values) {
            String safeTitle = (title == null || title.trim().isEmpty()) ? "Untitled" : title.trim();
            int[] safeValues = (values == null) ? new int[0] : values;

            System.out.println("--- CSV Export Start ---");
            System.out.println("Title: " + safeTitle);
            System.out.print("Values: ");
            for (int i = 0; i < safeValues.length; i++) {
                System.out.print(safeValues[i] + (i < safeValues.length - 1 ? ", " : ""));
            }
            System.out.println("\n--- CSV Export End ---");
        }

        @Override
        public String getFormatName() {
            return "CSV Format";
        }
    }

    // 3. 實作二：JSON 格式導出器
    public static class JsonExporter implements ReportExporter {
        @Override
        public void export(String title, int[] values) {
            String safeTitle = (title == null || title.trim().isEmpty()) ? "Untitled" : title.trim();
            int[] safeValues = (values == null) ? new int[0] : values;

            System.out.println("--- JSON Export Start ---");
            System.out.println("{");
            System.out.println("  \"title\": \"" + safeTitle + "\",");
            System.out.println("  \"values\": " + Arrays.toString(safeValues));
            System.out.println("}");
            System.out.println("--- JSON Export End ---");
        }

        @Override
        public String getFormatName() {
            return "JSON Format";
        }
    }

    // 4. 實作三：純文字格式導出器 (TextExporter)
    public static class TextExporter implements ReportExporter {
        @Override
        public void export(String title, int[] values) {
            String safeTitle = (title == null || title.trim().isEmpty()) ? "Untitled" : title.trim();
            int[] safeValues = (values == null) ? new int[0] : values;

            System.out.println("--- Plain Text Export Start ---");
            System.out.println("Report Title : " + safeTitle);
            System.out.println("Total Count  : " + safeValues.length);
            System.out.print("Data Content : ");
            if (safeValues.length == 0) {
                System.out.println("[No Data Available]");
            } else {
                for (int val : safeValues) {
                    System.out.print("[" + val + "] ");
                }
                System.out.println();
            }
            System.out.println("--- Plain Text Export End ---");
        }

        @Override
        public String getFormatName() {
            return "Text Format";
        }
    }

    // 5. 工廠方法：根據傳入的 format 字串建立對應的 Exporter
    public static ReportExporter createExporter(String format) {
        if (format == null) {
            return new TextExporter();
        }

        // 依據傳入格式不區分大小寫匹配
        switch (format.trim().toUpperCase()) {
            case "CSV":
                return new CsvExporter();
            case "JSON":
                return new JsonExporter();
            case "TEXT":
            case "TXT":
                return new TextExporter();
            default:
                // 不支援的 format 自動退回預設的 TextExporter
                System.out.println("[FACTORY WARNING] Unsupported format '" + format + "'. Falling back to TextExporter.");
                return new TextExporter();
        }
    }

    // 6. 業務邏輯方法：只依賴 ReportExporter 介面，絕不使用 instanceof
    public static void exportReport(ReportExporter exporter, String title, int[] values) {
        if (exporter == null) {
            System.out.println("[ERROR] Exporter instance cannot be null.");
            return;
        }

        // 純粹依靠介面引導多型行為，絕不使用 instanceof 做型態切換
        exporter.export(title, values);
    }

    // 主程式測試區
    public static void main(String[] args) {
        int[] sampleData = {105, 220, 315, 480};

        System.out.println("=== 1. Testing Normal Formats (CSV, JSON, TEXT) ===");
        
        ReportExporter csvExporter = createExporter("CSV");
        exportReport(csvExporter, "Quarterly Sales", sampleData);
        System.out.println();

        ReportExporter jsonExporter = createExporter("json");
        exportReport(jsonExporter, "User Metrics", sampleData);
        System.out.println();

        ReportExporter textExporter = createExporter("TEXT");
        exportReport(textExporter, "System Log Summary", sampleData);
        System.out.println();

        System.out.println("=== 2. Testing Unsupported Format (Fallback to Text) ===");
        // 傳入不支援的 XML 格式
        ReportExporter unknownExporter = createExporter("XML");
        exportReport(unknownExporter, "Unsupported Format Test", sampleData);
        System.out.println();

        System.out.println("=== 3. Testing Null Values (Null-Safety Check) ===");
        // 測試 values 為 null 時的情況，確保程式不爆開（不發生 NullPointerException）
        ReportExporter nullDataExporter = createExporter("CSV");
        exportReport(nullDataExporter, "Empty Report Test", null);
    }
}
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class CourseTagReport {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 宣告並初始化三種集合資料結構
        List<String> rawTagList = new ArrayList<>();
        Set<String> uniqueTagSet = new HashSet<>();
        Map<String, Integer> tagCountMap = new HashMap<>();

        // 提示使用者輸入課程標籤（以空格分隔）
        System.out.println("=== Course Tag Collector ===");
        System.out.print("Enter tags separated by space: ");
        String inputLine = scanner.nextLine().trim();

        if (!inputLine.isEmpty()) {
            // 切分輸入字串為標籤陣列
            String[] inputTags = inputLine.split("\\s+");

            for (String tag : inputTags) {
                // 1. List: 依序保留原始輸入順序（允許重複）
                rawTagList.add(tag);

                // 2. Set: 自動過濾重複標籤，僅保留不重複項目
                uniqueTagSet.add(tag);

                // 3. Map: 統計每個標籤出現的累計次數
                tagCountMap.put(tag, tagCountMap.getOrDefault(tag, 0) + 1);
            }
        }

        // 主控台輸出：展示 List 資料內容與用途說明
        System.out.println("\n--- 1. Original Sequence (List) ---");
        System.out.println("Data: " + rawTagList);
        System.out.println("Usage: Preserves insertion order and allows duplicates. Ideal for logging user entry history.");

        // 主控台輸出：展示 Set 資料內容與用途說明
        System.out.println("\n--- 2. Unique Tags (Set) ---");
        System.out.println("Data: " + uniqueTagSet);
        System.out.println("Usage: Eliminates duplicate values. Ideal for generating quick-filter categories or unique tag lists.");

        // 主控台輸出：展示 Map 資料內容與用途說明
        System.out.println("\n--- 3. Tag Frequency Count (Map) ---");
        System.out.println("Data: " + tagCountMap);
        System.out.println("Usage: Maps unique keys to count values. Ideal for analytics, popularity ranking, and frequency reports.");

        scanner.close();
    }
}
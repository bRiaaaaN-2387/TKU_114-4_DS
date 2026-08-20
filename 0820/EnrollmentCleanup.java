import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class EnrollmentCleanup {

    public static void main(String[] args) {
        // 建立包含重複、空白與 null 資料的初始 List
        List<String> rawList = new ArrayList<>(Arrays.asList(
            "Alice",
            "Bob",
            "",
            "  ",
            null,
            "Alice",
            "Charlie",
            "Bob",
            "David",
            "   ",
            null,
            "Eva"
        ));

        // 輸出清理前的原始名單
        System.out.println("=== 1. Raw Enrollment List ===");
        System.out.println("Data: " + rawList);
        System.out.println("Total Count: " + rawList.size());

        // 使用 Iterator 移除不合法資料 (null、空字串、全空白字串)
        Iterator<String> iterator = rawList.iterator();
        while (iterator.hasNext()) {
            String name = iterator.next();
            if (name == null || name.trim().isEmpty()) {
                iterator.remove();
            }
        }

        // 宣告 Set 用於過濾與記錄重複姓名
        Set<String> seenSet = new HashSet<>();
        Set<String> duplicateSet = new HashSet<>();
        List<String> cleanedList = new ArrayList<>();

        // 走訪清理過不合法字串的名單，找出重複者並建立最終乾淨名單
        for (String name : rawList) {
            String trimmedName = name.trim();
            // 若 Set 已經包含該姓名，代表它是重複資料
            if (!seenSet.add(trimmedName)) {
                duplicateSet.add(trimmedName);
            } else {
                cleanedList.add(trimmedName);
            }
        }

        // 主控台輸出清理後的名單與重複報告
        System.out.println("\n=== 2. Cleaned Enrollment List (Valid & Unique) ===");
        System.out.println("Data: " + cleanedList);
        System.out.println("Valid Count: " + cleanedList.size());

        System.out.println("\n=== 3. Duplicate Names Report ===");
        System.out.println("Duplicate Names Found: " + duplicateSet);
        System.out.println("Duplicate Count: " + duplicateSet.size());
    }
}
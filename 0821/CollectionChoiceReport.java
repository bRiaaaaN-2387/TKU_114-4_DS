import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class CollectionChoiceReport {

    public static void main(String[] args) {
        
        System.out.println("=== Collection Choice Report ===\n");

        // 1. 保留搜尋紀錄且允許重複
        // 選擇原因：List 有序且允許重複元素，ArrayList 提供快速的循序存取。
        System.out.println("--- 1. Search History (Duplicates Allowed) ---");
        System.out.println("Interface chosen     : java.util.List");
        System.out.println("Implementation chosen: java.util.ArrayList");
        
        List<String> searchHistory = new ArrayList<>();
        searchHistory.add("Java Collections");
        searchHistory.add("Spring Boot");
        searchHistory.add("Java Collections"); // 測試重複加入
        
        System.out.println("Result: " + searchHistory);
        System.out.println();

        
        // 2. 保存不重複會員編號
        // 選擇原因：Set 保證元素唯一性，HashSet 透過 Hash 演算法提供極快的存取速度。
        System.out.println("--- 2. Unique Member IDs ---");
        System.out.println("Interface chosen     : java.util.Set");
        System.out.println("Implementation chosen: java.util.HashSet");
        
        Set<String> memberSet = new HashSet<>();
        memberSet.add("M001");
        memberSet.add("M002");
        memberSet.add("M001"); // 測試重複加入，應被自動忽略
        
        System.out.println("Result: " + memberSet);
        System.out.println();


        // 3. 以學號查詢成績
        // 選擇原因：Map 提供 Key-Value 對應關係，HashMap 能以 O(1) 時間複雜度透過 Key (學號) 查出 Value (成績)。
        System.out.println("--- 3. Query Grade by Student ID ---");
        System.out.println("Interface chosen     : java.util.Map");
        System.out.println("Implementation chosen: java.util.HashMap");
        
        Map<String, Integer> gradeMap = new HashMap<>();
        gradeMap.put("S101", 95);
        gradeMap.put("S102", 82);
        
        System.out.println("Result [S101 grade]: " + gradeMap.get("S101"));
        System.out.println("Result [All grades]: " + gradeMap);
        System.out.println();


        // 4. 依到達順序處理列印工作
        // 選擇原因：Queue 代表先進先出 (FIFO) 結構，LinkedList 是 Java 中標準的 Queue 實作。
        System.out.println("--- 4. Print Jobs Processing (FIFO) ---");
        System.out.println("Interface chosen     : java.util.Queue");
        System.out.println("Implementation chosen: java.util.LinkedList");
        
        Queue<String> printQueue = new LinkedList<>();
        printQueue.offer("Homework.pdf"); // 加入工作
        printQueue.offer("Photo.png");
        
        System.out.println("Processing first job: " + printQueue.poll()); // 取出最先加入的工作
        System.out.println("Remaining jobs      : " + printQueue);
        System.out.println();


        // 5. 復原最近操作 (Undo)
        // 選擇原因：需要後進先出 (LIFO) 特性。現代 Java 建議使用 Deque 介面取代舊的 Stack 類別，ArrayDeque 效能最佳。
        System.out.println("--- 5. Undo Recent Operation (LIFO) ---");
        System.out.println("Interface chosen     : java.util.Deque");
        System.out.println("Implementation chosen: java.util.ArrayDeque");
        
        Deque<String> undoStack = new ArrayDeque<>();
        undoStack.push("Type 'Hello'"); // 模擬操作紀錄
        undoStack.push("Type ' World'");
        
        System.out.println("Current actions  : " + undoStack);
        System.out.println("Undoing last step: " + undoStack.pop()); // 移除並回傳最後一次加入的操作
        System.out.println("Remaining actions: " + undoStack);
    }
}
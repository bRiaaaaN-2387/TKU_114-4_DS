import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ListImplementationLab {

    // 建立只接收 List<Integer> 的方法，集中執行各種操作以測試功能一致性
    public static void executeListOperations(List<Integer> list, String listName) {
        System.out.println("=== Testing: " + listName + " ===");

        // 1. 尾端新增 (Append)
        list.add(10);
        list.add(20);
        list.add(30);
        System.out.println("After appending (10, 20, 30): " + list);

        // 2. 指定位置插入 (Insert at index)
        // 在索引 1 的位置插入 15
        list.add(1, 15);
        System.out.println("After inserting 15 at index 1: " + list);

        // 3. 搜尋 (Search)
        // 尋找數值 20 所在的索引位置
        int searchTarget = 20;
        int targetIndex = list.indexOf(searchTarget);
        System.out.println("Search for value " + searchTarget + " -> Found at index: " + targetIndex);

        // 4. 刪除 (Delete)
        // 刪除索引 2 的元素 (即剛剛搜尋到的 20)
        list.remove(2);
        System.out.println("After deleting element at index 2: " + list);

        // 5. 總和 (Sum)
        // 走訪集合並計算所有元素的加總
        int sum = 0;
        for (Integer num : list) {
            sum += num;
        }
        System.out.println("Sum of all elements: " + sum);
        System.out.println();
    }

    public static void main(String[] args) {
        // 測試 ArrayList 實作
        List<Integer> arrayList = new ArrayList<>();
        executeListOperations(arrayList, "ArrayList");

        // 測試 LinkedList 實作
        List<Integer> linkedList = new LinkedList<>();
        executeListOperations(linkedList, "LinkedList");

        // 主控台輸出：說明兩者內部成本差異
        System.out.println("=== Internal Cost Differences ===");
        
        System.out.println("[ArrayList]");
        System.out.println("1. Structure: Backed by a dynamic array.");
        System.out.println("2. Search/Access: O(1) time complexity. Extremely fast for accessing elements by index.");
        System.out.println("3. Insert/Delete: O(n) time complexity. Adding or removing elements in the middle requires shifting subsequent elements.");
        System.out.println("4. Memory: Contiguous memory blocks. More cache-friendly but may require expensive resizing operations.");
        
        System.out.println("\n[LinkedList]");
        System.out.println("1. Structure: Backed by a doubly-linked list.");
        System.out.println("2. Search/Access: O(n) time complexity. Requires node traversal from the beginning or end.");
        System.out.println("3. Insert/Delete: O(1) time complexity IF the node reference is already known (e.g., using Iterator). Otherwise, finding the index still takes O(n).");
        System.out.println("4. Memory: Scattered in memory heap. Higher memory overhead due to storing references (pointers) for previous and next nodes.");
    }
}
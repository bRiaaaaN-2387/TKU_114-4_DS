import java.util.Arrays;

public class GenericArrayTools {

    /**
     * 1. 計算陣列中與 target 匹配的元素數量
     * 安全處理：data 為 null、空陣列、target 為 null 或元素包含 null
     */
    public static <T> int countMatches(T[] data, T target) {
        if (data == null || data.length == 0) {
            return 0;
        }

        int count = 0;
        for (T item : data) {
            if (target == null) {
                if (item == null) {
                    count++;
                }
            } else {
                if (target.equals(item)) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * 2. 取得陣列的最後一個元素
     * 安全處理：data 為 null 或空陣列時回傳 null
     */
    public static <T> T last(T[] data) {
        if (data == null || data.length == 0) {
            return null;
        }
        return data[data.length - 1];
    }

    /**
     * 3. 交換陣列中兩個索引位置的元素
     * 安全處理：data 為 null、索引越界 (out of bounds) 或負數 index 時不處理且印出警告
     */
    public static <T> void swap(T[] data, int first, int second) {
        if (data == null || data.length == 0) {
            System.out.println("[SWAP WARNING] Array is null or empty. Operation skipped.");
            return;
        }

        if (first < 0 || first >= data.length || second < 0 || second >= data.length) {
            System.out.println("[SWAP WARNING] Invalid index (" + first + ", " + second + 
                               ") for array length " + data.length + ". Operation skipped.");
            return;
        }

        T temp = data[first];
        data[first] = data[second];
        data[second] = temp;
    }

    // 主程式測試區
    public static void main(String[] args) {
        System.out.println("=== 1. Testing countMatches() ===");
        String[] words = {"apple", "banana", "apple", null, "orange", "apple", null};
        System.out.println("Array: " + Arrays.toString(words));
        System.out.println("Matches for 'apple' : " + countMatches(words, "apple"));  // Expected: 3
        System.out.println("Matches for null    : " + countMatches(words, null));     // Expected: 2
        System.out.println("Matches on null array: " + countMatches(null, "apple")); // Expected: 0
        System.out.println();


        System.out.println("=== 2. Testing last() ===");
        Integer[] numbers = {10, 20, 30, 45, 99};
        System.out.println("Array: " + Arrays.toString(numbers));
        System.out.println("Last element: " + last(numbers)); // Expected: 99
        
        Integer[] emptyArray = new Integer[0];
        System.out.println("Last on empty array: " + last(emptyArray)); // Expected: null
        System.out.println("Last on null array : " + last(null));       // Expected: null
        System.out.println();


        System.out.println("=== 3. Testing swap() & Edge Cases ===");
        Double[] decimals = {1.1, 2.2, 3.3, 4.4};
        System.out.println("Before swap: " + Arrays.toString(decimals));
        
        // 正常交換 index 0 與 3
        swap(decimals, 0, 3);
        System.out.println("After swap(0, 3): " + Arrays.toString(decimals));

        // 測試無效索引 (Negative / Out of Bounds)
        System.out.println("\n--- Testing Invalid Index Bounds ---");
        swap(decimals, -1, 2);   // 不合法 index
        swap(decimals, 1, 10);   // 不合法 index
        System.out.println("Array remains intact: " + Arrays.toString(decimals));
    }
}
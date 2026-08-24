import java.util.Arrays;

public class RecursiveArrayStatistics {

    // 1. 尋找最大值 (Wrapper)
    public static int maximum(int[] values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("Array cannot be null or empty.");
        }
        return maxHelper(values, 0);
    }

    // 尋找最大值的遞迴 (Helper)
    private static int maxHelper(int[] values, int index) {
        // Base case: 到達陣列最後一個元素
        if (index == values.length - 1) {
            return values[index];
        }
        // Recursive case: 比較「當前元素」與「剩餘陣列中的最大值」
        return Math.max(values[index], maxHelper(values, index + 1));
    }


    // 2. 尋找最小值 (Wrapper)
    public static int minimum(int[] values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("Array cannot be null or empty.");
        }
        return minHelper(values, 0);
    }

    // 尋找最小值的遞迴 (Helper)
    private static int minHelper(int[] values, int index) {
        // Base case: 到達陣列最後一個元素
        if (index == values.length - 1) {
            return values[index];
        }
        // Recursive case: 比較「當前元素」與「剩餘陣列中的最小值」
        return Math.min(values[index], minHelper(values, index + 1));
    }


    // 3. 統計大於門檻值的數量 (Wrapper)
    public static int countAbove(int[] values, int threshold) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("Array cannot be null or empty.");
        }
        return countAboveHelper(values, threshold, 0);
    }

    // 統計大於門檻值數量的遞迴 (Helper)
    private static int countAboveHelper(int[] values, int threshold, int index) {
        // Base case: 超出陣列範圍，回傳 0
        if (index == values.length) {
            return 0;
        }
        // 檢查當前元素是否大於門檻值
        int count = (values[index] > threshold) ? 1 : 0;
        
        // Recursive case: 當前計數 + 剩餘陣列的計數結果
        return count + countAboveHelper(values, threshold, index + 1);
    }


    // 測試主程式
    public static void main(String[] args) {
        int[] testArray = {15, -4, 56, 12, 8, 99, 23};
        
        System.out.println("=== Valid Array Test ===");
        System.out.println("Array : " + Arrays.toString(testArray));
        System.out.println("Max   : " + maximum(testArray));
        System.out.println("Min   : " + minimum(testArray));
        System.out.println("Above 20: " + countAbove(testArray, 20));
        System.out.println();

        System.out.println("=== Exception Handling Test ===");
        
        // 測試空陣列
        try {
            int[] emptyArray = {};
            System.out.println("Testing empty array...");
            maximum(emptyArray);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught Exception: " + e.getMessage());
        }

        // 測試 Null 陣列
        try {
            int[] nullArray = null;
            System.out.println("Testing null array...");
            minimum(nullArray);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught Exception: " + e.getMessage());
        }
    }
}
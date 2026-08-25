public class RecursiveCallReport {

    // 實作遞迴加總，並追蹤呼叫過程
    public static int sum(int[] data, int index) {
        // Base case: 防呆檢查與終止條件 (陣列走到底或為空)
        if (data == null || index >= data.length) {
            System.out.println(getIndent(index) + "[Base Case] Index: " + index + " -> Return Value: 0");
            return 0;
        }

        int currentValue = data[index];
        
        // Recursive case: 往下傳遞，等待子問題 (smallerResult) 回傳
        int recursiveResult = sum(data, index + 1);
        
        // 計算當前層的最終回傳值
        int returnValue = currentValue + recursiveResult;

        // 依題目要求輸出四項資訊：index、current value、recursive result、return value
        System.out.println(getIndent(index) + "Index: " + index 
                + " | Current Value: " + currentValue 
                + " | Recursive Result: " + recursiveResult 
                + " | Return Value: " + returnValue);

        return returnValue;
    }

    // 輔助方法：產生縮排以利觀察遞迴深度 (Call Stack Depth)
    private static String getIndent(int depth) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            sb.append("  ");
        }
        return sb.toString();
    }

    // 測試主程式
    public static void main(String[] args) {
        
        System.out.println("=== 1. General Array Test ===");
        int[] generalArray = {10, 20, 30};
        System.out.println("Final Sum: " + sum(generalArray, 0) + "\n");

        System.out.println("=== 2. Single Element Test ===");
        int[] singleElement = {5};
        System.out.println("Final Sum: " + sum(singleElement, 0) + "\n");

        System.out.println("=== 3. Empty Array Test ===");
        int[] emptyArray = {};
        System.out.println("Final Sum: " + sum(emptyArray, 0) + "\n");
    }
}
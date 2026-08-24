public class RecursiveDigitReport {

    // 1. 計算數字總和 (Wrapper)
    public static int digitSum(int number) {
        // 負數先轉絕對值
        return sumHelper(Math.abs(number));
    }

    // 實作數字總和的遞迴 (Helper)
    private static int sumHelper(int n) {
        // Base case: 個位數直接回傳
        if (n < 10) {
            return n;
        }
        // Recursive case: 目前個位數 + 剩餘數字的總和
        return (n % 10) + sumHelper(n / 10);
    }

    // 2. 計算位數長度 (Wrapper)
    public static int digitCount(int number) {
        // 負數轉絕對值，且當傳入 0 時，helper 也會正確回傳 1
        return countHelper(Math.abs(number));
    }

    // 實作位數長度的遞迴 (Helper)
    private static int countHelper(int n) {
        // Base case: 小於 10 的數字，位數皆為 1
        if (n < 10) {
            return 1;
        }
        // Recursive case: 1 + 剩餘數字的位數
        return 1 + countHelper(n / 10);
    }

    // 3. 統計特定數字出現次數 (Wrapper)
    public static int countDigit(int number, int target) {
        if (target < 0 || target > 9) {
            System.out.println("Error: Target digit must be between 0 and 9.");
            return -1;
        }
        return countDigitHelper(Math.abs(number), target);
    }

    // 實作統計出現次數的遞迴 (Helper)
    private static int countDigitHelper(int n, int target) {
        // Base case: 剩餘最後一位數時，檢查是否與 target 相符
        if (n < 10) {
            return (n == target) ? 1 : 0;
        }
        // 檢查當前的個位數是否相符
        int match = (n % 10 == target) ? 1 : 0;
        // Recursive case: 當前結果 + 剩餘數字的檢查結果
        return match + countDigitHelper(n / 10, target);
    }

    // 主程式：執行指定的測試案例
    public static void main(String[] args) {
        // 指定測試陣列：50205, 0, -731
        int[] testCases = {50205, 0, -731};
        
        // 為了展示多種 target 的尋找結果，設定這兩個目標數字
        int targetA = 0;
        int targetB = 5;

        for (int num : testCases) {
            System.out.println("=== Test Value: " + num + " ===");
            System.out.println("digitSum            : " + digitSum(num));
            System.out.println("digitCount          : " + digitCount(num));
            System.out.println("countDigit (find " + targetA + "): " + countDigit(num, targetA));
            System.out.println("countDigit (find " + targetB + "): " + countDigit(num, targetB));
            System.out.println();
        }
    }
}
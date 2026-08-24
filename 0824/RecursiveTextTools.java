public class RecursiveTextTools {

    // 1. 反轉字串
    public static String reverse(String text) {
        // Base case: 字串為空或 null，直接回傳
        if (text == null || text.isEmpty()) {
            return text;
        }
        // Recursive case: 將第一個字元移到最後，剩餘字串繼續遞迴
        return reverse(text.substring(1)) + text.charAt(0);
    }

    // 2. 檢查是否為迴文 (Wrapper)
    public static boolean isPalindrome(String text) {
        if (text == null) {
            return false;
        }
        // 預處理：去除所有空白字元，並轉為小寫
        String cleanedText = text.replaceAll("\\s+", "").toLowerCase();
        
        // 呼叫 Helper 開始比對，設定頭尾指標
        return palindromeHelper(cleanedText, 0, cleanedText.length() - 1);
    }

    // 檢查迴文的遞迴 (Helper)
    private static boolean palindromeHelper(String text, int left, int right) {
        // Base case: 左右指標交會或交錯，代表全部字元比對皆相符
        if (left >= right) {
            return true;
        }
        // 若左右字元不同，則不是迴文
        if (text.charAt(left) != text.charAt(right)) {
            return false;
        }
        // Recursive case: 範圍往中間縮減，繼續檢查下一對字元
        return palindromeHelper(text, left + 1, right - 1);
    }

    // 3. 計算特定字元出現次數 (Wrapper)
    public static int countCharacter(String text, char target) {
        if (text == null || text.isEmpty()) {
            return 0;
        }
        // 呼叫 Helper，起始索引為 0
        return countCharHelper(text, target, 0);
    }

    // 算字元的遞迴 (Helper)
    private static int countCharHelper(String text, char target, int index) {
        // Base case: 索引到達字串長度，走訪完畢
        if (index == text.length()) {
            return 0;
        }
        // 檢查當下索引的字元是否符合目標
        int match = (text.charAt(index) == target) ? 1 : 0;
        
        // Recursive case: 當前比對結果 + 剩餘字串的比對結果
        return match + countCharHelper(text, target, index + 1);
    }

    // 測試主程式
    public static void main(String[] args) {
        
        System.out.println("=== Test 1: Reverse ===");
        System.out.println("Reverse 'Java'     : " + reverse("Java"));
        System.out.println("Reverse 'Recursion': " + reverse("Recursion"));
        System.out.println();

        System.out.println("=== Test 2: isPalindrome ===");
        // 依照題目要求測試特定情境
        String[] palindromeTests = {
            "",                           // Empty string
            "A",                          // Single character
            "Level",                      // Mixed case palindrome
            "A nut for a jar of tuna",    // General string (Palindrome with spaces)
            "Hello World"                 // General string (Not a palindrome)
        };
        
        for (String testCase : palindromeTests) {
            System.out.println("isPalindrome('" + testCase + "') -> " + isPalindrome(testCase));
        }
        System.out.println();

        System.out.println("=== Test 3: countCharacter ===");
        System.out.println("Count 'e' in 'engineering': " + countCharacter("engineering", 'e'));
        System.out.println("Count 'z' in 'hello'      : " + countCharacter("hello", 'z'));
    }
}
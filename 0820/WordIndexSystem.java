import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WordIndexSystem {

    public static void main(String[] args) {
        // 內建句子陣列 (包含大小寫與標點符號，如句點、逗號)
        String[] sentences = {
            "Java is a popular programming language.",
            "Java is object-oriented, robust, and secure.",
            "Data structures and algorithms are important for programming."
        };

        // 宣告 Map 保存單字頻率，Set 保存不重複單字
        Map<String, Integer> wordCountMap = new HashMap<>();
        Set<String> uniqueWords = new HashSet<>();

        // 解析與統計單字
        for (String sentence : sentences) {
            // 轉換為小寫，並使用正規表示式移除句點與逗號
            String cleanedSentence = sentence.toLowerCase().replaceAll("[,.]", "");

            // 以空白分隔切分為單字陣列
            String[] words = cleanedSentence.split("\\s+");

            for (String word : words) {
                if (!word.isEmpty()) {
                    // 加入 Set 保存不重複單字
                    uniqueWords.add(word);

                    // 更新 Map 統計單字出現次數
                    wordCountMap.put(word, wordCountMap.getOrDefault(word, 0) + 1);
                }
            }
        }

        // 篩選出出現至少兩次 (count >= 2) 的單字
        List<String> frequentWords = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : wordCountMap.entrySet()) {
            if (entry.getValue() >= 2) {
                frequentWords.add(entry.getKey() + " (" + entry.getValue() + " times)");
            }
        }

        // 主控台輸出結果
        System.out.println("=== 1. All Unique Words (Set) ===");
        System.out.println("Total Unique Words: " + uniqueWords.size());
        System.out.println("Words: " + uniqueWords);

        System.out.println("\n=== 2. Word Frequency Statistics (Map) ===");
        for (Map.Entry<String, Integer> entry : wordCountMap.entrySet()) {
            System.out.printf("Word: %-15s | Count: %d%n", entry.getKey(), entry.getValue());
        }

        System.out.println("\n=== 3. Words Appearing At Least Twice ===");
        if (frequentWords.isEmpty()) {
            System.out.println("No words found with frequency >= 2.");
        } else {
            for (String item : frequentWords) {
                System.out.println("- " + item);
            }
        }
    }
}
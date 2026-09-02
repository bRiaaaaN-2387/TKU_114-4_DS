import java.util.HashSet;
import java.util.Set;

public class InterestSetComparison {

    public static void compareInterests(Set<String> set1, Set<String> set2) {
        // Union (聯集)：包含兩個 Set 中的所有元素
        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);

        // Intersection (交集)：只保留兩個 Set 共有的元素
        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        // First-only (差集)：只在第一個 Set 且不在第二個 Set 的元素
        Set<String> firstOnly = new HashSet<>(set1);
        firstOnly.removeAll(set2);

        // Second-only (差集)：只在第二個 Set 且不在第一個 Set 的元素
        Set<String> secondOnly = new HashSet<>(set2);
        secondOnly.removeAll(set1);

        System.out.println("set1=" + set1);
        System.out.println("set2=" + set2);
        System.out.println("union=" + union);
        System.out.println("intersection=" + intersection);
        System.out.println("first-only=" + firstOnly);
        System.out.println("second-only=" + secondOnly);
    }

    public static void main(String[] args) {
        // 使用 Set.of 建立不可變的集合作為輸入測試
        Set<String> person1 = Set.of("Reading", "Swimming", "Coding", "Music");
        Set<String> person2 = Set.of("Coding", "Gaming", "Reading", "Traveling");

        System.out.println("--- interest comparison report ---");
        compareInterests(person1, person2);
    }
}
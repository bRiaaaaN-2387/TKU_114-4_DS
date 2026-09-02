import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EnrollmentConflictSet {
    
    // 複合 key：record 會自動實作 equals 與 hashCode
    public record EnrollmentKey(String studentId, String courseId) {}

    public static void analyzeEnrollments(List<EnrollmentKey> records) {
        if (records == null || records.isEmpty()) return;

        Set<EnrollmentKey> accepted = new HashSet<>();
        List<EnrollmentKey> duplicates = new ArrayList<>();
        
        // 記錄每人課程集合
        Map<String, Set<String>> studentCourses = new HashMap<>();
        // 記錄每門課修課名單 (使用 Set 避免同一人重複計算)
        Map<String, Set<String>> courseStudents = new HashMap<>();

        for (EnrollmentKey record : records) {
            if (record.studentId() == null || record.courseId() == null) continue;
            
            // 利用 HashSet 的 add 方法回傳值判斷是否為重複紀錄
            if (!accepted.add(record)) {
                duplicates.add(record);
            } else {
                studentCourses.computeIfAbsent(record.studentId(), k -> new HashSet<>()).add(record.courseId());
                courseStudents.computeIfAbsent(record.courseId(), k -> new HashSet<>()).add(record.studentId());
            }
        }

        System.out.println("--- duplicate records ---");
        for (EnrollmentKey dup : duplicates) {
            System.out.println("student=" + dup.studentId() + " course=" + dup.courseId());
        }

        System.out.println("\n--- student course sets ---");
        for (Map.Entry<String, Set<String>> entry : studentCourses.entrySet()) {
            System.out.println("student=" + entry.getKey() + " courses=" + entry.getValue());
        }

        System.out.println("\n--- course enrollment count ---");
        for (Map.Entry<String, Set<String>> entry : courseStudents.entrySet()) {
            System.out.println("course=" + entry.getKey() + " count=" + entry.getValue().size());
        }
    }

    public static void main(String[] args) {
        List<EnrollmentKey> data = List.of(
            new EnrollmentKey("B11001", "CS101"),
            new EnrollmentKey("B11001", "MATH201"),
            new EnrollmentKey("B11002", "CS101"),
            new EnrollmentKey("B11001", "CS101"), // 模擬重複選課
            new EnrollmentKey("B11003", "ENG101")
        );
        
        analyzeEnrollments(data);
    }
}
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Q05_StudentHashIndex {
    // 雙向索引 Map
    private final Map<String, Set<String>> studentToCourses = new HashMap<>();
    private final Map<String, Set<String>> courseToStudents = new HashMap<>();
    private int totalEnrollments = 0;

    // 正規化輸入：轉大寫並去除前後空白，若為 null 或 blank 回傳 null
    private String normalize(String input) {
        if (input == null || input.isBlank()) {
            return null;
        }
        return input.trim().toUpperCase();
    }

    // 選課
    public boolean enroll(String studentId, String courseId) {
        String sId = normalize(studentId);
        String cId = normalize(courseId);

        if (sId == null || cId == null) return false;

        Set<String> courses = studentToCourses.computeIfAbsent(sId, k -> new HashSet<>());
        
        // 若已選修過該課程 (add 回傳 false)，則拒絕重複選課
        if (!courses.add(cId)) return false;

        // 同步更新課程的學生名單
        courseToStudents.computeIfAbsent(cId, k -> new HashSet<>()).add(sId);
        totalEnrollments++;
        return true;
    }

    // 退選
    public boolean drop(String studentId, String courseId) {
        String sId = normalize(studentId);
        String cId = normalize(courseId);

        if (sId == null || cId == null) return false;

        Set<String> courses = studentToCourses.get(sId);
        if (courses == null || !courses.remove(cId)) return false; // 學生未選此課

        // 若學生已無選修任何課程，移除空 key
        if (courses.isEmpty()) {
            studentToCourses.remove(sId);
        }

        // 同步更新課程的學生名單
        Set<String> students = courseToStudents.get(cId);
        if (students != null) {
            students.remove(sId);
            // 若課程已無任何學生選修，移除空 key
            if (students.isEmpty()) {
                courseToStudents.remove(cId);
            }
        }

        totalEnrollments--;
        return true;
    }

    // 查詢學生選修的課程
    public Set<String> coursesOf(String studentId) {
        String sId = normalize(studentId);
        if (sId == null || !studentToCourses.containsKey(sId)) {
            return Set.of();
        }
        // Set.copyOf() 回傳不可修改且獨立的副本
        return Set.copyOf(studentToCourses.get(sId));
    }

    // 查詢修課的學生名單
    public Set<String> studentsIn(String courseId) {
        String cId = normalize(courseId);
        if (cId == null || !courseToStudents.containsKey(cId)) {
            return Set.of();
        }
        return Set.copyOf(courseToStudents.get(cId));
    }

    // 總選課紀錄數
    public int enrollmentCount() {
        return totalEnrollments;
    }

    public static void main(String[] args) {
        Q05_StudentHashIndex index = new Q05_StudentHashIndex();

        System.out.println("--- normal enroll & normalization ---");
        System.out.println("enroll b11001 CS101=" + index.enroll(" b11001 ", "cs101 "));
        System.out.println("enroll B11001 MATH201=" + index.enroll("B11001", "MATH201"));
        System.out.println("enroll B11002 CS101=" + index.enroll("B11002", "CS101"));
        
        System.out.println("coursesOf B11001=" + index.coursesOf("b11001")); // 測試查詢正規化
        System.out.println("studentsIn CS101=" + index.studentsIn("CS101"));
        System.out.println("enrollmentCount=" + index.enrollmentCount());

        System.out.println("\n--- invalid & duplicate cases ---");
        System.out.println("enroll duplicate=" + index.enroll("B11001", "cs101"));
        System.out.println("enroll blank=" + index.enroll("  ", "CS101"));
        System.out.println("enroll null=" + index.enroll("B11001", null));

        System.out.println("\n--- drop & cleanup cases ---");
        System.out.println("drop B11001 MATH201=" + index.drop("b11001", "math201"));
        System.out.println("drop B11001 CS101=" + index.drop("B11001", "CS101")); // 退選最後一門課
        System.out.println("coursesOf B11001 (after drop all)=" + index.coursesOf("B11001"));
        System.out.println("enrollmentCount=" + index.enrollmentCount());

        System.out.println("\n--- result protection ---");
        Set<String> students = index.studentsIn("CS101");
        try {
            students.add("B99999"); // 嘗試修改回傳的 Set
        } catch (UnsupportedOperationException e) {
            System.out.println("modification prevented: UnsupportedOperationException");
        }
    }
}
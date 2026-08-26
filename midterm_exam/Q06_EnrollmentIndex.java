import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Q06_EnrollmentIndex {
    private final Map<String, Set<String>> enrollmentMapR26;

    public Q06_EnrollmentIndex() {
        // 使用 TreeMap 讓 courseCode 自動以字典順序排列
        this.enrollmentMapR26 = new TreeMap<>();
    }

    public boolean enroll(String courseCode, String studentId) {
        if (courseCode == null || courseCode.trim().isEmpty() || studentId == null || studentId.trim().isEmpty()) {
            return false;
        }
        // 使用 TreeSet 讓 studentId 自動以字典順序排列
        enrollmentMapR26.putIfAbsent(courseCode, new TreeSet<>());
        return enrollmentMapR26.get(courseCode).add(studentId);
    }

    public boolean drop(String courseCode, String studentId) {
        if (courseCode == null || courseCode.trim().isEmpty() || studentId == null || studentId.trim().isEmpty()) {
            return false;
        }
        Set<String> students = enrollmentMapR26.get(courseCode);
        if (students != null && students.remove(studentId)) {
            // 若該課程已無人選課，移除該 courseCode
            if (students.isEmpty()) {
                enrollmentMapR26.remove(courseCode);
            }
            return true;
        }
        return false;
    }

    public int courseSize(String courseCode) {
        if (courseCode == null) return 0;
        Set<String> students = enrollmentMapR26.get(courseCode);
        return students == null ? 0 : students.size();
    }

    public java.util.List<String> studentsOf(String courseCode) {
        Set<String> students = enrollmentMapR26.get(courseCode);
        if (students == null) {
            return new ArrayList<>();
        }
        // 回傳新的 ArrayList，避免外部修改內部 Set
        return new ArrayList<>(students);
    }

    public java.util.List<String> coursesOf(String studentId) {
        List<String> courses = new ArrayList<>();
        if (studentId == null || studentId.trim().isEmpty()) {
            return courses;
        }
        // TreeMap 的 entrySet 已經是依據 courseCode 的字典順序排列
        for (Map.Entry<String, Set<String>> entry : enrollmentMapR26.entrySet()) {
            if (entry.getValue().contains(studentId)) {
                courses.add(entry.getKey());
            }
        }
        return courses;
    }

    public java.util.Map<String, Integer> summary() {
        Map<String, Integer> result = new TreeMap<>();
        for (Map.Entry<String, Set<String>> entry : enrollmentMapR26.entrySet()) {
            result.put(entry.getKey(), entry.getValue().size());
        }
        return result;
    }
}
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class EnrollmentSetSystem {

    // 課程報名類別
    public static class Enrollment {
        private final String studentId;
        private final String courseCode;

        public Enrollment(String studentId, String courseCode) {
            this.studentId = studentId;
            this.courseCode = courseCode;
        }

        public String getStudentId() {
            return studentId;
        }

        public String getCourseCode() {
            return courseCode;
        }

        // 以 studentId 與 courseCode 共同作為身分識別依據
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Enrollment that = (Enrollment) o;
            return Objects.equals(studentId, that.studentId) &&
                   Objects.equals(courseCode, that.courseCode);
        }

        // 確保 equals 相等的物件具有相同的 hashCode
        @Override
        public int hashCode() {
            return Objects.hash(studentId, courseCode);
        }

        @Override
        public String toString() {
            return String.format("Enrollment[StudentID=%s, CourseCode=%s]", studentId, courseCode);
        }
    }

    public static void main(String[] args) {
        Set<Enrollment> enrollmentSet = new HashSet<>();

        System.out.println("=== 1. Testing Addition & Duplication ===");
        
        // 建立基本報名物件
        Enrollment e1 = new Enrollment("S001", "CS101");
        
        // 新增第一筆課程報名
        boolean isAdded1 = enrollmentSet.add(e1);
        System.out.println("Add S001 to CS101: " + isAdded1);

        // 同一人可加入不同課程
        Enrollment e2 = new Enrollment("S001", "CS102");
        boolean isAdded2 = enrollmentSet.add(e2);
        System.out.println("Add S001 to CS102 (Different Course): " + isAdded2);

        // 同一人不可重複加入同一課程 (傳入相同物件或實體不同但身分相同的物件)
        Enrollment e3 = new Enrollment("S001", "CS101");
        boolean isAdded3 = enrollmentSet.add(e3);
        System.out.println("Add S001 to CS101 again (Duplicate): " + isAdded3);

        System.out.println("\nCurrent Total Enrollments: " + enrollmentSet.size());

        System.out.println("\n=== 2. Testing contains() and remove() with NEW Objects ===");

        // 建立全新實體物件，但 studentId 與 courseCode 與原資料相同
        Enrollment testTarget = new Enrollment("S001", "CS101");

        // 使用新物件測試 contains()
        boolean containsResult = enrollmentSet.contains(testTarget);
        System.out.println("Contains new Enrollment(\"S001\", \"CS101\"): " + containsResult);

        // 使用新物件測試 remove()
        boolean removeResult = enrollmentSet.remove(testTarget);
        System.out.println("Remove new Enrollment(\"S001\", \"CS101\"): " + removeResult);

        // 再次測試 contains() 確認是否成功刪除
        boolean containsAfterRemove = enrollmentSet.contains(testTarget);
        System.out.println("Contains after remove: " + containsAfterRemove);

        System.out.println("\nFinal Total Enrollments: " + enrollmentSet.size());
    }
}
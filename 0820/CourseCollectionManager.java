import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CourseCollectionManager {

    // 學生報名紀錄實體類別
    public static class StudentRecord {
        private final String studentId;
        private final String name;
        private final String tag; // 可能為空白
        private int score;

        public StudentRecord(String studentId, String name, String tag, int score) {
            this.studentId = studentId;
            this.name = name;
            this.tag = (tag == null) ? "" : tag.trim();
            this.score = score;
        }

        public String getStudentId() {
            return studentId;
        }

        public String getName() {
            return name;
        }

        public String getTag() {
            return tag;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            StudentRecord record = (StudentRecord) o;
            return studentId != null ? studentId.equals(record.studentId) : record.studentId == null;
        }

        @Override
        public int hashCode() {
            return studentId != null ? studentId.hashCode() : 0;
        }

        @Override
        public String toString() {
            return String.format("StudentRecord[ID=%s, Name=%s, Tag='%s', Score=%d]", studentId, name, tag, score);
        }
    }

    // 課程管理系統核心類別
    public static class CourseSystem {
        private final List<StudentRecord> recordList = new ArrayList<>();
        private final Set<StudentRecord> recordSet = new HashSet<>();
        private final Map<String, StudentRecord> recordMap = new HashMap<>();

        // 新增學生紀錄（若學號已存在則忽略）
        public boolean addRecord(StudentRecord record) {
            if (record == null || recordMap.containsKey(record.getStudentId())) {
                return false;
            }
            recordList.add(record);
            recordSet.add(record);
            recordMap.put(record.getStudentId(), record);
            return true;
        }

        // 1. 更新分數
        public boolean updateScore(String studentId, int score) {
            StudentRecord record = recordMap.get(studentId);
            if (record != null) {
                record.setScore(score);
                return true;
            }
            return false;
        }

        // 2. 依 Tag 查詢學生名單
        public List<StudentRecord> findByTag(String tag) {
            List<StudentRecord> result = new ArrayList<>();
            String targetTag = (tag == null) ? "" : tag.trim();

            for (StudentRecord record : recordList) {
                if (record.getTag().equalsIgnoreCase(targetTag)) {
                    result.add(record);
                }
            }
            return result;
        }

        // 3. 統計成績等第分佈 (A: >= 90, B: 80-89, C: 70-79, D: 60-69, F: < 60)
        public Map<String, Integer> scoreDistribution() {
            Map<String, Integer> distMap = new HashMap<>();
            distMap.put("A", 0);
            distMap.put("B", 0);
            distMap.put("C", 0);
            distMap.put("D", 0);
            distMap.put("F", 0);

            for (StudentRecord record : recordList) {
                int s = record.getScore();
                String grade;
                if (s >= 90) grade = "A";
                else if (s >= 80) grade = "B";
                else if (s >= 70) grade = "C";
                else if (s >= 60) grade = "D";
                else grade = "F";

                distMap.put(grade, distMap.get(grade) + 1);
            }
            return distMap;
        }

        // 4. 取得前 N 名學生（依分數降冪，同分時依學號升冪）
        public List<StudentRecord> top(int count) {
            List<StudentRecord> sortedList = new ArrayList<>(recordList);
            sortedList.sort(Comparator.comparingInt(StudentRecord::getScore).reversed()
                    .thenComparing(StudentRecord::getStudentId));

            if (count >= sortedList.size()) {
                return sortedList;
            }
            return sortedList.subList(0, Math.max(0, count));
        }

        // 5. 移除分數低於 minimum 的紀錄，並保持 List, Set, Map 一致
        public void removeBelow(int minimum) {
            Iterator<StudentRecord> iterator = recordList.iterator();
            while (iterator.hasNext()) {
                StudentRecord record = iterator.next();
                if (record.getScore() < minimum) {
                    recordSet.remove(record);
                    recordMap.remove(record.getStudentId());
                    iterator.remove();
                }
            }
        }

        public void printState() {
            System.out.println("List Size: " + recordList.size());
            System.out.println("Set Size:  " + recordSet.size());
            System.out.println("Map Size:  " + recordMap.size());
            System.out.println("List Items:");
            for (StudentRecord r : recordList) {
                System.out.println("  " + r);
            }
        }
    }

    public static void main(String[] args) {
        CourseSystem system = new CourseSystem();

        System.out.println("=== 1. Adding Records (Including duplicates & empty tags) ===");
        // 至少 6 筆報名嘗試，包含重複學號 (S001)、同分 (85分: S002與S005)、空白 Tag
        StudentRecord r1 = new StudentRecord("S001", "Alice", "Java", 92);
        StudentRecord r2 = new StudentRecord("S002", "Bob", "Python", 85);
        StudentRecord r3 = new StudentRecord("S003", "Charlie", "", 55);       // 空白 Tag
        StudentRecord r4 = new StudentRecord("S004", "David", "  ", 72);       // 全空白 Tag
        StudentRecord r5 = new StudentRecord("S005", "Eva", "Java", 85);        // 與 S002 同分
        StudentRecord r6 = new StudentRecord("S006", "Frank", "Python", 48);
        StudentRecord r1Dup = new StudentRecord("S001", "AliceDuplicate", "Java", 100); // 重複學號

        system.addRecord(r1);
        system.addRecord(r2);
        system.addRecord(r3);
        system.addRecord(r4);
        system.addRecord(r5);
        system.addRecord(r6);
        boolean duplicateAdded = system.addRecord(r1Dup);

        System.out.println("Add Duplicate S001 Result: " + duplicateAdded);
        system.printState();

        // 2. 測試 updateScore
        System.out.println("\n=== 2. Testing updateScore() ===");
        System.out.println("Update S003 score to 65: " + system.updateScore("S003", 65));

        // 3. 測試 findByTag
        System.out.println("\n=== 3. Testing findByTag() ===");
        System.out.println("Find tag 'Java': " + system.findByTag("Java"));
        System.out.println("Find tag '' (empty): " + system.findByTag(""));

        // 4. 測試 scoreDistribution
        System.out.println("\n=== 4. Testing scoreDistribution() ===");
        Map<String, Integer> dist = system.scoreDistribution();
        System.out.println("Distribution: " + dist);

        // 5. 測試 top(count)
        System.out.println("\n=== 5. Testing top(3) & top(10) ===");
        System.out.println("Top 3:");
        for (StudentRecord r : system.top(3)) {
            System.out.println("  " + r);
        }
        System.out.println("Top 10 (exceeds size):");
        for (StudentRecord r : system.top(10)) {
            System.out.println("  " + r);
        }

        // 6. 測試 removeBelow 並檢查三種集合一致性
        System.out.println("\n=== 6. Testing removeBelow(60) ===");
        system.removeBelow(60);
        system.printState();
    }
}
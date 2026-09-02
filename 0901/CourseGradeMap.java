import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseGradeMap {
    private final Map<String, List<Integer>> gradesMap = new HashMap<>();

    // 新增成績，若課號不存在則自動建立 List
    public void addGrade(String courseCode, int grade) {
        if (courseCode == null || courseCode.isBlank()) return;
        
        gradesMap.computeIfAbsent(courseCode, k -> new ArrayList<>()).add(grade);
    }

    // 計算平均分數
    public double getAverage(String courseCode) {
        List<Integer> grades = gradesMap.get(courseCode);
        if (grades == null || grades.isEmpty()) return 0.0;
        
        int sum = 0;
        for (int g : grades) {
            sum += g;
        }
        return (double) sum / grades.size();
    }

    // 取得最高分
    public Integer getMaxScore(String courseCode) {
        List<Integer> grades = gradesMap.get(courseCode);
        if (grades == null || grades.isEmpty()) return null;
        
        return Collections.max(grades);
    }

    // 依課號排序並輸出報告
    public void printSortedReport() {
        // 取出所有課號並排序
        List<String> sortedCourses = new ArrayList<>(gradesMap.keySet());
        Collections.sort(sortedCourses);

        System.out.println("--- course grade report ---");
        for (String course : sortedCourses) {
            List<Integer> grades = gradesMap.get(course);
            System.out.printf("course=%s grades=%s avg=%.2f max=%d%n",
                    course, grades, getAverage(course), getMaxScore(course));
        }
    }

    public static void main(String[] args) {
        CourseGradeMap report = new CourseGradeMap();
        
        report.addGrade("CS101", 85);
        report.addGrade("CS101", 92);
        report.addGrade("MATH201", 78);
        report.addGrade("MATH201", 88);
        report.addGrade("MATH201", 95);
        report.addGrade("ENG101", 80);

        report.printSortedReport();
    }
}
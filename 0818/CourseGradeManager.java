public class CourseGradeManager {

    // 內部類別：課程成績 (CourseGrade)
    public static class CourseGrade {
        private String studentId;
        private String name;
        private double dailyScore;   // 平時成績 (50%)
        private double midtermScore; // 期中成績 (20%)
        private double finalScore;   // 期末成績 (20%)
        private double attendance;   // 出席成績 (10%)

        // 建構子 (Constructor)
        public CourseGrade(String studentId, String name, double dailyScore, double midtermScore, double finalScore, double attendance) {
            this.studentId = studentId;
            this.name = name;
            // 使用內建的 clamp 邏輯將成績限制在 0 ~ 100 之間
            this.dailyScore = sanitizeScore(dailyScore);
            this.midtermScore = sanitizeScore(midtermScore);
            this.finalScore = sanitizeScore(finalScore);
            this.attendance = sanitizeScore(attendance);
        }

        // 成績值範圍校正防護：低於 0 為 0，高於 100 為 100
        private double sanitizeScore(double score) {
            if (score < 0) return 0;
            if (score > 100) return 100;
            return score;
        }

        // 計算學期總分：平時 50%、期中 20%、期末 20%、出席 10%
        public double calculateFinalScore() {
            return (dailyScore * 0.50) + (midtermScore * 0.20) + (finalScore * 0.20) + (attendance * 0.10);
        }

        // 依總分回傳等第：A、B、C、D 或 F
        public String getLevel() {
            double total = calculateFinalScore();
            if (total >= 90) return "A";
            if (total >= 80) return "B";
            if (total >= 70) return "C";
            if (total >= 60) return "D";
            return "F";
        }

        public String getStudentId() {
            return studentId;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return String.format("ID: %s | Name: %-8s | Total: %5.1f | Grade: %s",
                    studentId, name, calculateFinalScore(), getLevel());
        }
    }

    // 主程式區
    public static void main(String[] args) {
        // 1. 初始化物件陣列保存至少 5 筆學生資料
        CourseGrade[] grades = new CourseGrade[] {
            new CourseGrade("S001", "Alice",   85, 90, 88, 100), // 高分
            new CourseGrade("S002", "Bob",     40, 50, 45, 60),  // 不及格 (< 60)
            new CourseGrade("S003", "Charlie", 70, 75, 80, 90),
            new CourseGrade("S004", "David",   30, 40, 35, 50),  // 不及格 (< 60)
            new CourseGrade("S005", "Eve",     95, 92, 96, 100)  // 最高分
        };

        // 任務一：輸出所有學生成績摘要
        System.out.println("=== 1. All Student Summary ===");
        for (CourseGrade student : grades) {
            System.out.println(student);
        }
        System.out.println();

        // 任務二：計算班級平均總分
        double totalSum = 0;
        for (CourseGrade student : grades) {
            totalSum += student.calculateFinalScore();
        }
        double classAverage = totalSum / grades.length;
        
        System.out.println("=== 2. Class Statistics ===");
        System.out.printf("Class Average Score: %.2f\n\n", classAverage);

        // 任務三：找出最高分的學生
        CourseGrade topStudent = grades[0];
        for (int i = 1; i < grades.length; i++) {
            if (grades[i].calculateFinalScore() > topStudent.calculateFinalScore()) {
                topStudent = grades[i];
            }
        }
        System.out.println("=== 3. Top Student ===");
        System.out.println("Top Student: " + topStudent.getName() + 
                           " (Score: " + String.format("%.1f", topStudent.calculateFinalScore()) + 
                           ", Grade: " + topStudent.getLevel() + ")");
        System.out.println();

        // 任務四：輸出不及格名單 (總分 < 60，即 Grade 為 F)
        System.out.println("=== 4. Failed Students List (Score < 60 / Grade F) ===");
        for (CourseGrade student : grades) {
            if (student.calculateFinalScore() < 60) {
                System.out.println("Failed: " + student.getName() + 
                                   " [ID: " + student.getStudentId() + "]" +
                                   " - Final Score: " + String.format("%.1f", student.calculateFinalScore()));
            }
        }
    }
}
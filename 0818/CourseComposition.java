public class CourseComposition {

    // 授課者類別 (Instructor)
    public static class Instructor {
        private String id;
        private String name;

        public Instructor(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    // 課程類別 (Course)
    public static class Course {
        private String courseCode;
        private String title;
        private Instructor instructor; // Composition: 引用 Instructor 物件

        public Course(String courseCode, String title, Instructor instructor) {
            this.courseCode = courseCode;
            this.title = title;
            this.instructor = instructor;
        }

        // 回傳完整課程資訊 (透過 composition 取得 instructor 的資訊)
        public String summary() {
            return String.format("Course: [%s] %s | Instructor: %s (%s)",
                    courseCode, 
                    title, 
                    instructor.getName(), 
                    instructor.getId());
        }
    }

    // 主程式測試區
    public static void main(String[] args) {
        // 1. 建立一個 Instructor 物件 (老師)
        Instructor profJohn = new Instructor("INS001", "Dr. John");

        // 2. 建立兩門課程，並將同一個 Instructor 物件傳入 (共用授課者)
        Course javaCourse = new Course("CS101", "Java Programming", profJohn);
        Course dbCourse = new Course("CS201", "Database Systems", profJohn);

        // 3. 測試輸出 summary()
        System.out.println("=== 課程清單 ===");
        System.out.println(javaCourse.summary());
        System.out.println(dbCourse.summary());
    }
}
import java.util.ArrayList;
import java.util.List;

// 1. 領域物件 (Domain Object)
class Course {
    final String courseCode; // Key，物件存續期間保持不變[cite: 1]
    String courseName;
    int credit;

    public Course(String courseCode, String courseName, int credit) {
        if (credit < 1 || credit > 6) {
            throw new IllegalArgumentException("Error: Credit for " + courseCode + " must be between 1 and 6.");
        }
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credit = credit;
    }

    @Override
    public String toString() {
        return "[Code: " + courseCode + " | Name: " + courseName + " | Credit: " + credit + "]";
    }
}

// 2. BST 樹節點
class CourseNode {
    Course data;
    CourseNode left;
    CourseNode right;

    public CourseNode(Course data) {
        this.data = data;
    }
}

// 3. 索引系統
public class CourseBstIndex {
    
    private CourseNode root;

    // --- 新增課程 (Add) ---
    public boolean add(Course course) {
        if (course == null) return false;
        
        if (root == null) {
            root = new CourseNode(course);
            System.out.println("Success: Added " + course.courseCode);
            return true;
        }

        CourseNode current = root;
        while (true) {
            int cmp = course.courseCode.compareTo(current.data.courseCode);
            if (cmp == 0) {
                System.out.println("Error: Course code '" + course.courseCode + "' already exists.");
                return false; // 拒絕重複的 Key[cite: 1]
            }
            if (cmp < 0) {
                if (current.left == null) {
                    current.left = new CourseNode(course);
                    System.out.println("Success: Added " + course.courseCode);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new CourseNode(course);
                    System.out.println("Success: Added " + course.courseCode);
                    return true;
                }
                current = current.right;
            }
        }
    }

    // --- 尋找課程 (Find) ---
    public Course find(String courseCode) {
        CourseNode current = root;
        while (current != null) {
            int cmp = courseCode.compareTo(current.data.courseCode);
            if (cmp == 0) {
                return current.data;
            }
            // 利用 decision path 向下尋找[cite: 1]
            current = (cmp < 0) ? current.left : current.right;
        }
        return null; // Missing case[cite: 1]
    }

    // --- 更新學分 (Update Credit) ---
    public boolean updateCredit(String courseCode, int newCredit) {
        if (newCredit < 1 || newCredit > 6) {
            System.out.println("Error: Credit must be between 1 and 6.");
            return false;
        }

        Course course = find(courseCode);
        if (course == null) {
            System.out.println("Error: Course '" + courseCode + "' not found.");
            return false;
        }

        course.credit = newCredit;
        System.out.println("Success: Updated credit for " + courseCode + " to " + newCredit);
        return true;
    }

    // --- 移除課程 (Remove) ---
    public boolean remove(String courseCode) {
        if (find(courseCode) == null) {
            System.out.println("Error: Cannot remove. Course '" + courseCode + "' not found.");
            return false;
        }
        root = removeHelper(root, courseCode);
        System.out.println("Success: Removed course " + courseCode);
        return true;
    }

    private CourseNode removeHelper(CourseNode node, String courseCode) {
        if (node == null) return null;

        int cmp = courseCode.compareTo(node.data.courseCode);

        if (cmp < 0) {
            node.left = removeHelper(node.left, courseCode);
        } else if (cmp > 0) {
            node.right = removeHelper(node.right, courseCode);
        } else {
            // Case 1 & 2: Leaf node 或 One-child node[cite: 1]
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            
            // Case 3: Two-children node (尋找右子樹最小值作為 successor)[cite: 1]
            CourseNode successor = minimumNode(node.right);
            node.data = successor.data; 
            node.right = removeHelper(node.right, successor.data.courseCode); // 刪除原 successor[cite: 1]
        }
        return node;
    }

    private CourseNode minimumNode(CourseNode node) {
        while (node.left != null) node = node.left;
        return node;
    }

    // --- 範圍查詢 (Range Query) ---
    public void queryRange(String low, String high) {
        // 防呆：確保 low <= high
        if (low.compareTo(high) > 0) {
            String temp = low;
            low = high;
            high = temp;
        }

        System.out.println("--- Range Query [" + low + " ~ " + high + "] ---");
        rangeHelper(root, low, high);
        System.out.println("----------------------------------------");
    }

    private void rangeHelper(CourseNode node, String low, String high) {
        if (node == null) return;

        // 剪枝：當前值大於 low，才往左子樹找[cite: 1]
        if (node.data.courseCode.compareTo(low) > 0) {
            rangeHelper(node.left, low, high);
        }

        // 包含端點 (inclusive boundary)[cite: 1]
        if (node.data.courseCode.compareTo(low) >= 0 && node.data.courseCode.compareTo(high) <= 0) {
            System.out.println(node.data);
        }

        // 剪枝：當前值小於 high，才往右子樹找[cite: 1]
        if (node.data.courseCode.compareTo(high) < 0) {
            rangeHelper(node.right, low, high);
        }
    }

    // --- 排序報表 (Sorted Report) ---
    public void printSortedReport() {
        System.out.println("=== Course Index Report (Sorted by Code) ===");
        if (root == null) {
            System.out.println("[Index is empty]");
        } else {
            inorderHelper(root);
        }
        System.out.println("============================================");
    }

    private void inorderHelper(CourseNode node) {
        if (node == null) return;
        inorderHelper(node.left); // Inorder 走訪產生升冪排序結果[cite: 1]
        System.out.println(node.data);
        inorderHelper(node.right);
    }

    // --- 測試主程式 ---
    public static void main(String[] args) {
        CourseBstIndex index = new CourseBstIndex();
        
        System.out.println("=== 1. Add Courses ===");
        index.add(new Course("CS101", "Introduction to Programming", 3));
        index.add(new Course("MATH201", "Calculus I", 4));
        index.add(new Course("ENG101", "English Composition", 2));
        index.add(new Course("PHYS301", "Quantum Mechanics", 3));
        index.add(new Course("CS202", "Data Structures", 3));
        
        // 測試重複代碼防呆
        index.add(new Course("CS101", "Duplicate Course", 3));
        
        // 測試非法學分建立 (預期拋出例外)
        try {
            index.add(new Course("ART101", "Art History", 7)); 
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        System.out.println();

        System.out.println("=== 2. Find & Update Credit ===");
        // 正常更新
        index.updateCredit("CS202", 4);
        // 測試非法學分更新
        index.updateCredit("MATH201", 0);
        // 找尋並印出
        System.out.println("Find ENG101: " + index.find("ENG101"));
        System.out.println();

        System.out.println("=== 3. Range Query ===");
        // 查詢字首介於 "CS" 到 "MATH" 之間的課程
        index.queryRange("CS000", "MATH999");
        System.out.println();

        System.out.println("=== 4. Remove Course ===");
        // 移除具有兩個子節點的情況 (假設結構為標準樹，具體視插入順序而定)
        index.remove("CS101");
        System.out.println();

        System.out.println("=== 5. Final Sorted Report ===");
        index.printSortedReport();
    }
}
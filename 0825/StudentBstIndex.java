// 學生資料物件
class Student {
    String studentId;
    String name;

    public Student(String studentId, String name) {
        this.studentId = studentId;
        this.name = name;
    }

    @Override
    public String toString() {
        return "[ID: " + studentId + ", Name: " + name + "]";
    }
}

// 樹狀結構節點
class StudentNode {
    Student student;
    StudentNode left;
    StudentNode right;

    public StudentNode(Student student) {
        this.student = student;
    }
}

public class StudentBstIndex {

    private StudentNode root;

    public StudentBstIndex() {
        this.root = null;
    }

    // 1. 新增學生 (Insert) - Wrapper
    public void insert(Student student) {
        root = insertHelper(root, student);
    }

    // 新增邏輯 (Helper)
    private StudentNode insertHelper(StudentNode node, Student newStudent) {
        // Base case: 找到空位，建立新節點
        if (node == null) {
            System.out.println("Success: Inserted student " + newStudent.studentId);
            return new StudentNode(newStudent);
        }

        // 比較學號大小 (使用 String 的 compareTo)
        int cmp = newStudent.studentId.compareTo(node.student.studentId);

        if (cmp < 0) {
            node.left = insertHelper(node.left, newStudent); // 較小往左
        } else if (cmp > 0) {
            node.right = insertHelper(node.right, newStudent); // 較大往右
        } else {
            // cmp == 0 代表 ID 已經存在，拒絕加入
            System.out.println("Error: Student ID '" + newStudent.studentId + "' already exists. Insertion rejected.");
        }
        return node;
    }

    // 2. 搜尋學生 (Search) - Wrapper
    public Student search(String studentId) {
        return searchHelper(root, studentId);
    }

    // 搜尋邏輯 (Helper)
    private Student searchHelper(StudentNode node, String studentId) {
        if (node == null) {
            return null; // 找不到
        }

        int cmp = studentId.compareTo(node.student.studentId);

        if (cmp < 0) {
            return searchHelper(node.left, studentId);
        } else if (cmp > 0) {
            return searchHelper(node.right, studentId);
        } else {
            return node.student; // 找到目標
        }
    }

    // 3. 刪除學生 (Delete) - Wrapper
    public void delete(String studentId) {
        // 先確認目標是否存在，以利輸出正確提示
        if (search(studentId) == null) {
            System.out.println("Error: Cannot delete. Student ID '" + studentId + "' not found.");
            return;
        }
        root = deleteHelper(root, studentId);
        System.out.println("Success: Deleted student " + studentId);
    }

    // 刪除邏輯 (Helper)
    private StudentNode deleteHelper(StudentNode node, String studentId) {
        if (node == null) return null;

        int cmp = studentId.compareTo(node.student.studentId);

        if (cmp < 0) {
            node.left = deleteHelper(node.left, studentId);
        } else if (cmp > 0) {
            node.right = deleteHelper(node.right, studentId);
        } else {
            // 找到目標節點，處理三種刪除情境
            
            // Case 1 & 2: 葉節點 或 單一子節點
            if (node.left == null) {
                return node.right;
            } else if (node.right == null) {
                return node.left;
            }

            // Case 3: 雙子節點 (尋找右子樹最小值來替換)
            StudentNode minNode = getMin(node.right);
            node.student = minNode.student; // 替換資料
            node.right = deleteHelper(node.right, minNode.student.studentId); // 刪除被提上來的舊節點
        }
        return node;
    }

    // 取得子樹最小值的輔助方法
    private StudentNode getMin(StudentNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    // 4. 中序走訪 (Inorder Traversal) 以驗證排序結果
    public void printInorder() {
        System.out.print("Inorder (Sorted by ID): ");
        inorderHelper(root);
        System.out.println("\n");
    }

    private void inorderHelper(StudentNode node) {
        if (node == null) return;
        inorderHelper(node.left);
        System.out.print(node.student.studentId + " ");
        inorderHelper(node.right);
    }

    // 測試主程式
    public static void main(String[] args) {
        StudentBstIndex bst = new StudentBstIndex();

        System.out.println("=== 1. Insertion & Duplicate Test ===");
        bst.insert(new Student("412630855", "Alice"));
        bst.insert(new Student("412294620", "Bob"));
        bst.insert(new Student("412687457", "Charlie"));
        bst.insert(new Student("412719810", "David"));
        bst.insert(new Student("412445840", "Eve"));
        
        // 測試重複學號
        bst.insert(new Student("412630855", "Alice Clone")); 
        System.out.println();

        System.out.println("=== 2. Current Tree Status ===");
        bst.printInorder();

        System.out.println("=== 3. Search Test ===");
        String target1 = "412630855";
        Student s1 = bst.search(target1);
        System.out.println("Search '" + target1 + "': " + (s1 != null ? "Found " + s1 : "Not Found"));

        String target2 = "999999999";
        Student s2 = bst.search(target2);
        System.out.println("Search '" + target2 + "': " + (s2 != null ? "Found " + s2 : "Not Found"));
        System.out.println();

        System.out.println("=== 4. Deletion Test ===");
        // 刪除不存在的資料
        bst.delete("111111111");
        
        // 刪除葉節點 (Leaf)
        bst.delete("412445840"); 
        
        // 刪除擁有雙子節點的 Root
        bst.delete("412630855"); 
        
        System.out.println("\n--- Status After Deletion ---");
        bst.printInorder();
}
// 任務資料結構
class Task {
    String id;
    String description;

    public Task(String id, String description) {
        this.id = id;
        this.description = description;
    }

    @Override
    public String toString() {
        return "{ID: " + id + ", Desc: " + description + "}";
    }
}

// 單向鏈結清單的節點
class TaskNode {
    Task task;
    TaskNode next;

    public TaskNode(Task task) {
        this.task = task;
        this.next = null; // 預設下一節點為 null
    }
}

// 自訂單向鏈結清單
class TaskLinkedList {
    private TaskNode head;
    private int size;

    public TaskLinkedList() {
        this.head = null;
        this.size = 0;
    }

    // 輔助方法：檢查 ID 是否已存在
    private boolean containsId(String id) {
        TaskNode current = head;
        while (current != null) {
            if (current.task.id.equals(id)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    // 1. 新增至清單最前方
    public void addFirst(Task task) {
        if (containsId(task.id)) {
            System.out.println("Error: Task ID '" + task.id + "' already exists.");
            return;
        }
        TaskNode newNode = new TaskNode(task);
        newNode.next = head;
        head = newNode;
        size++;
        System.out.println("Success: Task '" + task.id + "' added to the front.");
    }

    // 2. 新增至清單最後方
    public void addLast(Task task) {
        if (containsId(task.id)) {
            System.out.println("Error: Task ID '" + task.id + "' already exists.");
            return;
        }
        TaskNode newNode = new TaskNode(task);
        if (head == null) {
            head = newNode;
        } else {
            TaskNode current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
        System.out.println("Success: Task '" + task.id + "' added to the end.");
    }

    // 3. 依 ID 尋找任務
    public Task findById(String id) {
        TaskNode current = head;
        while (current != null) {
            if (current.task.id.equals(id)) {
                return current.task;
            }
            current = current.next;
        }
        return null;
    }

    // 4. 在指定 ID 的節點後方插入新節點
    public void insertAfter(String existingId, Task task) {
        if (containsId(task.id)) {
            System.out.println("Error: Task ID '" + task.id + "' already exists.");
            return;
        }
        
        TaskNode current = head;
        while (current != null) {
            if (current.task.id.equals(existingId)) {
                TaskNode newNode = new TaskNode(task);
                newNode.next = current.next;
                current.next = newNode;
                size++;
                System.out.println("Success: Task '" + task.id + "' inserted after '" + existingId + "'.");
                return;
            }
            current = current.next;
        }
        System.out.println("Error: Target ID '" + existingId + "' not found for insertion.");
    }

    // 5. 依 ID 刪除節點
    public void removeById(String id) {
        // 處理空清單情境
        if (head == null) {
            System.out.println("Error: List is empty. Cannot remove '" + id + "'.");
            return;
        }

        // 處理刪除目標為 Head (頭部)
        if (head.task.id.equals(id)) {
            head = head.next;
            size--;
            System.out.println("Success: Removed head node -> " + id);
            return;
        }

        // 尋找目標並處理 Middle (中間) 或 Tail (尾部) 的刪除
        TaskNode current = head;
        while (current.next != null) {
            if (current.next.task.id.equals(id)) {
                boolean isTail = (current.next.next == null);
                current.next = current.next.next; // 跳過目標節點，將指標連接到下一個
                size--;
                
                if (isTail) {
                    System.out.println("Success: Removed tail node -> " + id);
                } else {
                    System.out.println("Success: Removed middle node -> " + id);
                }
                return;
            }
            current = current.next;
        }
        
        // 處理找不到 ID 的情境
        System.out.println("Error: Task ID '" + id + "' not found for removal.");
    }

    // 6. 取得清單長度
    public int size() {
        return size;
    }

    // 7. 印出所有節點
    public void printAll() {
        System.out.println("--- Current List (Size: " + size + ") ---");
        if (head == null) {
            System.out.println("[Empty List]");
            return;
        }
        TaskNode current = head;
        while (current != null) {
            System.out.print("[" + current.task.id + "]");
            if (current.next != null) {
                System.out.print(" -> ");
            }
            current = current.next;
        }
        System.out.println("\n--------------------------------");
    }
}

// 測試主程式
public class LinkedTaskListSystem {
    public static void main(String[] args) {
        TaskLinkedList list = new TaskLinkedList();

        System.out.println("=== Test 1: Empty List Removal & Not Found ===");
        list.removeById("T001"); // 測試空 list 刪除
        list.printAll();
        
        System.out.println("\n=== Test 2: Add Nodes & Duplicate Check ===");
        list.addLast(new Task("T001", "Design DB"));
        list.addLast(new Task("T002", "Write API"));
        list.addFirst(new Task("T000", "Setup Server"));
        list.addLast(new Task("T002", "Duplicate Task")); // 測試重複加入
        list.insertAfter("T001", new Task("T001-A", "Design Table Schema"));
        
        list.printAll();
        
        System.out.println("\n=== Test 3: Find By ID ===");
        Task found = list.findById("T001");
        if (found != null) {
            System.out.println("Found task: " + found.toString());
        }
        
        System.out.println("\n=== Test 4: Remove Tail ===");
        list.removeById("T002"); // T002 目前在尾部
        list.printAll();

        System.out.println("\n=== Test 5: Remove Middle ===");
        list.removeById("T001"); // T001 目前在中間
        list.printAll();

        System.out.println("\n=== Test 6: Remove Head ===");
        list.removeById("T000"); // T000 目前在頭部
        list.printAll();
        
        System.out.println("\n=== Test 7: Remove ID Not Found ===");
        list.removeById("T999"); // 測試找不到 ID
    }
}
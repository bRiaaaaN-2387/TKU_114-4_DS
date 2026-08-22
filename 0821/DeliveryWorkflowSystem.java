import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

// 定義物流配送物件
class Delivery {
    String id;
    String destination;
    String status;

    public Delivery(String id, String destination) {
        this.id = id;
        this.destination = destination;
        this.status = "PENDING"; // 預設狀態為等待中
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Dest: " + destination + ", Status: " + status;
    }
}

public class DeliveryWorkflowSystem {
    
    // 使用 Map 依編號快速查詢
    private Map<String, Delivery> deliveryMap;
    // 使用 Queue 保存等待配送的項目
    private Queue<Delivery> pendingQueue;
    // 使用 Stack 保存已完成並允許復原的歷程
    private Stack<Delivery> completedStack;

    // 建構子初始化資料結構
    public DeliveryWorkflowSystem() {
        deliveryMap = new HashMap<>();
        pendingQueue = new LinkedList<>();
        completedStack = new Stack<>();
    }

    // 1. 新增配送
    public void addDelivery(String id, String destination) {
        // 檢查是否有重複的 id
        if (deliveryMap.containsKey(id)) {
            System.out.println("Error: Delivery ID '" + id + "' already exists!");
            return;
        }
        
        Delivery newDelivery = new Delivery(id, destination);
        deliveryMap.put(id, newDelivery);
        pendingQueue.offer(newDelivery); // 加入等待佇列
        
        System.out.println("Success: Delivery '" + id + "' added to pending list.");
    }

    // 2. 處理配送
    public void processDelivery() {
        // 檢查佇列是否為空
        if (pendingQueue.isEmpty()) {
            System.out.println("Info: No pending deliveries to process.");
            return;
        }
        
        // 取出佇列最前方的物件並更新狀態
        Delivery currentDelivery = pendingQueue.poll();
        currentDelivery.status = "COMPLETED";
        completedStack.push(currentDelivery); // 存入已完成堆疊
        
        System.out.println("Success: Delivery '" + currentDelivery.id + "' processed.");
    }

    // 3. 復原上一步 (Undo)
    public void undoLastDelivery() {
        // 檢查堆疊是否有可復原的項目
        if (completedStack.isEmpty()) {
            System.out.println("Info: No completed deliveries to undo.");
            return;
        }
        
        // 從堆疊取出最後完成的項目
        Delivery lastCompleted = completedStack.pop();
        lastCompleted.status = "PENDING";
        
        // 將復原的項目加回佇列最前方 (使用 LinkedList 的 addFirst 特性)
        ((LinkedList<Delivery>) pendingQueue).addFirst(lastCompleted);
        
        System.out.println("Success: Delivery '" + lastCompleted.id + "' undone and moved back to pending.");
    }

    // 4. 查詢特定配送
    public void queryDelivery(String id) {
        Delivery target = deliveryMap.get(id);
        if (target == null) {
            System.out.println("Result: Delivery ID '" + id + "' not found.");
        } else {
            System.out.println("Result: [" + target.toString() + "]");
        }
    }

    // 5. 顯示統計數據
    public void printStatistics() {
        System.out.println("=== Delivery Statistics ===");
        System.out.println("Total Deliveries : " + deliveryMap.size());
        System.out.println("Pending (Queue)  : " + pendingQueue.size());
        System.out.println("Completed (Stack): " + completedStack.size());
        System.out.println("===========================");
    }

    // 主程式 (CLI 互動選單)
    public static void main(String[] args) {
        DeliveryWorkflowSystem system = new DeliveryWorkflowSystem();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n--- Delivery Workflow Menu ---");
            System.out.println("1. Add Delivery");
            System.out.println("2. Process Delivery");
            System.out.println("3. Undo Last Delivery");
            System.out.println("4. Query Delivery");
            System.out.println("5. Print Statistics");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Enter Delivery ID: ");
                    String id = scanner.nextLine();
                    System.out.print("Enter Destination: ");
                    String dest = scanner.nextLine();
                    system.addDelivery(id, dest);
                    break;
                case "2":
                    system.processDelivery();
                    break;
                case "3":
                    system.undoLastDelivery();
                    break;
                case "4":
                    System.out.print("Enter Delivery ID to query: ");
                    String queryId = scanner.nextLine();
                    system.queryDelivery(queryId);
                    break;
                case "5":
                    system.printStatistics();
                    break;
                case "0":
                    System.out.println("Exiting system. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Error: Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }
}
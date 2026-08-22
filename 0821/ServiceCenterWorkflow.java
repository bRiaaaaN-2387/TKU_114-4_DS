import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

// 服務票券物件
class ServiceTicket {
    String id;
    String description;
    String status;

    public ServiceTicket(String id, String description) {
        this.id = id;
        this.description = description;
        this.status = "WAITING"; // 預設狀態為等待中
    }

    @Override
    public String toString() {
        return "Ticket[ID=" + id + ", Desc=" + description + ", Status=" + status + "]";
    }
}

public class ServiceCenterWorkflow {

    // 依 ticket id 快速查詢
    private Map<String, ServiceTicket> ticketMap;
    // 作為等待處理的 Queue (FIFO)
    private Deque<ServiceTicket> waitingQueue;
    // 作為已處理歷程的 Stack (LIFO)
    private Deque<ServiceTicket> completedStack;
    // 專門用於防止重複 id 的集合
    private Set<String> idSet;

    public ServiceCenterWorkflow() {
        ticketMap = new HashMap<>();
        // LinkedList 實作 Deque，方便在取消時從中間移除特定物件
        waitingQueue = new LinkedList<>(); 
        // ArrayDeque 實作 Deque 作為 Stack 效能最佳
        completedStack = new ArrayDeque<>();
        idSet = new HashSet<>();
    }

    // 1. 建立新票券
    public void createTicket(String id, String description) {
        // 使用 Set 檢查並防止重複 id
        if (idSet.contains(id)) {
            System.out.println("Error: Ticket ID '" + id + "' already exists!");
            return;
        }

        ServiceTicket ticket = new ServiceTicket(id, description);
        idSet.add(id);
        ticketMap.put(id, ticket);
        waitingQueue.offerLast(ticket); // 加入等待 Queue 的尾端

        System.out.println("Success: Created -> " + id);
    }

    // 2. 處理下一位等待者
    public void processNext() {
        if (waitingQueue.isEmpty()) {
            System.out.println("Info: Waiting queue is empty. No tickets to process.");
            return;
        }

        // 取出 Queue 最前方的票券
        ServiceTicket ticket = waitingQueue.pollFirst();
        ticket.status = "COMPLETED";
        completedStack.push(ticket); // 推進完成歷程 Stack

        System.out.println("Success: Processed -> " + ticket.id);
    }

    // 3. 取消等待中的票券
    public void cancelWaiting(String id) {
        ServiceTicket ticket = ticketMap.get(id);

        // 檢查票券是否存在，以及是否處於等待狀態
        if (ticket == null) {
            System.out.println("Error: Cannot cancel. Ticket ID '" + id + "' not found.");
            return;
        }
        if (!ticket.status.equals("WAITING")) {
            System.out.println("Error: Cannot cancel. Ticket '" + id + "' is already " + ticket.status + ".");
            return;
        }

        // 從 Queue 中移除並更新狀態
        waitingQueue.remove(ticket);
        ticket.status = "CANCELED";
        System.out.println("Success: Canceled -> " + id);
    }

    // 4. 復原最後一筆完成的票券
    public void undoLastCompletion() {
        if (completedStack.isEmpty()) {
            System.out.println("Info: Completed stack is empty. Nothing to undo.");
            return;
        }

        // 從 Stack 取出最後完成的票券
        ServiceTicket lastCompleted = completedStack.pop();
        lastCompleted.status = "WAITING";
        
        // 依照需求，將復原的票券放回 Queue 的「前端」優先處理
        waitingQueue.offerFirst(lastCompleted);
        
        System.out.println("Success: Undid completion for -> " + lastCompleted.id + ". Moved to front of queue.");
    }

    // 5. 依 ID 查詢
    public void findById(String id) {
        ServiceTicket ticket = ticketMap.get(id);
        if (ticket != null) {
            System.out.println("Query Result: " + ticket.toString());
        } else {
            System.out.println("Query Result: Ticket ID '" + id + "' not found.");
        }
    }

    // 6. 輸出狀態總結
    public void printSummary() {
        System.out.println("\n--- Service Center Summary ---");
        System.out.println("Total Unique IDs : " + idSet.size());
        System.out.println("Waiting Queue    : " + waitingQueue.size());
        System.out.println("Completed Stack  : " + completedStack.size());
        System.out.println("------------------------------\n");
    }

    // 測試主程式
    public static void main(String[] args) {
        ServiceCenterWorkflow center = new ServiceCenterWorkflow();

        System.out.println("=== Test 1: Empty Queue Process ===");
        center.processNext();

        System.out.println("\n=== Test 2: Add Tickets & Duplicate ID ===");
        center.createTicket("TK01", "Password Reset");
        center.createTicket("TK02", "Network Issue");
        center.createTicket("TK03", "Software Install");
        center.createTicket("TK02", "Hardware Repair"); // 測試重複加入

        System.out.println("\n=== Test 3: Cancel Non-existent & Processed ===");
        center.cancelWaiting("TK99"); // 取消不存在的 id
        center.processNext(); // 處理 TK01
        center.cancelWaiting("TK01"); // 嘗試取消已處理的 id

        System.out.println("\n=== Test 4: Cancel Waiting ===");
        center.cancelWaiting("TK03"); // 成功取消 TK03
        
        System.out.println("\n=== Test 5: Process and Double Undo ===");
        center.createTicket("TK04", "Email Setup");
        center.processNext(); // 處理 TK02
        center.processNext(); // 處理 TK04
        
        center.printSummary(); // 檢視 Undo 前的狀態
        
        center.undoLastCompletion(); // 復原 TK04
        center.undoLastCompletion(); // 復原 TK02
        
        System.out.println("\n=== Final Check ===");
        center.findById("TK02");
        center.findById("TK04");
        center.printSummary();
    }
}
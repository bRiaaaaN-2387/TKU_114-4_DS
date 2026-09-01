import java.util.Comparator;
import java.util.PriorityQueue;

public class EmergencyTriageQueue {
    // 儲存病患資料：病歷號、危急程度、到院順序
    record Patient(String id, int severity, long arrivalOrder) {}

    private final PriorityQueue<Patient> queue;
    private long orderCounter = 0; // 記錄到院順序

    public EmergencyTriageQueue() {
        // 多欄位排序：危急程度(大到小) -> 到院順序(小到大) -> 病歷號(字典序)
        Comparator<Patient> comparator = Comparator
                .comparingInt(Patient::severity).reversed()
                .thenComparingLong(Patient::arrivalOrder)
                .thenComparing(Patient::id);
        
        queue = new PriorityQueue<>(comparator);
    }

    // 報到
    public void checkIn(String id, int severity) {
        Patient p = new Patient(id, severity, ++orderCounter);
        queue.offer(p);
        System.out.println("check-in=" + id + " severity=" + severity);
    }

    // 查看下一位
    public void peekNext() {
        Patient next = queue.peek();
        if (next == null) {
            System.out.println("peek=empty queue");
        } else {
            System.out.println("peek=" + next.id() + " severity=" + next.severity());
        }
    }

    // 叫號
    public void callNext() {
        Patient p = queue.poll();
        if (p == null) {
            System.out.println("call=empty queue");
        } else {
            System.out.println("call=" + p.id() + " severity=" + p.severity());
        }
    }

    // 查詢目前人數
    public void printWaitingCount() {
        System.out.println("waiting count=" + queue.size());
    }

    public static void main(String[] args) {
        EmergencyTriageQueue triage = new EmergencyTriageQueue();
        
        // 測試空佇列處理
        triage.callNext(); 
        triage.peekNext();
        
        // 模擬報到
        triage.checkIn("P001", 3);
        triage.checkIn("P002", 5); // 最危急
        triage.checkIn("P003", 3); // 相同危急，但較晚到
        triage.checkIn("P004", 1);
        
        triage.printWaitingCount();
        triage.peekNext();
        
        System.out.println("--- start calling ---");
        while (!triage.queue.isEmpty()) {
            triage.callNext();
        }
        
        // 再次測試空佇列
        triage.callNext();
    }
}
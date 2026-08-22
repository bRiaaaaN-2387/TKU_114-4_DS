import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ClinicQueueSystem {

    // 病患實體類別
    public static class Patient {
        private final String recordId; // 病歷號
        private final String name;     // 姓名

        public Patient(String recordId, String name) {
            this.recordId = recordId;
            this.name = name;
        }

        public String getRecordId() {
            return recordId;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return String.format("Patient[ID: %s, Name: %s]", recordId, name);
        }
    }

    // 使用 LinkedList 實作 Queue，方便進行 FIFO 操作與走訪刪除
    private final Queue<Patient> waitingQueue;
    
    // 使用 List 記錄當日已完成看診的病患
    private final List<Patient> completedList;

    public ClinicQueueSystem() {
        this.waitingQueue = new LinkedList<>();
        this.completedList = new ArrayList<>();
    }

    // 1. 一般掛號 (Enqueue): 將病患加入等候隊列尾端
    public void register(String recordId, String name) {
        Patient newPatient = new Patient(recordId, name);
        waitingQueue.offer(newPatient);
        System.out.println("Action: Registered -> " + newPatient);
    }

    // 2. 取消指定病歷號: 使用 Iterator 尋找並移除特定病患，不影響其他人順序
    public void cancelRegistration(String recordId) {
        Iterator<Patient> iterator = waitingQueue.iterator();
        while (iterator.hasNext()) {
            Patient p = iterator.next();
            if (p.getRecordId().equals(recordId)) {
                iterator.remove();
                System.out.println("Action: Cancelled -> " + p);
                return;
            }
        }
        System.out.println("Action: Cancel Failed -> Record ID " + recordId + " not found in waiting queue.");
    }

    // 3. 叫號 (Dequeue): 依 FIFO 原則呼叫下一位病患，並加入完成清單
    public void callNext() {
        Patient nextPatient = waitingQueue.poll();
        if (nextPatient == null) {
            System.out.println("Action: Call Next -> Failed (No patients waiting).");
        } else {
            System.out.println("Action: Calling -> " + nextPatient + " to the doctor's office.");
            completedList.add(nextPatient);
        }
    }

    // 4. 查看下一位 (Peek): 查看隊列首位但不移除
    public void peekNext() {
        Patient nextPatient = waitingQueue.peek();
        if (nextPatient == null) {
            System.out.println("Status: Peek Next -> Queue is empty.");
        } else {
            System.out.println("Status: Next in line is -> " + nextPatient);
        }
    }

    // 5. 查看當日完成清單
    public void showCompletedList() {
        System.out.println("\n--- Today's Completed List ---");
        if (completedList.isEmpty()) {
            System.out.println("No patients have completed their visits yet.");
        } else {
            for (int i = 0; i < completedList.size(); i++) {
                System.out.println((i + 1) + ". " + completedList.get(i));
            }
        }
        System.out.println("------------------------------");
    }

    // 顯示目前等候人數
    public void showWaitingCount() {
        System.out.println("Status: Patients currently waiting: " + waitingQueue.size());
    }

    public static void main(String[] args) {
        ClinicQueueSystem clinic = new ClinicQueueSystem();
        System.out.println("=== Clinic Queue System Started ===\n");

        // 測試掛號功能
        System.out.println("--- 1. Patient Registration ---");
        clinic.register("M001", "Alice");
        clinic.register("M002", "Bob");
        clinic.register("M003", "Charlie");
        clinic.register("M004", "David");
        clinic.showWaitingCount();

        // 測試查看下一位
        System.out.println("\n--- 2. Peeking Next Patient ---");
        clinic.peekNext();

        // 測試叫號功能 (應為 Alice)
        System.out.println("\n--- 3. Calling Patients (FIFO) ---");
        clinic.callNext();
        clinic.callNext(); // 應為 Bob
        clinic.showWaitingCount();

        // 測試取消特定病患 (取消 M004 David)
        System.out.println("\n--- 4. Patient Cancellation ---");
        clinic.cancelRegistration("M004");
        // 測試取消不存在的病患
        clinic.cancelRegistration("M999");
        clinic.showWaitingCount();

        // 繼續叫號 (應為剩餘的 Charlie)
        System.out.println("\n--- 5. Calling Remaining Patients ---");
        clinic.callNext();
        
        // 測試空隊列叫號
        clinic.callNext();

        // 查看完成名單 (應包含 Alice, Bob, Charlie)
        clinic.showCompletedList();
    }
}
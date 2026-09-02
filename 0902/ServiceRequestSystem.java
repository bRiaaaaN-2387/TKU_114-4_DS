import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class ServiceRequestSystem {
    // 定義請求結構：priority 數值越小代表越優先，數值相同時依 id 字典序
    public record Request(String id, int priority) {}

    private final Map<String, Request> lookupMap = new HashMap<>();
    private final PriorityQueue<Request> priorityQueue;

    public ServiceRequestSystem() {
        Comparator<Request> comparator = Comparator
                .comparingInt(Request::priority)
                .thenComparing(Request::id);
        priorityQueue = new PriorityQueue<>(comparator);
    }

    // 新增請求，若 id 已存在則拒絕以確保資料一致性
    public boolean addRequest(String id, int priority) {
        if (id == null || id.isBlank() || lookupMap.containsKey(id)) {
            return false;
        }
        Request request = new Request(id, priority);
        lookupMap.put(id, request);
        priorityQueue.offer(request);
        return true;
    }

    // 取出並移除最優先的請求，同步更新兩份資料結構
    public Request getNext() {
        Request next = priorityQueue.poll();
        if (next != null) {
            lookupMap.remove(next.id());
        }
        return next;
    }

    // 依 id 取消請求，同步更新兩份資料結構
    public boolean cancelRequest(String id) {
        if (id == null || !lookupMap.containsKey(id)) {
            return false;
        }
        Request request = lookupMap.remove(id);
        return priorityQueue.remove(request);
    }

    // 查詢特定請求的狀態
    public Request checkRequest(String id) {
        return lookupMap.get(id);
    }

    // 輸出系統內部資料筆數，以驗證一致性
    public void printSystemStatus() {
        System.out.println("map size=" + lookupMap.size() + " queue size=" + priorityQueue.size());
    }

    public static void main(String[] args) {
        ServiceRequestSystem system = new ServiceRequestSystem();

        system.addRequest("REQ-001", 3);
        system.addRequest("REQ-002", 1);
        system.addRequest("REQ-003", 2);
        system.addRequest("REQ-004", 1); // 測試 priority 相同，預期 REQ-002 先出

        System.out.println("--- normal addition state ---");
        system.printSystemStatus();

        System.out.println("\n--- retrieving next requests ---");
        System.out.println("next=" + system.getNext());
        System.out.println("next=" + system.getNext());
        system.printSystemStatus();

        System.out.println("\n--- canceling request ---");
        boolean canceled = system.cancelRequest("REQ-001");
        System.out.println("cancel REQ-001=" + canceled);
        System.out.println("check REQ-001=" + system.checkRequest("REQ-001"));
        system.printSystemStatus();

        System.out.println("\n--- missing and empty edge cases ---");
        System.out.println("cancel unknown=" + system.cancelRequest("UNKNOWN"));
        System.out.println("next=" + system.getNext());
        System.out.println("next=" + system.getNext()); // 預期為 null
        system.printSystemStatus();
    }
}
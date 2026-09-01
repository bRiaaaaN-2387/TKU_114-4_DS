import java.util.Comparator;
import java.util.PriorityQueue;

public class EventSimulationQueue {
    // 建立 Event 結構，包含時間、順序與類型
    record Event(long time, int sequence, String type) {}

    private final PriorityQueue<Event> queue;
    private int sequenceCounter = 0; // 產生唯一的 sequence 作為 tie-breaker 與識別碼

    public EventSimulationQueue() {
        // 多欄位排序：先比較時間(小到大) -> 再比較順序(小到大)
        Comparator<Event> comparator = Comparator
                .comparingLong(Event::time)
                .thenComparingInt(Event::sequence);
        
        queue = new PriorityQueue<>(comparator);
    }

    // 排程新事件並回傳 sequence ID 以便後續取消
    public int scheduleEvent(long time, String type) {
        int seq = ++sequenceCounter;
        Event event = new Event(time, seq, type);
        queue.offer(event);
        System.out.println("scheduled=" + type + " time=" + time + " seq=" + seq);
        return seq;
    }

    // 利用 sequence ID 尋找並取消指定事件
    public void cancelEvent(int targetSequence) {
        Event target = null;
        for (Event e : queue) {
            if (e.sequence() == targetSequence) {
                target = e;
                break;
            }
        }

        if (target != null) {
            queue.remove(target); // 自佇列移除該事件
            System.out.println("canceled seq=" + targetSequence + " type=" + target.type());
        } else {
            System.out.println("cancel failed: seq=" + targetSequence + " not found");
        }
    }

    // 依序執行所有事件並輸出紀錄
    public void runSimulation() {
        System.out.println("--- execution record ---");
        if (queue.isEmpty()) {
            System.out.println("no events to execute");
            return;
        }

        while (!queue.isEmpty()) {
            Event e = queue.poll();
            System.out.println("time=" + e.time() + " seq=" + e.sequence() + " type=" + e.type());
        }
    }

    public static void main(String[] args) {
        EventSimulationQueue simulator = new EventSimulationQueue();

        // 安排事件 (時間相同的事件，會依照排入的 sequence 決定先後)
        simulator.scheduleEvent(100, "System Initialization");
        int seqToCancel = simulator.scheduleEvent(500, "Data Backup");
        simulator.scheduleEvent(500, "User Login");
        simulator.scheduleEvent(200, "Network Check");
        simulator.scheduleEvent(800, "System Shutdown");

        // 測試取消事件
        System.out.println("\n--- canceling event ---");
        simulator.cancelEvent(seqToCancel); // 取消 Data Backup
        simulator.cancelEvent(999); // 測試取消不存在的事件

        // 執行模擬
        System.out.println();
        simulator.runSimulation();
    }
}
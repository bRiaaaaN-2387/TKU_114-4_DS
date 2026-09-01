import java.util.Comparator;
import java.util.PriorityQueue;

public class SupportTicketQueue {
    // 建立 Ticket 結構
    record Ticket(String id, int severity, long createdOrder) {}

    public static void main(String[] args) {
        // 定義排序規則：severity 越大越優先，若相同則 createdOrder 越小越優先
        Comparator<Ticket> order = Comparator
                .comparingInt(Ticket::severity).reversed()
                .thenComparingLong(Ticket::createdOrder);

        PriorityQueue<Ticket> queue = new PriorityQueue<>(order);
        
        // 加入測試資料
        queue.offer(new Ticket("ticket-A", 1, 3));
        queue.offer(new Ticket("ticket-B", 3, 2));
        queue.offer(new Ticket("ticket-C", 3, 1));
        queue.offer(new Ticket("ticket-D", 2, 4));

        System.out.println("process order:");
        // 依序取出並列印
        while (!queue.isEmpty()) {
            Ticket t = queue.poll();
            System.out.println(t.id() + "|" + t.severity() + "|" + t.createdOrder());
        }
    }
}
import java.util.ArrayDeque;
import java.util.Deque;

public class CounterWaitingQueue {

    // 顧客實體類別
    public static class Customer {
        private final String name;

        public Customer(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "Customer[" + name + "]";
        }
    }

    // 使用 Deque 作為 FIFO 佇列管理等候顧客
    private final Deque<Customer> queue;

    public CounterWaitingQueue() {
        this.queue = new ArrayDeque<>();
    }

    // 加入：將新顧客排入隊列尾端
    public void enqueue(String customerName) {
        Customer newCustomer = new Customer(customerName);
        queue.offerLast(newCustomer);
        System.out.println("Action: Added " + newCustomer + " to the queue.");
    }

    // 查看下一位：取得隊列首位顧客但不移除
    public void peekNext() {
        Customer nextCustomer = queue.peekFirst();
        if (nextCustomer == null) {
            // 空隊列處理
            System.out.println("Action: Peek next -> Queue is empty.");
        } else {
            System.out.println("Action: Peek next -> " + nextCustomer + " is waiting.");
        }
    }

    // 服務下一位：取得並移除隊列首位顧客
    public void serveNext() {
        Customer servedCustomer = queue.pollFirst();
        if (servedCustomer == null) {
            // 空隊列處理
            System.out.println("Action: Serve next -> Failed. No customers in the queue.");
        } else {
            System.out.println("Action: Served -> " + servedCustomer + ". Have a nice day!");
        }
    }

    // 顯示等候數：印出目前隊列長度
    public void showWaitingCount() {
        System.out.println("Status: Current waiting count = " + queue.size());
    }

    public static void main(String[] args) {
        CounterWaitingQueue counter = new CounterWaitingQueue();

        System.out.println("=== Counter Queue Simulation ===");

        // 1. 測試空隊列處理 (查看與服務)
        System.out.println("\n--- Testing Empty Queue ---");
        counter.peekNext();
        counter.serveNext();
        counter.showWaitingCount();

        // 2. 測試顧客加入隊列
        System.out.println("\n--- Customers Arriving ---");
        counter.enqueue("Alice");
        counter.enqueue("Bob");
        counter.enqueue("Charlie");
        counter.showWaitingCount();

        // 3. 測試查看下一位 (應為 Alice)
        System.out.println("\n--- Checking Next ---");
        counter.peekNext();

        // 4. 測試服務顧客 (FIFO 原則)
        System.out.println("\n--- Serving Customers ---");
        counter.serveNext(); // 服務 Alice
        counter.showWaitingCount();
        counter.serveNext(); // 服務 Bob
        
        // 5. 再次查看下一位與服務最後一位
        System.out.println("\n--- Final Operations ---");
        counter.peekNext();  // 應為 Charlie
        counter.serveNext(); // 服務 Charlie
        counter.showWaitingCount(); // 應為 0
        
        // 6. 再次測試全空後的安全處理
        counter.serveNext();
    }
}
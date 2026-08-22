import java.util.Arrays;

public class CircularQueuePractice {

    // 建立泛型 CircularQueue<T>，底層使用 Object[] 陣列
    public static class CircularQueue<T> {
        private final Object[] elements;
        private int front;
        private int rear;
        private int size;
        private final int capacity;

        // 建構子：初始化指定容量的陣列，並設定指標與大小
        public CircularQueue(int capacity) {
            this.capacity = capacity;
            this.elements = new Object[capacity];
            this.front = 0;
            this.rear = -1; // 初始化為 -1，首次加入時透過 modulo 會變成 0
            this.size = 0;
        }

        // 檢查佇列是否已滿
        public boolean isFull() {
            return size == capacity;
        }

        // 檢查佇列是否為空
        public boolean isEmpty() {
            return size == 0;
        }

        // 尾端加入元素 (Enqueue)
        public boolean enqueue(T item) {
            if (isFull()) {
                System.out.println("Action: Enqueue " + item + " -> Failed (Queue is full)");
                return false;
            }
            // 使用 modulo 計算下一個存放位置，實現循環效果
            rear = (rear + 1) % capacity;
            elements[rear] = item;
            size++;
            printState("Enqueue " + item);
            return true;
        }

        // 首端移除元素 (Dequeue)
        @SuppressWarnings("unchecked")
        public T dequeue() {
            if (isEmpty()) {
                System.out.println("Action: Dequeue -> Failed (Queue is empty)");
                return null;
            }
            T item = (T) elements[front];
            // 將移出的位置設為 null，幫助記憶體回收並利於觀察陣列狀態
            elements[front] = null;
            // 使用 modulo 移動 front 指標，絕不搬移陣列元素
            front = (front + 1) % capacity;
            size--;
            printState("Dequeue (" + item + ")");
            return item;
        }

        // 印出內部狀態：包含實際陣列內容與各項指標
        private void printState(String action) {
            System.out.printf("Action: %-13s | Array: %-25s | Front: %d | Rear: %d | Size: %d%n",
                    action, Arrays.toString(elements), front, rear, size);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Circular Queue State Tracking (Capacity = 4) ===\n");
        CircularQueue<String> queue = new CircularQueue<>(4);

        // 1. enqueue A, enqueue B, enqueue C
        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("C");

        // 2. dequeue, dequeue
        queue.dequeue();
        queue.dequeue();

        // 3. enqueue D, enqueue E, enqueue F
        // 此時會觸發 wrap-around (循環至陣列前端)
        queue.enqueue("D");
        queue.enqueue("E");
        queue.enqueue("F");

        // 4. dequeue, enqueue G
        queue.dequeue();
        queue.enqueue("G");

        System.out.println("\n=== Final Pop All (FIFO Order) ===");
        // 依 FIFO 順序取出所有剩餘元素
        while (!queue.isEmpty()) {
            queue.dequeue();
        }
    }
}
import java.util.ArrayList;
import java.util.List;

public class Q03_MinHeapRemove {

    private final List<Integer> heap;

    // 建構子：過濾 null 並執行 Bottom-up heapify
    public Q03_MinHeapRemove(List<Integer> values) {
        heap = new ArrayList<>();
        if (values != null) {
            for (Integer v : values) {
                if (v != null) {
                    heap.add(v);
                }
            }
        }
        
        // 從最後一個非葉節點開始反向 bubble-down (O(n) 建構)
        for (int i = (heap.size() / 2) - 1; i >= 0; i--) {
            bubbleDown(i);
        }
    }

    // 移除並回傳最小值
    public Integer removeMin() {
        if (heap.isEmpty()) {
            return null;
        }
        
        int min = heap.get(0);
        int lastIndex = heap.size() - 1;
        
        if (lastIndex == 0) {
            heap.remove(0);
            return min;
        }
        
        // 將最後一個元素移至 root，並執行 bubble-down
        heap.set(0, heap.remove(lastIndex));
        bubbleDown(0);
        
        return min;
    }

    // 向下比較與交換
    private void bubbleDown(int index) {
        int size = heap.size();
        while (true) {
            int leftChild = 2 * index + 1;
            int rightChild = 2 * index + 2;
            int smallest = index;

            // 找出自己、左子節點、右子節點中的最小值
            if (leftChild < size && heap.get(leftChild) < heap.get(smallest)) {
                smallest = leftChild;
            }
            if (rightChild < size && heap.get(rightChild) < heap.get(smallest)) {
                smallest = rightChild;
            }

            // 若最小值已是自己，代表符合 Heap 性質，停止走訪
            if (smallest == index) {
                break;
            }

            // 交換並繼續向下檢查
            int temp = heap.get(index);
            heap.set(index, heap.get(smallest));
            heap.set(smallest, temp);
            index = smallest;
        }
    }

    // 取得最小值但不移除
    public Integer peek() {
        return heap.isEmpty() ? null : heap.get(0);
    }

    // 取得 Heap 大小
    public int size() {
        return heap.size();
    }

    // 回傳內部資料複本
    public List<Integer> snapshot() {
        return new ArrayList<>(heap);
    }

    public static void main(String[] args) {
        // 測試包含 null 的初始資料
        List<Integer> initialData = new ArrayList<>();
        initialData.add(10);
        initialData.add(null);
        initialData.add(20);
        initialData.add(5);
        initialData.add(15);
        initialData.add(2);
        initialData.add(null);
        initialData.add(8);

        System.out.println("--- constructor & bottom-up heapify ---");
        Q03_MinHeapRemove minHeap = new Q03_MinHeapRemove(initialData);
        System.out.println("initial snapshot=" + minHeap.snapshot()); // 預期 root 為 2

        System.out.println("\n--- remove min test ---");
        System.out.println("removeMin=" + minHeap.removeMin());
        System.out.println("snapshot after remove=" + minHeap.snapshot());
        System.out.println("removeMin=" + minHeap.removeMin());
        
        System.out.println("\n--- empty & single element edge cases ---");
        Q03_MinHeapRemove emptyHeap = new Q03_MinHeapRemove(null);
        System.out.println("empty peek=" + emptyHeap.peek());
        System.out.println("empty removeMin=" + emptyHeap.removeMin());

        Q03_MinHeapRemove singleHeap = new Q03_MinHeapRemove(List.of(99));
        System.out.println("single snapshot=" + singleHeap.snapshot());
        System.out.println("single removeMin=" + singleHeap.removeMin());
        System.out.println("single size after remove=" + singleHeap.size());
    }
}
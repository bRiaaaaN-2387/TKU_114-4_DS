import java.util.ArrayList;
import java.util.List;

public class Q02_MinHeapInsert {
    
    // 自行管理的內部陣列
    private final List<Integer> heap = new ArrayList<>();

    // 新增元素並執行 Bubble-up
    public void add(int value) {
        heap.add(value);
        bubbleUp(heap.size() - 1);
    }

    // 向上比較與交換
    private void bubbleUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            // 若當前節點大於等於父節點，則已符合 Min Heap 性質，停止交換
            if (heap.get(index) >= heap.get(parentIndex)) {
                break;
            }
            // 交換當前節點與父節點
            int temp = heap.get(index);
            heap.set(index, heap.get(parentIndex));
            heap.set(parentIndex, temp);
            
            // 繼續向上檢查
            index = parentIndex;
        }
    }

    // 回傳最小值，若 Heap 為空則回傳 null
    public Integer peek() {
        if (heap.isEmpty()) {
            return null;
        }
        return heap.get(0);
    }

    // 回傳 Heap 目前大小
    public int size() {
        return heap.size();
    }

    // 回傳內部 List 的複本，避免外部修改破壞封裝
    public List<Integer> snapshot() {
        return new ArrayList<>(heap);
    }

    // 驗證是否符合 Min Heap 性質 (父節點 <= 子節點)
    public boolean isValidMinHeap() {
        int n = heap.size();
        for (int i = 0; i <= (n - 2) / 2; i++) {
            int leftChild = 2 * i + 1;
            int rightChild = 2 * i + 2;
            
            if (leftChild < n && heap.get(i) > heap.get(leftChild)) {
                return false;
            }
            if (rightChild < n && heap.get(i) > heap.get(rightChild)) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        Q02_MinHeapInsert minHeap = new Q02_MinHeapInsert();

        System.out.println("--- empty heap case ---");
        System.out.println("peek=" + minHeap.peek());
        System.out.println("size=" + minHeap.size());
        System.out.println("isValid=" + minHeap.isValidMinHeap());

        System.out.println("\n--- normal insert & bubble-up ---");
        minHeap.add(10);
        minHeap.add(5);
        minHeap.add(15);
        minHeap.add(2); // 會一路 bubble-up 到 root
        minHeap.add(5); // 測試重複值

        System.out.println("snapshot=" + minHeap.snapshot());
        System.out.println("peek=" + minHeap.peek());
        System.out.println("size=" + minHeap.size());
        System.out.println("isValid=" + minHeap.isValidMinHeap());

        System.out.println("\n--- encapsulation check ---");
        List<Integer> copy = minHeap.snapshot();
        copy.clear(); // 嘗試破壞內部資料
        System.out.println("snapshot after external clear=" + minHeap.snapshot()); // 內部資料應不受影響
    }
}
import java.util.ArrayList;
import java.util.List;

public class MaxHeapInsertTrace {
    private final List<Integer> data = new ArrayList<>();

    // 加入新元素並進行 bubble-up
    public void add(int value) {
        data.add(value);
        int index = data.size() - 1;
        
        while (index > 0) {
            int parent = (index - 1) / 2;
            
            // Max Heap 條件：parent 必須大於等於 child，符合則停止修正
            if (data.get(parent) >= data.get(index)) {
                break;
            }
            
            swap(parent, index);
            index = parent;
        }
        // 加入每筆資料後印出陣列快照
        System.out.println("add=" + value + " snapshot=" + snapshot());
    }

    // 取得最大值 (root)
    public Integer peekMax() {
        return data.isEmpty() ? null : data.get(0);
    }

    // 回傳當前陣列的唯讀複本
    public List<Integer> snapshot() {
        return List.copyOf(data);
    }

    // 交換陣列中兩個元素
    private void swap(int first, int second) {
        int temp = data.get(first);
        data.set(first, data.get(second));
        data.set(second, temp);
    }

    public static void main(String[] args) {
        MaxHeapInsertTrace heap = new MaxHeapInsertTrace();
        int[] testData = {25, 40, 10, 50, 30, 50};
        
        for (int val : testData) {
            heap.add(val);
        }
        
        System.out.println("root=" + heap.peekMax());
    }
}
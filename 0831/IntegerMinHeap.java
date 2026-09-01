import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class IntegerMinHeap {
    private final List<Integer> data = new ArrayList<>();

    // 加入新元素並進行 bubble-up
    public void add(int value) {
        data.add(value);
        int index = data.size() - 1;
        while (index > 0) {
            int parent = (index - 1) / 2;
            // Min Heap：若 parent 不大於 child 則停止
            if (data.get(parent) <= data.get(index)) break;
            swap(parent, index);
            index = parent;
        }
    }

    // 取得最小值 (不移除)
    public int peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("heap is empty");
        }
        return data.get(0);
    }

    // 取出並移除最小值，進行 bubble-down
    public int removeMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("heap is empty");
        }
        int result = data.get(0);
        int last = data.remove(data.size() - 1);
        
        // 若移除後仍有元素，將最後一個元素放到 root 並向下修正
        if (!isEmpty()) {
            data.set(0, last);
            bubbleDown(0);
        }
        return result;
    }

    private void bubbleDown(int index) {
        while (true) {
            int left = index * 2 + 1;
            int right = index * 2 + 2;
            if (left >= data.size()) return;

            // 找出 left 與 right 之中較小的 child
            int smaller = left;
            if (right < data.size() && data.get(right) < data.get(left)) {
                smaller = right;
            }
            
            // 若當前節點不大於較小的 child，則停止修正
            if (data.get(index) <= data.get(smaller)) return;
            
            swap(index, smaller);
            index = smaller;
        }
    }

    public int size() {
        return data.size();
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    private void swap(int first, int second) {
        int temp = data.get(first);
        data.set(first, data.get(second));
        data.set(second, temp);
    }

    public static void main(String[] args) {
        IntegerMinHeap heap = new IntegerMinHeap();
        int[] testData = {45, 12, 30, 8, 20, 18, 12};
        
        for (int val : testData) {
            heap.add(val);
        }

        System.out.println("size=" + heap.size());
        
        System.out.print("remove order=");
        while (!heap.isEmpty()) {
            System.out.print(heap.removeMin() + " ");
        }
        System.out.println();
        
        // 驗證例外處理
        try {
            heap.peek();
        } catch (NoSuchElementException e) {
            System.out.println("exception caught=" + e.getMessage());
        }
    }
}
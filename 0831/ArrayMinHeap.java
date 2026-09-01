import java.util.Arrays;
import java.util.NoSuchElementException;

public class ArrayMinHeap {
    private int[] data;
    private int size;

    public ArrayMinHeap(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("capacity must be positive");
        }
        this.data = new int[initialCapacity];
        this.size = 0;
    }

    // 加入新元素，容量不足時擴充為兩倍
    public void add(int value) {
        if (size == data.length) {
            resize();
        }
        data[size] = value;
        bubbleUp(size);
        size++;
    }

    // 取出並移除最小值
    public int removeMin() {
        if (size == 0) {
            throw new NoSuchElementException("heap is empty");
        }
        int min = data[0];
        data[0] = data[size - 1];
        size--;
        
        if (size > 0) {
            bubbleDown(0);
        }
        return min;
    }

    // 查看最小值
    public int peek() {
        if (size == 0) {
            throw new NoSuchElementException("heap is empty");
        }
        return data[0];
    }

    // 回傳當前有效資料的陣列快照
    public int[] snapshot() {
        return Arrays.copyOf(data, size);
    }

    private void resize() {
        int newCapacity = data.length * 2;
        System.out.println("resizing array from " + data.length + " to " + newCapacity);
        data = Arrays.copyOf(data, newCapacity);
    }

    private void bubbleUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            // Min Heap 條件：若 parent 不大於 child 則停止
            if (data[parent] <= data[index]) {
                break;
            }
            swap(parent, index);
            index = parent;
        }
    }

    private void bubbleDown(int index) {
        while (true) {
            int left = index * 2 + 1;
            int right = index * 2 + 2;
            if (left >= size) return;

            // 必須選擇較小的 child 進行交換
            int smaller = left;
            if (right < size && data[right] < data[left]) {
                smaller = right;
            }

            if (data[index] <= data[smaller]) return;
            
            swap(index, smaller);
            index = smaller;
        }
    }

    private void swap(int first, int second) {
        int temp = data[first];
        data[first] = data[second];
        data[second] = temp;
    }

    public static void main(String[] args) {
        // 初始化容量為 5，以觸發多次擴充
        ArrayMinHeap heap = new ArrayMinHeap(5);
        
        // 測試加入至少 20 筆資料
        int[] testData = {
            45, 12, 30, 8, 20, 18, 99, 1, 7, 50,
            33, 21, 6, 14, 88, 2, 4, 19, 72, 3
        };

        System.out.println("--- inserting 20 elements ---");
        for (int val : testData) {
            heap.add(val);
        }
        
        System.out.println("snapshot=" + Arrays.toString(heap.snapshot()));
        System.out.println("current min=" + heap.peek());

        System.out.println("--- removing elements ---");
        while (heap.size > 0) {
            System.out.print(heap.removeMin() + " ");
        }
        System.out.println();
    }
}
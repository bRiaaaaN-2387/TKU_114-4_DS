import java.util.List;

public class HeapPropertyValidator {

    // 驗證是否為 Min Heap
    public static boolean isMinHeap(List<Integer> heap) {
        if (heap == null) return false;
        if (heap.size() <= 1) return true;

        for (int parent = 0; parent < heap.size(); parent++) {
            int left = parent * 2 + 1;
            int right = parent * 2 + 2;
            
            // Min Heap 條件：parent 必須小於或等於 child
            if (left < heap.size() && heap.get(parent) > heap.get(left)) {
                return false;
            }
            if (right < heap.size() && heap.get(parent) > heap.get(right)) {
                return false;
            }
        }
        return true;
    }

    // 驗證是否為 Max Heap
    public static boolean isMaxHeap(List<Integer> heap) {
        if (heap == null) return false;
        if (heap.size() <= 1) return true;

        for (int parent = 0; parent < heap.size(); parent++) {
            int left = parent * 2 + 1;
            int right = parent * 2 + 2;
            
            // Max Heap 條件：parent 必須大於或等於 child
            if (left < heap.size() && heap.get(parent) < heap.get(left)) {
                return false;
            }
            if (right < heap.size() && heap.get(parent) < heap.get(right)) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        // 邊界測試
        System.out.println("isMinHeap(null)=" + isMinHeap(null));
        System.out.println("isMinHeap(empty)=" + isMinHeap(List.of()));
        System.out.println("isMinHeap(single)=" + isMinHeap(List.of(10)));

        // 正常測試
        List<Integer> validMin = List.of(8, 12, 18, 45, 20, 30);
        List<Integer> invalidMin = List.of(10, 30, 5, 50);
        System.out.println("validMin=" + isMinHeap(validMin));
        System.out.println("invalidMin=" + isMinHeap(invalidMin));

        List<Integer> validMax = List.of(50, 40, 30, 10, 20, 15);
        List<Integer> invalidMax = List.of(50, 20, 60, 10, 5);
        System.out.println("validMax=" + isMaxHeap(validMax));
        System.out.println("invalidMax=" + isMaxHeap(invalidMax));
    }
}
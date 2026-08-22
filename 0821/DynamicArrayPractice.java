public class DynamicArrayPractice {

    // 建立泛型 DynamicArray<T>，底層使用 Object[] 陣列
    public static class DynamicArray<T> {
        private Object[] elements;
        private int size;

        // 建構子：初始容量設定為 2，以便快速測試擴充機制
        public DynamicArray() {
            this.elements = new Object[2];
            this.size = 0;
        }

        // 取得目前元素數量
        public int size() {
            return size;
        }

        // 取得目前底層陣列的容量
        public int capacity() {
            return elements.length;
        }

        // 內部機制：檢查容量，若已滿則擴充為兩倍
        private void ensureCapacity() {
            if (size == elements.length) {
                int newCapacity = elements.length * 2;
                Object[] newElements = new Object[newCapacity];
                // 複製舊陣列資料到新陣列
                System.arraycopy(elements, 0, newElements, 0, size);
                elements = newElements;
            }
        }

        // 尾端新增元素
        public void add(T value) {
            ensureCapacity();
            elements[size++] = value;
        }

        // 指定位置插入元素
        public void add(int index, T value) {
            // 允許的 index 範圍為 0 到 size (等於 size 代表尾端新增)
            if (index < 0 || index > size) {
                throw new IndexOutOfBoundsException("Add failed. Index: " + index + ", Size: " + size);
            }
            ensureCapacity();
            
            // 將 index 開始的元素全部向右平移一格
            for (int i = size; i > index; i--) {
                elements[i] = elements[i - 1];
            }
            elements[index] = value;
            size++;
        }

        // 取得指定位置的元素
        @SuppressWarnings("unchecked")
        public T get(int index) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("Get failed. Index: " + index + ", Size: " + size);
            }
            return (T) elements[index];
        }

        // 替換指定位置的元素，並回傳舊值
        @SuppressWarnings("unchecked")
        public T set(int index, T value) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("Set failed. Index: " + index + ", Size: " + size);
            }
            T oldValue = (T) elements[index];
            elements[index] = value;
            return oldValue;
        }

        // 移除指定位置的元素，並回傳被移除的值
        @SuppressWarnings("unchecked")
        public T remove(int index) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("Remove failed. Index: " + index + ", Size: " + size);
            }
            T removedValue = (T) elements[index];
            
            // 將 index 之後的元素全部向左平移一格
            for (int i = index; i < size - 1; i++) {
                elements[i] = elements[i + 1];
            }
            size--;
            
            // 移除後，將最後一個無效格設為 null，避免 Memory Leak (協助 GC)
            elements[size] = null;
            return removedValue;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < size; i++) {
                sb.append(elements[i]);
                if (i < size - 1) {
                    sb.append(", ");
                }
            }
            sb.append("]");
            return sb.toString();
        }
    }

    public static void main(String[] args) {
        System.out.println("=== 1. Testing DynamicArray<String> ===");
        DynamicArray<String> strArray = new DynamicArray<>();
        System.out.println("Initial Size: " + strArray.size() + ", Capacity: " + strArray.capacity());

        // 測試 Add
        strArray.add("Java");
        strArray.add("Python");
        System.out.println("Added 2 elements: " + strArray + " | Capacity: " + strArray.capacity());

        // 測試容量倍增 (2 -> 4)
        strArray.add("C++");
        System.out.println("Added 3rd element (Resized): " + strArray + " | Capacity: " + strArray.capacity());

        // 測試指定位置插入與替換
        strArray.add(1, "Go");
        System.out.println("Inserted 'Go' at index 1: " + strArray);
        System.out.println("Set index 2 to 'Rust', old value: " + strArray.set(2, "Rust"));
        System.out.println("Array now: " + strArray);

        // 測試刪除
        System.out.println("Removed item at index 0: " + strArray.remove(0));
        System.out.println("Array after removal: " + strArray + " | Size: " + strArray.size());


        System.out.println("\n=== 2. Testing DynamicArray<Integer> ===");
        DynamicArray<Integer> intArray = new DynamicArray<>();
        intArray.add(100);
        intArray.add(200);
        intArray.add(1, 150);
        System.out.println("Integer Array: " + intArray);
        System.out.println("Get element at index 1: " + intArray.get(1));


        System.out.println("\n=== 3. Testing Edge Cases & Exceptions ===");
        DynamicArray<Double> emptyArray = new DynamicArray<>();

        // 邊界測試：index = -1
        try {
            System.out.print("Testing add at index -1 -> ");
            emptyArray.add(-1, 99.9);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Exception Caught: " + e.getMessage());
        }

        // 邊界測試：空結構刪除
        try {
            System.out.print("Testing remove on empty structure -> ");
            emptyArray.remove(0);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Exception Caught: " + e.getMessage());
        }

        // 邊界測試：index = size 時進行刪除 (越界)
        emptyArray.add(3.14);
        try {
            System.out.print("Testing remove at index == size (1) -> ");
            emptyArray.remove(emptyArray.size());
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Exception Caught: " + e.getMessage());
        }
    }
}
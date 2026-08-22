public class GenericArrayStackDemo {

    // 定義泛型堆疊類別 ArrayStack<T>，底層嚴格限制只使用原生陣列 (Raw Array)
    public static class ArrayStack<T> {
        // 使用 Object 陣列作為底層儲存結構，以避開 Java 泛型無法直接 new T[] 的限制
        private final Object[] elements; 
        private int top;            // 記錄堆疊頂端的索引位置
        private final int capacity; // 記錄堆疊的最大容量

        // 建構子：初始化陣列與相關變數
        public ArrayStack(int capacity) {
            if (capacity <= 0) {
                throw new IllegalArgumentException("Capacity must be greater than 0");
            }
            this.capacity = capacity;
            this.elements = new Object[capacity];
            this.top = -1; // 初始為 -1，代表堆疊為空
        }

        // 檢查堆疊是否為空
        public boolean isEmpty() {
            return top == -1;
        }

        // 檢查堆疊是否已滿
        public boolean isFull() {
            return top == capacity - 1;
        }

        // 取得目前堆疊內的元素數量
        public int size() {
            return top + 1;
        }

        // 堆入元素 (Push)：若堆疊未滿，則將索引加一並存入元素
        public boolean push(T item) {
            if (isFull()) {
                System.out.println("Error: Stack is full. Cannot push [" + item + "]");
                return false;
            }
            elements[++top] = item;
            return true;
        }

        // 彈出元素 (Pop)：若堆疊非空，取得頂端元素，將該位置清空 (幫助 GC)，並將索引減一
        @SuppressWarnings("unchecked")
        public T pop() {
            if (isEmpty()) {
                System.out.println("Error: Stack is empty. Cannot pop.");
                return null;
            }
            T item = (T) elements[top];
            elements[top] = null; // 清除參考以利垃圾回收
            top--;
            return item;
        }

        // 查看頂端元素 (Peek)：取得頂端元素但不移除
        @SuppressWarnings("unchecked")
        public T peek() {
            if (isEmpty()) {
                System.out.println("Error: Stack is empty. Cannot peek.");
                return null;
            }
            return (T) elements[top];
        }
    }

    public static void main(String[] args) {
        System.out.println("=== 1. Testing ArrayStack<String> ===");
        // 建立容量為 3 的字串堆疊
        ArrayStack<String> stringStack = new ArrayStack<>(3);
        
        System.out.println("Is stack empty? " + stringStack.isEmpty());
        
        // 測試 Push
        stringStack.push("Java");
        stringStack.push("Python");
        stringStack.push("C++");
        System.out.println("Current Size: " + stringStack.size());
        
        // 測試滿載時繼續 Push
        System.out.println("Is stack full? " + stringStack.isFull());
        stringStack.push("JavaScript"); 

        // 測試 Peek
        System.out.println("Top element (peek): " + stringStack.peek());
        
        // 測試 Pop
        System.out.println("Popped element: " + stringStack.pop());
        System.out.println("Popped element: " + stringStack.pop());
        
        System.out.println("Current Size after 2 pops: " + stringStack.size());


        System.out.println("\n=== 2. Testing ArrayStack<Integer> ===");
        // 建立容量為 2 的整數堆疊
        ArrayStack<Integer> intStack = new ArrayStack<>(2);
        
        // 測試 Push
        intStack.push(100);
        intStack.push(200);
        
        System.out.println("Top element (peek): " + intStack.peek());
        
        // 測試 Pop 直到堆疊為空
        System.out.println("Popped element: " + intStack.pop());
        System.out.println("Popped element: " + intStack.pop());
        
        // 測試空載時繼續 Pop 與 Peek
        System.out.println("Is stack empty? " + intStack.isEmpty());
        intStack.pop();
        intStack.peek();
    }
}
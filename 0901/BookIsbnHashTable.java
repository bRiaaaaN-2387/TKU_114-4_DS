import java.util.ArrayList;
import java.util.List;

public class BookIsbnHashTable {
    // 儲存 ISBN 與書名的 Entry
    private record Entry(String isbn, String title) {}

    private final List<List<Entry>> buckets;
    private int size;

    public BookIsbnHashTable(int bucketCount) {
        if (bucketCount <= 0) {
            throw new IllegalArgumentException("bucketCount must be positive");
        }
        buckets = new ArrayList<>();
        for (int i = 0; i < bucketCount; i++) {
            buckets.add(new ArrayList<>());
        }
    }

    // 計算 index，使用 floorMod 避免負數結果
    private int getIndex(String isbn) {
        if (isbn == null) throw new IllegalArgumentException("isbn cannot be null");
        return Math.floorMod(isbn.hashCode(), buckets.size());
    }

    // 新增或更新圖書
    public void put(String isbn, String title) {
        List<Entry> chain = buckets.get(getIndex(isbn));
        
        // 尋找是否已存在相同 ISBN，若有則更新書名，size 不變
        for (int i = 0; i < chain.size(); i++) {
            if (chain.get(i).isbn().equals(isbn)) {
                chain.set(i, new Entry(isbn, title));
                return;
            }
        }
        
        // 若不存在，則加入新節點並增加 size
        chain.add(new Entry(isbn, title));
        size++;
    }

    // 搜尋圖書
    public String get(String isbn) {
        List<Entry> chain = buckets.get(getIndex(isbn));
        for (Entry entry : chain) {
            if (entry.isbn().equals(isbn)) {
                return entry.title();
            }
        }
        return null;
    }

    // 刪除圖書
    public boolean remove(String isbn) {
        List<Entry> chain = buckets.get(getIndex(isbn));
        for (int i = 0; i < chain.size(); i++) {
            if (chain.get(i).isbn().equals(isbn)) {
                chain.remove(i);
                size--; // 成功移除才減少 size
                return true;
            }
        }
        return false;
    }

    // 取得總筆數
    public int size() {
        return size;
    }

    // 計算 Load factor
    public double loadFactor() {
        return (double) size / buckets.size();
    }

    // 輸出 Bucket 分佈報告
    public void bucketReport() {
        System.out.println("--- bucket report ---");
        System.out.println("size=" + size + " load factor=" + String.format("%.2f", loadFactor()));
        for (int i = 0; i < buckets.size(); i++) {
            System.out.println("bucket[" + i + "]=" + buckets.get(i));
        }
    }

    public static void main(String[] args) {
        BookIsbnHashTable table = new BookIsbnHashTable(3);
        
        // 測試新增
        table.put("978-0134685991", "Effective Java");
        table.put("978-0132350884", "Clean Code");
        table.put("978-0201835953", "Mythical Man-Month");
        
        // 測試更新
        table.put("978-0134685991", "Effective Java (3rd Edition)");
        
        table.bucketReport();
        
        // 測試搜尋與刪除
        System.out.println("\nget 978-0134685991=" + table.get("978-0134685991"));
        System.out.println("remove 978-0132350884=" + table.remove("978-0132350884"));
        System.out.println("remove unknown=" + table.remove("000-0000000000"));
        
        System.out.println("\nafter removal:");
        table.bucketReport();
    }
}
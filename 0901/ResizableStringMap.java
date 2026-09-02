import java.util.ArrayList;
import java.util.List;

public class ResizableStringMap {
    private record Entry(String key, String value) {}

    private List<List<Entry>> buckets;
    private int size;

    public ResizableStringMap(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("capacity must be positive");
        }
        buckets = new ArrayList<>();
        for (int i = 0; i < initialCapacity; i++) {
            buckets.add(new ArrayList<>());
        }
    }

    private int getIndex(String key, int bucketCount) {
        if (key == null) throw new IllegalArgumentException("key cannot be null");
        return Math.floorMod(key.hashCode(), bucketCount);
    }

    public void put(String key, String value) {
        if ((double) size / buckets.size() > 0.75) {
            resize();
        }

        int index = getIndex(key, buckets.size());
        List<Entry> chain = buckets.get(index);
        
        for (int i = 0; i < chain.size(); i++) {
            if (chain.get(i).key().equals(key)) {
                chain.set(i, new Entry(key, value));
                return;
            }
        }
        
        chain.add(new Entry(key, value));
        size++;
    }

    private void resize() {
        int newCapacity = buckets.size() * 2 + 1;
        System.out.println("trigger rehash: resizing to " + newCapacity);
        
        List<List<Entry>> newBuckets = new ArrayList<>();
        for (int i = 0; i < newCapacity; i++) {
            newBuckets.add(new ArrayList<>());
        }

        for (List<Entry> chain : buckets) {
            for (Entry e : chain) {
                int newIndex = getIndex(e.key(), newCapacity);
                newBuckets.get(newIndex).add(e);
            }
        }
        buckets = newBuckets; 
    }

    public int size() {
        return size;
    }

    public void printReport() {
        System.out.println("--- map report ---");
        System.out.println("size=" + size + " bucket count=" + buckets.size());
        for (int i = 0; i < buckets.size(); i++) {
            if (!buckets.get(i).isEmpty()) {
                System.out.println("bucket[" + i + "]=" + buckets.get(i));
            }
        }
    }

    public static void main(String[] args) {
        ResizableStringMap map = new ResizableStringMap(3);
        
        map.put("A", "Alpha");
        map.put("B", "Beta");
        map.put("C", "Charlie"); 
        
        map.put("A", "Alpha-Updated"); 
        map.put("D", "Delta");
        
        map.printReport();
    }
}
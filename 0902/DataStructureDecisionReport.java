import java.util.ArrayList;
import java.util.List;

public class DataStructureDecisionReport {

    // 定義 12 組常見的操作需求
    public enum Requirement {
        INDEX_ACCESS,
        FIFO_QUEUE,
        LIFO_STACK,
        SORTED_RANGE_QUERY,
        NEXT_PRIORITY_POLL,
        KEY_VALUE_LOOKUP,
        UNIQUE_MEMBERSHIP_TEST,
        SPARSE_RELATION_TRAVERSAL,
        DENSE_EDGE_CHECK,
        MAINTAIN_INSERTION_ORDER_SET,
        MAINTAIN_INSERTION_ORDER_MAP,
        SORTED_UNIQUE_ELEMENTS
    }

    // 將結構選擇、理由與主要 Big-O 封裝為 Record
    public record Decision(String structure, String reason, String bigO) {}

    // 依據需求回傳對應的資料結構決策
    public static Decision getDecision(Requirement req) {
        if (req == null) {
            return new Decision("UNKNOWN", "null requirement provided", "N/A");
        }
        
        return switch (req) {
            case INDEX_ACCESS -> new Decision("ArrayList", "fast element retrieval by integer index", "get O(1), insert O(n)");
            case FIFO_QUEUE -> new Decision("ArrayDeque (Queue)", "efficient adding to tail and removing from head", "offer/poll O(1)");
            case LIFO_STACK -> new Decision("ArrayDeque (Stack)", "efficient adding and removing from the same end", "push/pop O(1)");
            case SORTED_RANGE_QUERY -> new Decision("TreeMap (BST)", "maintains keys in sorted order for range operations", "O(log n) balanced, O(n) worst");
            case NEXT_PRIORITY_POLL -> new Decision("PriorityQueue (Heap)", "efficiently retrieves and removes extreme values", "peek O(1), add/remove O(log n)");
            case KEY_VALUE_LOOKUP -> new Decision("HashMap", "fast key-based value retrieval", "avg O(1), worst O(n)");
            case UNIQUE_MEMBERSHIP_TEST -> new Decision("HashSet", "ensures uniqueness and fast membership checks", "avg O(1)");
            case SPARSE_RELATION_TRAVERSAL -> new Decision("Adjacency List", "space efficient for sparse relations, good for BFS/DFS", "O(V+E)");
            case DENSE_EDGE_CHECK -> new Decision("Adjacency Matrix", "constant time edge existence check for fixed vertices", "O(1) check, O(V^2) space");
            case MAINTAIN_INSERTION_ORDER_SET -> new Decision("LinkedHashSet", "uniqueness with predictable iteration order", "avg O(1)");
            case MAINTAIN_INSERTION_ORDER_MAP -> new Decision("LinkedHashMap", "key-value mapping with predictable iteration order", "avg O(1)");
            case SORTED_UNIQUE_ELEMENTS -> new Decision("TreeSet", "unique elements maintained in sorted order", "O(log n)");
        };
    }

    // 輸出決策報告
    public static void printReport(List<Requirement> requirements) {
        if (requirements == null || requirements.isEmpty()) {
            System.out.println("report generation failed: no requirements provided");
            return;
        }

        System.out.println("--- data structure decision report ---");
        for (int i = 0; i < requirements.size(); i++) {
            Requirement req = requirements.get(i);
            Decision dec = getDecision(req);
            
            System.out.printf("%2d. Req: %-30s%n", (i + 1), (req != null ? req.name() : "NULL"));
            System.out.printf("    Structure : %s%n", dec.structure());
            System.out.printf("    Big-O     : %s%n", dec.bigO());
            System.out.printf("    Reason    : %s%n", dec.reason());
        }
    }

    public static void main(String[] args) {
        // 測試 12 組完整需求案例
        List<Requirement> allReqs = List.of(Requirement.values());
        System.out.println("--- 12 scenarios case ---");
        printReport(allReqs);

        // 測試 Null 元素案例
        System.out.println("\n--- null element case ---");
        List<Requirement> withNull = new ArrayList<>();
        withNull.add(Requirement.FIFO_QUEUE);
        withNull.add(null);
        printReport(withNull);

        // 測試空陣列案例
        System.out.println("\n--- empty list case ---");
        printReport(List.of());
    }
}
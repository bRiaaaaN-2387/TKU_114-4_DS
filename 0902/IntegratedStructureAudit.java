import java.util.Map;

public class IntegratedStructureAudit {

    // 定義稽核診斷結果結構
    public record AuditResult(String structure, String scenario, boolean isReasonable, String diagnosis) {}

    // 評估資料結構與使用情境是否合理
    public static AuditResult audit(String structure, String scenario) {
        if (structure == null || structure.isBlank() || scenario == null || scenario.isBlank()) {
            return new AuditResult("UNKNOWN", "UNKNOWN", false, "invalid or empty input parameters");
        }

        String s = structure.trim().toLowerCase();
        String sc = scenario.trim().toLowerCase();

        // 規則對應與診斷評估
        return switch (s) {
            case "list", "arraylist" -> {
                if (sc.contains("index access") || sc.contains("sequential read")) {
                    yield new AuditResult(structure, scenario, true, "efficient O(1) random access[cite: 1]");
                } else if (sc.contains("frequent insert") || sc.contains("middle delete")) {
                    yield new AuditResult(structure, scenario, false, "inefficient O(n) shifting cost for insertions/deletions[cite: 1]");
                } else {
                    yield new AuditResult(structure, scenario, true, "generally acceptable usage");
                }
            }
            case "queue", "arraydeque" -> {
                if (sc.contains("fifo") || sc.contains("bfs") || sc.contains("task queue")) {
                    yield new AuditResult(structure, scenario, true, "optimal O(1) performance at both ends[cite: 1]");
                } else {
                    yield new AuditResult(structure, scenario, false, "inappropriate for random access or sorting");
                }
            }
            case "bst", "treemap" -> {
                if (sc.contains("sorted range") || sc.contains("ordered traversal")) {
                    yield new AuditResult(structure, scenario, true, "maintains ordered keys for efficient range queries[cite: 1]");
                } else {
                    yield new AuditResult(structure, scenario, false, "overhead compared to hash map if ordering is not needed");
                }
            }
            case "heap", "priorityqueue" -> {
                if (sc.contains("priority") || sc.contains("extreme value") || sc.contains("next task")) {
                    yield new AuditResult(structure, scenario, true, "efficient O(log n) retrieval of top priority items[cite: 1]");
                } else if (sc.contains("index access") || sc.contains("random search")) {
                    yield new AuditResult(structure, scenario, false, "heap does not support fast index lookup or searching[cite: 1]");
                } else {
                    yield new AuditResult(structure, scenario, true, "acceptable priority management");
                }
            }
            case "hash table", "hashmap", "hashtable" -> {
                if (sc.contains("key lookup") || sc.contains("dictionary") || sc.contains("id mapping")) {
                    yield new AuditResult(structure, scenario, true, "average O(1) fast key-based retrieval[cite: 1]");
                } else {
                    yield new AuditResult(structure, scenario, false, "does not maintain sorted order or range queries");
                }
            }
            case "graph", "adjacency list" -> {
                if (sc.contains("relation") || sc.contains("network") || sc.contains("pathfinding")) {
                    yield new AuditResult(structure, scenario, true, "ideal representation for sparse vertex-edge relations[cite: 1]");
                } else {
                    yield new AuditResult(structure, scenario, false, "overkill for simple linear data or key-value lookups");
                }
            }
            default -> new AuditResult(structure, scenario, false, "unknown or unsupported data structure");
        };
    }

    // 批次執行稽核並輸出診斷報告
    public static void auditScenarios(Map<String, String> testCases) {
        System.out.println("--- integrated structure audit report ---");
        if (testCases == null || testCases.isEmpty()) {
            System.out.println("no test cases provided for audit.");
            System.out.println();
            return;
        }

        for (Map.Entry<String, String> entry : testCases.entrySet()) {
            String structure = entry.getKey();
            String scenario = entry.getValue();
            AuditResult result = audit(structure, scenario);

            System.out.println("structure=" + result.structure());
            System.out.println("scenario=" + result.scenario());
            System.out.println("isReasonable=" + result.isReasonable());
            System.out.println("diagnosis=" + result.diagnosis());
            System.out.println();
        }
    }

    public static void main(String[] args) {
        // 測試案例 1：一般正常與不合理情境
        Map<String, String> normalCases = Map.of(
            "List", "frequent insert at middle index",
            "Queue", "FIFO task processing",
            "Heap", "fast random index access",
            "Hash Table", "key lookup by ID",
            "Graph", "network pathfinding"
        );

        System.out.println("--- normal and mismatch audit cases ---");
        auditScenarios(normalCases);

        // 測試案例 2：邊界條件 (Missing/Empty/Invalid)
        System.out.println("--- edge cases audit ---");
        auditScenarios(Map.of("", "FIFO processing", "UnknownStruct", "some scenario"));
        
        System.out.println("--- empty input case ---");
        auditScenarios(null);
    }
}